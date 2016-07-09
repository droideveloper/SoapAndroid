package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.DoubleTextXMLParser
 */
public final class DoubleTextXMLParser extends BaseTextXMLParser<Double> {

    @Override protected String toTextType(Double refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Double toObjectType(String text) throws Exception {
        return Double.parseDouble(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return double.class.equals(referenceType) ||Double.class.equals(referenceType);
    }
}
