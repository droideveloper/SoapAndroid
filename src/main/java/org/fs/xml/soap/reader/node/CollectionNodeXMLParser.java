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

import org.fs.xml.soap.reader.ReaderUtility;
import org.fs.xml.soap.reader.SoapXMLParserFactory;
import org.fs.xml.soap.reader.text.BaseTextXMLParser;
import org.fs.xml.soap.reader.text.CollectionTextXMLParser;
import org.fs.xml.soap.reader.text.TextXMLParserFactory;
import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.fs.xml.soap.reflection.type.NodeTypeReference;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Collection;

public final class CollectionNodeXMLParser
    implements NodeXMLReader<XmlPullParser, NodeFieldReference>, NodeXMLWriter<XmlSerializer, NodeFieldReference> {

  private final String entryName;

  public CollectionNodeXMLParser(final String entryName) {
    this.entryName = entryName;
  }

  @Override public void read(XmlPullParser reader, NodeFieldReference ref) throws Exception {
    Collection<Object> collection = new ArrayList<>();
    int event = reader.getEventType();
    SoapXMLParserFactory soapFactory = SoapXMLParserFactory.sharedInstance();
    //end document cause exit
    while (event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        String nodeName = reader.getName();
        //skip collection start_tag
        if (!ref.name().equalsIgnoreCase(nodeName)) {
          //primitive
          if (entryName.equalsIgnoreCase(nodeName)) {
            String xsType = reader.getAttributeValue(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME);
            Class<?> typeClass = ReferenceUtility.xsType(xsType);
            if (ReferenceUtility.isPrimitiveType(typeClass)) {
              TextXMLParserFactory factory = TextXMLParserFactory.sharedInstance();
              BaseTextXMLParser<?> xmlReader = factory.readXMLParser(typeClass);
              if (xmlReader != null) {
                Object entry = xmlReader.read(reader);
                collection.add(entry);
                continue;
              } else {
                throw new NullPointerException("impossible primitive @{ " + typeClass.getName() + " }");
              }
            }
          } else if (soapFactory.getCollectionNodeName().equalsIgnoreCase(nodeName)) {
            TextXMLParserFactory factory = TextXMLParserFactory.sharedInstance();
            CollectionTextXMLParser xmlReader = factory.collectionXMLParser();
            Object entry = xmlReader.read(reader);
            collection.add(entry);
            continue;
          } else if (!TextUtils.isEmpty(nodeName)) {
            String xsClassName = reader.getAttributeValue(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME);
            Class<?> xsClass = ReferenceUtility.xsType(xsClassName);
            NodeTypeReference newRef = ReferenceUtility.toTypeReference(xsClass);
            NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
            TypeNodeXMLParser xmlReader = factory.typeXMLParser();
            if (xmlReader.isReadPossible(reader, newRef)) {
              xmlReader.read(reader, newRef);
              Object entry = newRef.get();
              collection.add(entry);
              continue;
            } else {
              throw new NullPointerException("impossible type @{ " + xsClass.getName() + " }");
            }
          }
        }
      }
      if (event == XmlPullParser.END_TAG) {
        String nodeName = reader.getName();
        if (ref.name().equalsIgnoreCase(nodeName)) {
          break;
        }
      }
      event = reader.next();
    }
    ref.set(collection);
    //go next START_TAG
    ReaderUtility.nextStartTag(reader);
  }

  @SuppressWarnings("unchecked")
  @Override public void write(XmlSerializer writer, NodeFieldReference ref) throws Exception {
    writer.startTag(ref.namespace(), ref.name());
    Object refValue = ref.get();
    if (refValue != null) {
      Collection<?> collection = ReferenceUtility.castAs(ref.get(), Collection.class);
      if(!collection.isEmpty()) {
        for (Object entry : collection) {
          if (entry != null) {
            Class<?> classEntry = entry.getClass();
            if (ReferenceUtility.isPrimitiveType(classEntry)) {
              TextXMLParserFactory factory = TextXMLParserFactory.sharedInstance();
              BaseTextXMLParser<?> xmlWriter = factory.writeXMLParser(classEntry);
              if (xmlWriter != null) {
                //start entry
                writer.startTag(null, entryName);
                //put xs:type
                writer.attribute(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME, ReferenceUtility.xsString(classEntry));
                //write with special parser
                BaseTextXMLParser xmlRawWriter = (BaseTextXMLParser) xmlWriter;
                xmlRawWriter.write(writer, entry);
                //end entry
                writer.endTag(null, entryName);
              } else {
                throw new NullPointerException("impossible primitive type @{ " + classEntry.getName() + " }");
              }
            } else if (ReferenceUtility.isExtensionType(classEntry)) {
              if (Collection.class.isAssignableFrom(classEntry)) {
                Collection<?> array = ReferenceUtility.castAs(entry, Collection.class);
                TextXMLParserFactory factory = TextXMLParserFactory.sharedInstance();
                CollectionTextXMLParser xmlWriter = factory.collectionXMLParser();
                if (xmlWriter.isWritePossible(classEntry)) {
                  xmlWriter.write(writer, array);
                } else {
                  throw new NullPointerException("impossible extension @{ " + classEntry.getName() + " }");
                }
              } else {
                throw new IllegalArgumentException("impossible extension type @{ " + classEntry.getName() + " }");
              }
            } else {
              NodeTypeReference newRef = ReferenceUtility.toTypeReference(entry);
              NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
              TypeNodeXMLParser xmlWriter = factory.typeXMLParser();
              if (xmlWriter.isWritePossible(newRef)) {
                xmlWriter.write(writer, newRef);
              } else {
                throw new NullPointerException("impossible type @{ " + classEntry.getName() + " }");
              }
            }
          }
        }
      }
    }
    writer.endTag(ref.namespace(), ref.name());
  }

  @Override public boolean isReadPossible(XmlPullParser reader, NodeFieldReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType) && nodeNameMatches(reader, ref.name());
  }

  @Override public boolean isWritePossible(NodeFieldReference ref) throws Exception {
    Class<?> referenceType = ReferenceUtility.castAs(ref.type());
    return typeMatches(referenceType);
  }

  protected boolean typeMatches(Class<?> referenceType) {
    return Collection.class.isAssignableFrom(referenceType);
  }

  protected boolean nodeNameMatches(XmlPullParser reader, String referenceName) throws Exception {
    final String cursorNodeName = reader.getName();
    return !TextUtils.isEmpty(cursorNodeName) && !TextUtils.isEmpty(referenceName) && referenceName.equalsIgnoreCase(cursorNodeName);
  }
}
