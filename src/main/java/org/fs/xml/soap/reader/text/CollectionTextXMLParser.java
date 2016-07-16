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

import android.text.TextUtils;

import org.fs.xml.soap.reader.ReaderUtility;
import org.fs.xml.soap.reader.node.NodeXMLParserFactory;
import org.fs.xml.soap.reader.node.TypeNodeXMLParser;
import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.type.NodeTypeReference;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Collection;

public final class CollectionTextXMLParser
    implements TextXMLWriter<XmlSerializer, Collection<?>>, TextXMLReader<XmlPullParser, Collection<?>>{

  private final String parentName;
  private final String entryName;

  public CollectionTextXMLParser(String parentName, String entryName) {
    this.parentName = parentName;
    this.entryName = entryName;
  }

  @Override public Collection<?> read(XmlPullParser reader) throws Exception {
    Collection<Object> collection = new ArrayList<>();
    int event = reader.getEventType();
    boolean start = true;
    //end document cause exit
    while (event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.START_TAG) {
        String nodeName = reader.getName();
        if (entryName.equalsIgnoreCase(nodeName)) {
          String typeString = reader.getAttributeValue(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME);
          if (!TextUtils.isEmpty(typeString)) {
            Class<?> entryClass = ReferenceUtility.xsType(typeString);
            TextXMLParserFactory factory = TextXMLParserFactory.sharedInstance();
            BaseTextXMLParser<?> xmlReader = factory.readXMLParser(entryClass);
            if (xmlReader != null) {
              Object entry = xmlReader.read(reader);
              collection.add(entry);
              continue;
            } else {
              throw new NullPointerException("impossible primitive @{ " + entryClass.getName() + " }");
            }
          }
          //here the dark side comes in hand we are already here so want to feel how the bufferOverFlow gone through ?
        } else if (parentName.equalsIgnoreCase(nodeName)) {
          if (!start) {
            Object entry = read(reader);
            collection.add(entry);
            continue;
          }
        } else if (!TextUtils.isEmpty(nodeName)) {
          String xsType = reader.getAttributeValue(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME);
          Class<?> typeClass = ReferenceUtility.xsType(xsType);
          NodeTypeReference ref = ReferenceUtility.toTypeReference(typeClass);
          NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
          TypeNodeXMLParser xmlReader = factory.typeXMLParser();
          if (xmlReader.isReadPossible(reader, ref)) {
            xmlReader.read(reader, ref);
            Object entry = ref.get();
            collection.add(entry);
            continue;
          } else {
            throw new NullPointerException("impossible type @{ " + typeClass.getName() + " }");
          }
        }
      } else if (event == XmlPullParser.END_TAG) {
        String nodeName = reader.getName();
        if (parentName.equalsIgnoreCase(nodeName)) {
          break;
        }
      }
      start = false;
      event = reader.next();
    }
    //go next START_TAG
    ReaderUtility.nextStartTag(reader);
    return collection;
  }

  @SuppressWarnings("unchecked")
  @Override public void write(XmlSerializer writer, Collection<?> value) throws Exception {
    writer.startTag(null, parentName);
    if (!value.isEmpty()) {
      for (Object entry : value) {
        if (entry != null) {
          Class<?> entryClass = entry.getClass();
          if (ReferenceUtility.isPrimitiveType(entryClass)) {
            TextXMLParserFactory factory = TextXMLParserFactory.sharedInstance();
            BaseTextXMLParser xmlWriter = factory.writeXMLParser(entryClass);
            if (xmlWriter != null) {
              //start entry
              writer.startTag(null, entryName);
              //xs:type
              writer.attribute(ReferenceUtility.NAMESPACE_XSI, ReferenceUtility.ATTR_TYPE_NAME, ReferenceUtility.xsString(entryClass));
              //write value
              xmlWriter.write(writer, entry);
              //end entry
              writer.endTag(null, entryName);
            } else {
              throw new NullPointerException("impossible primitive @{ " + entryClass.getName() + " }");
            }
          } else if(ReferenceUtility.isExtensionType(entryClass)) {
            if (Collection.class.isAssignableFrom(entryClass)) {
              write(writer, ReferenceUtility.castAs(entry, Collection.class));
            } else {
              throw new NullPointerException("impossible extension @{ " + entryClass.getName() + " }");
            }
          } else {
            NodeTypeReference nodeRef = ReferenceUtility.toTypeReference(entry);
            NodeXMLParserFactory factory = NodeXMLParserFactory.sharedInstance();
            TypeNodeXMLParser xmlWriter = factory.typeXMLParser();
            if (xmlWriter.isWritePossible(nodeRef)) {
              xmlWriter.write(writer, nodeRef);
            } else {
              throw new NullPointerException("impossible type @{ " + entryClass.getName() + " }");
            }
          }
        }
      }
    }
    writer.endTag(null, parentName);
  }

  @Override public boolean isWritePossible(Class<?> expectedType) {
    return typeMatches(expectedType);
  }

  @Override public boolean isReadPossible(Class<?> expectedType) {
    return typeMatches(expectedType);
  }

  protected boolean  typeMatches(Class<?> referenceType) {
    return Collection.class.isAssignableFrom(referenceType);
  }
}
