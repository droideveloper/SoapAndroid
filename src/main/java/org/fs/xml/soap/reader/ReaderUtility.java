package org.fs.xml.soap.reader;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Fatih on 03/07/16.
 * as org.fs.xml.soap.reader.ReaderUtility
 */
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
