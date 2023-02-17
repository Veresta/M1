package fr.uge.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class ClientEOS {

    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public static final int BUFFER_SIZE = 1024;
    public static final Logger logger = Logger.getLogger(ClientEOS.class.getName());

    /**
     * This method: 
     * - connect to server 
     * - writes the bytes corresponding to request in UTF8 
     * - closes the write-channel to the server 
     * - stores the bufferSize first bytes of server response 
     * - return the corresponding string in UTF8
     *
     * @param request
     * @param server
     * @param bufferSize
     * @return the UTF8 string corresponding to bufferSize first bytes of server
     *         response
     * @throws IOException
     */

    public static String getFixedSizeResponse(String request, SocketAddress server, int bufferSize) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(server);
        var send = ByteBuffer.allocate(bufferSize);
        var receive = ByteBuffer.allocate(bufferSize);
        send.put(UTF8_CHARSET.encode(request));
        send.flip();
        sc.write(send);
        sc.shutdownOutput();
        /*int read;
        do{
            read = sc.read(receive);
            logger.info("Read " + read + " bytes");
            if(read == -1) break;
        }while(read != 0);*/
        readFully(sc, receive);
        sc.close();
        receive.flip();
        return UTF8_CHARSET.decode(receive).toString();
    }

    /**
     * This method: 
     * - connect to server 
     * - writes the bytes corresponding to request in UTF8 
     * - closes the write-channel to the server 
     * - reads and stores all bytes from server until read-channel is closed 
     * - return the corresponding string in UTF8
     *
     * @param request
     * @param server
     * @return the UTF8 string corresponding the full response of the server
     * @throws IOException
     */

    public static String getUnboundedResponse(String request, SocketAddress server) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(server);
        var send = ByteBuffer.allocate(BUFFER_SIZE);
        var receive = ByteBuffer.allocate(BUFFER_SIZE);
        send.put(UTF8_CHARSET.encode(request));
        send.flip();
        sc.write(send);
        sc.shutdownOutput();
        /*int read;
        do{
            var tmpBuf = ByteBuffer.allocate(BUFFER_SIZE);
            read = sc.read(tmpBuf);
            logger.info("Read " + read + " bytes");
            tmpBuf.flip();
            if(tmpBuf.remaining() > receive.remaining()){
                var tmp = ByteBuffer.allocate(receive.capacity()*2);
                receive.flip();
                tmp.put(receive);
                receive = tmp;
            }
            receive.put(tmpBuf);

        }while(read != -1);*/

        while(readFully(sc, receive)){
            if(!receive.hasRemaining()){
                var tmp = ByteBuffer.allocate(receive.flip().capacity()*2);
                tmp.put(receive);
                receive = tmp;
            }
        }

        sc.close();
        return UTF8_CHARSET.decode(receive.flip()).toString();
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

    public static void main(String[] args) throws IOException {
        var google = new InetSocketAddress("www.google.fr", 80);
        System.out.println(getFixedSizeResponse("GET / HTTP/1.1\r\nHost: www.google.fr\r\n\r\n", google, 512));
        //System.out.println(getUnboundedResponse("GET / HTTP/1.1\r\nHost: www.google.fr\r\n\r\n", google));
    }
}