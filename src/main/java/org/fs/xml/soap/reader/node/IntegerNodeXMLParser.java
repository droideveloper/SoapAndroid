package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.IntegerNodeXMLParser
 */
public final class IntegerNodeXMLParser extends BaseNodeXMLParser<Integer> {

    @Override protected String toTextType(Integer refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Integer toReferenceType(String text) throws Exception {
        return Integer.parseInt(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Integer.class.equals(referenceType) || int.class.equals(referenceType);
    }
}
