package top.ylonline.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author YL
 */
@FeignClient(
        name = "csp-interface-platform",
        url = "http://172.20.1.168:8882/smtccs_agent_intf/services/wsagent"
)
public interface CspClient {

    /**
     * 调用csp平台接口
     *
     * @param xml 入参
     *
     * @return 接口出参
     */
    @PostMapping(headers = {"SOAPAction=\"\"", "Content-Type=text/xml; charset=gbk"})
    String call(@RequestBody String xml);
}
