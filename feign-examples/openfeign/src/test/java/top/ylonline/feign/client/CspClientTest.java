package top.ylonline.feign.client;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author YL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@EnableAutoConfiguration
@EnableFeignClients(clients = CspClient.class)
public class CspClientTest {

    @Autowired
    private CspClient cspClient;

    @Test
    public void contributors() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas" +
                ".xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3" +
                ".org/2001/XMLSchema-instance\"><soapenv:Body><ns1:input soapenv:encodingStyle=\"http://schemas" +
                ".xmlsoap.org/soap/encoding/\" xmlns:ns1=\"wsagent\"><ns1:arg0 xsi:type=\"soapenc:string\" " +
                "xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"><![CDATA[<?xml version=\"1.0\" " +
                "encoding=\"GB2312\"?><SendData><BaseInfo><syscode>10000MH</syscode><password>123456</password" +
                "><FuncCode>AllServInfo</FuncCode></BaseInfo><param rows=\"1\" cols=\"5\"><row rownum=\"1\"><col " +
                "colnum=\"1\" param_id=\"1\">769</col><col colnum=\"2\" param_id=\"2\">1</col><col colnum=\"3\" " +
                "param_id=\"3\">1</col><col colnum=\"4\" param_id=\"4\">15362069290</col><col colnum=\"5\" " +
                "param_id=\"5\">201</col></row></param></SendData>]]></ns1:arg0></ns1:input></soapenv:Body></soapenv" +
                ":Envelope>";

        String str = cspClient.call(xml);
        System.out.println(StringEscapeUtils.unescapeXml(str));

    }
}