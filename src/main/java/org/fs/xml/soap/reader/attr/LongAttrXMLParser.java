package org.fs.xml.soap.reader.attr;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.attr.LongAttrXMLParser
 */
public final class LongAttrXMLParser extends BaseAttrXMLParser<Long> {

    @Override protected String toTextType(Long refValue) throws Exception {
        return String.valueOf(refValue);
    }

    @Override protected Long toReferenceType(String text) throws Exception {
        return Long.parseLong(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Long.class.equals(referenceType) || long.class.equals(referenceType);
    }
}
