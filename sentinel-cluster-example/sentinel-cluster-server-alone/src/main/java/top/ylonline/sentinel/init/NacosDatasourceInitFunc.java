package top.ylonline.sentinel.init;

import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Set;

/**
 * @author YL
 */
public class NacosDatasourceInitFunc implements InitFunc {
    private static final String SERVER_ADDR = "192.168.56.101:8848";
    private static final String GROUP_ID = "SENTINEL_GROUP";

    private static final String APP_NAME = AppNameUtil.getAppName();
    private static final String FLOW_DATA_ID_POSTFIX = APP_NAME + "-flow-rules";
    private static final String PARAMETER_FLOW_DATA_ID_POSTFIX = APP_NAME + "-parameter-flow-rules";

    private static final String CLUSTER_SERVER_NAMESPACE_DATA_ID_POSTFIX = "-server-namespace-config";
    private static final String CLUSTER_SERVER_TRANSPORT_DATA_ID_POSTFIX = "-server-transport-config";

    @Override
    public void init() throws Exception {
        registerClusterRuleSupplier();
        register();
    }

    /**
     * for token server
     */
    private void registerClusterRuleSupplier() {
        // Register cluster flow rule property supplier which creates data source by namespace.
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<FlowRule>> flowDS = new NacosDataSource<>(
                    SERVER_ADDR,
                    GROUP_ID,
                    FLOW_DATA_ID_POSTFIX,
                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                    })
            );
            return flowDS.getProperty();
        });

        // Register cluster parameter flow rule property supplier which creates data source by namespace.
        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<ParamFlowRule>> paramFlowDS = new NacosDataSource<>(
                    SERVER_ADDR,
                    GROUP_ID,
                    PARAMETER_FLOW_DATA_ID_POSTFIX,
                    source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                    })
            );
            return paramFlowDS.getProperty();
        });
    }

    /**
     * for token server
     */
    private void register() {
        // cluster server namespace set(scope) datasource
        ReadableDataSource<String, Set<String>> namespaceDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                AppNameUtil.getAppName() + CLUSTER_SERVER_NAMESPACE_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<Set<String>>() {
                })
        );
        ClusterServerConfigManager.registerNamespaceSetProperty(namespaceDS.getProperty());

        // cluster server transport configuration datasource
        ReadableDataSource<String, ServerTransportConfig> transportDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                AppNameUtil.getAppName() + CLUSTER_SERVER_TRANSPORT_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<ServerTransportConfig>() {
                })
        );
        ClusterServerConfigManager.registerServerTransportProperty(transportDS.getProperty());
    }
}
