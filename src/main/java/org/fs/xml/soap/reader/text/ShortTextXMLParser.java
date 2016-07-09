package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.ShortTextXMLParser
 */
public final class ShortTextXMLParser extends BaseTextXMLParser<Short> {

    @Override protected String toTextType(Short refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Short toObjectType(String text) throws Exception {
        return Short.parseShort(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return short.class.equals(referenceType) || Short.class.equals(referenceType);
    }
}
