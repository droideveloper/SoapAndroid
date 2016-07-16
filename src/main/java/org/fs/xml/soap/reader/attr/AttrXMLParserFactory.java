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

import org.fs.xml.soap.reflection.field.AttributeFieldReference;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class AttrXMLParserFactory {

  private final List<BaseAttrXMLParser<?>> attrXMLParsers;

  private static AttrXMLParserFactory sharedInstance;

  public void addBooleanXMLParser(int style) {
    attrXMLParsers.add(new BooleanAttrXMLParser(style));
  }

  public void addDateXMLParser(SimpleDateFormat parser) {
    attrXMLParsers.add(new DateAttrXMLParser(parser));
  }

  public BaseAttrXMLParser<?> writeXMLParser(AttributeFieldReference ref) throws Exception {
    for (int i = 0, z = attrXMLParsers.size(); i < z; i++) {
      BaseAttrXMLParser<?> parser = attrXMLParsers.get(i);
      if (parser.isWritePossible(ref)) {
        return parser;
      }
    }
    return null;
  }

  public BaseAttrXMLParser<?> readXMLParser(XmlPullParser reader, AttributeFieldReference ref) throws Exception {
    for (int i = 0, z = attrXMLParsers.size(); i < z; i++) {
      BaseAttrXMLParser<?> parser = attrXMLParsers.get(i);
      if(parser.isReadPossible(reader, ref)) {
        return parser;
      }
    }
    return null;
  }

  public static AttrXMLParserFactory sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new AttrXMLParserFactory();
    }
    return sharedInstance;
  }

  private AttrXMLParserFactory() {
    //defaults
    attrXMLParsers = new ArrayList<>();
    attrXMLParsers.add(new ByteArrayAttrXMLParser());
    attrXMLParsers.add(new DoubleAttrXMLParser());
    attrXMLParsers.add(new FloatAttrXMLParser());
    attrXMLParsers.add(new IntegerAttrXMLParser());
    attrXMLParsers.add(new LongAttrXMLParser());
    attrXMLParsers.add(new ShotAttrXMLParser());
    attrXMLParsers.add(new StringAttrXMLParser());
  }
}
