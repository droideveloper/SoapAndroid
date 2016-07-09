package org.fs.xml.soap.reader.node;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.DateNodeXMLParser
 */
public final class DateNodeXMLParser extends BaseNodeXMLParser<Date> {

    private final SimpleDateFormat parser;

    public DateNodeXMLParser(SimpleDateFormat parser) {
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
