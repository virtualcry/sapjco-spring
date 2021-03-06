package cn.gitlab.virtualcry.sapjco.beans.factory;

/**
 * Provider for {@link JCoConnectionFactory}.
 *
 * @author VirtualCry
 * @since 3.2.3
 */
public class JCoConnectionFactoryProvider extends Provider<JCoConnectionFactory> {

    private static class JCoConnectionFactoryProviderInstance {
        private static final JCoConnectionFactoryProvider INSTANCE = new JCoConnectionFactoryProvider();
    }

    private JCoConnectionFactoryProvider() { }

    public static JCoConnectionFactoryProvider getSingleton() {  // singleton
        return JCoConnectionFactoryProviderInstance.INSTANCE;
    }
}
