package org.fs.xml.soap.reader.text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.DateTextXMLParser
 */
public final class DateTextXMLParser extends BaseTextXMLParser<Date> {

    private final SimpleDateFormat parser;

    public DateTextXMLParser(SimpleDateFormat parser) {
        this.parser = parser;
        if (parser == null) {
            throw new NullPointerException("you should provide valid, formatter");
        }
    }

    @Override protected String toTextType(Date refValue) throws Exception {
        return parser.format(refValue);
    }

    @Override protected Date toObjectType(String text) throws Exception {
        return parser.parse(text);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return Date.class.equals(referenceType);
    }
}
