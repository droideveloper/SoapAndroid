package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.ShortNodeXMLParser
 */
public final class ShortNodeXMLParser extends BaseNodeXMLParser<Short> {

    @Override protected String toTextType(Short refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Short toReferenceType(String text) throws Exception {
        return Short.parseShort(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return short.class.equals(referenceType) || Short.class.equals(referenceType);
    }
}
