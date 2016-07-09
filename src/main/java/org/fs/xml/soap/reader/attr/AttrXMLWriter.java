package org.fs.xml.soap.reader.attr;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.attr.AttrXMLWriter
 */
public interface AttrXMLWriter<T, R> {
    void        write(T writer, R ref) throws Exception;
    boolean     isWritePossible(R ref) throws Exception;
}
