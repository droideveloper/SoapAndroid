package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.text.TextXMLWriter
 */
public interface TextXMLWriter<T, R> {
    void        write(T writer, R value) throws Exception;
    boolean     isWritePossible(Class<?> expectedType);
}
