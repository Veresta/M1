package fr.uge.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.*;

public class ClientIdUpperCaseUDPOneByOne {

	private static Logger logger = Logger.getLogger(ClientIdUpperCaseUDPOneByOne.class.getName());
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	private static final int BUFFER_SIZE = 1024;

	private record Response(long id, String message) {
		public String toString(){
			return "id : id | " + message;
		}
	};

	private final String inFilename;
	private final String outFilename;
	private final long timeout;
	private final InetSocketAddress server;
	private final DatagramChannel dc;
	private final SynchronousQueue<Response> queue = new SynchronousQueue<>();
	private long currentAnswer = 0;

	public static void usage() {
		System.out.println("Usage : ClientIdUpperCaseUDPOneByOne in-filename out-filename timeout host port ");
	}

	public ClientIdUpperCaseUDPOneByOne(String inFilename, String outFilename, long timeout, InetSocketAddress server)
			throws IOException {
		this.inFilename = Objects.requireNonNull(inFilename);
		this.outFilename = Objects.requireNonNull(outFilename);
		this.timeout = timeout;
		this.server = server;
		this.dc = DatagramChannel.open();
		dc.bind(null);
	}

	private void listenerThreadRun() {
		var buffer = ByteBuffer.allocate(BUFFER_SIZE);
		while(!Thread.interrupted()) {
			try {
				buffer.clear();
				dc.receive(buffer);
				buffer.flip();
				if(buffer.remaining() < Long.BYTES){
					logger.info("Long not inside the buffer");
					continue;
				}
				var id = buffer.getLong();
				var msg = UTF8.decode(buffer);
				queue.put(new Response(id, msg.toString()));

			} catch (InterruptedException e){
				logger.info("listener interrupted while adding the message to the queue " + e);
			} catch (ClosedByInterruptException e) {
				logger.info("interrupted while waiting for message" + e);
			} catch (AsynchronousCloseException e) {
				logger.info("channel closed by the main while reading " + e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Receive exception", e);
			}
		}
	}

	public void launch() throws IOException, InterruptedException {
		try {

			var listenerThread = Thread.ofPlatform().start(this::listenerThreadRun);
			
			// Read all lines of inFilename opened in UTF-8
			var lines = Files.readAllLines(Path.of(inFilename), UTF8);

			var upperCaseLines = new ArrayList<String>();

			var buffer = ByteBuffer.allocate(BUFFER_SIZE);
			Response response;
			for(var line : lines){
				System.out.println(line);
				try {
					buffer.putLong(currentAnswer);
					buffer.put(UTF8.encode(line));
					buffer.flip();
					do{
						dc.send(buffer, server);
						logger.info("Envoie");
						buffer.clear();
						var mesure = this.timeout;
						do{
							var begTime = System.currentTimeMillis();
							response = queue.poll(mesure, TimeUnit.MILLISECONDS);
							mesure -= System.currentTimeMillis() - begTime;
							logger.info("Time mesured :" + mesure);
						} while(response != null && response.id() != currentAnswer && mesure > 0);
					}while (response == null || response.id() != currentAnswer);
					logger.info("Reçu : " + response);
					upperCaseLines.add(response.message);
				} catch (IOException | InterruptedException e) {
					logger.log(Level.SEVERE, "Severe Exception", e);
				}
				currentAnswer++;
			}
			listenerThread.interrupt();
			Files.write(Paths.get(outFilename), upperCaseLines, UTF8, CREATE, WRITE, TRUNCATE_EXISTING);
		} finally {
			dc.close();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length != 5) {
			usage();
			return;
		}

		var inFilename = args[0];
		var outFilename = args[1];
		var timeout = Long.parseLong(args[2]);
		var server = new InetSocketAddress(args[3], Integer.parseInt(args[4]));

		// Create client with the parameters and launch it
		new ClientIdUpperCaseUDPOneByOne(inFilename, outFilename, timeout, server).launch();
	}
}