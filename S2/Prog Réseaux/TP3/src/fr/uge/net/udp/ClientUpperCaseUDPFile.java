package fr.uge.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.*;

public class ClientUpperCaseUDPFile {
    private final static Charset UTF8 = StandardCharsets.UTF_8;
    private final static int BUFFER_SIZE = 1024;
    private static final Logger LOGGER = Logger.getLogger(ClientUpperCaseUDPFile.class.getName());

    private static void usage() {
        System.out.println("Usage : ClientUpperCaseUDPFile in-filename out-filename timeout host port ");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 5) {
            usage();
            return;
        }

        var inFilename = args[0];
        var outFilename = args[1];
        var timeout = Integer.parseInt(args[2]);
        var server = new InetSocketAddress(args[3], Integer.parseInt(args[4]));
        var cs = StandardCharsets.UTF_8;

        // Read all lines of inFilename opened in UTF-8
        var lines = Files.readAllLines(Path.of(inFilename), UTF8);
        var upperCaseLines = new ArrayList<String>();
        var queue = new ArrayBlockingQueue<String>(10);

        try (DatagramChannel dc = DatagramChannel.open();) {
            dc.bind(null);
            var threadListener = Thread.ofPlatform().start(() -> {
                var buffer = ByteBuffer.allocate(BUFFER_SIZE);
                while(!Thread.interrupted()) {
                    try {
                        dc.receive(buffer);
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

            for(var line : lines){
                try {
                    dc.send(cs.encode(line), server);
                    var msg = queue.poll(1, TimeUnit.SECONDS);
                    while(msg == null){
                        LOGGER.warning("Le serveur n'a pas répondu");
                        dc.send(cs.encode(line), server);
                        msg = queue.poll(timeout, TimeUnit.SECONDS);
                    }
                    LOGGER.info("Reçu : " + msg);
                    upperCaseLines.add(msg);
                } catch (IOException | InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Severe Exception", e);
                }
            }
            threadListener.interrupt();
        }

        // Write upperCaseLines to outFilename in UTF-8
        Files.write(Path.of(outFilename), upperCaseLines, UTF8, CREATE, WRITE, TRUNCATE_EXISTING);
    }
}