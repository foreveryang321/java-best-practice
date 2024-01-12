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
import org.springframework.util.StringUtils;
import top.ylonline.common.cache.annotation.Expired;
import top.ylonline.common.cache.util.CacheUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 {@link org.springframework.cache.interceptor.CacheResolver} 以实现动态过期时间配置
 *
 * @author YL
 */
@Slf4j
public class TCacheResolver extends AbstractCacheResolver {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public TCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Method method = context.getMethod();
        Object[] args = context.getArgs();
        Set<String> cacheNames = context.getOperation().getCacheNames();
        // Shortcut if no args need to be loaded
        if (ObjectUtils.isEmpty(args)) {
            return cacheNames;
        }
        Expired expired = AnnotationUtils.findAnnotation(method, Expired.class);
        if (expired == null) {
            return cacheNames;
        }
        String expiredEl = expired.el();
        if (StringUtils.hasText(expiredEl) && expiredEl.startsWith("#")) {
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
            Expression expression = parser.parseExpression(expiredEl);
            /**
             * 这里可能会抛出异常，不用处理，这样有利于程序发现{@link Expired#el}配置错误问题
             */
            Long ttl = expression.getValue(eval, Long.class);
            if (ttl == null || ttl <= 0) {
                return cacheNames;
            }
            Set<String> names = new HashSet<>();
            for (String cacheName : cacheNames) {
                names.add(CacheUtils.buildCacheNameForTtl(cacheName, ttl));
            }
            return names;
        } else {
            long expiredValue = expired.value();
            if (expiredValue <= 0) {
                return cacheNames;
            }
            Set<String> names = new HashSet<>();
            for (String cacheName : cacheNames) {
                names.add(CacheUtils.buildCacheNameForTtl(cacheName, expiredValue));
            }
            return names;
        }
    }
}
