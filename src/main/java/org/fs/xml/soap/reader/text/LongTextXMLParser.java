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

public final class LongTextXMLParser extends BaseTextXMLParser<Long> {

  @Override protected String toTextType(Long refValue) throws Exception {
    return String.valueOf(refValue);
  }

  @Override protected Long toObjectType(String text) throws Exception {
    return Long.parseLong(text);
  }

  @Override protected boolean typeMatches(Class<?> referenceType) {
    return Long.class.equals(referenceType) || long.class.equals(referenceType);
  }
}
