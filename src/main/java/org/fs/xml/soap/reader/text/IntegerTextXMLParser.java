package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.IntegerTextXMLParser
 */
public final class IntegerTextXMLParser extends BaseTextXMLParser<Integer> {

    @Override protected String toTextType(Integer refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Integer toObjectType(String text) throws Exception {
        return Integer.parseInt(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return int.class.equals(referenceType) || Integer.class.equals(referenceType);
    }
}
