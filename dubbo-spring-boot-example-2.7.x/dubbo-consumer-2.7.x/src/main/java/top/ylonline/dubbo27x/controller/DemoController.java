package top.ylonline.dubbo27x.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ylonline.dubbo27x.api.DubboService;
import top.ylonline.dubbo27x.api.MultipleService;
import top.ylonline.dubbo27x.api.RestService;

/**
 * @author YL
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    // @Resource
    // private MultipleService multipleService;
    // @Resource
    // private DubboService dubboService;
    // @Resource
    // private RestService restService;

    @Reference(protocol = "dubbo")
    private MultipleService multipleService;
    @Reference
    private DubboService dubboService;
    @Reference
    private RestService restService;

    @GetMapping("/multiple")
    public String user(@RequestParam String message) {
        return multipleService.echo(message);
    }

    @GetMapping("/dubbo")
    public String dubbo(@RequestParam String message) {
        return dubboService.echo(message);
    }

    @GetMapping("/rest")
    public String rest(@RequestParam String message) {
        return restService.echo(message);
    }
}
