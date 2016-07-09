package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.StringTextXMLParser
 */
public final class StringTextXMLParser extends BaseTextXMLParser<String> {

    @Override protected String toTextType(String refValue) throws Exception {
        return refValue;
    }

    @Override protected String toObjectType(String text) throws Exception {
        return text;
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return String.class.equals(referenceType);
    }
}
