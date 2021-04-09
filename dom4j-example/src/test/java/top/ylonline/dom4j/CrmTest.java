package top.ylonline.dom4j;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author YL
 */
public class CrmTest extends TestCase {
    private static final boolean KEEP_ATTRRIBUTES = false;
    private static final String ENCODING = "UTF-8";

    @Test
    public void crmReceive() {
        parse("crm.receive.txt");
    }

    @Test
    public void crmReceiveCdata() {
        parse("crm.receive.cdata.txt");
    }

    @Test
    public void crmSend() {
        parse("crm.send.txt");
    }

    @Test
    public void crmSendCdata() {
        parse("crm.send.cdata.txt");
    }

    private void parse(String fileName) {
        XMLParserConfiguration configuration = new XMLParserConfiguration(false, KEEP_ATTRRIBUTES);
        String xml = getByFile(fileName, ENCODING);

        // 去掉<?xml version="1.0" encoding="UTF-8"?>
        xml = xml.replaceAll("(<\\?[^<]*\\?>)?", "");

        String json = JSON.toJSONString(Dom4jUtils.parse(xml, configuration));
        System.out.println(fileName + " --->\n<--- " + json);
    }
}
