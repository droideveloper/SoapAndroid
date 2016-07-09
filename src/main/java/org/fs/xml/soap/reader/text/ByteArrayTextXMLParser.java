package org.fs.xml.soap.reader.text;

import android.util.Base64;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.ByteArrayTextXMLParser
 */
public final class ByteArrayTextXMLParser extends BaseTextXMLParser<byte[]> {

    @Override protected String toTextType(byte[] refValue) throws Exception {
        byte[] buffer = Base64.encode(refValue, Base64.NO_WRAP);
        return new String(buffer, "UTF-8");
    }

    @Override protected byte[] toObjectType(String text) throws Exception {
        byte[] buffer = text.getBytes("UTF-8");
        return Base64.decode(buffer, Base64.NO_WRAP);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return byte[].class.equals(referenceType);
    }
}
