package org.fs.xml.soap.reader.attr;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.attr.ShotAttrXMLParser
 */
public final class ShotAttrXMLParser extends BaseAttrXMLParser<Short> {

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
