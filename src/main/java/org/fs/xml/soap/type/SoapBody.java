package org.fs.xml.soap.type;

import org.fs.xml.soap.annotation.Node;
import org.fs.xml.soap.reader.SoapXMLParserFactory;

/**
 * Created by Fatih on 09/07/16.
 * as org.fs.xml.soap.type.SoapBody
 */
@Node(name = "Body",
        namespace = SoapXMLParserFactory.NAMESPACE_SOAP)
public final class SoapBody {

    private Object o;

    public SoapBody() { }
    public <T> SoapBody(T o) {
        this.o = o;
    }
}
