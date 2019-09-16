package cn.gitlab.virtualcry.sapjco.beans.factory;

import cn.gitlab.virtualcry.sapjco.client.DefaultJCoClient;
import cn.gitlab.virtualcry.sapjco.client.JCoClient;
import cn.gitlab.virtualcry.sapjco.client.semaphore.JCoClientCreatedOnErrorSemaphore;
import cn.gitlab.virtualcry.sapjco.config.Connections;
import cn.gitlab.virtualcry.sapjco.config.JCoSettings;
import cn.gitlab.virtualcry.sapjco.server.DefaultJCoServer;
import cn.gitlab.virtualcry.sapjco.server.JCoServer;
import cn.gitlab.virtualcry.sapjco.server.semaphore.JCoServerCreatedOnErrorSemaphore;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RFC connection factory.
 *
 * @author VirtualCry
 */
public class ConnectionFactory {

    private static final Map<String, JCoClient>         clients;
    private static final Map<String, JCoServer>         servers;

    static {
        clients = new ConcurrentHashMap<>();
        servers = new ConcurrentHashMap<>();
    }


    /**
     * Get or create a new {@link JCoClient} using the given {@link JCoSettings}
     * @param clientName The {@literal clientName} to be used to cache.
     * @param settings The {@link JCoSettings} to be used.
     * @return A new {@link JCoClient}
     */
    public static JCoClient getOrCreateClient(String clientName, JCoSettings settings) {
        if (clientName == null || "".equals(clientName))
            throw new JCoClientCreatedOnErrorSemaphore("Could not find client name.");
        if (settings == null)
            throw new JCoClientCreatedOnErrorSemaphore("Could not find jco settings.");

        return clients.computeIfAbsent(settings.getUniqueKey(Connections.CLIENT), key -> new DefaultJCoClient(settings));
    }


    /**
     * Get a {@link JCoClient} using the given {@literal clientName}
     * @param clientName The {@literal clientName} to be used to matching.
     * @return  A cache {@link JCoClient}
     */
    public static JCoClient getClient(String clientName) {
        return clients.get(clientName);
    }


    /**
     * Get all cache {@link JCoClient}s
     * @return All cache {@link JCoClient}s
     */
    public static Map<String, JCoClient> getClients() {
        return Collections.unmodifiableMap(clients);
    }


    /**
     * Get or create a new {@link JCoServer} using the given {@link JCoSettings}
     * @param serverName The {@literal serverName} to be used to cache.
     * @param settings The {@link JCoSettings} to be used.
     * @return A new {@link JCoServer}
     */
    public static JCoServer getOrCreateServer(String serverName, JCoSettings settings) {
        if (serverName == null || "".equals(serverName))
            throw new JCoServerCreatedOnErrorSemaphore("Could not find server name.");
        if (settings == null)
            throw new JCoServerCreatedOnErrorSemaphore("Could not find jco settings.");

        return servers.computeIfAbsent(settings.getUniqueKey(Connections.SERVER), key -> new DefaultJCoServer(settings));
    }


    /**
     * Get a {@link JCoServer} using the given {@literal clientName}
     * @param serverName The {@literal serverName} to be used to matching.
     * @return  A cache {@link JCoServer}
     */
    public static JCoServer getServer(String serverName) {
        return servers.get(serverName);
    }


    /**
     * Get all cache {@link JCoServer}s
     * @return All cache {@link JCoServer}s
     */
    public static Map<String, JCoServer> getServers() {
        return Collections.unmodifiableMap(servers);
    }


    /**
     * Release client connection.
     * @param clientName The {@literal clientName} to be used to release.
     */
    public static void releaseClient(String clientName) {
        Optional.ofNullable(clients.remove(clientName))
                .ifPresent(JCoClient::release);
    }


    /**
     * Release client connection.
     * @param serverName The {@literal serverName} to be used to release.
     */
    public static void releaseServer(String serverName) {
        Optional.ofNullable(servers.remove(serverName))
                .ifPresent(JCoServer::release);
    }

}
