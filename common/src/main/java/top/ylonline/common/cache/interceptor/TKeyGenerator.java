package top.ylonline.common.cache.interceptor;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * spring-cache 自定义 key 生成规则
 *
 * @author YL
 */
public class TKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object o, Method method, Object... params) {
        StringBuilder sb = new StringBuilder(32);
        // 拼接规则：类名.函数名
        sb.append(o.getClass().getSimpleName());
        sb.append(".");
        sb.append(method.getName());
        if (params.length == 0) {
            return sb.toString();
        }
        if (params.length == 1) {
            Object param = params[0];
            if (param != null && !param.getClass().isArray()) {
                sb.append(compute(param));
            }
            return sb.toString();
        }
        // 参数拼接规则：类名#函数名#p1.p2.p3
        Object[] arr = new Object[params.length];
        System.arraycopy(params, 0, arr, 0, params.length);
        String str = StringUtils.arrayToDelimitedString(arr, ".");
        sb.append(compute(str));
        return sb.toString();
    }

    private String compute(Object str) {
        return "#" + str;
    }
}
