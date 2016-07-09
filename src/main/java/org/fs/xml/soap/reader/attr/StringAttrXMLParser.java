package org.fs.xml.soap.reader.attr;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.attr.StringAttrXMLParser
 */
public final class StringAttrXMLParser extends BaseAttrXMLParser<String> {

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
