package fr.uge.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientConcatenation {

    public static final Logger logger = Logger.getLogger(ClientLongSum.class.getName());

    public static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;
    public static final int BUFF_SIZE = 1024;

    private static String concatenationRequest(InetSocketAddress server) throws IOException {
        var sendBuffer = ByteBuffer.allocate(BUFF_SIZE);
        var receiveBuffer = ByteBuffer.allocate(BUFF_SIZE);
        try(SocketChannel sc = SocketChannel.open(server);
            Scanner scanner = new Scanner(System.in)){
            var chaine = new ArrayList<String>();
            String tmp;
            do{
                tmp = scanner.nextLine();
                System.out.println(tmp);
                if(tmp.isEmpty()) break;
                chaine.add(tmp);
            }while(true);

            var nbChaine = chaine.size();
            sendBuffer.putInt(nbChaine);
            sendBuffer.flip();
            sc.write(sendBuffer);
            sendBuffer.clear();
            var listBuf = chaine.stream().map(ch -> {
                var buf = ByteBuffer.allocate(BUFF_SIZE);
                var encoded = UTF8_CHARSET.encode(ch);
                buf.putInt(encoded.capacity());
                buf.put(encoded);
                return buf;
            }).toArray(ByteBuffer[]::new);

            for(var i = 0; i < listBuf.length ; i++){
                sc.write(listBuf[i].flip());
                logger.info("Sending "+ i +" message to the server");
            }

            sc.shutdownOutput();
            if(!readFully(sc, receiveBuffer)){
                logger.warning("Connection with server lost.");
                return null;
            };
            receiveBuffer.flip();
            logger.info("received "+ receiveBuffer.getInt() +" octets message from the server");
            return UTF8_CHARSET.decode(receiveBuffer).toString();
        }
    }

    public static void main(String[] args) throws IOException {
        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        System.out.println(concatenationRequest(server));
    }

    /**
     * Fill the workspace of the Bytebuffer with bytes read from sc.
     *
     * @param sc
     * @param buffer
     * @return false if read returned -1 at some point and true otherwise
     * @throws IOException
     */
    static boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        while(true){
            var read = sc.read(buffer);
            if(read == 0) return true;
            if(read == -1) return false;
        }
    }
}
