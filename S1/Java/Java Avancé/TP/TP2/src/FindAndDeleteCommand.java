package fr.uge.sed;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record FindAndDeleteCommand(Pattern p) {

	public FindAndDeleteCommand {
		Objects.requireNonNull(p);
	}

	public boolean ismatching(String stringToBeMatched) {
		Matcher matcher = p.matcher(stringToBeMatched.replaceAll("\\s", ""));
		return matcher.find();
	}

}