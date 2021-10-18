import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.xml.stream.events.Attribute;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;


public String getJson(URL url, String xPathString) {
    Util util = new Util();
    Document TOCDoc = util.getDocument(url);
    String jsonString = "[";

    Element node = null;
    if (xPathString.equals("/")) {

        node = TOCDoc.getRootElement();
    } else {
        node = (Element) TOCDoc.selectSingleNode(xPathString);
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