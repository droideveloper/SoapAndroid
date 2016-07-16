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
package org.fs.xml.soap.reader.text;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public abstract class BaseTextXMLParser<T>
    implements TextXMLReader<XmlPullParser, T>, TextXMLWriter<XmlSerializer, T> {

  @Override public void write(XmlSerializer writer, T value) throws Exception {
    final String text = toTextType(value);
    writer.text(text);
  }

  @Override public T read(XmlPullParser reader) throws Exception {
    T value = null;
    int event = reader.getEventType();
    while(event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.TEXT) {
        final String text = reader.getText();
        value = toObjectType(text);
        reader.next();//WHITESPACE_TEXT
        reader.next();//START_TAG
        break;
      }
      event = reader.next();
    }
    return value;
  }

  @Override public boolean isReadPossible(Class<?> expectedType) {
    return typeMatches(expectedType);
  }

  @Override public boolean isWritePossible(Class<?> expectedType) {
    return typeMatches(expectedType);
  }

  protected abstract String   toTextType(T refValue) throws Exception;
  protected abstract T        toObjectType(String text) throws Exception;
  protected abstract boolean  typeMatches(Class<?> referenceType);
}
