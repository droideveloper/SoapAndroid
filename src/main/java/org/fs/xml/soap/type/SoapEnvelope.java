package org.fs.xml.soap.type;

import android.text.TextUtils;

import org.fs.xml.soap.annotation.Ignore;
import org.fs.xml.soap.annotation.Node;
import org.fs.xml.soap.reader.SoapXMLParserFactory;

/**
 * Created by Fatih on 09/07/16.
 * as org.fs.xml.soap.type.SoapEnvelope
 */
@Node(name = "Envelope",
        namespace = SoapXMLParserFactory.NAMESPACE_SOAP)
public final class SoapEnvelope {

    private SoapHeader a;
    private SoapBody   b;

    @Ignore
    private String soapAction;

    public SoapEnvelope(String soapAction) {
        this.soapAction = soapAction;
        if (TextUtils.isEmpty(soapAction)) {
            throw new RuntimeException("soapAction can not be null");
        }
    }

    public SoapEnvelope addHeader(SoapHeader a) {
        this.a = a;
        return this;
    }

    public SoapEnvelope addBody(SoapBody b) {
        this.b = b;
        return this;
    }
}
