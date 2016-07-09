package org.fs.xml.soap.reflection.type;

import org.fs.xml.soap.reflection.Reference;
import org.fs.xml.soap.reflection.ReferenceUtility;
import org.fs.xml.soap.reflection.field.AttributeFieldReference;
import org.fs.xml.soap.reflection.field.FieldReference;
import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.xmlpull.v1.XmlPullParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reflection.type.TypeReference
 */
public abstract class TypeReference<A extends Annotation> extends Reference<Class<?>, A> {

    protected List<FieldReference<?>> attrChildren;
    protected List<FieldReference<?>> nodeChildren;

    public void addChild(FieldReference<?> child) {
        if (child instanceof AttributeFieldReference) {
            if (attrChildren == null) {
                attrChildren = new ArrayList<>();
            }
            attrChildren.add(child);
        } else if (child instanceof NodeFieldReference) {
            if (nodeChildren == null) {
                nodeChildren = new ArrayList<>();
            }
            nodeChildren.add(child);
        }
    }

    public int attrChildrenSize() {
        return attrChildren != null && !attrChildren.isEmpty() ? attrChildren.size() : 0;
    }

    public int nodeChildrenSize() {
        return nodeChildren != null && !nodeChildren.isEmpty() ? nodeChildren.size() : 0;
    }

    public FieldReference<?> attrChildrenAt(int index) {
        if (index >= 0 || index < attrChildrenSize()) {
            return attrChildren.get(index);
        }
        return null;
    }

    public FieldReference<?> nodeChildrenAt(int index) {
        if (index >= 0 || index < nodeChildrenSize()) {
            return nodeChildren.get(index);
        }
        return null;
    }

    public FieldReference<?> nodeChildrenReader(XmlPullParser reader) throws Exception {
        for (int i = 0; i < nodeChildrenSize(); i++) {
            FieldReference<?> child = nodeChildrenAt(i);
            if (child.name().equalsIgnoreCase(reader.getName())) {
                nodeChildren.remove(child);
                return child;
            }
        }
        return null;
    }

    @Override public boolean hasAnyChildren() {
        return hasAttrChildren() || hasNodeChildren();
    }

    @Override public boolean hasAttrChildren() {
        return attrChildrenSize() > 0;
    }

    @Override public boolean hasNodeChildren() {
        return nodeChildrenSize() > 0;
    }

    @Override public Type type() throws Exception {
        Object instance = get();
        return ReferenceUtility.isNotNull(instance) ? instance.getClass() : this.reference;
    }

    @Override public void set(Object value) throws Exception {
        this.target = value;
    }

    @Override public Object get() throws Exception {
        return this.target;
    }
}
