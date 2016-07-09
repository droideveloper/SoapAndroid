package org.fs.xml.soap.reflection.field;

import android.text.TextUtils;

import org.fs.xml.soap.annotation.Node;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reflection.field.NodeFieldReference
 */
public class NodeFieldReference extends FieldReference<Node> {

    @Override public String namespace() {
        return this.definition != null ? this.definition.namespace() : null;
    }

    @Override public String name() {
        return this.definition != null && !TextUtils.isEmpty(this.definition.name()) ? this.definition.name() : this.reference.getName();
    }
}
