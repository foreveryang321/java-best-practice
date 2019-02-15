package top.ylonline.dom4j;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
public class Dom4jUtils {
    /**
     * String 转 org.dom4j.Document
     */
    private static Document strToDocument(String xml) {
        try {
            // SAXReader reader = new SAXReader();
            // try {
            //     // xxe 漏洞：禁用 DTD、Entity（外部实体类）
            //     reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            //     reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            //     reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            //     reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            // } catch (SAXException e) {
            //     // ignore
            // }
            return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            return null;
        }
    }

    /**
     * org.dom4j.Document 转  com.alibaba.fastjson.JSONObject
     *
     * @param xml    xml 报文
     * @param config 需不需要解析 attribute
     */
    public static Map<String, Object> documentToJSONObject(String xml, XMLParserConfiguration config) {
        Document document = strToDocument(xml);
        return parse(document, config);
    }

    private static Map<String, Object> parse(Document document, XMLParserConfiguration config) {
        Map<String, Object> map = new HashMap<>(1);
        if (document == null) {
            return map;
        }
        Element root = document.getRootElement();
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) {
            Element element = it.next();
            String name = element.getName();
            // System.out.println("-1: " + name + " ---> " + element.getTextTrim());
            if (!element.elements().isEmpty()) {
                Map<String, Object> data = parse(element, config);
                hasChildElementToMap(config, element, data);
                map.put(name, data);
            } else {
                map.put(name, element.getTextTrim());
            }
        }
        if (config.isKeepRoot()) {
            Map<String, Object> tmp = new HashMap<>(1);
            tmp.put(root.getName(), map);
            return tmp;
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parse(Element e, XMLParserConfiguration config) {
        Map<String, Object> map = new HashMap<>(4);

        if (e.elements().isEmpty()) {
            map.put(e.getName(), e.getTextTrim());
            return map;
        }

        List<Element> elements = e.elements();
        for (Element element : elements) {
            String name = element.getName();
            // String textTrim = element.getTextTrim();

            // System.out.println("0: " + name + " ---> " + textTrim);

            List list;

            if (element.elements().size() > 0) {
                // System.out.println("1: " + name + " ---> " + textTrim);
                Map<String, Object> data = parse(element, config);
                if (map.get(name) != null) {
                    Object obj = map.get(name);
                    if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                        list = (List) obj;
                        list.add(data);
                        // System.out.println("2: " + name + " ---> " + data);
                    } else {
                        list = new ArrayList();
                        list.add(obj);
                        // list.add(data);

                        hasChildElementToMap(config, element, data);
                        list.add(data);
                        // System.out.println("3: " + name + " ---> " + data);
                    }
                    map.put(name, list);
                } else {
                    // System.out.println("4: " + name + " ---> " + data);
                    // map.put(name, data);

                    hasChildElementToMap(config, element, data);
                    map.put(name, data);
                }
            } else {
                if (map.get(name) != null) {
                    // System.out.println("5: " + name + " ---> " + textTrim);
                    Object obj = map.get(name);
                    if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                        list = (List) obj;
                        // list.add(textTrim);
                        textOnlyElementToList(config, element, list);
                    } else {
                        list = new ArrayList();
                        list.add(obj);
                        // list.add(textTrim);
                        textOnlyElementToList(config, element, list);
                    }
                    map.put(name, list);
                } else {
                    // System.out.println("6: " + name + " ---> " + textTrim);
                    // map.put(name, textTrim);
                    textOnlyElementToMap(config, element, map);
                }
            }
        }
        return map;
    }

    private static void hasChildElementToMap(XMLParserConfiguration config, Element element, Map<String,
            Object> data) {
        if (config.isKeepAttributes() && !element.attributes().isEmpty()) {
            Map<String, Object> tmp = attributeToMap(element, false, null);
            data.putAll(tmp);
        }
    }

    @SuppressWarnings("unchecked")
    private static void textOnlyElementToList(XMLParserConfiguration config, Element element, List list) {
        if (config.isKeepAttributes() && !element.attributes().isEmpty()) {
            Map<String, Object> tmp = attributeToMap(element, true, config.getCdataTagName());
            list.add(tmp);
        } else {
            String textTrim = element.getTextTrim();
            list.add(textTrim);
        }
    }

    private static void textOnlyElementToMap(XMLParserConfiguration config, Element element, Map<String,
            Object> map) {
        String name = element.getName();
        if (config.isKeepAttributes() && !element.attributes().isEmpty()) {
            Map<String, Object> tmp = attributeToMap(element, true, config.getCdataTagName());
            map.put(name, tmp);
        } else {
            String textTrim = element.getTextTrim();
            map.put(name, textTrim);
        }
    }

    private static Map<String, Object> attributeToMap(Element element, boolean needText, String tagName) {
        Map<String, Object> tmp = new HashMap<>(4);
        if (needText) {
            if (tagName == null || "".equals(tagName.trim())) {
                throw new NullPointerException("tagName can not be empty!");
            }
            tmp.put(tagName, element.getTextTrim());
        }
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            String attributeName = attribute.getName();
            tmp.put("@" + attributeName, attribute.getValue());
        }
        return tmp;
    }
}
