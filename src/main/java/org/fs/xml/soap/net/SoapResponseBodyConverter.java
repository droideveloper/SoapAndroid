package org.fs.xml.soap.net;

import org.fs.xml.soap.reader.SoapXMLParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Fatih on 09/07/16.
 * as org.fs.xml.soap.net.SoapResponseBodyConverter
 */
public final class SoapResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final SoapXMLParserFactory factory;
    private final Type                 type;

    public SoapResponseBodyConverter(SoapXMLParserFactory factory, Type type) {
        this.factory = factory;
        this.type = type;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        try {
            InputStream in = value.byteStream();
            Class<T> c = (Class<T>) type;
            return factory.deserialize(in, c, true);//we just read only for inside body do we need anything else ?
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
