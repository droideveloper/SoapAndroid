package org.fs.xml.soap.reader.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fatih on 03/07/16.
 * as org.fs.xml.soap.reader.text.TextXMLParserFactory
 */
public final class TextXMLParserFactory {

    public final static int STYLE_BINARY = 0x01;
    public final static int STYLE_STRING = 0x02;

    private final List<BaseTextXMLParser<?>> textXMLParsers;
    private CollectionTextXMLParser collectionTextXMLParser;

    private static TextXMLParserFactory sharedInstance;

    public void addBooleanXMLParser(int style) {
        textXMLParsers.add(new BooleanTextXMLParser(style));
    }

    public void addDateXMLParser(SimpleDateFormat parser) {
        textXMLParsers.add(new DateTextXMLParser(parser));
    }

    public void addCollectionXMLParser(String parentName, String entryName) {
        collectionTextXMLParser = new CollectionTextXMLParser(parentName, entryName);
    }

    public CollectionTextXMLParser collectionXMLParser() {
        return collectionTextXMLParser;
    }

    public BaseTextXMLParser<?> writeXMLParser(Class<?> targetClass) throws Exception {
        for (int i = 0, z = textXMLParsers.size(); i < z; i++) {
            BaseTextXMLParser<?> parser = textXMLParsers.get(i);
            if(parser.isWritePossible(targetClass)) {
                return parser;
            }
        }
        return null;
    }

    public BaseTextXMLParser<?> readXMLParser(Class<?> targetClass) throws Exception {
        for (int i = 0, z = textXMLParsers.size(); i < z; i++) {
            BaseTextXMLParser<?> parser = textXMLParsers.get(i);
            if (parser.isReadPossible(targetClass)) {
                return parser;
            }
        }
        return null;
    }

    public static TextXMLParserFactory sharedInstance() {
        if(sharedInstance == null) {
            sharedInstance = new TextXMLParserFactory();
        }
        return sharedInstance;
    }

    private TextXMLParserFactory() {
        //defaults
        textXMLParsers = new ArrayList<>();
        textXMLParsers.add(new ByteArrayTextXMLParser());
        textXMLParsers.add(new DoubleTextXMLParser());
        textXMLParsers.add(new FloatTextXMLParser());
        textXMLParsers.add(new IntegerTextXMLParser());
        textXMLParsers.add(new LongTextXMLParser());
        textXMLParsers.add(new ShortTextXMLParser());
        textXMLParsers.add(new StringTextXMLParser());
    }
}
