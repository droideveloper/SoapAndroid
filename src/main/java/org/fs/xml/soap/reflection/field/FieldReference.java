package org.fs.xml.soap.reflection.field;

import org.fs.xml.soap.reflection.Reference;
import org.fs.xml.soap.reflection.ReferenceUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reflection.FieldReference
 */
public abstract class FieldReference<A extends Annotation> extends Reference<Field, A> {

    @Override public Type type() throws Exception {
        Object instance = get();
        return ReferenceUtility.isNotNull(instance) ? instance.getClass() : this.reference.getType();
    }

    @Override public void set(Object value) throws Exception {
        makeAccessible();
        this.reference.set(this.target, value);
    }

    @Override public Object get() throws Exception {
        makeAccessible();
        return this.reference.get(this.target);
    }

    private void makeAccessible() {
        this.reference.setAccessible(true);
    }
}
