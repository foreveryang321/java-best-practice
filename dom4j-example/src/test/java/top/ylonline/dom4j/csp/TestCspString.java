package top.ylonline.dom4j.csp;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import top.ylonline.dom4j.Dom4jUtils;
import top.ylonline.dom4j.TestCase;
import top.ylonline.dom4j.XMLParserConfiguration;

/**
 * @author YL
 */
public class TestCspString extends TestCase {
    private static final boolean KEEP_ATTRRIBUTES = false;

    @Test
    public void TdBalInfoReceive() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("csp/TdBalInfo.receive.xml", "gbk");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }

    @Test
    public void TdBalInfoReceiveCdata() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("csp/TdBalInfo.receive.cdata.xml", "utf-8");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }

    @Test
    public void TdBalInfoSend() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("csp/TdBalInfo.send.xml", "utf-8");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }

    @Test
    public void TdBalInfoSendCdata() {
        XMLParserConfiguration configuration = new XMLParserConfiguration(KEEP_ATTRRIBUTES, null, "#text");
        String xml = getByFile("csp/TdBalInfo.send.cdata.xml", "gbk");
        String json = JSON.toJSONString(Dom4jUtils.documentToJSONObject(xml, configuration));
        System.out.println("json -> " + json);
    }
}
