package top.ylonline.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author YL
 */
@FeignClient(
        name = "ic",
        url = "http://127.0.0.1:8888/ic/services/ws"
)
public interface CspClient {

    /**
     * 调用 ic 平台接口
     *
     * @param xml 入参
     *
     * @return 接口出参
     */
    @PostMapping(headers = {"SOAPAction=\"\"", "Content-Type=text/xml; charset=gbk"})
    String call(@RequestBody String xml);
}
