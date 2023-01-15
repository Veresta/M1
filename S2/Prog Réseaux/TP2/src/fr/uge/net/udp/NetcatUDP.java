package fr.uge.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NetcatUDP {
    public static final int BUFFER_SIZE = 1024;

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
                //Dans le buffer "buffer"
                buffer.flip(); // Passage en mode lecture
                var msg = cs.decode(buffer); // Décodage pour obtenir le message
                System.out.println("Received " + buffer.remaining() + " bytes from " + sender + " msg -> " + msg);
                buffer.clear();
            }
        }

        //$ java -jar ServerUpperCaseUDP.jar 4545 Latin1
        //$ java fr.uge.net.udp.NetcatUDP 4545 UTF-8

        //coté client, on envoie un msg formaté en UTF-8
        //lorsque le serveur le reçoit, il ne va pas interpreter les séquences debits de la meme manière qu'en UTF-8
        //d'où le message reçu différent.
    }
}