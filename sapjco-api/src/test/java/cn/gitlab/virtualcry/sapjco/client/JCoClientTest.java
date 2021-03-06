package cn.gitlab.virtualcry.sapjco.client;

import cn.gitlab.virtualcry.sapjco.beans.factory.*;
import cn.gitlab.virtualcry.sapjco.client.function.zmm_shp_getdnhb.DnHeader;
import cn.gitlab.virtualcry.sapjco.client.function.zmm_shp_getdnhb.TableParameter;
import cn.gitlab.virtualcry.sapjco.client.handler.FunctionRequestHandler;
import cn.gitlab.virtualcry.sapjco.client.handler.FunctionResponseHandler;
import cn.gitlab.virtualcry.sapjco.config.JCoDataProvider;
import cn.gitlab.virtualcry.sapjco.config.JCoSettings;
import cn.gitlab.virtualcry.sapjco.util.data.JCoDataUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link JCoClient}.
 *
 * @author VirtualCry
 */
public class JCoClientTest {

    static {
        JCoDataProvider.registerInEnvironment();
        JCoBeanFactoryProvider.getSingleton()
                .register(new DefaultJCoBeanFactory());
        JCoConnectionFactoryProvider.getSingleton()
                .register(new DefaultJCoConnectionFactory());
    }

    private final JCoConnectionFactory connectionFactory;

    public JCoClientTest() {
        this.connectionFactory = JCoConnectionFactoryProvider.getSingleton().getIfAvailable();
    }

    @Before
    public void initialize() {
        JCoSettings settings = JCoSettings.builder()
                .ashost("192.168.0.51")
                .sysnr("00")
                .client("200")
                .user("ittest")
                .password("987654321w")
                .language("ZH")
                .poolCapacity("20")
                .peakLimit("10")
                .build();
        connectionFactory.createClient("testClient", settings);
    }

    @Test
    public void invokeSapFunc() {
        JCoClient client = connectionFactory.getClient("testClient");
        String functionName = "ZMM_SHP_GETDNHB";
        FunctionRequestHandler requestHandler = (importParameter, tableParameter, changingParameter) -> {
            TableParameter tableParameterValue = TableParameter.builder()
                    .dnHeaders(Collections.singletonList(DnHeader.builder().dnNo("0080055489").build()))
                    .build();
            JCoDataUtils.setJCoParameterListValue(tableParameter, tableParameterValue);
        };
        FunctionResponseHandler responseHandler = response -> {
            Map<String, Object> invokeResult = new HashMap<>();
            response.forEach(jCoField -> invokeResult.put(jCoField.getName(), JCoDataUtils.getJCoFieldValue(jCoField)));
            System.out.println(JSON.toJSONString(invokeResult));
        };
        client.invokeSapFunc(functionName, requestHandler, responseHandler);
    }
}
