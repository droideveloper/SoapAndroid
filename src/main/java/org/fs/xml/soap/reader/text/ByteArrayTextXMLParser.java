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

import android.util.Base64;

public final class ByteArrayTextXMLParser extends BaseTextXMLParser<byte[]> {

  @Override protected String toTextType(byte[] refValue) throws Exception {
    byte[] buffer = Base64.encode(refValue, Base64.NO_WRAP);
    return new String(buffer, "UTF-8");
  }

  @Override protected byte[] toObjectType(String text) throws Exception {
    byte[] buffer = text.getBytes("UTF-8");
    return Base64.decode(buffer, Base64.NO_WRAP);
  }

  @Override protected boolean typeMatches(Class<?> referenceType) {
    return byte[].class.equals(referenceType);
  }
}
