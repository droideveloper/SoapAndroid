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
package org.fs.xml.soap.reflection;

import org.fs.xml.soap.annotation.Attribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class Reference<T, A extends Annotation> {

  protected T       reference;
  protected Object  target;
  protected A       definition;

  public abstract String  namespace();
  public abstract String  name();
  public abstract Type    type() throws Exception;
  public abstract Object  get() throws Exception;
  public abstract void    set(Object value) throws Exception;

  public boolean hasAnyChildren() {
    return false;
  }

  public boolean hasAttrChildren() {
    return false;
  }

  public boolean hasNodeChildren() {
    return false;
  }

  public boolean hasDefinition() {
    return  this.definition != null;
  }

  public boolean isAttribute() {
    return this.definition != null && this.definition instanceof Attribute;
  }

  public void reference(T reference) {
    this.reference = reference;
  }

  public void target(Object target) {
    this.target = target;
  }

  public void definition(A definition) {
    this.definition = definition;
  }

  public A definition() {
    return this.definition;
  }
}
