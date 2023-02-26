package fr.uge.net.tcp.nonblocking;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.logging.Logger;

public class ServerSum {
    private static final int BUFFER_SIZE = 2 * Integer.BYTES;
    private static final Logger logger = Logger.getLogger(ServerSumOneShot.class.getName());
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public ServerSum(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        selector = Selector.open();
    }

    public void launch() throws IOException {
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (!Thread.interrupted()) {
            Helpers.printKeys(selector); // for debug
            System.out.println("Starting select");
            selector.select(this::treatKey);
            System.out.println("Select finished");
        }
    }

    private void treatKey(SelectionKey key) {
        Helpers.printSelectedKey(key); // for debug
        try{
            if (key.isValid() && key.isAcceptable()) {
                doAccept(key);
            }
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
        try{
            if (key.isValid() && key.isWritable()) {
                doWrite(key);
            }
            if (key.isValid() && key.isReadable()) {
                doRead(key);
            }
        }catch(IOException e){
            silentlyClose(key);
        }
    }

    private void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        if (sc == null) {
            return; // the selector gave a bad hint
        }
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(BUFFER_SIZE));
    }

    private void doRead(SelectionKey key) throws IOException {
        var buffer = (ByteBuffer) key.attachment();
        SocketChannel ssc = (SocketChannel) key.channel();
        if(ssc.read(buffer) == -1){
            logger.warning("Connexion closed");
            silentlyClose(key);
            return;
        }
        buffer.flip();
        if(buffer.remaining() != 2 * Integer.BYTES){
            //Mauvaise taille de buffer
            buffer.compact();
            return;
        }
        logger.info("Receive packet");
        var sum = buffer.getInt() + buffer.getInt();
        buffer.clear();
        buffer.putInt(sum);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void doWrite(SelectionKey key) throws IOException {
        SocketChannel ssc = (SocketChannel) key.channel();
        var buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        ssc.write(buffer);
        /*if(buffer.hasRemaining()){
            buffer.compact();
            return;
        }*/
        while (buffer.hasRemaining()){
            ssc.write(buffer);
        }
        logger.info("Packet send");
        buffer.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    private void silentlyClose(SelectionKey key) {
        var sc = (Channel) key.channel();
        try {
            sc.close();
        } catch (IOException e) {
            // ignore exception
        }
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        if (args.length != 1) {
            usage();
            return;
        }
        new ServerSum(Integer.parseInt(args[0])).launch();
    }

    private static void usage() {
        System.out.println("Usage : ServerSum port");
    }
}
