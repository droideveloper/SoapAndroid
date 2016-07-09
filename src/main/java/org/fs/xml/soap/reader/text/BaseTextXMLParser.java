package org.fs.xml.soap.reader.text;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.BaseTextXMLParser
 */
public abstract class BaseTextXMLParser<T> implements TextXMLReader<XmlPullParser, T>, TextXMLWriter<XmlSerializer, T> {

    @Override public void write(XmlSerializer writer, T value) throws Exception {
        final String text = toTextType(value);
        writer.text(text);
    }

    @Override public T read(XmlPullParser reader) throws Exception {
        T value = null;
        int event = reader.getEventType();
        while(event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.TEXT) {
                final String text = reader.getText();
                value = toObjectType(text);
                reader.next();//WHITESPACE_TEXT
                reader.next();//START_TAG
                break;
            }
            event = reader.next();
        }
        return value;
    }

    @Override public boolean isReadPossible(Class<?> expectedType) {
        return typeMatches(expectedType);
    }

    @Override public boolean isWritePossible(Class<?> expectedType) {
        return typeMatches(expectedType);
    }

    protected abstract String   toTextType(T refValue) throws Exception;
    protected abstract T        toObjectType(String text) throws Exception;
    protected abstract boolean  typeMatches(Class<?> referenceType);
}
