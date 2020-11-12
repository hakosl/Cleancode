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

public String getJson(URL url, String xPathString) {
    Element nodeTree = getNodeTree(xPathString, url);
    Array<Element> list = nodeTreeToList(nodeTree);
    Array<Element> filteredList = getFilteredList(list);

    String jsonString = "[";
    jsonString += getJsonStringFromElements(filteredList, xPathString)
    jsonString = jsonString.substring(0, jsonString.length() - 1);
    jsonString += "]";
    return jsonString;
}

private String getJsonStringFromElements() {
    Array<String> jsonStrings = Stream.of(filteredList)
        .map(element -> getJsonStringFromElement(element, xPathString));
    return StringUtils.join(jsonStrings, "");
}

private String getJsonStringFromElement(Element element, String xPathString) {
    String attributesString = getJsonStringFromAttributes(list, element);
    String stateString = getStateString(element);

    return String.format("{%s%s}", attributesString, stateString)
}

private getJsonStringFromAttributes(Element element) {
    List<Attribute> list = element.attributes();
    String titleAttrContent = element.attributeValue(ATTRIBUTE_NAME.TITLE);

    String jsonString = "";
    for (Attribute attribute : list) {
        jsonString += String.format("'data':'%s',", titleAttrContent);

        if (isAttributeName(attribute, [ATTRIBUTE_NAME.KEY, ATTRIBUTE_NAME.TR_NUM])) {
            jsonString += getJsonStringByAttributeName(attribute, element);
            break;
        }
    }
    return jsonString;
}

private String getJsonStringFromAttribute(Attribute attribute, Element element) {
    String attributeName = attribute.getName();
    String elementName = element.getName();

    String idPostFix = contentPrefixes.get(attributeName).get(elementName);
    String content = element.attributeValue(attributeName);
    return getAttributeString(idPostFix, content, element)
}

private String getAttributeString(String idPostfix, String idContent, Element element) {
    String fileAttrContent = element.attributeValue(ATTRIBUTE_NAME.FILE);
    return String.format("'attr':{'id':'%s_%s:%s','file':'%s'}", xPathString, idPostFix, idContent, fileAttrContent)
}

private String getStateString(Element element) {
    Boolean hasChildren = element.elements().size() > 0;
    Boolean isDocument = element.getName() === ELEMENT_TYPE.DOCUMENT;

    if (isDocument) {
        return hasChildren ? ",'state':'closed'" : "'state':'???'";
    }
    return "";
}

private List<Element> nodeTreeToList(Element nodeTree) {
    Array<Element> list = new Array();

    for (Iterator<Element> i = nodeTree.elementIterator(); i.hasNext();) {
        list.push((Element) i.next());
    }
    return list;
}

private List<Element> getFilteredList(Element nodeTree) {
    Predicate<Element> byType = element -> isDocumentOrFolder(element);
    Array<Element> filteredList = list.stream().filter(byType).collect(Collectors.toList());
    return filteredList;
}

private boolean isAttributeName(Attribute attribute, ATTRIBUTE_NAME[] names) {
    String attributeName = attribute.getName();
    List<String> names = Arrays.asList(names);
    return names.contains(attributeName);
}

private boolean isDocumentOrFolder(Element element) {
    String elementName = element.getName();
    List<String> types = Arrays.asList([ELEMENT_TYPE.DOCUMENT, ELEMENT_TYPE.FOLDER]);
    return types.contains(elementName);
}

private Element getNodeTree(String xPathString, String url) {
    Document TOCDoc = util.getDocument(url);
    if (xPathString.equals("/")) {
        return TOCDoc.getRootElement();
    }

    String realXPathString = pathMapping(xPathString);
    return (Element) TOCDoc.selectSingleNode(realXPathString);
}