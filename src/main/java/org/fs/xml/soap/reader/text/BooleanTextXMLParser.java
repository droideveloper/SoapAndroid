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

public final class BooleanTextXMLParser extends BaseTextXMLParser<Boolean> {

  private final int style;

  public BooleanTextXMLParser(int style) {
    this.style = style;
    if (style < 0x01 || style > 0x02) {
      throw new NullPointerException("style should be 0x01 or 0x02");
    }
  }

  @Override protected String toTextType(Boolean refValue) throws Exception {
    if (style == 0x01) {
      return String.valueOf(refValue ? 1 : 0);
    } else if (style == 0x02) {
      return String.valueOf(refValue);
    } else {
      throw new IllegalAccessException("you should not modify style via reflection");
    }
  }

  @Override protected Boolean toObjectType(String text) throws Exception {
    if (style == 0x01) {
      return Integer.parseInt(text) == 1;
    } else if(style == 0x02){
      return Boolean.valueOf(text);
    } else {
      throw new IllegalAccessException("you should not modify style via reflection");
    }
  }

  @Override protected boolean typeMatches(Class<?> referenceType) {
    return boolean.class.equals(referenceType) || Boolean.class.equals(referenceType);
  }
}
