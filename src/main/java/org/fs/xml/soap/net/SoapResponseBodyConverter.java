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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class SoapResponseBodyConverter<T>
    implements Converter<ResponseBody, T> {

  private final SoapXMLParserFactory factory;
  private final Type                 type;

  public SoapResponseBodyConverter(SoapXMLParserFactory factory, Type type) {
    this.factory = factory;
    this.type = type;
  }

  @Override public T convert(ResponseBody value) throws IOException {
    try {
      //we do not check if there is fault or not
      InputStream in = value.byteStream();
      Class<T> c = (Class<T>) type;
      return factory.deserialize(in, c, true);//we just read only for inside body do we need anything else ?
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
