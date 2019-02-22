package top.ylonline.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
@FeignClient(name = "githut-api", url = "https://api.github.com")
public interface GithubClient {
    /**
     * 获取 github 仓库的贡献者
     *
     * @param owner github 拥有者
     * @param repo  github repository name
     *
     * @return 贡献者列表
     */
    @GetMapping(value = "/repos/{owner}/{repo}/contributors")
    List<Map<String, Object>> contributors(@PathVariable("owner") String owner, @PathVariable("repo") String repo);
}
