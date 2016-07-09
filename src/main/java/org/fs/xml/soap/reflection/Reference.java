package org.fs.xml.soap.reflection;

import org.fs.xml.soap.annotation.Attribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by Fatih on 01/07/16.
 * as PACKAGE_NAME.Reference
 */
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
