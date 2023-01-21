package fr.uge.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientUpperCaseUDPTimeout {
    private static final int BUFFER_SIZE = 1024;

    private static void usage() {
        System.out.println("Usage : NetcatUDP host port charset");
    }

    private static final Logger LOGGER = Logger.getLogger(ClientUpperCaseUDPTimeout.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            usage();
            return;
        }

        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        var cs = Charset.forName(args[2]);
        var buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try (DatagramChannel dc = DatagramChannel.open();) {
            dc.bind(null);
            var threadListener = Thread.ofPlatform().start(() -> {
                while(!Thread.interrupted()) {
                    try {
                        var sender = (InetSocketAddress) dc.receive(buffer);
                        buffer.flip();
                        var msg = cs.decode(buffer);
                        queue.put(msg.toString());
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Receive exception", e);
                    } catch (InterruptedException e){
                        LOGGER.info("Interrupted");
                        return;
                    }
                    buffer.clear();
                }
            });
            try (var scanner = new Scanner(System.in)){
                while (scanner.hasNextLine()) {
                    try {
                        var line = scanner.nextLine();
                        var sendBuffer = cs.encode(line);
                        dc.send(sendBuffer, server);
                        var msg = queue.poll(1, TimeUnit.SECONDS);
                        while(msg == null){
                            LOGGER.warning("Le serveur n'a pas répondu");
                            sendBuffer.clear();
                            dc.send(sendBuffer, server);
                            msg = queue.poll(1, TimeUnit.SECONDS);
                        }
                        System.out.println(msg);
                    } catch (IOException | InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Severe Exception", e);
                    }
                }
            }
            threadListener.interrupt();
        }
    }
}
