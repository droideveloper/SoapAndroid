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
package org.fs.xml.soap.reflection.field;

import android.text.TextUtils;

import org.fs.xml.soap.annotation.Attribute;

public class AttributeFieldReference extends FieldReference<Attribute> {

  @Override public String namespace() {
    return this.definition != null ? this.definition.namespace() : null;
  }

  @Override public String name() {
    return this.definition != null && !TextUtils.isEmpty(this.definition.name()) ? this.definition.name() : this.reference.getName();
  }
}
