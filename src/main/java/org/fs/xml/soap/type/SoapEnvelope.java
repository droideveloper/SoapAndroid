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
package org.fs.xml.soap.type;

import android.text.TextUtils;

import org.fs.xml.soap.annotation.Ignore;
import org.fs.xml.soap.annotation.Node;
import org.fs.xml.soap.reader.SoapXMLParserFactory;

@Node(name = "Envelope",
        namespace = SoapXMLParserFactory.NAMESPACE_SOAP)
public final class SoapEnvelope {

  private SoapHeader a;
  private SoapBody   b;

  @Ignore private String soapAction;

  public SoapEnvelope() { }

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

  public String getSoapAction() {
      return soapAction;
    }
  public SoapBody get() {
      return b;
    }
}
