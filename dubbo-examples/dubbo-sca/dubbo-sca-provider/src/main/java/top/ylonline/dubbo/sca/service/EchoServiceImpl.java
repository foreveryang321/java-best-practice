package top.ylonline.dubbo.sca.service;

import org.apache.dubbo.config.annotation.Service;
import top.ylonline.dubbo.sca.api.EchoService;

/**
 * @author YL
 */
@Service(protocol = "dubbo")
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return "Echo, Hello " + message;
    }
}
