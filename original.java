import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.xml.stream.events.Attribute;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

/**
 * The getJson function tries to parse a xml document structure into json based on 
 * the url that is passed in. It then gets the start node of the document, either the root
 * '/' or by some relative xPath. We can assume that the function 'pathMapping' parses
 * the xPath to something the TOCDoc class likes.
 *
 * After if has a start node it starts iterating through. We can assume that the data structuree
 * is a tree as there is a check for each node it it has children. However the code 
 * doesnt seem to iterate recursively through the tree. It rather iterates through it
 * more like a plain list. Inside the loop the building of the jsonString is done.
 * 
 * When we look at the for loop we can see that for each iteration it checks whether
 * the current element name is either of type 'doc' or 'folder'. Otherwise it 
 * continues without adding anything to the jsonString.
 * 
 * After the for loop is done it seems to be doing some ending preparation to the jsonString
 * before its returned
 *
 *
 * From this quick analysis of the function we can see that the function does 3 main things
 * 
 * 1. Gets the first node of the xml tree structure.
 * 2. Loops through the node tree and builds a json string 
 * 3. Finishes up the json string
 *
 * These 3 parts of the code are great candidates for moving over to functions to get
 * our refactoring started.
 * 
 * THE JSON_STRING BUILDING
 * This is where all the meat of the function is. Theres a lot of nested if/elses
 * and further for loops going on. Thats a lot to keep in your head at the same time.
 * Lets work on reducing those if elses. The easiest way is to break down each block
 * into their own functions. When they are separated its easies to look for code duplication
 * and to get an overview what is going on.
 * 

 * - Breaking down the first code blocks
 * We can see that we build on the json if the element type is 'doc' or 'folder'.
 * Lets move those two blocks to their own functions.
 *
 
 * - Looking for duplicated code
 * Now lets compare the two functions. Can we se any similarities? are there actually 
 * Where do they even differ?
 * 
 * **Same:
 * - Opening and closing strings
 * - Iterates through attributes
 * - Adds data string as first entry
 * - Checks for key or trnum

 * **Different:
 * - Document checks for hasChildren.
 * - key and trnum jstrings in long string concat functions.
 *
 * So it seems the only difference here is that document adds a string for hasChildren.
 * and the key and trnum strings are slightly different.
 * Lets newliine the concat strings to analyse each line more closely.
 *
 
 * - Simplifying the string concat.
 * When looking at the doc and folder key/trnum strings, the only place they differ really
 * is the keyContent: '_dk:/_fk:' and trnumContent: '_dtrn:/_fth:' string parts.
 * The rest is totally the same. What we can also see, is that even key and trnum strings
 * only differ in their key or trum contents. So we should probably be able to merge
 * these 4 blocks of code, into only 1 and only conditionally add the 'content' part.


 */



// Create enums for element type to avoid typos, and increase semantic naming (doc, folder)
// Create enums for attribute names 

Util util = new Util();

public String getJson(URL url, String xPathString) {
    Document TOCDoc = util.getDocument(url);
    String jsonString = "[";

    Element node = null;
    if (xPathString.equals("/")) {

        node = TOCDoc.getRootElement();
    } else {
        String realXPathString = pathMapping(xPathString);
        System.out.println(realXPathString);
        node = (Element) TOCDoc.selectSingleNode(realXPathString);
    }

    for (Iterator<Element> i = node.elementIterator(); i.hasNext();) {
        Element elem = (Element) i.next();
        Boolean hasChildren = false;

        if ((elem.elements().size() > 0)) {
            hasChildren = true;
            //current element has children itself, state shoud be "closed"
        }

        List<Attribute> list = elem.attributes();
        String titleAttrContent = elem.attributeValue("title");
        String fileAttrContent = elem.attributeValue("file");

        if (elem.getName() == "doc") {
            jsonString = jsonString.concat("{");

            for (Attribute attribute : list) {
                String attrName = attribute.getName();

                //each one has to have "data" line, "attr" line "state" line and "children" line
                jsonString = jsonString.concat("'data':'").concat(titleAttrContent).concat("',");
                if (attrName.equals("key")) {
                    String keyContent = elem.attributeValue("key");
                    jsonString = jsonString.concat("'attr':{'id':'").concat(xPathString).concat("_dk:").concat(keyContent).concat("','file':'").concat(fileAttrContent).concat("'}");

                    break;
                }

                else if (attrName.equals("trnum")) {

                    String trnumContent = elem.attributeValue("trnum");
                    jsonString = jsonString.concat("'attr':{'id':'").concat(xPathString).concat("_dtrn:").concat(trnumContent).concat("','file':'").concat(fileAttrContent).concat("'}");

                    break;
                }
            }

            if (hasChildren) {
                //state set up as "closed" and no need to set up "children" field
                jsonString = jsonString.concat(",'state':'closed'");
            } else {
                //no need to put anything
                jsonString = jsonString.concat("'state':'???'");
            }
            jsonString = jsonString.concat("},");
        } else if (elem.getName() == "folder") {
            jsonString = jsonString.concat("{");

            for (Attribute attribute : list) {
                String attrName = attribute.getName();
                jsonString = jsonString.concat("'data':'").concat(titleAttrContent).concat("',");

                if (attrName.equals("key")) {
                    String keyContent = elem.attributeValue("key");
                    jsonString = jsonString.concat("'attr':{'id':'").concat(xPathString).concat("_fk:").concat(keyContent).concat("','file':'").concat(fileAttrContent).concat("'}");
            
                    break;
                } else if (attrName.equals("trnum")) {
                    String trnumContent = elem.attributeValue("trnum");
                    jsonString = jsonString.concat("'attr':{'id':'").concat(xPathString).concat("_fth,").concat(trnumContent).concat("','file':'").concat(fileAttrContent).concat("'}");
                    break;
                }
            }
            jsonString = jsonString.concat("},");
        }
        continue;
    }
    //return list;
    jsonString = jsonString.substring(0, jsonString.length() - 1);
    jsonString = jsonString.concat("]");
    return jsonString;
}