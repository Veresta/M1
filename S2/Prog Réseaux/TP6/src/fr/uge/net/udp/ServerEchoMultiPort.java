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

public class ServerEchoMultiPort {
    private static final Logger LOGGER = Logger.getLogger(ServerEchoMultiPort.class.getName());
    private final Selector selector;
    private final int BUFFER_SIZE = 1024;
    private SocketAddress sender;
    private int start_port;
    private int end_port;

    private class Context {
        private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        private InetSocketAddress address;
    }

    public ServerEchoMultiPort(int start_port, int end_port) throws IOException {
        this.start_port = start_port;
        this.end_port = end_port;
        selector = Selector.open();
        for(var i = start_port; i < end_port; i++){
            var dc = DatagramChannel.open();
            dc.bind(new InetSocketAddress(i));
            dc.configureBlocking(false);
            dc.register(selector, SelectionKey.OP_READ, new Context());
        }
    }

    public void serve() throws IOException {
        LOGGER.info("ServerEcho started on port " + start_port + " to " + end_port);
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
        var keyContext = (Context) key.attachment();
        var buff = keyContext.buffer;
        buff.clear();
        DatagramChannel dc = (DatagramChannel) key.channel();
        keyContext.address = (InetSocketAddress) dc.receive(buff);
        if(keyContext == null){
            LOGGER.warning("Reception address null");
            return;
        }
        buff.flip();
        LOGGER.info("Receive from " + keyContext.address);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void doWrite(SelectionKey key) throws IOException {
        var keyContext = (Context) key.attachment();
        var buff = keyContext.buffer;
        DatagramChannel dc = (DatagramChannel) key.channel();
        dc.send(buff, keyContext.address);
        if(buff.hasRemaining()){
            return;
        }
        LOGGER.info("Packet send to : " + keyContext.address);
        key.interestOps(SelectionKey.OP_READ);
    }

    public static void usage() {
        System.out.println("Usage : ServerEchoMultiPort start_port end_port");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            usage();
            return;
        }
        new ServerEchoMultiPort(Integer.parseInt(args[0]), Integer.parseInt(args[1])).serve();
    }



}
