package top.ylonline.feign.client;

import feign.Param;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * @author YL
 */
public interface Github {
    /**
     * 获取 github 仓库的贡献者
     *
     * @param owner github 拥有者
     * @param repo  github repository name
     *
     * @return 贡献者列表
     */
    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Map<String, Object>> contributors(@Param("owner") String owner, @Param("repo") String repo);
}
