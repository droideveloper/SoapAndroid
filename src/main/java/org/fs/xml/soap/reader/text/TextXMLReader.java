package org.fs.xml.soap.reader.text;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.text.TextXMLReader
 */
public interface TextXMLReader<T, R> {
    R           read(T reader) throws Exception;
    boolean     isReadPossible(Class<?> expectedType);
}
