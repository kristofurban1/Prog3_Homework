package com.hq21tl_homework;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.hq21tl_homework.Utils.KeyValuePair;
import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogBehaviour;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogResult;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogType;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;

//EagerInitializedSingleton
@SuppressWarnings("java:S6548") // Singleton is intentionally not exposing an instance.
public class Locales {

    public static interface LocalizationChangeListener {

        void localizationChanged();
    }

    public static class LocalizationChangeEventHandler {

        private final List<LocalizationChangeListener> listeners = new ArrayList<>();

        // Method to add listeners
        public void addValueChangeListener(LocalizationChangeListener listener) {
            listeners.add(listener);
        }

        // Method to remove listeners
        public void removeValueChangeListener(LocalizationChangeListener listener) {
            listeners.remove(listener);
        }

        // Fire event to all registered listeners
        public void fireLocalizationChanged() {
            for (LocalizationChangeListener listener : listeners) {
                listener.localizationChanged();
            }
        }
    }

    public static class LocaleFormatException extends Exception {

        public LocaleFormatException(String msg) {
            super(msg);
        }
    }

    public static class LocaleUnavailableException extends Exception {

        public LocaleUnavailableException(String msg) {
            super(msg);
        }
    }

    public static final LocalizationChangeEventHandler eventHandler = new LocalizationChangeEventHandler();

    // Singleton field
    private static final Locales instance = new Locales();

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

    private static final String ERRMSG_FORMAT_LOCALE_AVAILABLE_ROOT = "File: " + LOCALES_FOLDER + LOCALES_AVAILABLE + " -- Root node is not named " + LOCALES_AVAILABLE_ROOT; //NOSONAR // "File: " is duplicated 3 times, not worth extracting those 7 characters into a separate const.
    private static final String ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY = "File: " + LOCALES_FOLDER + LOCALES_AVAILABLE + " -- Locale entry is not named " + LOCALES_AVAILABLE_ENTRY;
    private static final String ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY_NAME = "File: " + LOCALES_FOLDER + LOCALES_AVAILABLE + " -- Locale Entry node has no attribute " + LOCALES_AVAILABLE_ENTRY_ATTR_NAME;

    private static final String ERRMSG_FORMAT_LOCALE_ROOT = "File: " + LOCALES_FOLDER + "%s.xml -- Root node is not named " + LOCALES_ROOT;
    private static final String ERRMSG_FORMAT_LOCALE_ROOT_LOCALE = "File: " + LOCALES_FOLDER + "%s.xml -- Root node has no attribute " + LOCALES_ATTR_LOCALE;
    private static final String ERRMSG_FORMAT_LOCALE_ROOT_LOCALE_VALUE = "File: " + LOCALES_FOLDER + "%s.xml -- Root node's locale attribute mismatch: %s";
    private static final String ERRMSG_FORMAT_LOCALE_ENTRY = "File: " + LOCALES_FOLDER + "%s.xml -- Locale entry is not named " + LOCALES_ENTRY;
    private static final String ERRMSG_FORMAT_LOCALE_ENTRY_ID = "File: " + LOCALES_FOLDER + "%s.xml -- String Entry node has no attribute " + LOCALES_ENTRY_ATTR_ID;
    private static final String ERRMSG_FORMAT_LOCALE_ENTRY_DUPLICATE = "File: " + LOCALES_FOLDER + "%s.xml -- Contains duplicate entries with ID: %s";

    private static final String ERRMSG_UNAVAILABLE_NO_LOCALES_LOADED = "File: " + LOCALES_FOLDER + LOCALES_AVAILABLE + " -- No locale entries in file.";
    private static final String ERRMSG_UNAVAILABLE_NO_LOCALE_FOUND = "File: " + LOCALES_FOLDER + LOCALES_AVAILABLE + " -- No locale entry \"%s\" in file.";

    // Array containing all available locales and their display names.
    private Utils.KeyValuePair<String, String>[] availableLocales = null;

    // Currently selected locale and the map containing all strings of the lcoale.
    private String selectedLocale;
    private HashMap<String, String> localeStringsMap = new HashMap<>();

    public Utils.KeyValuePair<String, String> getInstanceSelectedLocale() {
        for (KeyValuePair<String, String> locale : availableLocales) {
            if (selectedLocale.equals(locale.getKey())) {
                return locale;
            }
        }
        return null; // ILLEGAL STATE.
    }

    private Document getDocumentFromPath(String filepath) throws ParserConfigurationException, SAXException, IOException {
        Document doc = null;
        try (
                InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(filepath)) {
            if (resourceStream != null) {
                doc = Utils.xmlParser(resourceStream);
            }
        }
        if (doc == null) {
            throw new FileNotFoundException(String.format(ERRMSG_FILE_NOT_FOUND, filepath));
        }
        return doc;
    }

    private Locales() { // NOSONAR // Cannot decresase complexity, when complexity comes from errorchecking.
        // Constructs a Locles object, this can only be called by the static Instance member of Locales.
        try {
            // Parsing an XML with org.w3c.dom package
            String filename = LOCALES_FOLDER + LOCALES_AVAILABLE;
            Document doc = getDocumentFromPath(filename);

            // Getting the root element of the document, this should be named "locales"
            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals(LOCALES_AVAILABLE_ROOT)) {
                throw new LocaleFormatException(ERRMSG_FORMAT_LOCALE_AVAILABLE_ROOT);
            }

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

                Element localeEntry = (Element) localeChild;

                if (!localeEntry.getNodeName().equals(LOCALES_AVAILABLE_ENTRY)) {
                    throw new LocaleFormatException(ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY);
                }

                // Locale nodes need to have a name. Name should match the filename of that locale. (excluding file type)
                // The content of these nodes should contain the display name of the entry.
                String localeName = localeEntry.getAttribute(LOCALES_AVAILABLE_ENTRY_ATTR_NAME);
                if (localeName.isBlank()) {
                    throw new LocaleFormatException(ERRMSG_FORMAT_LOCALE_AVAILABLE_ENTRY_NAME);
                }

                // Default locale is set by having one locale with "default" attribute set. 
                //      It does not matter what the attributes value is, as long as it is not null.
                //  Having multiple defaults is undefined behaviour, in this implementation the last locale with default attribute will be set.
                //      Waring! All previous marked default ones are lost and will not be loaded!
                if (localeEntry.hasAttribute(LOCALES_AVAILABLE_ENTRY_ATTR_DEFAULT)) {
                    availableLocalesBuilder.set(
                            0,
                            new Utils.KeyValuePair<>(localeName, localeEntry.getTextContent())
                    ); 
                }else {
                    availableLocalesBuilder.add(
                            new Utils.KeyValuePair<>(localeName, localeEntry.getTextContent())
                    );
                }

                localeChild = localeChild.getNextSibling();
            }

            // If there are no defaults, remove the spot reserved for it, so the first locale listed will be come default.
            if (availableLocalesBuilder.get(0) == null) {
                availableLocalesBuilder.remove(0);
            }

            if (availableLocalesBuilder.isEmpty()) {
                throw new LocaleUnavailableException(ERRMSG_UNAVAILABLE_NO_LOCALES_LOADED);
            }

            // Convert the arraylist into a fixed lenght string array.
            @SuppressWarnings("unchecked")  // Suppress the unchecked cast warning
            KeyValuePair<String, String>[] _availableLocales = availableLocalesBuilder.toArray(KeyValuePair[]::new); //NOSONAR // Var starting with '_', could be named ...Temp
            this.availableLocales = _availableLocales;

        } catch (LocaleUnavailableException | LocaleFormatException | IOException | ParserConfigurationException | SAXException e) {
            ErrorDialog.ErrorDialogSettings settings = new ErrorDialog.ErrorDialogSettings(
                    "Locales: Initialization failed!",
                    ErrorLevel.FATAL,
                    DialogType.OK,
                    DialogBehaviour.BLOCKING_DIALOG,
                    "Exception occured: " + (e.getMessage() == null ? "Unspecified" : e.getMessage()),
                    Arrays.toString(e.getStackTrace())
            );
            ErrorDialog error = new ErrorDialog(settings);
            error.showError();
            System.exit(1);
        }

        // We swqitch the instance language to the default language.
        switchInstanceLanguage(availableLocales[0].getKey());
    }

    private void verifyLanguageAvailable(String lang) throws LocaleUnavailableException {
        boolean langAvailable = false;
        for (KeyValuePair<String, String> locale : availableLocales) {
            if (lang.equals(locale.getKey())) {
                langAvailable = true;
                break;
            }
        }
        if (!langAvailable) {
            throw new LocaleUnavailableException(String.format(ERRMSG_UNAVAILABLE_NO_LOCALE_FOUND, lang));
        }
    }

    private void switchInstanceLanguage(String lang) { // NOSONAR // Cannot decresase complexity, when complexity comes from errorchecking.
        HashMap<String, String> newLocaleStringsMap = new HashMap<>();
        try {
            // If we are already at the desired language, don't reaload locale.
            if (lang.equals(selectedLocale)) {
                return;
            }

            verifyLanguageAvailable(lang);

            // Set selected
            selectedLocale = lang;

            // Reading locale file
            String filename = LOCALES_FOLDER + lang + LOCALES_EXTENSION;
            Document doc = getDocumentFromPath(filename);
            

            // Getting root of file, must be names "strings"
            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals(LOCALES_ROOT)) {
                throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ROOT, lang));
            }

            // Root must have a "locale" attribute with the value of locale
            if (!root.hasAttribute(LOCALES_ATTR_LOCALE)) {
                throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ROOT_LOCALE, lang));
            }

            // This locale attribute's value must equal the one given in file name. 
            if (!root.getAttribute(LOCALES_ATTR_LOCALE).equals(lang)) {
                throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ROOT_LOCALE_VALUE, lang, root.getAttribute(LOCALES_ATTR_LOCALE)));
            }

            // Iterate over the children of root.
            Node entryNode = root.getFirstChild();
            while (entryNode != null) {
                // If the node is not type Element then ignore it.
                if (!(entryNode instanceof Element)) {
                    entryNode = entryNode.getNextSibling();
                    continue;
                }
                Element entry = (Element) entryNode;

                // Entry must be named "string"
                if (!entry.getNodeName().equals(LOCALES_ENTRY)) {
                    throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ENTRY, lang));
                }

                // Entry must have attribute "ID". This is the key the string's value will be accessed by.
                if (!entry.hasAttribute(LOCALES_ENTRY_ATTR_ID)) {
                    throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ENTRY_ID, lang));
                }

                String id = entry.getAttribute(LOCALES_ENTRY_ATTR_ID);

                // Filter out duplicated IDs
                if (newLocaleStringsMap.containsKey(id)) {
                    throw new LocaleFormatException(String.format(ERRMSG_FORMAT_LOCALE_ENTRY_DUPLICATE, lang, id));
                }

                newLocaleStringsMap.put(id, entry.getTextContent());

                entryNode = entryNode.getNextSibling();
            }

            localeStringsMap = newLocaleStringsMap;

        } catch (LocaleUnavailableException | LocaleFormatException | IOException | SAXException | ParserConfigurationException e) {
            ErrorDialog.ErrorDialogSettings settings = new ErrorDialog.ErrorDialogSettings(
                    "Locales: Language switching failed!",
                    ErrorLevel.ERROR,
                    DialogType.YES_NO,
                    DialogBehaviour.BLOCKING_DIALOG,
                    "Exception occured: " + (e.getMessage() == null ? "Unspecified" : e.getMessage()) + "\nWould you like to exit application?",
                    null
            );
            ErrorDialog error = new ErrorDialog(settings);
            ErrorDialog.DialogResult result = error.showError();
            if (result == DialogResult.YES) {
                System.exit(1);
            }

        }
    }

    public static String getSelectedLocale() {
        return instance.getInstanceSelectedLocale().getKey();
    }

    public static Utils.KeyValuePair<String, String>[] getAvailableLocales(){
        return instance.availableLocales;
    }

    /**
     * Switching localizaation langugage. Fires LocalizationChanged Event
     *
     * @param lang The language that the localization should switch to.
     */
    public static void switchLanguage(String lang) {
        instance.switchInstanceLanguage(lang);
        eventHandler.fireLocalizationChanged();
    }

    public static String getString(String key) {
        if (!instance.localeStringsMap.containsKey(key)) {
            ErrorDialog.ErrorDialogSettings settings = new ErrorDialog.ErrorDialogSettings(
                    "Locales: Missing string",
                    ErrorLevel.WARNING,
                    DialogType.OK, DialogBehaviour.NON_BLOCKING_DIALOG,
                    String.format("Missing string {%s} in locale %s(%s)", key, instance.getInstanceSelectedLocale().getKey(), instance.getInstanceSelectedLocale().getValue()),
                    null);
            ErrorDialog errDialog = new ErrorDialog(settings);
            errDialog.showError();
        }
        return instance.localeStringsMap.get(key);
    }

    public static String getStringOrDefault(String key, String defaultString) {
        if (instance == null) {
            return defaultString;
        }
        return instance.localeStringsMap.getOrDefault(key, defaultString);
    }
}
