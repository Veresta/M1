package fr.uge.net.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoundedOnDemandConcurrentLongSumServer {
    private static final Logger logger = Logger.getLogger(BoundedOnDemandConcurrentLongSumServer.class.getName());
    private final ServerSocketChannel serverSocketChannel;
    private final Semaphore semaphore;

    public BoundedOnDemandConcurrentLongSumServer(int port, int maxClient) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        semaphore = new Semaphore(maxClient);
        logger.info(this.getClass().getName() + " starts on port " + port + " with at most " + maxClient + " threads.");
    }

    public void launch() throws IOException, InterruptedException {
        logger.info("Server started");
        while (!Thread.interrupted()) {
            semaphore.acquire();
            SocketChannel client = serverSocketChannel.accept();
            threadServe(client);

        }
    }

    private void threadServe(SocketChannel client){
        logger.info("Thread is starting");
        Thread.ofPlatform().start(() -> {
            try {
                logger.info("Client : " + client.getRemoteAddress());
                serve(client);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Connection terminated with client by IOException", e.getCause());
            }finally {
                semaphore.release();
                silentlyClose(client);
            }
        });
    }

    /**
     * Treat the connection sc applying the protocol. All IOException are thrown
     *
     * @param sc
     * @throws IOException
     */
    private void serve(SocketChannel sc) throws IOException {
        var receiveBuffer = ByteBuffer.allocate(Integer.BYTES);
        while(true){
            if(!readFully(sc, receiveBuffer)){
                logger.info("Connexion closed");
                return;
            };
            receiveBuffer.flip();
            receiveBuffer.order(ByteOrder.BIG_ENDIAN);
            if(Integer.BYTES > receiveBuffer.remaining()){
                logger.info("Packet empty");
                return;
            }
            var nbOperand = receiveBuffer.getInt();
            var tmpBuff = ByteBuffer.allocate(Long.BYTES * nbOperand);
            if(!readFully(sc, tmpBuff)){
                logger.info("Connexion closed");
                return;
            };
            tmpBuff.flip();
            var sum = 0L;
            for(var i = 0; i < nbOperand;i++){
                sum+=tmpBuff.getLong();
            }
            var sendingBuffer = ByteBuffer.allocate(Long.BYTES);
            sendingBuffer.putLong(sum);
            sendingBuffer.flip();
            sc.write(sendingBuffer);
            sendingBuffer.clear();
            receiveBuffer.clear();
        }
    }

    /**
     * Close a SocketChannel while ignoring IOExecption
     *
     * @param sc
     */

    private void silentlyClose(Closeable sc) {
        if (sc != null) {
            try {
                sc.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    static boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            if (sc.read(buffer) == -1) {
                logger.info("Input stream closed");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = new BoundedOnDemandConcurrentLongSumServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        server.launch();
    }

    public static class Semaphore {
        private final int nbPermits;
        private int currentPermits;
        private final Object lock = new Object();

        public Semaphore(int nbPermits){
            if(nbPermits <= 0) throw new IllegalArgumentException("Negative number of permits");
            this.nbPermits = nbPermits;
            this.currentPermits = nbPermits;
        }

        public void acquire() throws InterruptedException {
            synchronized (lock){
                if(currentPermits > 0){
                    currentPermits--;
                }

                while(currentPermits == 0){
                    lock.wait();
                }
            }
        }

        public void release(){
            synchronized (lock){
                if(currentPermits > nbPermits){
                    return;
                }
                currentPermits++;
                lock.notifyAll();
            }
        }
    }
}
