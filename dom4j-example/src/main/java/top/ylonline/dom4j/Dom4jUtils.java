package top.ylonline.dom4j;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import top.ylonline.common.util.StrUtils;

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
     * org.dom4j.Document 转 Map
     *
     * @param xml xml 报文
     */
    public static Map<String, Object> parse(String xml) {
        return parse(xml, new XMLParserConfiguration());
    }

    /**
     * org.dom4j.Document 转 Map
     *
     * @param xml    xml 报文
     * @param config 自定义配置
     */
    public static Map<String, Object> parse(String xml, XMLParserConfiguration config) {
        Document document = strToDocument(xml);
        return parse(document, config);
    }

    private static Map<String, Object> parse(Document document, XMLParserConfiguration config) {
        Map<String, Object> map = new HashMap<>(3);
        if (document == null) {
            return map;
        }
        Element root = document.getRootElement();
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) {
            Element element = it.next();
            String name = element.getName();
            if (!element.elements().isEmpty()) {
                Map<String, Object> data = parse(element, config);
                hasChildElementToMap(config, element, data);
                // 如果存在多个
                Object obj = map.get(name);
                if (obj != null) {
                    List<Object> list;
                    if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                        list = (List<Object>) obj;
                        list.add(data);
                    } else {
                        list = new ArrayList<>();
                        list.add(obj);
                        list.add(data);
                    }
                    map.put(name, list);
                } else {
                    map.put(name, data);
                }
            } else {
                textOnlyElementToMap(config, element, map);
            }
        }
        if (config.isKeepRoot()) {
            Map<String, Object> tmp = new HashMap<>(3);
            tmp.put(root.getName(), map);
            return tmp;
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parse(Element e, XMLParserConfiguration config) {
        Map<String, Object> map = new HashMap<>(4);

        if (e.elements().isEmpty()) {
            textOnlyElementToMap(config, e, map);
            return map;
        }

        List<Element> elements = e.elements();
        for (Element element : elements) {
            String name = element.getName();
            List<Object> list;
            if (element.elements().size() > 0) {
                Map<String, Object> data = parse(element, config);
                if (map.get(name) != null) {
                    Object obj = map.get(name);
                    if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                        list = (List<Object>) obj;
                        list.add(data);
                    } else {
                        list = new ArrayList<>();
                        list.add(obj);
                        hasChildElementToMap(config, element, data);
                        list.add(data);
                    }
                    map.put(name, list);
                } else {
                    hasChildElementToMap(config, element, data);
                    map.put(name, data);
                }
            } else {
                if (map.get(name) != null) {
                    Object obj = map.get(name);
                    if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                        list = (List<Object>) obj;
                        textOnlyElementToList(config, element, list);
                    } else {
                        list = new ArrayList<>();
                        list.add(obj);
                        textOnlyElementToList(config, element, list);
                    }
                    map.put(name, list);
                } else {
                    textOnlyElementToMap(config, element, map);
                }
            }
        }
        return map;
    }

    private static void hasChildElementToMap(XMLParserConfiguration config, Element element, Map<String,
            Object> data) {
        if (config.isKeepAttributes() && !element.attributes().isEmpty()) {
            Map<String, Object> tmp = attributeToMap(element, false);
            data.putAll(tmp);
        }
    }

    private static void textOnlyElementToList(XMLParserConfiguration config, Element element, List<Object> list) {
        if (config.isKeepAttributes() && !element.attributes().isEmpty()) {
            Map<String, Object> tmp = attributeToMap(element, true);
            list.add(tmp);
        } else {
            String textTrim = element.getTextTrim();
            // 判断是否为空，防止某些 Map、List 节点（比如：<CumulativeItemList />）错误返回成 String 类型
            if (StrUtils.isNotBlank(textTrim)) {
                list.add(textTrim);
            }
        }
    }

    private static void textOnlyElementToMap(XMLParserConfiguration config, Element element, Map<String,
            Object> map) {
        String name = element.getName();
        if (config.isKeepAttributes() && !element.attributes().isEmpty()) {
            Map<String, Object> tmp = attributeToMap(element, true);
            map.put(name, tmp);
        } else {
            String textTrim = element.getTextTrim();
            // 判断是否为空，防止某些 Map、List 节点（比如：<CumulativeItemList />）错误返回成 String 类型
            if (StrUtils.isNotBlank(textTrim)) {
                map.put(name, textTrim);
            }
        }
    }

    private static Map<String, Object> attributeToMap(Element element, boolean needText) {
        Map<String, Object> tmp = new HashMap<>(4);
        if (needText) {
            tmp.put("#text", element.getTextTrim());
        }
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            String name = attribute.getName();
            String value = attribute.getStringValue();
            tmp.put("@" + name, value);
        }
        return tmp;
    }
}
