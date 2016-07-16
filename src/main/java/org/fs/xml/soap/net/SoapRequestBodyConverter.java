/*
 * Copyright (C) 2016 Fatih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.xml.soap.net;

import org.fs.xml.soap.reader.SoapXMLParserFactory;
import org.fs.xml.soap.type.SoapEnvelope;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Converter;

public final class SoapRequestBodyConverter<T>
    implements Converter<T, RequestBody> {

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
          .addHeader(HEADER_SOAP_ACTION, String.format(Locale.US, "\"%s\"", soapAction))//bug fix
          .build();
      return chain.proceed(request);
    }
  }
}
