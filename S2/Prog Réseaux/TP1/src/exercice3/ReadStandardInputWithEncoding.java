package exercice3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

public class ReadStandardInputWithEncoding {

	private static final int BUFFER_SIZE = 1024;

	private static void usage() {
		System.out.println("Usage: ReadStandardInputWithEncoding charset");
	}

	private static String stringFromStandardInput(Charset cs) throws IOException {
		var mainBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		try(var scanner = Channels.newChannel(System.in)){
			while(scanner.read(mainBuffer) != -1){
				if(!mainBuffer.hasRemaining()){
					var tmp = ByteBuffer.allocate(mainBuffer.capacity()*2);
					mainBuffer.flip();
					tmp.put(mainBuffer);
					mainBuffer = tmp;
				}
			}
			mainBuffer.flip();
			return cs.decode(mainBuffer).toString();
		}
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			usage();
			return;
		}
		Charset cs = Charset.forName(args[0]);
		System.out.print(stringFromStandardInput(cs));
	}
}