package com.hq21tl_homework.recipe_book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Utils;
import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogBehaviour;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogType;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorDialogSettings;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.recipe_book.Ingredient.IngredientBuilder;
import com.hq21tl_homework.recipe_book.Recipe.RecipeBuilder;
import com.hq21tl_homework.recipe_book.RecipeEntry.RecipeEntryBuilder;


/*
 * Code from https://mkyong.com/java/how-to-create-xml-file-in-java-dom/
 * Not intended to plagerize, just have no idea how this flow works.
 */


public class RecipeEntryXMLHander {
    private RecipeEntryXMLHander() {}

    private static void showImportError(File file, Node parent, Node child){
        ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                "Failed reading XML", 
                ErrorLevel.WARNING, 
                DialogType.OK,
                DialogBehaviour.BLOCKING_DIALOG, 
                "Unknown node("+child.getNodeName()+") in "+parent.getNodeName()+" in file " + file.getName(), 
                null);
            ErrorDialog dialog = new ErrorDialog(settings);
            dialog.showError();
    }


    public static RecipeEntry importRecipe(File importFile){ //NOSONAR // Its fineeeee
        Document doc;
        try {
            doc = Utils.xmlParser(new FileInputStream(importFile));
        } 
        catch (ParserConfigurationException | SAXException | IOException e) {
            ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                "Failed reading XML", 
                ErrorLevel.FATAL, 
                DialogType.OK,
                DialogBehaviour.BLOCKING_DIALOG, 
                Locales.getString(e.getMessage()), 
                Arrays.toString(e.getStackTrace()));
            ErrorDialog dialog = new ErrorDialog(settings);
            dialog.showError();
            System.exit(1);
            return null;
        }

        RecipeEntryBuilder entryBuilder = new RecipeEntryBuilder();
        Element root = doc.getDocumentElement();

        NodeList rootChildren = root.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++) {
            Node rootNode = rootChildren.item(i);
            if(rootNode.getNodeType() != Node.ELEMENT_NODE) continue;
            
            if (rootNode.getNodeName().equals("name"))
                entryBuilder.name = rootNode.getTextContent();
            else if (rootNode.getNodeName().equals("category"))
                entryBuilder.category = rootNode.getTextContent();
            else if (rootNode.getNodeName().equals("description"))
                entryBuilder.description = rootNode.getTextContent();
            else if (rootNode.getNodeName().equals("recipes")){
                NodeList recipesChildren = rootNode.getChildNodes();
                for (int j = 0; j < recipesChildren.getLength(); j++) {
                    Node recipesNode = recipesChildren.item(j);
                    if(recipesNode.getNodeType() != Node.ELEMENT_NODE) continue;
                    if (!recipesNode.getNodeName().equals("recipe")){
                        showImportError(importFile, rootNode, recipesNode);
                        return null;
                    }

                    RecipeBuilder recipeBuilder = new RecipeBuilder();
                    NodeList recipeChildren = recipesNode.getChildNodes();
                    for (int k = 0; k < recipeChildren.getLength(); k++) {
                        Node recipeNode = recipeChildren.item(k);
                        if(recipeNode.getNodeType() != Node.ELEMENT_NODE) continue;

                        if (recipeNode.getNodeName().equals("ingredients")){
                            NodeList ingredientsChildren = recipeNode.getChildNodes();
                            for (int l = 0; l < ingredientsChildren.getLength(); l++) {
                                Node ingredientsNode = ingredientsChildren.item(l);
                                if(ingredientsNode.getNodeType() != Node.ELEMENT_NODE) continue;
                                if (!ingredientsNode.getNodeName().equals("ingredient")){
                                    showImportError(importFile, recipeNode, ingredientsNode);
                                    return null;
                                }
                                
                                IngredientBuilder ingredientBuilder = new IngredientBuilder();
                                NodeList ingredientChildren = ingredientsNode.getChildNodes();
                                for (int m = 0; m < ingredientChildren.getLength(); m++) {
                                    Node ingredientNode = ingredientChildren.item(m);
                                    if(ingredientNode.getNodeType() != Node.ELEMENT_NODE) continue;
                                    
                                    if (ingredientNode.getNodeName().equals("name"))
                                        ingredientBuilder.name = ingredientNode.getTextContent();
                                    else if (ingredientNode.getNodeName().equals("amount"))
                                        ingredientBuilder.amount = Double.parseDouble(ingredientNode.getTextContent());
                                    else if (ingredientNode.getNodeName().equals("quantifyer"))
                                        ingredientBuilder.quantifyer = ingredientNode.getTextContent();
                                    else{
                                        showImportError(importFile, ingredientsNode, ingredientNode);
                                        return null;
                                    }
                                }
                                recipeBuilder.ingredients.add(ingredientBuilder);
                            }

                        }
                        else if (recipeNode.getNodeName().equals("instructions")){
                            NodeList instructionsChildren = recipeNode.getChildNodes();
                            for (int l = 0; l < instructionsChildren.getLength(); l++) {
                                Node instructionsNode = instructionsChildren.item(l);
                                if(instructionsNode.getNodeType() != Node.ELEMENT_NODE) continue;
                                if (!instructionsNode.getNodeName().equals("instruction")){
                                    showImportError(importFile, recipeNode, instructionsNode);
                                    return null;
                                }
                                    
                                recipeBuilder.instructions.add(instructionsNode.getTextContent());
                            }
                        }
                        else{
                            showImportError(importFile, recipesNode, recipeNode);
                            return null;
                        }
                    }
                    entryBuilder.recipes.add(recipeBuilder);
                }
            }
            else{
                showImportError(importFile, root, rootNode);
                return null;
            }
        }

        entryBuilder.cleanup();
        return entryBuilder.build();
    }
    public static File exportRecipe(RecipeEntry entry){
        Document xmlDoc;
        try {
            xmlDoc = Utils.newDocument();
        } catch (ParserConfigurationException e) {
            ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                    "Failed writing XML", 
                    ErrorLevel.FATAL, 
                    DialogType.OK,
                    DialogBehaviour.BLOCKING_DIALOG, 
                    Locales.getString(e.getMessage()), 
                    Arrays.toString(e.getStackTrace()));
                ErrorDialog dialog = new ErrorDialog(settings);
                dialog.showError();
                System.exit(1);
                return null;
        }

        Element root = xmlDoc.createElement("RecipeEntry");
        xmlDoc.appendChild(root);
        
        Element nameElement = xmlDoc.createElement("name");
        nameElement.appendChild(xmlDoc.createTextNode(entry.getName()));
        root.appendChild(nameElement);

        Element categoryElement = xmlDoc.createElement("category");
        categoryElement.appendChild(xmlDoc.createTextNode(entry.getCategory()));
        root.appendChild(categoryElement);

        Element descriptionElement = xmlDoc.createElement("description");
        descriptionElement.appendChild(xmlDoc.createTextNode(entry.getDescription()));
        root.appendChild(descriptionElement);

        Element recipesElement = xmlDoc.createElement("recipes");
        root.appendChild(recipesElement);
        for(Recipe recipe : entry.getRecipes()){
            Element recipeElement = xmlDoc.createElement("recipe");
            
            Element ingredientsElement = xmlDoc.createElement("ingredients");
            for (Ingredient ingredient : recipe.getIngredients()){
                Element ingredientElement = xmlDoc.createElement("ingredient");
                Element ingredientNameElement = xmlDoc.createElement("name");
                ingredientNameElement.appendChild(xmlDoc.createTextNode(ingredient.getName()));
                ingredientElement.appendChild(ingredientNameElement);

                Element ingredientAmountElement = xmlDoc.createElement("amount");
                ingredientAmountElement.appendChild(xmlDoc.createTextNode(ingredient.getAmount()+""));
                ingredientElement.appendChild(ingredientAmountElement);

                Element ingredientQuantifyerElement = xmlDoc.createElement("quantifyer");
                ingredientQuantifyerElement.appendChild(xmlDoc.createTextNode(ingredient.getQuantifyer()));
                ingredientElement.appendChild(ingredientQuantifyerElement);

                ingredientsElement.appendChild(ingredientElement);
            }
            recipeElement.appendChild(ingredientsElement);
            
            Element instructionsElement = xmlDoc.createElement("instructions");
            for (String instruction : recipe.getInstructions()){
                Element instructionElement = xmlDoc.createElement("instruction");
                instructionElement.appendChild(xmlDoc.createTextNode(instruction));
                instructionsElement.appendChild(instructionElement);

            }
            recipeElement.appendChild(instructionsElement);

            recipesElement.appendChild(recipeElement);
        }

        File outfile = new File(entry.getName() + ".xml");
        try (FileOutputStream output =
                     new FileOutputStream(outfile)) {
            writeXml(xmlDoc, output);
        } catch (IOException | TransformerException e) {
            ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                    "Failed writing XML", 
                    ErrorLevel.FATAL, 
                    DialogType.OK,
                    DialogBehaviour.BLOCKING_DIALOG, 
                    Locales.getString(e.getMessage()), 
                    Arrays.toString(e.getStackTrace()));
                ErrorDialog dialog = new ErrorDialog(settings);
                dialog.showError();
                System.exit(1);
                return null;
        }
        return outfile;
    }

    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}

