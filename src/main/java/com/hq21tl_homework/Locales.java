package com.hq21tl_homework;

import java.io.FileNotFoundException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.hq21tl_homework.Utils.KeyValuePair;

//EagerInitializedSingleton
@SuppressWarnings("java:S6548") // Singleton is intentionally not exposing an instance.
public class Locales { 
    
    public static class LocaleFormatException extends Exception{
        public LocaleFormatException(String msg){
            super(msg);
        }
    }
    public static class LocaleUnavailableException extends Exception{
        public LocaleUnavailableException(String msg){
            super(msg);
        }
    }

    // Singleton field
    private static final Locales instance  = new Locales();


    private static final String LOCALES_AVAILABLE = "locales.xml";
    private static final String LOCALES_AVAILABLE_ROOT = "locales";
    private static final String LOCALES_AVAILABLE_ENTRY = "locale";
    private static final String LOCALES_AVAILABLE_ENTRY_ATTR_NAME = "name";
    private static final String LOCALES_AVAILABLE_ENTRY_ATTR_DEFAULT = "default";

    
    private static final String LOCALES_FOLDER = "locales/";
    private static final String LOCALES_EXTENSION = ".xml";
    private static final String LOCALES_ROOT = "strings";
    private static final String LOCALES_ATTR_LOCALE = "locale";
    private static final String LOCALES_ENTRY = "string";
    private static final String LOCALES_ENTRY_ATTR_ID = "ID";

    private static final String ERRMSG_FILE_NOT_FOUND = "File: (%s) is not found!";

    private static final String ERRMSG_FORMAT_LOCALE_AVAILABLE_ROOT = "File: " +LOCALES_FOLDER+LOCALES_AVAILABLE + " -- Root node is not named " + LOCALES_AVAILABLE_ROOT; //NOSONAR // "File: " is duplicated 3 times, not worth extracting those 7 characters into a separate const.
    private static final String ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY = "File: " +LOCALES_FOLDER+LOCALES_AVAILABLE + " -- Locale entry is not named " + LOCALES_AVAILABLE_ENTRY;
    private static final String ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY_NAME = "File: " +LOCALES_FOLDER+LOCALES_AVAILABLE + " -- Locale Entry node has no attribute " + LOCALES_AVAILABLE_ENTRY_ATTR_NAME;

    private static final String ERRMSG_FORMAT_LOCALE_ROOT = "File: " +LOCALES_FOLDER+"%s.xml -- Root node is not named " + LOCALES_ROOT;
    private static final String ERRMSG_FORMAT_LOCALE_ROOT_LOCALE = "File: " +LOCALES_FOLDER+"%s.xml -- Root node has no attribute " + LOCALES_ATTR_LOCALE;
    private static final String ERRMSG_FORMAT_LOCALE_ROOT_LOCALE_VALUE = "File: " +LOCALES_FOLDER+"%s.xml -- Root node's locale attribute mismatch: %s";
    private static final String ERRMSG_FORMAT_LOCALE_ENTRY = "File: " +LOCALES_FOLDER+"%s.xml -- Locale entry is not named " + LOCALES_ENTRY;
    private static final String ERRMSG_FORMAT_LOCALE_ENTRY_ID = "File: " +LOCALES_FOLDER+"%s.xml -- String Entry node has no attribute " + LOCALES_ENTRY_ATTR_ID;
    private static final String ERRMSG_FORMAT_LOCALE_ENTRY_DUPLICATE = "File: " +LOCALES_FOLDER+"%s.xml -- Contains duplicate entries with ID: %s";

    private static final String ERRMSG_UNAVAILABLE_NO_LOCALES_LOADED = "File: " +LOCALES_FOLDER+LOCALES_AVAILABLE + " -- No locale entries in file.";

    private static final String LOCALE_MISSING_STRING = "-- MISSING STRING --";

    // Array containing all available locales and their display names.
    private Utils.KeyValuePair<String, String>[] availableLocales = null;

    // Currently selected locale and the map containing all strings of the lcoale.
    private String selectedLocale;
    private HashMap<String, String> localeStringsMap = new HashMap<>();


    private Locales(){
        // Constructs a Locles object, this can only be called by the static Instance member of Locales.
        try {
            // Parsing an XML with org.w3c.dom package
            Document doc;
            String filename = LOCALES_FOLDER + LOCALES_AVAILABLE;
            try ( 
                    InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(filename)) {
                doc = Utils.xmlParser(resourceStream);
            }
            if (doc == null) throw new FileNotFoundException(String.format(ERRMSG_FILE_NOT_FOUND, filename));
            
            // Getting the root element of the document, this should be named "locales"
            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals(LOCALES_AVAILABLE_ROOT)) 
                throw new LocaleFormatException(ERRMSG_FORMAT_LOCALE_AVAILABLE_ROOT);
        
            // Collection for the read locale names.
            //  First element is null, it is to be set to the default locale.
            ArrayList<Utils.KeyValuePair<String, String>> availableLocalesBuilder = new ArrayList<>();
            availableLocalesBuilder.add(null);

            // Iterating over the root's children until we reach the end. These must be named "locale".
            Node localeChild = root.getFirstChild();
            while (localeChild != null) {
                if (!(localeChild instanceof Element)) {
                    localeChild = localeChild.getNextSibling();
                    continue;
                }

                Element localeEntry = (Element)localeChild;
                
                if (!localeEntry.getNodeName().equals(LOCALES_AVAILABLE_ENTRY))
                    throw new LocaleFormatException(ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY);

                // Locale nodes need to have a name. Name should match the filename of that locale. (excluding file type)
                // The content of these nodes should contain the display name of the entry.
                String localeName = localeEntry.getAttribute(LOCALES_AVAILABLE_ENTRY_ATTR_NAME);
                if (localeName.isBlank())
                    throw new LocaleFormatException(ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY_NAME);
                
                // Default locale is set by having one locale with "default" attribute set. 
                //      It does not matter what the attributes value is, as long as it is not null.
                //  Having multiple defaults is undefined behaviour, in this implementation the last locale with default attribute will be set.
                //      Waring! All previous marked default ones are lost and will not be loaded!
                if (localeEntry.hasAttribute(LOCALES_AVAILABLE_ENTRY_ATTR_DEFAULT))
                    availableLocalesBuilder.set(
                        0, 
                        new Utils.KeyValuePair<>(localeName, localeEntry.getTextContent())
                    );
                else
                    availableLocalesBuilder.add(
                        new Utils.KeyValuePair<>(localeName, localeEntry.getTextContent())
                    );

                localeChild = localeChild.getNextSibling();
            }

            // If there are no defaults, remove the spot reserved for it, so the first locale listed will be come default.
            if (availableLocalesBuilder.get(0) == null){
                availableLocalesBuilder.remove(0);
            }

            if (availableLocalesBuilder.isEmpty()){
                throw new LocaleUnavailableException(ERRMSG_UNAVAILABLE_NO_LOCALES_LOADED);
            }

            // Convert the arraylist into a fixed lenght string
            @SuppressWarnings("unchecked")  // Suppress the unchecked cast warning
            KeyValuePair<String, String>[] _availableLocales = availableLocalesBuilder.toArray(KeyValuePair[]::new); //NOSONAR // Var starting with '_', could be named ...Temp
            this.availableLocales = _availableLocales;

        } catch (LocaleUnavailableException | LocaleFormatException | IOException | ParserConfigurationException | SAXException e) {
            // TODO: Dispaly custom error window.
            e.printStackTrace();
            System.exit(1);
        }

        // We swqitch the instance language to the default language.
        switchInstanceLanguage(availableLocales[0].getKey());
    }

    private void switchInstanceLanguage(String lang){
        // If we are already at the desired language, don't reaload locale.
        if (lang.equals(selectedLocale))
            return;
        
        // Set selected
        selectedLocale = lang;

        // Clear previous locale data.
        localeStringsMap.clear();
        try {
            Document doc;
            String filename = LOCALES_FOLDER + lang + LOCALES_EXTENSION;
            try ( 
                    InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(filename)) {
                doc = Utils.xmlParser(resourceStream);
            }
            if (doc == null) throw new FileNotFoundException(String.format(ERRMSG_FILE_NOT_FOUND, filename));
            

            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals(LOCALES_ROOT))
                throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ROOT, lang));
            
            if (!root.hasAttribute(LOCALES_ATTR_LOCALE))
                throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ROOT_LOCALE, lang));
            
            if (!root.getAttribute(LOCALES_ATTR_LOCALE).equals(lang))
                throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ROOT_LOCALE_VALUE, lang, root.getAttribute(LOCALES_ATTR_LOCALE)));
            
            Node entryNode = root.getFirstChild();
            while(entryNode != null){
                if (!(entryNode instanceof Element)) {
                    entryNode = entryNode.getNextSibling();
                    continue;
                }
                Element entry = (Element)entryNode;

                if (!entry.getNodeName().equals(LOCALES_ENTRY))
                    throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ENTRY, lang));
                
                if (!entry.hasAttribute(LOCALES_ENTRY_ATTR_ID))
                    throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ENTRY_ID, lang));
                    
                String id = entry.getAttribute(LOCALES_ENTRY_ATTR_ID);
                    
                if (localeStringsMap.containsKey(id))
                    throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ENTRY_DUPLICATE, lang, id));

                localeStringsMap.put(id, entry.getTextContent());

                entryNode = entryNode.getNextSibling();
            }
            
            
        } catch (LocaleFormatException | IOException | SAXException | ParserConfigurationException e) {
            // TODO: Dispaly custom error window.
            e.printStackTrace();
        }
    }

    public static void switchLanguage(String lang){
        instance.switchInstanceLanguage(lang);
    }
    public static String getString(String key){
        return instance.localeStringsMap.getOrDefault(key, LOCALE_MISSING_STRING);
    }
}
