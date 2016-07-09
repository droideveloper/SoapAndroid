package org.fs.xml.soap.net;

import org.fs.xml.soap.reader.SoapXMLParserFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Fatih on 09/07/16.
 * as org.fs.xml.soap.net.SoapConverterFactory
 */
public final class SoapConverterFactory extends Converter.Factory {

    private final SoapXMLParserFactory factory;

    public SoapConverterFactory(final SoapXMLParserFactory factory) {
        this.factory = factory;
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new SoapRequestBodyConverter<>(factory);
    }

    @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new SoapResponseBodyConverter<>(factory, type);
    }
}
