# dom4j
`org.dom4j.2.1.0` 版本以前，使用 `DocumentHelper.parseText`会存在`XXE` 漏洞，代码如下
```java
public static Document parseText(String text) throws DocumentException {
    SAXReader reader = new SAXReader();
    String encoding = getEncoding(text);

    InputSource source = new InputSource(new StringReader(text));
    source.setEncoding(encoding);

    Document result = reader.read(source);

    // if the XML parser doesn't provide a way to retrieve the encoding,
    // specify it manually
    if (result.getXMLEncoding() == null) {
        result.setXMLEncoding(encoding);
    }

    return result;
}
```
`org.dom4j.2.1.1` 版本以后，使用 `DocumentHelper.parseText`不会存在`XXE` 漏洞，代码如下
```java
public static Document parseText(String text) throws DocumentException {
    SAXReader reader = new SAXReader();
    try {
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    } catch (SAXException e) {
        //Parse with external resources downloading allowed.
    }

    String encoding = getEncoding(text);

    InputSource source = new InputSource(new StringReader(text));
    source.setEncoding(encoding);

    Document result = reader.read(source);

    // if the XML parser doesn't provide a way to retrieve the encoding,
    // specify it manually
    if (result.getXMLEncoding() == null) {
        result.setXMLEncoding(encoding);
    }

    return result;
}
```