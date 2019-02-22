package top.ylonline.dubbo.spring.boot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ylonline.dubbo.spring.boot.api.DubboService;
import top.ylonline.dubbo.spring.boot.api.MultipleService;
import top.ylonline.dubbo.spring.boot.api.RestService;

import javax.annotation.Resource;

/**
 * @author YL
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    /**
     * dubbo-2.6.5及以下版本的Reference还不支持protocol属性
     */
    @Resource
    private MultipleService multipleService;
    @Resource
    private DubboService dubboService;
    @Resource
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
