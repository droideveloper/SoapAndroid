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

public final class IntegerAttrXMLParser extends BaseAttrXMLParser<Integer> {

  @Override protected String toTextType(Integer refValue) throws Exception {
    return String.valueOf(refValue);
  }

  @Override protected Integer toReferenceType(String text) throws Exception {
    return Integer.parseInt(text);
  }

  @Override protected boolean typeMatches(Class<?> referenceType) {
    return Integer.class.equals(referenceType) || int.class.equals(referenceType);
  }
}
