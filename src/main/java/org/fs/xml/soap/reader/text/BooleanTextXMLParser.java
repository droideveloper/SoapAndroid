package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.BooleanTextXMLParser
 */
public final class BooleanTextXMLParser extends BaseTextXMLParser<Boolean> {

    private final int style;

    public BooleanTextXMLParser(int style) {
        this.style = style;
        if (style < 0x01 || style > 0x02) {
            throw new NullPointerException("style should be 0x01 or 0x02");
        }
    }

    @Override protected String toTextType(Boolean refValue) throws Exception {
        if (style == 0x01) {
            return String.valueOf(refValue ? 1 : 0);
        } else if (style == 0x02) {
            return String.valueOf(refValue);
        } else {
            throw new IllegalAccessException("you should not modify style via reflection");
        }
    }

    @Override protected Boolean toObjectType(String text) throws Exception {
        if (style == 0x01) {
            return Integer.parseInt(text) == 1;
        } else if(style == 0x02){
            return Boolean.valueOf(text);
        } else {
            throw new IllegalAccessException("you should not modify style via reflection");
        }
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return boolean.class.equals(referenceType) || Boolean.class.equals(referenceType);
    }
}
