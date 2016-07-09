package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.LongTextXMLParser
 */
public final class LongTextXMLParser extends BaseTextXMLParser<Long> {

    @Override protected String toTextType(Long refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Long toObjectType(String text) throws Exception {
        return Long.parseLong(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Long.class.equals(referenceType) || long.class.equals(referenceType);
    }
}
