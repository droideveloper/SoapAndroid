package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.FloatNodeXMLParser
 */
public final class FloatNodeXMLParser extends BaseNodeXMLParser<Float> {

    @Override protected String toTextType(Float refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Float toReferenceType(String text) throws Exception {
        return Float.parseFloat(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Float.class.equals(referenceType) || float.class.equals(referenceType);
    }
}
