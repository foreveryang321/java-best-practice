package top.ylonline.sentinel.init;

import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * @author YL
 */
public class NacosDatasourceInitFunc implements InitFunc {
    private static final String SERVER_ADDR = "192.168.56.101:8848";
    private static final String GROUP_ID = "SENTINEL_GROUP";

    private static final String APP_NAME = AppNameUtil.getAppName();
    private static final String FLOW_DATA_ID_POSTFIX = APP_NAME + "-flow-rules";
    private static final String PARAMETER_FLOW_DATA_ID_POSTFIX = APP_NAME + "-parameter-flow-rules";
    private static final String AUTHORITY_DATA_ID_POSTFIX = APP_NAME + "-authority-rules";
    private static final String DEGRADE_DATA_ID_POSTFIX = APP_NAME + "-degrade-rules";
    private static final String SYSTEM_DATA_ID_POSTFIX = APP_NAME + "-system-rules";

    private static final String CLUSTER_CLIENT_CONFIG_DATA_ID_POSTFIX = APP_NAME + "-cluster-client-config";

    @Override
    public void init() throws Exception {
        registerDynamicRuleProperty();
        registerClusterClientConfigProperty();
    }

    /**
     * 注册动态数据源
     */
    private void registerDynamicRuleProperty() {
        // flow rule
        ReadableDataSource<String, List<FlowRule>> flowDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                FLOW_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                })
        );
        FlowRuleManager.register2Property(flowDS.getProperty());

        // parameter rule
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                PARAMETER_FLOW_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                })
        );
        ParamFlowRuleManager.register2Property(paramFlowDS.getProperty());

        // degrade rule
        ReadableDataSource<String, List<DegradeRule>> degradeDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                DEGRADE_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                })
        );
        DegradeRuleManager.register2Property(degradeDS.getProperty());

        // authority rule
        ReadableDataSource<String, List<AuthorityRule>> authorityDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                AUTHORITY_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {
                })
        );
        AuthorityRuleManager.register2Property(authorityDS.getProperty());

        // system rule
        ReadableDataSource<String, List<SystemRule>> systemDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                SYSTEM_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
                })
        );
        SystemRuleManager.register2Property(systemDS.getProperty());
    }

    private void registerClusterClientConfigProperty() {
        ReadableDataSource<String, ClusterClientConfig> clusterClientDS = new NacosDataSource<>(
                SERVER_ADDR,
                GROUP_ID,
                CLUSTER_CLIENT_CONFIG_DATA_ID_POSTFIX,
                source -> JSON.parseObject(source, new TypeReference<ClusterClientConfig>() {
                })
        );
        ClusterClientConfigManager.register2Property(clusterClientDS.getProperty());
    }
}
