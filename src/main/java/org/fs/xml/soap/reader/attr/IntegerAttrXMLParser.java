package org.fs.xml.soap.reader.attr;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.attr.IntegerAttrXMLParser
 */
public final class IntegerAttrXMLParser extends BaseAttrXMLParser<Integer> {

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
