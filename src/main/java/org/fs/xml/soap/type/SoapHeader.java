package org.fs.xml.soap.type;

import org.fs.xml.soap.annotation.Node;
import org.fs.xml.soap.reader.SoapXMLParserFactory;

/**
 * Created by Fatih on 09/07/16.
 * as org.fs.xml.soap.type.SoapHeader
 */
@Node(name = "Header",
        namespace = SoapXMLParserFactory.NAMESPACE_SOAP)
public final class SoapHeader {

    public Object o;

    public SoapHeader() { }
    public <T> SoapHeader(T o) {
        this.o = o;
    }
}
