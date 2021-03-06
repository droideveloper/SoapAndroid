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
package org.fs.xml.soap.reader;

import junit.framework.Assert;

import org.fs.xml.soap.reader.attr.AttrXMLParserFactory;
import org.fs.xml.soap.reader.node.NodeXMLParserFactory;
import org.fs.xml.soap.reader.node.TypeNodeXMLParser;
import org.fs.xml.soap.reader.text.TextXMLParserFactory;
import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.type.NodeTypeReference;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class SoapXMLParserFactory {

  private final static String UTF_8 = "UTF-8";

  private final static String PREFIX_SOAP        = "soap";
  private final static String PREFIX_ENC         = "enc";

  public  final static String NAMESPACE_ENC      = "http://schemas.xmlsoap.org/soap/encoding/";
  public  final static String NAMESPACE_SOAP     = "http://schemas.xmlsoap.org/soap/envelope/";

  private final static String SOAP_BODY          = "body";

  private final static String FEATURE_PRETTY     = "http://xmlpull.org/v1/doc/features.html#indent-output";

  private final Map<String, String> registeredNamespaces;

  private AttrXMLParserFactory attrFactory;
  private NodeXMLParserFactory nodeFactory;
  private TextXMLParserFactory textFactory;

  private static SoapXMLParserFactory sharedInstance;

  private String collectionNodeName;

  public static SoapXMLParserFactory sharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new SoapXMLParserFactory();
    }
    return sharedInstance;
  }

  private SoapXMLParserFactory() {
    //defaults
    registeredNamespaces = new HashMap<>();
    registeredNamespaces.put(PREFIX_SOAP, NAMESPACE_SOAP);
    registeredNamespaces.put(PREFIX_ENC, NAMESPACE_ENC);
    registeredNamespaces.put(ReferenceUtility.PREFIX_XS,  ReferenceUtility.NAMESPACE_XS);
    registeredNamespaces.put(ReferenceUtility.PREFIX_XSI, ReferenceUtility.NAMESPACE_XSI);
    //defaults
    attrFactory = AttrXMLParserFactory.sharedInstance();
    nodeFactory = NodeXMLParserFactory.sharedInstance();
    textFactory = TextXMLParserFactory.sharedInstance();
  }

  public void registerDateParser(SimpleDateFormat parser) {
    attrFactory.addDateXMLParser(parser);
    nodeFactory.addDateXMLParser(parser);
    textFactory.addDateXMLParser(parser);
  }

  public void registerBooleanParser(int style) {
    attrFactory.addBooleanXMLParser(style);
    nodeFactory.addBooleanXMLParser(style);
    textFactory.addBooleanXMLParser(style);
  }

  public void registerCollectionParser(String typeName, String entryName) {
    nodeFactory.addCollectionXMLParser(entryName);
    textFactory.addCollectionXMLParser(typeName, entryName);
    this.collectionNodeName = typeName;
  }

  public void registerNamespace(String prefix, String namespace) {
    registeredNamespaces.put(prefix, namespace);
  }

  public String getCollectionNodeName() {
    return collectionNodeName;
  }

  public <T> void serialize(OutputStreamWriter out, T object) throws Exception {
    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
    parserFactory.setNamespaceAware(true);//in write does it make any change?
    XmlSerializer serializer = parserFactory.newSerializer();
    serializer.setOutput(out);
    //add namespaces
    for (Map.Entry<String, String> entry : registeredNamespaces.entrySet()) {
      serializer.setPrefix(entry.getKey(), entry.getValue());
    }
    //set feature
    serializer.setFeature(FEATURE_PRETTY, true);
    //charset
    serializer.startDocument(UTF_8, null);
    //convert object into reference
    NodeTypeReference ref = ReferenceUtility.toTypeReference(object);
    TypeNodeXMLParser typeWriter = nodeFactory.typeXMLParser();
    typeWriter.write(serializer, ref);
    //end doc
    serializer.endDocument();
    //flush
    serializer.flush();
  }

  public <T> T  deserialize(InputStream in, Class<T> clazz, boolean fromBody) throws Exception {
    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
    parserFactory.setNamespaceAware(true);
    XmlPullParser deserializer = parserFactory.newPullParser();
    deserializer.setInput(in, UTF_8);
    if (fromBody) {
      nextTagBody(deserializer);//go START_TAG of ${Body}
    }else {
      //if we can read it
      int event = deserializer.getEventType();
      Assert.assertEquals(event, XmlPullParser.START_DOCUMENT);
      event = deserializer.next();
      Assert.assertEquals(event, XmlPullParser.START_TAG);
    }
    //we are good
    NodeTypeReference ref = ReferenceUtility.toTypeReference(clazz);
    TypeNodeXMLParser reader = nodeFactory.typeXMLParser();
    if (reader.isReadPossible(deserializer, ref)) {reader.read(deserializer, ref);
      return ReferenceUtility.castAs(ref.get(), clazz);
    } else {
      //try to read all whatever left
      StringBuilder error = new StringBuilder();
      int event = deserializer.getEventType();
      while(event != XmlPullParser.END_DOCUMENT) {
        if (event == XmlPullParser.START_TAG) {
          error.append(String.format(Locale.US, "<%s>", deserializer.getName()));
          error.append("\n");
        } else if (event == XmlPullParser.TEXT) {
          error.append(deserializer.getText());
          error.append("\n");
        } else if (event == XmlPullParser.END_TAG) {
          error.append(String.format(Locale.US, "</%s>", deserializer.getName()));
          error.append("\n");
        }
        event = deserializer.next();
      }
      throw new IllegalArgumentException("impossible type @{ " + clazz.getName() + " }\n" + error.toString());
    }
  }

  /**
   * <p>in soap communication take place mainly in body so we can ignore rest if we don't need any kind of header etc.</p>
   * @param reader XmlPullParser instance
   * @throws Exception XmlPullParserException
   */
  public void nextTagBody(XmlPullParser reader) throws Exception {
    int event = reader.getEventType();
    //let's hit it like that
    while (event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        final String nodeName = reader.getName();
        if (SOAP_BODY.equalsIgnoreCase(nodeName)) {
          reader.next();//TEXT AS WHITESPACE
          reader.next();//START_TAG of Next
          break;
        }
      }
      event = reader.next();
    }
  }
}
