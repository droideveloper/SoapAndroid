package org.fs.xml.soap.reader.attr;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.attr.DateAttrXMLReader
 */
public final class DateAttrXMLParser extends BaseAttrXMLParser<Date> {

    private final SimpleDateFormat parser;

    public DateAttrXMLParser(SimpleDateFormat parser) {
        this.parser = parser;
        if (parser == null) {
            throw new NullPointerException("you should provide valid, formatter");
        }
    }

    @Override protected Date toReferenceType(String text) throws Exception{
        return parser.parse(text);
    }

    @Override protected String toTextType(Date refValue) throws Exception {
        return parser.format(refValue);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Date.class.equals(referenceType);
    }
}
