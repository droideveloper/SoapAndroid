package org.fs.xml.soap.reader.attr;

import org.fs.xml.soap.reflection.field.AttributeFieldReference;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 03/07/16.
 * as org.fs.xml.soap.reader.attr.AttrXMLParserFactory
 */
public final class AttrXMLParserFactory {

    public final static int STYLE_BINARY = 0x01;
    public final static int STYLE_STRING = 0x02;

    private final List<BaseAttrXMLParser<?>> attrXMLParsers;

    private static AttrXMLParserFactory sharedInstance;

    public void addBooleanXMLParser(int style) {
        attrXMLParsers.add(new BooleanAttrXMLParser(style));
    }

    public void addDateXMLParser(SimpleDateFormat parser) {
        attrXMLParsers.add(new DateAttrXMLParser(parser));
    }

    public BaseAttrXMLParser<?> writeXMLParser(AttributeFieldReference ref) throws Exception {
        for (int i = 0, z = attrXMLParsers.size(); i < z; i++) {
            BaseAttrXMLParser<?> parser = attrXMLParsers.get(i);
            if (parser.isWritePossible(ref)) {
                return parser;
            }
        }
        return null;
    }

    public BaseAttrXMLParser<?> readXMLParser(XmlPullParser reader, AttributeFieldReference ref) throws Exception {
        for (int i = 0, z = attrXMLParsers.size(); i < z; i++) {
            BaseAttrXMLParser<?> parser = attrXMLParsers.get(i);
            if(parser.isReadPossible(reader, ref)) {
                return parser;
            }
        }
        return null;
    }

    public static AttrXMLParserFactory sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new AttrXMLParserFactory();
        }
        return sharedInstance;
    }

    private AttrXMLParserFactory() {
        //defaults
        attrXMLParsers = new ArrayList<>();
        attrXMLParsers.add(new ByteArrayAttrXMLParser());
        attrXMLParsers.add(new DoubleAttrXMLParser());
        attrXMLParsers.add(new FloatAttrXMLParser());
        attrXMLParsers.add(new IntegerAttrXMLParser());
        attrXMLParsers.add(new LongAttrXMLParser());
        attrXMLParsers.add(new ShotAttrXMLParser());
        attrXMLParsers.add(new StringAttrXMLParser());
    }
}
