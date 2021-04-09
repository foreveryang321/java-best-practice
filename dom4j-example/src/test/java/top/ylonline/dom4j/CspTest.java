package top.ylonline.dom4j;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author YL
 */
public class CspTest extends TestCase {
    private static final boolean KEEP_ATTRRIBUTES = false;
    private static final String ENCODING = "UTF-8";

    @Test
    public void cpsReceive() {
        parse("csp.receive.txt");
    }

    @Test
    public void cpsReceiveCdata() {
        parse("csp.receive.cdata.txt");
    }

    @Test
    public void cpsSend() {
        parse("csp.send.txt");
    }

    @Test
    public void cpsSendCdata() {
        parse("csp.send.cdata.txt");
    }

    private void parse(String fileName) {
        XMLParserConfiguration configuration = new XMLParserConfiguration(false, KEEP_ATTRRIBUTES);
        String xml = getByFile(fileName, ENCODING);
        // if ("csp.receive.txt".equals(fileName)){
        //     if (StringUtils.isNotBlank(xml)) {
        //         String ir = "<inputReturn xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap" +
        //                 ".org/soap/encoding/\">";
        //         if (xml.contains(ir)) {
        //             xml = xml.replace(ir, ir + "<![CDATA[")
        //                     .replace("</inputReturn>", "]]></inputReturn>");
        //         }
        //     }
        // }

        // 去掉<?xml version="1.0" encoding="UTF-8"?>
        xml = xml.replaceAll("(<\\?[^<]*\\?>)?", "");

        String json = JSON.toJSONString(Dom4jUtils.parse(xml, configuration));
        System.out.println(fileName + " --->\n<--- " + json);
    }
}
