package top.ylonline.dom4j;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author YL
 */
public class TestCase {
    protected String getByFile(String fileName, String encoding) {
        ClassLoader classLoader = TestCase.class.getClassLoader();
        try {
            URL url = classLoader.getResource(fileName);
            return IOUtils.toString(new FileInputStream(new File(url.getFile())), encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected String removeXmlStringNamespaceAndPreamble(String xmlString) {
        return xmlString//.replaceAll("(<\\?[^<]*\\?>)?", "") //去掉<?xml version="1.0" encoding="UTF-8"?>
                .replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") //去掉xmlns:
                .replaceAll("(<)(\\w+:)", "$1") //去掉开头标签的冒号前的前缀
                .replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); //去掉结尾标签的冒号前前缀
    }
}
