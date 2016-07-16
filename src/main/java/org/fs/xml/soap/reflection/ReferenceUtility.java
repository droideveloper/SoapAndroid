/*
 * Copyright (C) 2016 Fatih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.xml.soap.reflection;

import org.fs.xml.soap.annotation.Attribute;
import org.fs.xml.soap.annotation.Ignore;
import org.fs.xml.soap.annotation.Node;
import org.fs.xml.soap.reflection.field.AttributeFieldReference;
import org.fs.xml.soap.reflection.field.FieldReference;
import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.fs.xml.soap.reflection.type.NodeTypeReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class ReferenceUtility {

  public final static String NODE_COLLECTION = "array";
  public final static String NODE_ENTRY      = "entry";
  //public final static String NODE_MAP        = "struct";

  public final static String ATTR_TYPE_NAME = "type";

  public final static String PREFIX_XS     = "xs";
  public final static String PREFIX_XSI    = "xsi";

  public final static String NAMESPACE_XS  = "http://www.w3.org/2001/XMLSchema";
  public final static String NAMESPACE_XSI = "http://www.w3.org/2001/XMLSchema-instance";

  private final static List<Class<?>> extensionTypes;
  private final static List<Class<?>> primitiveTypes;
  static {
    //primitives
    primitiveTypes = new ArrayList<>();
    primitiveTypes.add(Date.class);//-+/
    primitiveTypes.add(String.class);//-+/
    primitiveTypes.add(Integer.class);//-+/
    primitiveTypes.add(int.class);//-+/
    primitiveTypes.add(Long.class);//-+/
    primitiveTypes.add(long.class);//-+/
    primitiveTypes.add(Double.class);//-+/
    primitiveTypes.add(double.class);//-+/
    primitiveTypes.add(Float.class);//-+/
    primitiveTypes.add(float.class);//-+/
    primitiveTypes.add(Boolean.class);//-+/
    primitiveTypes.add(boolean.class);//-+/
    primitiveTypes.add(Short.class);//-+/
    primitiveTypes.add(short.class);//-+/
    primitiveTypes.add(byte[].class);//-+/
    //extensions
    extensionTypes = new ArrayList<>();
    extensionTypes.add(Collection.class);//
    //extensionTypes.add(Map.class);// removed for now
  }

  public static boolean isNotNull(Object o) {
    return null != o;
  }

  public static <T> T castAs(Object o, Class<T> c) {
    return c.cast(o);
  }

  public static <T> Class<?> castAs(T t) {
    return t.getClass();
  }

  public static Class<?> castAs(Type type) {
    return (Class<?>) type;
  }

  @SuppressWarnings("unchecked") public static <T> T castLazy(Object o) {
    return (T)o;
  }

  public static boolean isPrimitiveType(Class<?> referenceType) {
    for (int i = 0, z = primitiveTypes.size(); i < z; i++) {
      Class<?> currentType = primitiveTypes.get(i);
      if (currentType.equals(referenceType)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isExtensionType(Class<?> referenceType) {
    for (int i = 0, z = extensionTypes.size(); i < z; i++) {
      Class<?> currentType = extensionTypes.get(i);
      if (currentType.isAssignableFrom(referenceType)) {
        return true;
      }
    }
    return false;
  }

  public static Class<?> xsType(String xsType) {
    switch (xsType) {
      case "xs:date"      : return Date.class;
      case "xs:string"    : return String.class;
      case "xs:integer"   : return Integer.class;
      case "xs:long"      : return Long.class;
      case "xs:double"    : return Double.class;
      case "xs:float"     : return Float.class;
      case "xs.boolean"   : return Boolean.class;
      case "xs:short"     : return Short.class;
      case "xs:byteArray" : return byte[].class;
      default: {
        if (xsType.startsWith(PREFIX_XS)) {
          final String className = xsType.substring(PREFIX_XS.length() + 1);
          try {
            return Class.forName(className);
          } catch (ClassNotFoundException ignored) {
            throw new RuntimeException("such class @{ " + className + " } not exists!");
          }
        }
      }
    }
    throw new RuntimeException("invalid @{ " + xsType + " } type definition!");
  }

  public static String xsString(Class<?> xsType) {
    final String xsTextFormat = "%s:%s";
    if (isPrimitiveType(xsType)) {
      return String.format(Locale.US, xsTextFormat,
          PREFIX_XS, xsType.getSimpleName().toLowerCase(Locale.US));
    }
    return String.format(Locale.US, xsTextFormat,
        PREFIX_XS, xsType.getName());
  }

  public static NodeTypeReference toTypeReference(NodeFieldReference ref) throws Exception {
    NodeTypeReference newRef = new NodeTypeReference();
    Class<?> classNew = castAs(ref.type());
    newRef.reference(classNew);
    newRef.set(newInstanceOrValue(ref));
    if (ref.hasDefinition()) {
      newRef.definition(ref.definition());
    } else {
      Node node = classNew.getAnnotation(Node.class);
      newRef.definition(node);
    }
    //load references
    loadChildrenIfNecessary(newRef);
    return newRef;
  }

  public static NodeTypeReference toTypeReference(Object obj) throws Exception {
    NodeTypeReference newRef = new NodeTypeReference();
    Class<?> classObject = obj.getClass();
    newRef.reference(classObject);
    newRef.set(obj);
    Node node = classObject.getAnnotation(Node.class);
    newRef.definition(node);
    //load references
    loadChildrenIfNecessary(newRef);
    return newRef;
  }

  public static NodeTypeReference toTypeReference(Class<?> clazz) throws Exception {
    NodeTypeReference newRef = new NodeTypeReference();
    newRef.reference(clazz);
    newRef.set(clazz.newInstance());
    Node node = clazz.getAnnotation(Node.class);
    newRef.definition(node);
    //load references
    loadChildrenIfNecessary(newRef);
    return newRef;
  }

  //TODO -> there is a case where if we do not catch the value for ourselves
  //TODO -> since that occurs we need to check for read or write of other values.
  private static void loadChildrenIfNecessary(NodeTypeReference ref) throws Exception {
    Class<?> classRef = castAs(ref.type());
    //get target
    Object targetRef = ref.get();
    //get children
    Field[] children = classRef.getDeclaredFields();
    if (children != null && children.length > 0) {
      //sort by their name
      sortFieldsByName(children);
      for (int i = 0, z = children.length; i < z; i++) {
        Field child = children[i];
        if (child != null) {
          Annotation def = get(child.getAnnotations());
          if (!(def instanceof Ignore)) {
            FieldReference<?> childRef = get(child, def, targetRef);
            ref.addChild(childRef);
          }
        }
      }
    }
  }

  private static FieldReference<?> get(Field field, Annotation def, Object targetRef)  throws Exception {
    FieldReference<?> ref = def instanceof Attribute ? new AttributeFieldReference() : new NodeFieldReference();
    ref.reference(field);
    ref.target(targetRef);
    if(def instanceof Attribute) {
      Attribute attr = castAs(def, Attribute.class);
      AttributeFieldReference attrRef = castAs(ref, AttributeFieldReference.class);
      attrRef.definition(attr);
    } else if (def instanceof Node){
      Node node = castAs(def, Node.class);
      NodeFieldReference nodeRef = castAs(ref, NodeFieldReference.class);
      nodeRef.definition(node);
    } else {
      Class<?> fieldClazz = castAs(field.getType());
      if (isPrimitiveType(fieldClazz) || isExtensionType(fieldClazz)) {
        ref.definition(null);
      } else {
        Node node = fieldClazz.getAnnotation(Node.class);
        NodeFieldReference nodeRef = castAs(ref, NodeFieldReference.class);
        nodeRef.definition(node);
      }
    }
    return ref;
  }

  private static Annotation get(Annotation[] array) {
    return array != null && array.length > 0 ? array[0] : null;
  }

  private static Object newInstanceOrValue(NodeFieldReference ref) throws Exception {
    Object refValue = ref.get();
    if (refValue == null) {
      Class<?> classRef = castAs(ref.type());
      refValue = classRef.newInstance();
      ref.set(refValue);
    }
    return refValue;
  }

  private static void sortFieldsByName(Field[] array) {
    Arrays.sort(array, new Comparator<Field>() {
            @Override public int compare(Field current, Field next) {
              String currentName  = current.getName();
              String nextName     = next.getName();
              return currentName.compareTo(nextName);
            }
        });
    }

  private ReferenceUtility() {
    throw new IllegalArgumentException("no sugar for ya");
  }
}
