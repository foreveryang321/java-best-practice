package top.ylonline.sb.redis.v2.core;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * spring-cache 自定义 key 生成规则
 *
 * @author YL
 */
class TKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(o.getClass().getSimpleName());
        sb.append(".");
        sb.append(method.getName());
        if (objects.length > 0) {
            sb.append("#");
        }
        String sp = "";
        for (Object object : objects) {
            sb.append(sp);
            if (object == null) {
                sb.append("NULL");
            } else {
                sb.append(object.toString());
            }
            sp = ".";
        }
        return sb.toString();
    }
}
