package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SPTP 客户端，支持负载均衡，每隔一段时间会重新解析一遍主机名
 */
public class SPTPClient {

    private static final int DEFAULT_PORT = 9921;

    private static final byte MAGIC = (byte) 0xfa;

    private static final byte MODE_SIMPLE = (byte) 0x00;

    private static final byte MODE_CHUNKED = (byte) 0x01;

    private static final int PACKET_MAX_SIZE = 8192;

    private static final int PACKET_PAYLOAD_MAX_SIZE = PACKET_MAX_SIZE - 12;

    private static final int CHUNKED_MAX_COUNT = 255;

    private static final int PAYLOAD_MAX_SIZE = CHUNKED_MAX_COUNT * PACKET_PAYLOAD_MAX_SIZE;

    private final byte[] buffer = new byte[PACKET_MAX_SIZE];

    private final IntervalChecker resolveChecker = new IntervalChecker(30000);

    @NotNull
    private final String[] hosts;

    private final InetAddressAndPort[] resolvedHosts;

    private final AtomicInteger count = new AtomicInteger();

    private DatagramSocket socket = null;

    public SPTPClient(String[] hosts) {
        this.hosts = hosts;
        this.resolvedHosts = new InetAddressAndPort[hosts.length];
        this.buffer[0] = MAGIC;
        resolveHosts();
    }

    private void resolveHosts() {
        for (int i = 0; i < hosts.length; i++) {
            resolvedHosts[i] = InetAddressAndPort.fromString(hosts[i], DEFAULT_PORT);
        }
    }

    public void send(byte[] payload) throws IOException {
        // payload max size exceeded
        if (PAYLOAD_MAX_SIZE < payload.length) {
            return;
        }
        // resolve hosts if needed
        if (resolveChecker.check()) {
            resolveHosts();
        }
        // calculate host index
        int hostIdx = count.incrementAndGet() % hosts.length;
        // single packet payload
        if (payload.length < PACKET_PAYLOAD_MAX_SIZE) {
            buffer[1] = MODE_SIMPLE;
            System.arraycopy(payload, 0, buffer, 2, payload.length);
            sendPacket(hostIdx, payload.length + 2);
        }
        // chunked packet payload
        // calculate chunks count
        int total = payload.length / PACKET_PAYLOAD_MAX_SIZE;
        if (total % PACKET_PAYLOAD_MAX_SIZE != 0) {
            total++;
        }
        // prepare chunked header
        buffer[1] = MODE_CHUNKED;
        Bytes.randomBytes(buffer, 2, 8);
        buffer[10] = (byte) total;
        for (int i = 0; i < total; i++) {
            buffer[11] = (byte) i;
            int offsetFrom = i * PACKET_PAYLOAD_MAX_SIZE;
            int offsetTo = (i + 1) * PACKET_PAYLOAD_MAX_SIZE;
            if (offsetTo > payload.length) {
                offsetTo = payload.length;
            }
            System.arraycopy(payload, offsetFrom, buffer, 12, offsetTo - offsetFrom);
            sendPacket(hostIdx, 12 + offsetTo - offsetFrom);
        }
    }

    private void sendPacket(int hostIndex, int length) throws IOException {
        if (socket == null) {
            this.socket = new DatagramSocket();
        }
        InetAddressAndPort anp = resolvedHosts[hostIndex];
        if (anp == null) {
            return;
        }
        this.socket.send(new DatagramPacket(buffer, 0, length, anp.getAddress(), anp.getPort()));
    }

}
