package org.fs.xml.soap.reader.node;

import org.fs.xml.soap.reflection.field.NodeFieldReference;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 03/07/16.
 * as org.fs.xml.soap.reader.node.NodeXMLParserFactory
 */
public final class NodeXMLParserFactory {

    public final static int STYLE_BINARY = 0x01;
    public final static int STYLE_STRING = 0x02;

    private final List<BaseNodeXMLParser<?>> nodeXMLParsers;

    private CollectionNodeXMLParser collectionXMLParser;
    private final TypeNodeXMLParser       typeXMLParser;

    private static NodeXMLParserFactory sharedInstance;


    public void addBooleanXMLParser(int style) {
        nodeXMLParsers.add(new BooleanNodeXMLParser(style));
    }

    public void addDateXMLParser(SimpleDateFormat parser) {
        nodeXMLParsers.add(new DateNodeXMLParser(parser));
    }

    public void addCollectionXMLParser(String entryName) {
        collectionXMLParser = new CollectionNodeXMLParser(entryName);
    }

    public CollectionNodeXMLParser collectionXMLParser() {
        return collectionXMLParser;
    }

    public TypeNodeXMLParser typeXMLParser() {
        return typeXMLParser;
    }

    public BaseNodeXMLParser<?> writeXMLParser(NodeFieldReference ref) throws Exception {
        for (int i = 0, z = nodeXMLParsers.size(); i < z; i++) {
            BaseNodeXMLParser<?> parser = nodeXMLParsers.get(i);
            if (parser.isWritePossible(ref)) {
                return parser;
            }
        }
        return null;
    }

    public BaseNodeXMLParser<?> readXMLParser(XmlPullParser reader, NodeFieldReference ref) throws Exception {
        for (int i = 0, z = nodeXMLParsers.size(); i < z; i++) {
            BaseNodeXMLParser<?> parser = nodeXMLParsers.get(i);
            if(parser.isReadPossible(reader, ref)) {
                return parser;
            }
        }
        return null;
    }

    public static NodeXMLParserFactory sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new NodeXMLParserFactory();
        }
        return sharedInstance;
    }

    private NodeXMLParserFactory() {
        nodeXMLParsers = new ArrayList<>();
        //defaults
        nodeXMLParsers.add(new ByteArrayNodeXMLParser());
        nodeXMLParsers.add(new DoubleNodeXMLParser());
        nodeXMLParsers.add(new FloatNodeXMLParser());
        nodeXMLParsers.add(new IntegerNodeXMLParser());
        nodeXMLParsers.add(new LongNodeXMLParser());
        nodeXMLParsers.add(new ShortNodeXMLParser());
        nodeXMLParsers.add(new StringNodeXMLParser());
        //default type parser
        typeXMLParser = new TypeNodeXMLParser();
    }
}
