package fr.uge.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientUpperCaseUDPTimeout {
    private static final int BUFFER_SIZE = 1024;

    private final ArrayBlockingQueue<String> array = new ArrayBlockingQueue<>(10);
    private static void usage() {
        System.out.println("Usage : NetcatUDP host port charset");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            usage();
            return;
        }

        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        var cs = Charset.forName(args[2]);
        var buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try (var scanner = new Scanner(System.in);
             DatagramChannel dc = DatagramChannel.open();) {
            dc.bind(null);
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var sendBuffer = cs.encode(line); // Encodage de la chaine en byteBuffer
                dc.send(sendBuffer, server); // Envoie du packet au server

                var sender = (InetSocketAddress) dc.receive(buffer); //Méthode bloquante attendant la récupération du packet
                buffer.flip(); // Passage en mode lecture
                var msg = cs.decode(buffer); // Décodage pour obtenir le message
                System.out.println("Received " + buffer.remaining() + " bytes from " + sender + " msg -> " + msg);
                buffer.clear();
            }
        }
    }
}
