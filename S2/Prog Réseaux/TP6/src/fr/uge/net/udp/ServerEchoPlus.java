package fr.uge.net.udp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.logging.Logger;

public class ServerEchoPlus {
    private static final Logger LOGGER = Logger.getLogger(ServerEchoPlus.class.getName());
    private final DatagramChannel dc;
    private final Selector selector;
    private final int BUFFER_SIZE = 1024;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private final ByteBuffer tmpBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private SocketAddress sender;
    private int port;

    public ServerEchoPlus(int port) throws IOException {
        this.port = port;
        selector = Selector.open();
        dc = DatagramChannel.open();
        dc.bind(new InetSocketAddress(port));
        dc.configureBlocking(false);
        dc.register(selector, SelectionKey.OP_READ);
    }

    public void serve() throws IOException {
        LOGGER.info("ServerEcho started on port " + port);
        while (!Thread.interrupted()) {
            try{
                selector.select(this::treatKey);
            } catch (UncheckedIOException io){
                throw io.getCause();
            }
        }
    }

    private void treatKey(SelectionKey key) {
        try {
            if (key.isValid() && key.isWritable()) {
                doWrite(key);
            }
            if (key.isValid() && key.isReadable()) {
                doRead(key);
            }
        } catch (IOException e) {
            LOGGER.severe("IOException");
            throw new UncheckedIOException(e);
        }
    }

    private void doRead(SelectionKey key) throws IOException {
        buffer.clear();
        sender = dc.receive(buffer);
        buffer.flip();
        if(sender == null){
            LOGGER.warning("Reception address null");
            return;
        }
        LOGGER.info("Packet receive from : " + sender);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void doWrite(SelectionKey key) throws IOException {
        tmpBuf.clear();
        while(buffer.hasRemaining()){
            tmpBuf.put((byte)(buffer.get()+ 1));
        }
        tmpBuf.flip();
        dc.send(tmpBuf, sender);
        if(tmpBuf.hasRemaining()){
            return;
        }
        LOGGER.info("Packer send to : " + sender);
        key.interestOps(SelectionKey.OP_READ);
    }

    public static void usage() {
        System.out.println("Usage : ServerEchoPlus port");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            usage();
            return;
        }
        new ServerEchoPlus(Integer.parseInt(args[0])).serve();
    }
}