package fr.uge.net.udp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ClientIdUpperCaseUDPBurst {

        private static Logger logger = Logger.getLogger(ClientIdUpperCaseUDPBurst.class.getName());
        private static final Charset UTF8 = StandardCharsets.UTF_8;
        private static final int BUFFER_SIZE = 1024;
        private final List<String> lines;
        private final int nbLines;
        private final String[] upperCaseLines; //
        private final int timeout;
        private final String outFilename;
        private final InetSocketAddress serverAddress;
        private final DatagramChannel dc;
        private final AnswersLog answersLog;         // Thread-safe structure keeping track of missing responses

        public static void usage() {
            System.out.println("Usage : ClientIdUpperCaseUDPBurst in-filename out-filename timeout host port ");
        }

        public ClientIdUpperCaseUDPBurst(List<String> lines,int timeout,InetSocketAddress serverAddress,String outFilename) throws IOException {
            this.lines = lines;
            this.nbLines = lines.size();
            this.timeout = timeout;
            this.outFilename = outFilename;
            this.serverAddress = serverAddress;
            this.dc = DatagramChannel.open();
            dc.bind(null);
            this.upperCaseLines = new String[nbLines];
            this.answersLog = new AnswersLog(nbLines);
        }

        private void senderThreadRun() {
            var buffer = IntStream.range(0, nbLines).mapToObj(index -> {
                var buf = ByteBuffer.allocate(BUFFER_SIZE);
                buf.putLong(index);
                buf.put(UTF8.encode(lines.get(index)));
                return buf;
            }).toArray(ByteBuffer[]::new);
            while(true){
                try{
                    for(var ind : answersLog.getArray()){
                        dc.send(buffer[ind].flip(), serverAddress);
                        logger.info("Send packet " +ind);
                    }
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    logger.info("Interrupted " + e);
                    return;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Receive exception", e);
                }
            }
        }

        public void launch() throws IOException {
            Thread senderThread = Thread.ofPlatform().start(this::senderThreadRun);
            var buffer = ByteBuffer.allocate(BUFFER_SIZE);
            try {
                while (!answersLog.isCompleted()) {
                    buffer.clear();
                    dc.receive(buffer);
                    buffer.flip();
                    if (buffer.remaining() < Long.BYTES) {
                        continue;
                    }
                    var id = buffer.getLong();
                    var msg = UTF8.decode(buffer).toString();
                    logger.info("Receive id:" + id + " msg: " + msg);
                    answersLog.setCompleted((int) id);
                    upperCaseLines[(int) id] = msg;
                }
            } catch (ClosedByInterruptException e) {
                logger.info("interrupted while waiting for message" + e);
            } catch (AsynchronousCloseException e) {
                logger.info("channel closed by the main while reading " + e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Receive exception", e);
            } finally {
                dc.close();
            }
            senderThread.interrupt();
            Files.write(Paths.get(outFilename),Arrays.asList(upperCaseLines), UTF8,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);

        }

        public static void main(String[] args) throws IOException, InterruptedException {
            if (args.length !=5) {
                usage();
                return;
            }

            String inFilename = args[0];
            String outFilename = args[1];
            int timeout = Integer.valueOf(args[2]);
            String host=args[3];
            int port = Integer.valueOf(args[4]);
            InetSocketAddress serverAddress = new InetSocketAddress(host,port);

            //Read all lines of inFilename opened in UTF-8
            List<String> lines= Files.readAllLines(Paths.get(inFilename),UTF8);
            //Create client with the parameters and launch it

            ClientIdUpperCaseUDPBurst client = new ClientIdUpperCaseUDPBurst(lines,timeout,serverAddress,outFilename);
            client.launch();
        }

        private static class AnswersLog {
            private final BitSet bitSet;
            private static final Object lock = new Object();

            //private final int size;
            public AnswersLog(int size){
                bitSet = new BitSet(size);
                //this.size = size;
                bitSet.flip(0, size); //Met les bits à 1
            }

            public void setCompleted(int index){
                synchronized (lock){
                    bitSet.set(index, false);
                }
            }

            public int[] getArray(){
                synchronized (lock){
                    return bitSet.stream().toArray();
                }
            }

            public boolean isCompleted(){
                synchronized (lock){
                    return bitSet.cardinality() == 0;
                }
            }

        }
    }

