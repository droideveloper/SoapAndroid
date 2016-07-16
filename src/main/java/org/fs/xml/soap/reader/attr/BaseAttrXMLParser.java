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
package org.fs.xml.soap.reader.attr;

import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.field.AttributeFieldReference;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public abstract class BaseAttrXMLParser<T>
    implements AttrXMLReader<XmlPullParser, AttributeFieldReference>, AttrXMLWriter<XmlSerializer, AttributeFieldReference> {

  @Override public void read(XmlPullParser reader, AttributeFieldReference ref) throws Exception {
    String text = reader.getAttributeValue(ref.namespace(), ref.name());
    T value = toReferenceType(text);
    ref.set(value);
  }

  @Override public void write(XmlSerializer writer, AttributeFieldReference ref) throws Exception {
    T value = ReferenceUtility.castLazy(ref.get());
    String text = toTextType(value);
    writer.attribute(ref.namespace(), ref.name(), text);
  }

  @Override public boolean isWritePossible(AttributeFieldReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType);
  }

  @Override public boolean isReadPossible(XmlPullParser reader, AttributeFieldReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType) && attrNameMatches(reader, ref.name());
  }

  protected boolean attrNameMatches(XmlPullParser reader, String referenceName) throws Exception {
    for (int i = 0, z = reader.getAttributeCount(); i < z; i++) {
      final String currentAttrName = reader.getAttributeName(i);
      if (referenceName.equals(currentAttrName)) {
        return true;
      }
    }
    return false;
  }

  protected abstract String   toTextType(T refValue) throws Exception;
  protected abstract T        toReferenceType(String text) throws Exception;
  protected abstract boolean  typeMatches(Class<?> referenceType);
}
