package fr.uge.sed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StreamEditor {

	public sealed interface Action {
		record DeleteAction() implements Action {
		}

		record PrintAction(String text) implements Action {
		}
	}

	@FunctionalInterface
	public interface Command {
		public Action apply(int i, String s);

		default Command andThen(Command c) {
			Objects.requireNonNull(c);
			return (i, s) -> {
				var res = this.apply(i, s);
				return switch (res) {
				case Action.PrintAction x -> c.apply(i, x.text);
				case Action.DeleteAction y -> y;
				};
			};
		}
	}

	private final Command command;

	public StreamEditor(Command a) {
		Objects.requireNonNull(a);
		command = a;
	}

	public StreamEditor() {
		command = null;
	}

	public void transform(LineNumberReader a, Writer w) throws IOException {
		Objects.requireNonNull(a);
		Objects.requireNonNull(w);
		String current_l = null;
		while ((current_l = a.readLine()) != null) {
			if (command != null) {
				switch (command.apply(a.getLineNumber(), current_l)) {
				case Action.PrintAction x -> w.append(x.text + '\n');
				case Action.DeleteAction y -> w.append("");
				}
			} else
				w.append(current_l + '\n');
		}
	}

	public static Command findAndDelete(Pattern p) {
		Objects.requireNonNull(p);
		return (i, s) -> {
			Matcher matcher = p.matcher(s);
			return (matcher.find()) ? new Action.DeleteAction() : new Action.PrintAction(s);
		};
	}

	public static Command lineDelete(int nb) {
		if (nb < 0)
			throw new IllegalArgumentException("Negative line");
		return (i, s) -> {
			return ((i == nb) ? new Action.DeleteAction() : new Action.PrintAction(s));
		};
	}

	public static Command substitute(Pattern p, String elm) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(elm);
		return (i, s) -> {
			Matcher matcher = p.matcher(s);
			if (matcher.find()) {
				var tmp = matcher.replaceAll(elm);
				return new Action.PrintAction(tmp);
			}
			return new Action.PrintAction(s);
		};
	}

	public static void main(String[] args) {
		Objects.requireNonNull(args);
		File file = new File(args[0]);
		try (Reader reader = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(reader);
				var ps = new PrintStream(System.out);
				var osw = new OutputStreamWriter(ps, StandardCharsets.UTF_8);) {
			var command = StreamEditor.lineDelete(2);
			var editor = new StreamEditor(command);
			editor.transform(lnr, osw);
		} catch (FileNotFoundException e) {
			throw new AssertionError();
		} catch (IOException e) {
			throw new AssertionError();
		}
	}

	/*
	 * public enum Action { DELETE, PRINT }
	 */
}
