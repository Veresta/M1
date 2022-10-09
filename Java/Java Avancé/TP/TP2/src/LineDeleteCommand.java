package fr.uge.sed;

import fr.uge.sed.StreamEditor.Action;

public record LineDeleteCommand(int nb) {

	public LineDeleteCommand {
		if (nb < 0)
			throw new IllegalArgumentException("Negative line");
	}

	public Action deleteOrPrint(int line) {
		return ((line == this.nb) ? Action.DELETE : Action.PRINT);
	}
}