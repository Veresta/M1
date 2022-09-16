# Compte rendu TP1 Java Avancé

### Mathis MENAA
------


## Exercice 2 - YMCA

#### Q1) Écrire le code de VillagePeople tel que l'on puisse créer des VillagePeople avec leur nom et leur sorte.

Création d'un record VillagePeople avec un champ String name et un champ Kind kind.
Dans le constructeur on vérifie avec un Objects.requireNonNull les champs du record.
On reféfinit la méthode toString selon la norme du sujet. 


#### Q2) On veut maintenant introduire une maison House qui va contenir des VillagePeople. Une maison possède une méthode add qui permet d'ajouter un VillagePeople dans la maison (note : il est possible d'ajouter plusieurs fois le même). L'affichage d'une maison doit renvoyer le texte "House with" suivi des noms des VillagePeople ajoutés à la maison, séparés par une virgule. Dans le cas où une maison est vide, le texte est "Empty House".

Définition d'une classe House, comprenant un champ house qui est une list de VillagePeople.
Ajout de la méthode add pour ajouter un VillagePeople à notre house.
Rédéfinition de la méthode toString pour avoir un affichage selon le sujet.


#### Q3) En fait on veut que l'affichage affiche les noms des VillagePeople dans l'ordre alphabétique, il va donc falloir trier les noms avant de les afficher.

Utilisation d'un stream pour l'affichage.

__house.stream().sorted(Comparator.comparing(Resident::name)).map(Resident::name).collect(Collectors.joining(", ")));__

#### Q4) En fait, avoir une maison qui ne peut accepter que des VillagePeople n'est pas une bonne décision en termes de business, ils ne sont pas assez nombreux. YMCA décide donc qu'en plus des VillagePeople ses maisons permettent maintenant d'accueillir aussi des Minions, une autre population sans logement.

Définition d'un record Minion composé d'un champ String name.
On rédéfinis la méthode toString pour qu'elle corresponde au sujet.
Pour pouvoir ajouter des Minions et des VillagePeople, on définit une Interface Résident qui va représenter les habitants d'une house.
Dans notre classe House, la list va de VillagePeople devient une list de Resident.

#### Q5) On cherche à ajouter une méthode averagePrice à House qui renvoie le prix moyen pour une nuit sachant que le prix pour une nuit pour un VillagePeople est 100 et le prix pour une nuit pour un Minion est 1.

On définit tout d'abord une méthode tarif qui sera commune aux VillagePeople et au Minion, elle renvera le prix d'une nuit pour chacun.
Création de la méthode averagePrice dans la classe House, on parcours notre list et on calcule la moyenne à l'aide de la méthode tarif.
Si la list est vide on renvoie NaN.

#### Q6) Modifier votre code pour introduire une méthode privée qui prend en paramètre un VillagePeople ou un Minion et renvoie son prix par nuit puis utilisez cette méthode pour calculer le prix moyen par nuit d'une maison.

Ajout d'une méthode private tarif2 utilisant le pattern matching, une preview implanté dans JAVA 19 permettant de faire un switch sur des objets. 
Ici elle permet de faire un switch sur un Resident pour indiquer si c'est un VillagePeople ou Minion.

#### Q7) Pour cela, on va interdire qu'une personne soit autre chose qu'un VillagePeople ou un Minion en scellant le super type commun.

Pour interdire autre chose qu'un VillagePeople ou un Minion, on utilise le mot clef sealed pour scellé l'interface et autorise à un etre un résident seulement les VillagePeople et les Minion.

#### Q8) Pour cela, on se propose d'ajouter une méthode addDiscount qui prend une sorte en paramètre et offre un discount pour tous les VillagePeople de cette sorte. Si l'on appelle deux fois addDiscount avec la même sorte, le discount n'est appliqué qu'une fois. 

Ajout d'un champ Set Discount dans la classe house qui stockera les Kinds à qui on appliquera une réduction.
Ajout d'une méthode privé __getPriceVillagePeople__ qui prend un VillagePeople ainsi que le prix d'un prix d'une nuit pour un VillagePeople.
Si le kind du VillagePeople appartient au champ discount alors on renvoie le prix après réduction et sinon son prix normal.
Ajout de cette méthode au case VillagePeople de la méthode Tarif2 pour qu'elle renvoie le bon prix.

#### Q9) Enfin, on souhaite pouvoir supprimer l'offre commerciale (discount) en ajoutant la méthode removeDiscount qui supprime le discount si celui-ci a été ajouté précédemment ou plante s'il n'y a pas de discount pour la sorte prise en paramètre. 

Ajout de la méthode removeDiscount pour supprimer un kind dans le set discount ou renvoie une exception IllegalStateException si le kind n'existe pas dans les discounts.