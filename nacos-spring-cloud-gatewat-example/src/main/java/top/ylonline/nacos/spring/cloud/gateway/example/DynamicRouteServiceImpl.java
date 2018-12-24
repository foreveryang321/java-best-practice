package top.ylonline.nacos.spring.cloud.gateway.example;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 动态路由实现逻辑
 *
 * @author Created by YL on 2018/12/22
 */
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    // 添加路由
    public String add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    // 更新路由
    public String update(RouteDefinition definition) {
        try {
            this.delete(definition.getId());
        } catch (Exception e) {
            return "update fail, not found route[" + definition.getId() + "].";
        }
        try {
            return this.add(definition);
        } catch (Exception e) {
            return "update fail, not found route[" + definition.getId() + "]. then add fail.";
        }
    }

    public Mono<ResponseEntity<Object>> delete(String id) {
        return this.routeDefinitionWriter.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(ResponseEntity.notFound().build()));
    }
}
