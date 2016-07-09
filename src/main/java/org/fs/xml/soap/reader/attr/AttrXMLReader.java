package org.fs.xml.soap.reader.attr;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.attr.AttrXMLReader
 */
public interface AttrXMLReader<T, R> {
    void    read(T reader, R ref) throws Exception;
    boolean isReadPossible(T reader, R ref) throws Exception;
}
