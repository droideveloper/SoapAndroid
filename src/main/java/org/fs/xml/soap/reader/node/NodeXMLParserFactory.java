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

import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class NodeXMLParserFactory {

  private final List<BaseNodeXMLParser<?>> nodeXMLParsers;

  private CollectionNodeXMLParser collectionXMLParser;
  private final TypeNodeXMLParser       typeXMLParser;

  private static NodeXMLParserFactory sharedInstance;

  public void addBooleanXMLParser(int style) {
    nodeXMLParsers.add(new BooleanNodeXMLParser(style));
  }

  public void addDateXMLParser(SimpleDateFormat parser) {
    nodeXMLParsers.add(new DateNodeXMLParser(parser));
  }

  public void addCollectionXMLParser(String entryName) {
    collectionXMLParser = new CollectionNodeXMLParser(entryName);
  }

  public CollectionNodeXMLParser collectionXMLParser() {
        return collectionXMLParser;
    }

  public TypeNodeXMLParser typeXMLParser() {
        return typeXMLParser;
    }

  public BaseNodeXMLParser<?> writeXMLParser(NodeFieldReference ref) throws Exception {
    for (int i = 0, z = nodeXMLParsers.size(); i < z; i++) {
      BaseNodeXMLParser<?> parser = nodeXMLParsers.get(i);
      if (parser.isWritePossible(ref)) {
        return parser;
      }
    }
    return null;
  }

  public BaseNodeXMLParser<?> readXMLParser(XmlPullParser reader, NodeFieldReference ref) throws Exception {
    for (int i = 0, z = nodeXMLParsers.size(); i < z; i++) {
      BaseNodeXMLParser<?> parser = nodeXMLParsers.get(i);
      if(parser.isReadPossible(reader, ref)) {
        return parser;
      }
    }
    return null;
  }

  public static NodeXMLParserFactory sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new NodeXMLParserFactory();
    }
    return sharedInstance;
  }

  private NodeXMLParserFactory() {
    nodeXMLParsers = new ArrayList<>();
    //defaults
    nodeXMLParsers.add(new ByteArrayNodeXMLParser());
    nodeXMLParsers.add(new DoubleNodeXMLParser());
    nodeXMLParsers.add(new FloatNodeXMLParser());
    nodeXMLParsers.add(new IntegerNodeXMLParser());
    nodeXMLParsers.add(new LongNodeXMLParser());
    nodeXMLParsers.add(new ShortNodeXMLParser());
    nodeXMLParsers.add(new StringNodeXMLParser());
    //default type parser
    typeXMLParser = new TypeNodeXMLParser();
  }
}
