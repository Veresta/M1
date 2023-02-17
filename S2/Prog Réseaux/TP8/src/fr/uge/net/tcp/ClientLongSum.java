package fr.uge.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Logger;

public class ClientLongSum {
    public static final Logger logger = Logger.getLogger(ClientLongSum.class.getName());

    private static List<Long> randomLongList(int size) {
        return new Random().longs(size).boxed().toList();
    }

    private static boolean checkSum(List<Long> list, long response) {
        return list.stream().reduce(Long::sum).orElse(0L) == response;
    }

    /**
     * Write all the longs in list in BigEndian on the server and read the long sent
     * by the server and returns it
     *
     * returns null if the protocol is not followed by the server but no
     * IOException is thrown
     *
     * @param sc
     * @param list
     * @return
     * @throws IOException
     */
    private static Long requestSumForList(SocketChannel sc, List<Long> list) throws IOException {
        var nbOperande = list.size();
        var sendBuff = ByteBuffer.allocate(Integer.BYTES + Long.BYTES * nbOperande);
        sendBuff.putInt(nbOperande);
        list.forEach(sendBuff::putLong);
        sendBuff.flip();
        sc.write(sendBuff);
        logger.info("Sending message to the server");
        var receiveBuff = ByteBuffer.allocate(Long.BYTES);
        if(!readFully(sc, receiveBuff)){
            return null;
        }
        receiveBuff.flip();
        return receiveBuff.getLong();
    }

    public static void main(String[] args) throws IOException {
        var server = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        try (var sc = SocketChannel.open(server)) {
            for (var i = 0; i < 5; i++) {
                var list = randomLongList(50);

                var sum = requestSumForList(sc, list);
                if (sum == null) {
                    logger.warning("Connection with server lost.");
                    return;
                }
                if (!checkSum(list, sum)) {
                    logger.warning("Oups! Something wrong happened!");
                }
            }
            logger.info("Everything seems ok");
        }
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
            var read =sc.read(buffer);
            if(read == 0) return true;
            if(read == -1) return false;
        }
    }
}