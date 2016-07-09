package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 01/07/16.
 * as org.fs.xml.soap.reader.node.NodeXMLReader
 */
public interface NodeXMLReader<T, R> {
    void    read(T reader, R ref) throws Exception;
    boolean isReadPossible(T reader, R ref) throws Exception;
}
