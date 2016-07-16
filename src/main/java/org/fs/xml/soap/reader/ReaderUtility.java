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

import org.xmlpull.v1.XmlPullParser;

public final class ReaderUtility {

  private ReaderUtility() {
    throw new NullPointerException("you can not have instance");
  }

  /**
   * Reads till hit's next start_tag be careful if there is none it will exit
   * @param xmlReader XmlPullParser instance
   * @throws Exception
   */
  public static void nextStartTag(XmlPullParser xmlReader) throws Exception {
    int event = xmlReader.next();
    while (event != XmlPullParser.START_TAG) {
      if (event == XmlPullParser.END_DOCUMENT) {
        //just exit if we see end of document
        break;
      }
      event = xmlReader.next();
    }
  }

  /**
   * Reads three #next() on XmlPullParser instance
   * @param xmlReader XmlPullParser instance
   * @throws Exception
   */
  public static void nextType(XmlPullParser xmlReader) throws Exception {
    //TEXT or WHITESPACE
    xmlReader.next();
    int event = xmlReader.next();
    Assert.assertEquals(XmlPullParser.END_TAG, event);
    event = xmlReader.next();
    Assert.assertEquals(XmlPullParser.START_TAG, event);
  }
}
