package fr.uge.ymca;

import java.util.Objects;

public record VillagePeople(String name, Kind kind) implements Resident {
	
	public VillagePeople{
		Objects.requireNonNull(name);
		Objects.requireNonNull(kind);
	}

	@Override
	public String toString() {
		return name + " (" + kind + ")";
	}
	
}
