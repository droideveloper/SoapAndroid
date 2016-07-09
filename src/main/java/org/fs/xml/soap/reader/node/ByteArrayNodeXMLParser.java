package org.fs.xml.soap.reader.node;

import android.util.Base64;

import java.nio.charset.Charset;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.ByteArrayNodeXMLParser
 */
public final class ByteArrayNodeXMLParser extends BaseNodeXMLParser<byte[]> {

    @Override protected String toTextType(byte[] refValue) throws Exception {
        byte[] buffer = Base64.encode(refValue, Base64.NO_WRAP);
        return new String(buffer, Charset.forName("UTF-8"));
    }

    @Override protected byte[] toReferenceType(String text) throws Exception {
        byte[] buffer = text.getBytes(Charset.forName("UTF-8"));
        return Base64.decode(buffer, Base64.NO_WRAP);
    }

    @Override protected boolean typeMatches(Class<?> referenceType) {
        return byte[].class.equals(referenceType);
    }
}
