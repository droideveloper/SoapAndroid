package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.DoubleNodeXMLParser
 */
public final class DoubleNodeXMLParser extends BaseNodeXMLParser<Double> {

    @Override protected String toTextType(Double refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Double toReferenceType(String text) throws Exception {
        return Double.parseDouble(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Double.class.equals(referenceType) || double.class.equals(referenceType);
    }
}
