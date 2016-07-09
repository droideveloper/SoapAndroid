package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.FloatTextXMLParser
 */
public final class FloatTextXMLParser extends BaseTextXMLParser<Float> {

    @Override protected String toTextType(Float refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Float toObjectType(String text) throws Exception {
        return Float.parseFloat(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return float.class.equals(referenceType) || Float.class.equals(referenceType);
    }
}
