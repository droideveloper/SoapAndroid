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
package org.fs.xml.soap.reader.node;

import android.text.TextUtils;

import org.fs.xml.soap.annotation.Node;
import org.fs.xml.soap.reader.ReaderUtility;
import org.fs.xml.soap.reader.attr.AttrXMLParserFactory;
import org.fs.xml.soap.reader.attr.BaseAttrXMLParser;
import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.field.AttributeFieldReference;
import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.fs.xml.soap.reflection.type.NodeTypeReference;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.Collection;

public final class TypeNodeXMLParser
    implements NodeXMLReader<XmlPullParser, NodeTypeReference>, NodeXMLWriter<XmlSerializer, NodeTypeReference> {

  @Override public void read(XmlPullParser reader, NodeTypeReference ref) throws Exception {
    if (ref.hasAnyChildren()) {
      if (ref.hasAttrChildren()) {
        for (int i = 0, z = ref.attrChildrenSize(); i < z; i++) {
          AttributeFieldReference attrRef = ReferenceUtility.castAs(ref.attrChildrenAt(i), AttributeFieldReference.class);
          AttrXMLParserFactory factory = AttrXMLParserFactory.sharedInstance();
          BaseAttrXMLParser<?> xmlReader = factory.readXMLParser(reader, attrRef);
          if(xmlReader != null) {
            xmlReader.read(reader, attrRef);
          } else {
            throw new NullPointerException("impossible primitive @{ " + ref.type() + " }");
          }
        }
      }
      if (ref.hasNodeChildren()) {
        //go next START_TAG
        ReaderUtility.nextStartTag(reader);
        //start reading
        while (ref.hasNodeChildren()) {
          NodeFieldReference nodeRef = ReferenceUtility.castAs(ref.nodeChildrenReader(reader), NodeFieldReference.class);
          if (nodeRef != null) {
            Class<?> classNode = ReferenceUtility.castAs(nodeRef.type());
            if (ReferenceUtility.isPrimitiveType(classNode)) {
              NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
              BaseNodeXMLParser<?> xmlReader = factory.readXMLParser(reader, nodeRef);
              if (xmlReader != null) {
                xmlReader.read(reader, nodeRef);
              } else {
                throw new NullPointerException("impossible primitive @{ " + classNode.getName() + " }");
              }
            } else if (ReferenceUtility.isExtensionType(classNode)) {
              NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
              CollectionNodeXMLParser xmlReader = factory.collectionXMLParser();
              if (xmlReader.isReadPossible(reader, nodeRef)) {
                xmlReader.read(reader, nodeRef);
              } else {
                throw new NullPointerException("impossible extension @{ " + classNode.getName() + " }");
              }
            } else {
              NodeTypeReference newRef = ReferenceUtility.toTypeReference(nodeRef);
              if (isReadPossible(reader, newRef)) {
                read(reader, newRef);
              } else {
                Node node = nodeRef.definition();
                //default required else if not defined as required it can skip reading
                if ((node != null && node.required()) || node == null) {
                  throw new NullPointerException("impossible type @{ " + classNode.getName() + " }");
                }
              }
            }
          } else {
            ReaderUtility.nextStartTag(reader);
            int event = reader.getEventType();
            //if has child and end of document
            if (event == XmlPullParser.END_DOCUMENT && ref.hasNodeChildren()) {
              throw new IllegalArgumentException("end of document yet you have children to read @{ " + ReferenceUtility.castAs(ref.type()).getName() + " }");
            }
          }
        }
      } else {
        //go next START_TAG
        ReaderUtility.nextStartTag(reader);
      }
    }  else {
      //go next START_TAG
      ReaderUtility.nextStartTag(reader);
    }
  }

  @Override public void write(XmlSerializer writer, NodeTypeReference ref) throws Exception {
    writer.startTag(ref.namespace(), ref.name());
    Class<?> refClass = ReferenceUtility.castAs(ref.type());
    writer.attribute(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME, ReferenceUtility.xsString(refClass));
    if (ref.hasAnyChildren()) {
      if (ref.hasAttrChildren()) {
        int attrSize = ref.attrChildrenSize();
        for (int i = 0; i < attrSize; i++) {
          AttributeFieldReference attr = ReferenceUtility.castAs(ref.attrChildrenAt(i), AttributeFieldReference.class);
          if (attr != null) {
            Class<?> classAttr = ReferenceUtility.castAs(attr.type());
            if (ReferenceUtility.isPrimitiveType(classAttr)) {
              AttrXMLParserFactory factory = AttrXMLParserFactory.sharedInstance();
              BaseAttrXMLParser<?> attrWriter = factory.writeXMLParser(attr);
              if (attrWriter != null) {
                attrWriter.write(writer, attr);
              } else {
                throw new NullPointerException("impossible primitive attr @{ " + attr.name() + "\t" + classAttr.getName() + " }");
              }
            } else {
              throw new IllegalArgumentException("you can not send custom type as attribute on a node, type is not primitive @{ " + classAttr.getName() + " }");
            }
          } else {
            throw new IllegalArgumentException("you should not put wrong type of reference in attributeReferences");
          }
        }
      }
      if (ref.hasNodeChildren()) {
        int nodeSize = ref.nodeChildrenSize();
        for (int i = 0; i < nodeSize; i++) {
          NodeFieldReference node = ReferenceUtility.castAs(ref.nodeChildrenAt(i), NodeFieldReference.class);
          if (node != null) {
            Class<?> classNode = ReferenceUtility.castAs(node.type());
            if (ReferenceUtility.isPrimitiveType(classNode)) {
              NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
              BaseNodeXMLParser<?> nodeWriter = factory.writeXMLParser(node);
              if (nodeWriter != null) {
                nodeWriter.write(writer, node);
              } else {
                throw new NullPointerException("impossible primitive node @{ " + node.name() + "\t" + classNode.getName() + " }");
              }
            } else if (ReferenceUtility.isExtensionType(classNode)) {
              if (Collection.class.isAssignableFrom(classNode)) {
                NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
                CollectionNodeXMLParser nodeWriter = factory.collectionXMLParser();
                if (nodeWriter.isWritePossible(node)) {
                  nodeWriter.write(writer, node);
                } else {
                  throw new NullPointerException("impossible collection node @{ " + node.name() + "\t" + classNode.getName() + " }");
                }
              } else {
                throw new NullPointerException("impossible extension type node @{ " + node.name() + "\t" + classNode.getName() + " }");
              }
            } else {
              Object nodeValue = node.get();
              //if it's not null then we write it else just ignore
              if (ReferenceUtility.isNotNull(nodeValue)) {
                NodeTypeReference parsed = ReferenceUtility.toTypeReference(node);
                if (isWritePossible(parsed)) {
                  write(writer, parsed);
                } else {
                  throw new NullPointerException("impossible type node @{ " + node.name() + "\t" + classNode.getName() + " }");
                }
              }
            }
          } else {
            throw new IllegalArgumentException("you should not put wrong type of reference in nodeReferences");
          }
        }
      }
    }
    writer.endTag(ref.namespace(), ref.name());
  }

  @Override public boolean isWritePossible(NodeTypeReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType);
  }

  @Override public boolean isReadPossible(XmlPullParser reader, NodeTypeReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return nodeNameMatches(reader, ref.name()) && typeMatches(referenceType);
  }

  protected boolean nodeNameMatches(XmlPullParser reader, String referenceName) throws Exception {
    final String cursorNodeName = reader.getName();
    return !TextUtils.isEmpty(cursorNodeName) && !TextUtils.isEmpty(referenceName) && referenceName.equalsIgnoreCase(cursorNodeName);
  }

  protected boolean typeMatches(Class<?> referenceType) throws Exception {
    return !(ReferenceUtility.isPrimitiveType(referenceType) && ReferenceUtility.isExtensionType(referenceType));
  }
}
