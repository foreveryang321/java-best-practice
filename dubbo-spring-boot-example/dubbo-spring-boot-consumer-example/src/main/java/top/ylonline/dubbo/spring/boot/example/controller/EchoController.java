package top.ylonline.dubbo.spring.boot.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ylonline.dubbo.spring.boot.example.api.EchoService;

import javax.annotation.Resource;

/**
 * @author YL
 */
@RestController
@RequestMapping("/demo")
public class EchoController {
    /**
     * dubbo-2.6.5及以下版本的Reference还不支持protocol属性
     */
    // @Reference//(url = "dubbo://localhost:20880")
    @Resource
    private EchoService dubboEchoService;
    // @Reference//(url = "rest://localhost:9090")
    @Resource
    private EchoService restEchoService;

    @GetMapping("/dubbo")
    public String dubbo(@RequestParam String message) {
        return dubboEchoService.echo(message);
    }

    @GetMapping("/rest")
    public String rest(@RequestParam String message) {
        return restEchoService.echo(message);
    }
}
