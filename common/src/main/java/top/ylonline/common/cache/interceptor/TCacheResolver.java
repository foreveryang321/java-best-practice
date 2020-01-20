package top.ylonline.common.cache.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import top.ylonline.common.cache.annotation.Expired;
import top.ylonline.common.util.StrUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 {@link org.springframework.cache.interceptor.CacheResolver} 以实现动态过期时间配置
 *
 * <p>
 * 由于该实现是重新构建一个cacheName，这样会导致配合CachePut、CacheEvict等使用时会有问题。
 * </p>
 *
 * @author YL
 */
@Slf4j
public class TCacheResolver extends AbstractCacheResolver {

    public TCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    private ExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Method method = context.getMethod();
        Object[] args = context.getArgs();
        Set<String> cacheNames = context.getOperation().getCacheNames();
        Expired expired = AnnotationUtils.findAnnotation(method, Expired.class);
        if (expired == null || StrUtils.isBlank(expired.spEl())) {
            return cacheNames;
        }
        // Shortcut if no args need to be loaded
        if (ObjectUtils.isEmpty(args)) {
            return cacheNames;
        }
        // Expose indexed variables as well as parameter names (if discoverable)
        String[] paramNames = this.discoverer.getParameterNames(method);
        int paramCount = (paramNames != null ? paramNames.length : method.getParameterCount());
        int argsCount = args.length;

        EvaluationContext eval = new StandardEvaluationContext();
        for (int i = 0; i < paramCount; i++) {
            Object value = null;
            if (argsCount > paramCount && i == paramCount - 1) {
                // Expose remaining arguments as vararg array for last parameter
                value = Arrays.copyOfRange(args, i, argsCount);
            } else if (argsCount > i) {
                // Actual argument found - otherwise left as null
                value = args[i];
            }
            /**
             * see {@link MethodBasedEvaluationContext#lazyLoadArguments()}
             */
            eval.setVariable("a" + i, value);
            eval.setVariable("p" + i, value);
            if (paramNames != null) {
                eval.setVariable(paramNames[i], value);
            }
        }
        Expression expression = parser.parseExpression(expired.spEl());
        Long ttl = expression.getValue(eval, Long.class);
        if (ttl == null || ttl <= 0) {
            return cacheNames;
        }
        Set<String> names = new HashSet<>();
        for (String cacheName : cacheNames) {
            names.add(cacheName + ".exp_" + ttl);
        }
        return names;
    }
}
