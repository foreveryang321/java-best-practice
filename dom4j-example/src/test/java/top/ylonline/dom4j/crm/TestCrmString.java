package top.ylonline.dom4j.crm;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import top.ylonline.dom4j.Dom4jUtils;
import top.ylonline.dom4j.TestCase;
import top.ylonline.dom4j.XMLParserConfiguration;

/**
 * @author YL
 */
public class TestCrmString extends TestCase {
    private static final boolean KEEP_ATTRRIBUTES = false;

    @Test
    public void func015Receive() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("crm/func015.receive.xml", "GB2312");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }

    @Test
    public void func015ReceiveCdata() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("crm/func015.receive.cdata.xml", "utf-8");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }

    @Test
    public void func015Send() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("crm/func015.send.xml", "utf-8");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }

    @Test
    public void func015SendCdata() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("crm/func015.send.cdata.xml", "utf-8");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }
}
