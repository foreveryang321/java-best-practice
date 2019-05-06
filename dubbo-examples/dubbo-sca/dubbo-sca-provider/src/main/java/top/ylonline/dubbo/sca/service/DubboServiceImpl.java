package top.ylonline.dubbo.sca.service;

import org.apache.dubbo.config.annotation.Service;
import top.ylonline.dubbo.sca.api.DubboService;

/**
 * @author YL
 */
@Service(protocol = "dubbo")
public class DubboServiceImpl implements DubboService {

    @Override
    public String echo(String message) {
        return "Hello " + message;
    }
}
