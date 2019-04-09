package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 简单的类，同时存储 IP 地址 和 端口号
 */
public class InetEndpoint {

    @NotNull
    private final InetAddress address;

    private final int port;

    public InetEndpoint(@NotNull InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public @NotNull InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    /**
     * 从字符串分析出地址和端口号，以冒号分割，因此暂时不支持 IPv6
     *
     * @param str         字符串
     * @param defaultPort 默认端口号
     * @return IP地址和端口号
     */
    public static @Nullable InetEndpoint fromString(@NotNull String str, int defaultPort) {
        String host = null;
        int port = 0;
        if (str.contains(":")) {
            String[] compos = str.split(":");
            host = compos[0];
            try {
                port = Integer.valueOf(compos[1]);
            } catch (NumberFormatException ignored) {
                port = defaultPort;
            }
        } else {
            host = str;
            port = defaultPort;
        }
        try {
            InetAddress address = InetAddress.getByName(host);
            return new InetEndpoint(address, port);
        } catch (UnknownHostException e) {
            return null;
        }
    }

}
