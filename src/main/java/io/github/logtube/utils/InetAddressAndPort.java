package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressAndPort {

    private final InetAddress address;

    private final int port;

    public InetAddressAndPort(@NotNull InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @NotNull
    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Nullable
    public static InetAddressAndPort fromString(@NotNull String str, int defaultPort) {
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
            return new InetAddressAndPort(address, port);
        } catch (UnknownHostException e) {
            return null;
        }
    }

}
