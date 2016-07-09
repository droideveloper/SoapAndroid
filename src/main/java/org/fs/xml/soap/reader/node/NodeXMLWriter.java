package org.fs.xml.soap.reader.node;

/**
 * Created by Fatih on 02/07/16.
 * as org.fs.xml.soap.reader.node.NodeXMLWriter
 */
public interface NodeXMLWriter<T, R> {
    void        write(T writer, R ref) throws Exception;
    boolean     isWritePossible(R ref) throws Exception;
}
