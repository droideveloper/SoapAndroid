package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.StringNodeXMLParser
 */
public final class StringNodeXMLParser extends BaseNodeXMLParser<String> {

    @Override protected String toTextType(String refValue) throws Exception {
        return refValue;
    }

    @Override protected String toReferenceType(String text) throws Exception {
        return text;
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return String.class.equals(referenceType);
    }
}
