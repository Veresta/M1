# Compte rendu TP1 Java Avancé

### Mathis MENAA
------


## Exercice 2 - YMCA

#### Q1) Écrire le code de VillagePeople tel que l'on puisse créer des VillagePeople avec leur nom et leur sorte.

Création d'un record VillagePeople avec un champ String name et un champ Kind kind.
Dans le constructeur on vérifie avec un Objects.requireNonNull les champs du record.
On reféfinit la méthode toString selon la norme du sujet. 

```java
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
```


#### Q2) On veut maintenant introduire une maison House qui va contenir des VillagePeople. Une maison possède une méthode add qui permet d'ajouter un VillagePeople dans la maison (note : il est possible d'ajouter plusieurs fois le même). L'affichage d'une maison doit renvoyer le texte "House with" suivi des noms des VillagePeople ajoutés à la maison, séparés par une virgule. Dans le cas où une maison est vide, le texte est "Empty House".

Définition d'une classe House, comprenant un champ house qui est une list de VillagePeople.
Ajout de la méthode add pour ajouter un VillagePeople à notre house.
Rédéfinition de la méthode toString pour avoir un affichage selon le sujet.

```java
import java.util.Objects;

public final class House {
	private final List<Resident> house = new ArrayList<>();
	/**
	 * Add a resident to the house.
	 * @param r
	 */
	public void add(Resident r) {
		Objects.requireNonNull(r);
		house.add(r);
	}
	
	@Override
	public String toString() {
		return (house.isEmpty() ? "Empty House" : "House with " + house.stream().sorted(Comparator.comparing(Resident::name)).map(Resident::name).collect(Collectors.joining(", ")));
	}
}
```

#### Q3) En fait on veut que l'affichage affiche les noms des VillagePeople dans l'ordre alphabétique, il va donc falloir trier les noms avant de les afficher.

Utilisation d'un stream pour l'affichage.

```java
@Override
	public String toString() {
		return (house.isEmpty() ? "Empty House" : "House with " + house.stream().sorted(Comparator.comparing(Resident::name)).map(Resident::name).collect(Collectors.joining(", ")));
	}
```

#### Q4) En fait, avoir une maison qui ne peut accepter que des VillagePeople n'est pas une bonne décision en termes de business, ils ne sont pas assez nombreux. YMCA décide donc qu'en plus des VillagePeople ses maisons permettent maintenant d'accueillir aussi des Minions, une autre population sans logement.

Définition d'un record Minion composé d'un champ String name.
On rédéfinis la méthode toString pour qu'elle corresponde au sujet.
Pour pouvoir ajouter des Minions et des VillagePeople, on définit une Interface Résident qui va représenter les habitants d'une house.
Dans notre classe House, la list va de VillagePeople devient une list de Resident.

```java
public sealed interface Resident permits Minion, VillagePeople{
	String name();
	//double tarif();
}

import java.util.Objects;

public record Minion(String name) implements Resident {
	
	public Minion {
		Objects.requireNonNull(name);
	}
	
	public String toString() {
		return name + " (MINION)";
	}
}
```

#### Q5) On cherche à ajouter une méthode averagePrice à House qui renvoie le prix moyen pour une nuit sachant que le prix pour une nuit pour un VillagePeople est 100 et le prix pour une nuit pour un Minion est 1.

On définit tout d'abord une méthode tarif qui sera commune aux VillagePeople et au Minion, elle renvera le prix d'une nuit pour chacun.
Création de la méthode averagePrice dans la classe House, on parcours notre list et on calcule la moyenne à l'aide de la méthode tarif.
Si la list est vide on renvoie NaN.

```java
    /**
	 * Return the total price paid in the house for a night.
	 * @return
	 */
	public double averagePrice() {
		var res = 0D;
		if (house.isEmpty()) {
			return Double.NaN;
		}
		for(var b : house) {
			res+=b.tarif();
		}
		return res/house.size();
	}
```

#### Q6) Modifier votre code pour introduire une méthode privée qui prend en paramètre un VillagePeople ou un Minion et renvoie son prix par nuit puis utilisez cette méthode pour calculer le prix moyen par nuit d'une maison.

Ajout d'une méthode private tarif2 utilisant le pattern matching, une preview implanté dans JAVA 19 permettant de faire un switch sur des objets. 
Ici elle permet de faire un switch sur un Resident pour indiquer si c'est un VillagePeople ou Minion.

```java
/**
	 * Return the price of night for the Resident r.
	 * @param r
	 * @return
	 */
	private double tarif2(Resident r) {
		return switch(r) {
		case Minion x -> 1.0;
		case VillagePeople x -> this.getPriceVillagePeople(x, 100.0);
		};
	}
```

#### Q7) Pour cela, on va interdire qu'une personne soit autre chose qu'un VillagePeople ou un Minion en scellant le super type commun.

Pour interdire autre chose qu'un VillagePeople ou un Minion, on utilise le mot clef sealed pour scellé l'interface et autorise à un etre un résident seulement les VillagePeople et les Minion.

```java
public sealed interface Resident permits Minion, VillagePeople{
	String name();
	//double tarif();
}
```

#### Q8) Pour cela, on se propose d'ajouter une méthode addDiscount qui prend une sorte en paramètre et offre un discount pour tous les VillagePeople de cette sorte. Si l'on appelle deux fois addDiscount avec la même sorte, le discount n'est appliqué qu'une fois. 

Ajout d'un champ Set Discount dans la classe house qui stockera les Kinds à qui on appliquera une réduction.
Ajout d'une méthode privé __getPriceVillagePeople__ qui prend un VillagePeople ainsi que le prix d'un prix d'une nuit pour un VillagePeople.
Si le kind du VillagePeople appartient au champ discount alors on renvoie le prix après réduction et sinon son prix normal.
Ajout de cette méthode au case VillagePeople de la méthode Tarif2 pour qu'elle renvoie le bon prix.

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class House {
	
	private final List<Resident> house = new ArrayList<>();
	private final Set<Kind> discounts = new HashSet<>();

    /**
	 * Add a Kind into Discount set.
	 * @param k
	 */
	public void addDiscount(Kind k) {
		Objects.requireNonNull(k);
		discounts.add(k);
	}

    /**
	 * Return the price for VillagePeople corresponding to the discount.
	 * @param v
	 * @param price
	 * @return price
	 */
	private double getPriceVillagePeople(VillagePeople v, double price) {
		return (discounts.contains(v.kind()) ? price * 0.2 : price);
	}

    /**
	 * Return the price of night for the Resident r.
	 * @param r
	 * @return
	 */
	private double tarif2(Resident r) {
		return switch(r) {
		case Minion x -> 1.0;
		case VillagePeople x -> this.getPriceVillagePeople(x, 100.0);
		};
	}
}
```

#### Q9) Enfin, on souhaite pouvoir supprimer l'offre commerciale (discount) en ajoutant la méthode removeDiscount qui supprime le discount si celui-ci a été ajouté précédemment ou plante s'il n'y a pas de discount pour la sorte prise en paramètre. 

Ajout de la méthode removeDiscount pour supprimer un kind dans le set discount ou renvoie une exception IllegalStateException si le kind n'existe pas dans les discounts.

```java
/**
	 * Delete the kind into Kind Discount Set, if absent sent an exception.
	 * @param k
	 */
	public void removeDiscount(Kind k) {
		Objects.requireNonNull(k);
		if(!(discounts.contains(k))) {
			throw new IllegalStateException("Exception : Kind doesn't exist");
		}
		discounts.remove(k);
	}
```

#### 10) faire en sorte que l'on puisse ajouter un discount suivi d'un pourcentage de réduction, c'est à dire un entier entre 0 et 100, en implantant une méthode addDiscount(kind, percent). Ajouter également une méthode priceByDiscount qui renvoie une table associative qui a un pourcentage renvoie la somme des prix par nuit auxquels on a appliqué ce pourcentage (la somme est aussi un entier). 

Modification de la méthode addDiscount en ajoutant des préconditions.
Modification de getVillagePrice pour qu'il renvoie le prix d'un VillagePeople en fonction de si il a une réduction ou non.
Fonction priceByDiscount qui à partir de la list de Resident et de la HashMap discount renvoie une hashmap calculant le prix des nuits en fonction des réductions et des différents résidents.


```java
	/**
	 * Add a Kind into Discount set.
	 * @param k
	 */
	/*public void addDiscount(Kind k) {
		Objects.requireNonNull(k);
		discounts.add(k);
	}*/
	public void addDiscount(Kind k, int percent) {
		Objects.requireNonNull(k);
		if(percent<0 || percent > 100) throw new IllegalArgumentException();
		this.discounts.put(k, percent);
	}
	
	
	/**
	 * Return the price for VillagePeople corresponding to the discount.
	 * @param v
	 * @param price
	 * @return price
	 */
	private double getPriceVillagePeople(VillagePeople v, int price) {
		return (discounts.containsKey(v.kind()) ? (price * ((100-discounts.get(v.kind())) * 0.01)) : price);
	}
	/*Return true if the resident has a discount else false.*/
	private boolean hasDiscount(Resident r) {
		return switch(r) {
		case Minion x -> false;
		case VillagePeople x -> (discounts.containsKey(x.kind()) ? true : false);
		};
	}
	
	/**
	 * Return Hashmap containing the price of all nights matching with the discount.
	 * @return
	 */
	public HashMap<Integer, Integer> priceByDiscount() {
		var res = new HashMap<Integer, Integer>();
		res.put(0, 0);
		discounts.forEach((k,v) -> res.put(v, 0));
		System.err.println(res.toString());
		for(var r : this.house) {
			switch(r) {
			case Minion x -> res.put(0, res.get(0) + (int)tarif2(x));
			case VillagePeople x -> {
				if(this.hasDiscount(x)) res.put(discounts.get(x.kind()), res.get(discounts.get(x.kind()))+ (int)tarif2(x));
				else res.put(0, res.get(0) + (int)tarif2(x));
			}};
		}
		return res;
	}
	
	/**
	 * Delete the kind into Kind Discount Set, if absent sent an exception.
	 * @param k
	 */
	public void removeDiscount(Kind k) {
		Objects.requireNonNull(k);
		if(!(discounts.containsKey(k))) {
			throw new IllegalArgumentException("Exception : Kind doesn't exist");
		}
		discounts.remove(k);
	}
```