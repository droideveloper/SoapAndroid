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

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateAttrXMLParser extends BaseAttrXMLParser<Date> {

  private final SimpleDateFormat parser;

  public DateAttrXMLParser(SimpleDateFormat parser) {
    this.parser = parser;
    if (parser == null) {
      throw new NullPointerException("you should provide valid, formatter");
    }
  }

  @Override protected Date toReferenceType(String text) throws Exception{
    return parser.parse(text);
  }

  @Override protected String toTextType(Date refValue) throws Exception {
    return parser.format(refValue);
  }

  @Override protected boolean typeMatches(Class<?> referenceType) {
    return Date.class.equals(referenceType);
  }
}
