package fr.uge.net.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Logger;

public class ServerLongSum {

    public record packet(int posOp, int totalOp, int value){};

    private final HashMap<InetSocketAddress, HashMap<Integer, packet>> data = new HashMap<>();

    private static final Logger logger = Logger.getLogger(ServerLongSum.class.getName());

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final int BUFFER_SIZE = 1024;
    private final DatagramChannel dc;

    private final ByteBuffer receiveBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private final ByteBuffer sendBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public ServerLongSum(int port) throws IOException {
        this.dc = DatagramChannel.open();
        dc.bind(new InetSocketAddress(port));
        logger.info("ServerLongSum started on port " + port);
    }

    public static void usage() {
        System.out.println("Usage : ServerLongSum port");
    }

    public void serve() throws IOException {
        try {
            while(!Thread.interrupted()){
                var sender = dc.receive(receiveBuffer);
                if(receiveBuffer.remaining() < Byte.BYTES){
                    logger.warning("Receive packet almost empty");
                    continue;
                }
                var byteValue = receiveBuffer.get();
                switch (byteValue){
                    case 0 ->
                        
                        break;
                    case 1 ->
                        break;
                    case 2 ->
                        break;
                    default -> logger.severe("Packet type unknown");
                }

            }
        } finally {
            dc.close();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            usage();
            return;
        }

        var port = Integer.parseInt(args[0]);

        if (!(port >= 1024) & port <= 65535) {
            logger.severe("The port number must be between 1024 and 65535");
            return;
        }

        try {
            new ServerLongSum(port).serve();
        } catch (BindException e) {
            logger.severe("Server could not bind on " + port + "\nAnother server is probably running on this port.");
            return;
        }
    }
}
