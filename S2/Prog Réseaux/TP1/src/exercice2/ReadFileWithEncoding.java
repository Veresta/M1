package exercice2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ReadFileWithEncoding {

	private static void usage() {
		System.out.println("Usage: ReadFileWithEncoding charset filename");
	}

	private static String stringFromFile(Charset cs, Path path) throws IOException {
		var res = "";
		try (FileChannel fc = FileChannel.open(path, StandardOpenOption.READ)) {
			var buffer = ByteBuffer.allocate((int)fc.size());
			while(buffer.hasRemaining() && fc.read(buffer) != -1){}
			buffer.flip();
			res = cs.decode(buffer).toString();
		} catch (IOException e){
			System.err.println("File error manipulation : " + e);
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			usage();
			return;
		}
		var cs = Charset.forName(args[0]);
		var path = Path.of(args[1]);
		System.out.print(stringFromFile(cs, path));
	}
}