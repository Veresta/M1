package fr.uge.ymca;

public sealed interface Resident permits Minion, VillagePeople{
	String name();
	//double tarif();
}
