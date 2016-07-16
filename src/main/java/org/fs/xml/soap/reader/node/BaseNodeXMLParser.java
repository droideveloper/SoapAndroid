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

import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public abstract class BaseNodeXMLParser<T>
    implements NodeXMLReader<XmlPullParser, NodeFieldReference>, NodeXMLWriter<XmlSerializer, NodeFieldReference> {

  @Override public void read(XmlPullParser reader, NodeFieldReference ref) throws Exception {
    T value = null;
    int event = reader.getEventType();
    while(event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.TEXT) {
        String text = reader.getText();
        value = toReferenceType(text);
      } else if (event == XmlPullParser.END_TAG) {
        reader.next();//WHITESPACE_TEXT
        reader.next();//START_TAG
        break;
      }
      event = reader.next();
    }
    ref.set(value);
  }

  @Override public void write(XmlSerializer writer, NodeFieldReference ref) throws Exception {
    T value = ReferenceUtility.castLazy(ref.get());
    String text = toTextType(value);
    writer.startTag(ref.namespace(), ref.name());
    writer.text(text);
    writer.endTag(ref.namespace(), ref.name());
  }

  @Override public boolean isWritePossible(NodeFieldReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType);
  }

  @Override public boolean isReadPossible(XmlPullParser reader, NodeFieldReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType) && nodeNameMatches(reader, ref.name());
  }

  protected boolean nodeNameMatches(XmlPullParser reader, String referenceName) throws Exception {
    final String cursorNodeName = reader.getName();
    return !TextUtils.isEmpty(cursorNodeName) && !TextUtils.isEmpty(referenceName) && referenceName.equalsIgnoreCase(cursorNodeName);
  }

  protected abstract String   toTextType(T refValue) throws Exception;
  protected abstract T        toReferenceType(String text) throws Exception;
  protected abstract boolean  typeMatches(Class<?> referenceType);
}
