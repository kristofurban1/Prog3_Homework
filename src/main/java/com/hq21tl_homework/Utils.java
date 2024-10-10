package com.hq21tl_homework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Utils {
    private Utils(){} // Unnesesessery constructor

    private static DocumentBuilderFactory documentBuilderFactory = null;
    private static DocumentBuilder documentBuilder = null;
    public static Document xmlParser(InputStream xmlFileStream) throws ParserConfigurationException, SAXException, IOException {
        if (documentBuilderFactory == null)
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
        if (documentBuilder == null)
            documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
        
        
        return documentBuilder.parse(xmlFileStream);
    }

    public static class KeyValuePair<T, U>{
        private T key;
        private U value;
        public KeyValuePair(T _key, U _value){ //NOSONAR // Ignore variable starting with _
            key = _key;
            value = _value;
        }
        
        public T getKey() {
            return key;
        }

        public void setKey(T key) {
            this.key = key;
        }
        
        public U getValue() {
            return value;
        }
        
        public void setValue(U value) {
            this.value = value;
        }
    }
}
