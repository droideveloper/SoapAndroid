package org.fs.xml.soap.net;

import org.fs.xml.soap.reader.SoapXMLParserFactory;
import org.fs.xml.soap.type.SoapEnvelope;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by Fatih on 09/07/16.
 * as org.fs.xml.soap.net.SoapRequestBodyConvert
 */
public final class SoapRequestBodyConverter<T> implements Converter<T, RequestBody> {

    //put this in okhttp client of retrofit... yeah I know.
    public static final SoapInterceptor SOAP_INTERCEPTOR = new SoapInterceptor();

    private final SoapXMLParserFactory factory;

    public SoapRequestBodyConverter(SoapXMLParserFactory factory) {
        this.factory = factory;
    }

    @Override public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        try {
            if (value instanceof SoapEnvelope) {
                SoapEnvelope env = (SoapEnvelope) value;
                SOAP_INTERCEPTOR.setSoapAction(env.getSoapAction());//we do need to update it
            }
            OutputStreamWriter writer = new OutputStreamWriter(buffer.outputStream(), Charset.forName("utf-8"));
            factory.serialize(writer, value);
            writer.flush();
            return RequestBody.create(MediaType.parse("text/xml; charset=utf-8"), buffer.readByteString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final static class SoapInterceptor implements Interceptor {

        private final static String HEADER_SOAP_ACTION = "SOAPAction";
        private String soapAction;

        public SoapInterceptor setSoapAction(String soapAction) {
            this.soapAction = soapAction;
            return this;
        }

        /**
         * Since we need to put soapAction on our request header this is the best way to intercept it
         * @param chain Chain instance
         * @return Response instance
         * @throws IOException io error
         */
        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                             .addHeader(HEADER_SOAP_ACTION, soapAction)
                             .build();
            return chain.proceed(request);
        }
    }
}
