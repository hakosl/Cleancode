import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.xml.stream.events.Attribute;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

enum ELEMENT_TYPE {
    DOCUMENT = "doc",
    FOLDER = "folder"
}

enum ATTRIBUTE_NAME {
    TITLE = "title",
    FILE = "file",
    KEY = "key",
    TR_NUM = "trnum",
}

Map<ATTRIBUTE_NAME, Map<ELEMENT_TYPE, string>> contentPrefixes = new HashMap(){
    put(ATTRIBUTE_NAME.KEY, {
        put(ELEMENT_TYPE.DOCUMENT, "dk");
        put(ELEMENT_TYPE.FOLDER, "fk");
    }),
    put(ATTRIBUTE_NAME.TR_NUM, {
        put(ELEMENT_TYPE.DOCUMENT, "dtrn");
        put(ELEMENT_TYPE.FOLDER, "fth");
    }),
};

public String getJson(URL url, String xPath) {
    Element nodeTree = getNodeTree(url, xPath);
    Array<Element> list = nodeTreeToList(nodeTree);
    Array<Element> filteredList = getListFilteredByDocOrFolder(list);

    String jsonString = getJsonStringFromElements(filteredList, xPath);

    return "[" + jsonString  "]";
}

private Element getNodeTree(String url, String xPath) {
    Util util = new Util();
    Document TOCDoc = util.getDocument(url);
    if (xPath.equals("/")) {
        return TOCDoc.getRootElement();
    }

    return (Element) TOCDoc.selectSingleNode(xPath);
}

private List<Element> nodeTreeToList(Element nodeTree) {
    Array<Element> list = new Array();

    for (Iterator<Element> i = nodeTree.elementIterator(); i.hasNext();) {
        list.push((Element) i.next());
    }
    return list;
}

private Array<Element> getListFilteredByDocOrFolder(Element list) {
    Predicate<Element> byDocumentOrFolder = element -> isDocumentOrFolder(element);
    Array<Element> filteredList = list.stream().filter(byDocumentOrFolder).collect(Collectors.toList());
    return filteredList;
}

private String getJsonStringFromElements(Array<Element> list, String xPath) {
    for (Iterator<Element> i = node.elementIterator(); i.hasNext();) {
        Element element = (Element) i.next();
        jsonString += getJsonStringFromAttributes(element, xPath);

        continue;
    }
}

private String getJsonStringFromAttributes(Element element, String xPath) {
    List<Attribute> attributes = element.attributes();
    String titleAttrContent = element.attributeValue(ATTRIBUTE_NAME.TITLE);

    Boolean isDocument = element.getName() === ELEMENT_TYPE.DOCUMENT;
    String stateString = isDocument ? getStateString(elem) : "";
    String attributesString = "";

    for (Attribute attribute : attributes) {
        attributesString += String.format("'data':'%s',", titleAttrContent);

        String attributeName = attribute.getName();
        Boolean isKeyOrTrnum = attributeName.equals(ATTRIBUTE_NAME.KEY) || attributeName.equals(ATTRIBUTE_NAME.TR_NUM);
        if (isKeyOrTrnum) {
            attributesString += getAttributeString(element, xPath);
            break;
        }
    }

    return String.format("{%s%s}", attributesString, stateString)
}

private getAttributeString(Element element, String xPath) {
    String fileAttrContent = element.attributeValue(ATTRIBUTE_NAME.FILE);
    String content = getKeyOrTrnumContent(element);

    return String.format("'attr':{'id':'%s_%s','file':'%s'}", xPath, content, fileAttrContent)
}

private String getKeyOrTrnumContent(Element element) {
    Boolean isDocument = element.getName() === ELEMENT_TYPE.DOCUMENT;

    if (attrName.equals(ATTRIBUTE_NAME.KEY)) {
        String keyContent = element.attributeValue(ATTRIBUTE_NAME.KEY);
        const prefix = isDocument ? "_dk:" : "_fk:";
        return prefix + keyContent;
    } else {
        String trnumContent = element.attributeValue(ATTRIBUTE_NAME.TR_NUM);
        const prefix = isDocument ? "_dtrn:" : "_fth";
        return prefix + trnumContent;
    }
}

// Alternative version
private String getKeyOrTrnumContent(Element element) {
    String attributeName = attribute.getName();
    String elementType = element.getName();

    String idPostFix = contentPrefixes.get(attributeName).get(elementType);
    String idContent = element.attributeValue(attributeName);
    return idPostFix + ":" + idContent;
}

private String getStateString(Element element) {
    return hasChildren(element) ? ",'state':'closed'" : "'state':'???'";
}

private Boolean hasChildren(Element elem) {
    return elem.elements().size() > 0;
}
