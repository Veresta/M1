package fr.uge.ymca;

import java.util.Objects;

public record Minion(String name) implements Resident {
	
	public Minion {
		Objects.requireNonNull(name);
	}
	
	public String toString() {
		return name + " (MINION)";
	}

}
