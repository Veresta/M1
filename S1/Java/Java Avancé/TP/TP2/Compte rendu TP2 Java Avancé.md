# Compte rendu TP2 Java Avancé

### Mathis MENAA
------

## Exercice 2: 

#### 1) Dans un premier temps, on va créer une classe StreamEditor dans le package fr.uge.sed avec une méthode d'instance transform qui prend en paramètre un LineNumberReader et un Writer et écrit, ligne à ligne, le contenu du LineNumberReader dans le Writer.

First of all, we need to create a class StreamEditor with 2 parameters : 
- LineNumberReader
- Writter w

Of course we're checking pre conditions to avoid exceptions.

```java
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.Objects;

public final class StreamEditor {

	public void transform(LineNumberReader a, Writer w) throws IOException {
		Objects.requireNonNull(a);
		Objects.requireNonNull(w);
		String line = null;
		while((line  = a.readLine()) != null) {
			w.append(line+'\n');
		}
	}
}
```

#### 2) On veut maintenant pouvoir spécifier une commande à la création du StreamEditor pour transformer les lignes du fichier en entrée. Ici, lineDelete renvoie un record LineDeleteCommand qui indique la ligne à supprimer (la première ligne d'un fichier est 1, pas 0).

Creation of a LineDeleteCommand, which contain the number of the line we need to delete.

Add a private final field to stock the LineDeletecommand.
Create 2 constructor, one with the command in argument and the other without.

```java
public record LineDeleteCommand(int nb) {
	public LineDeleteCommand {
		if(nb<0)throw new IllegalArgumentException("Negative line");
	}
}
public final class StreamEditor {
	
	private final LineDeleteCommand line;
	
	public StreamEditor(LineDeleteCommand a) {
		Objects.requireNonNull(a);
		line = a;
	}

	public StreamEditor() {
		line = null;
	}

	public void transform(LineNumberReader a, Writer w) throws IOException {
		Objects.requireNonNull(a);
		Objects.requireNonNull(w);
		String line = null;
		while((line  = a.readLine()) != null) {
			if((this.line != null)&&(a.getLineNumber() == this.line.nb())) {
				continue;
			}
			w.append(line+'\n');
			/*if(a.getLineNumber() != ((this.line == null) ? -10 : this.line.nb())) {
				w.append(line+'\n');
			}*/	
		}
	}
	
	public static LineDeleteCommand lineDelete(int nb) {
		if(nb<0)throw new IllegalArgumentException("Negative line");
		return new LineDeleteCommand(nb);
	}

}
```

#### 3) On souhaite maintenant écrire un main qui prend en paramètre sur la ligne de commande un nom de fichier, supprime la ligne 2 de ce fichier et écrit le résultat sur la sortie standard. 

Get the name of the file in the line command.
Use try-catch to open/close sources like the Reader, LineNumberReader,PrintStream,OutputStreamWriter.

```java
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
```

#### 4) Changer le code pour que le record LineDeleteCommand possède la méthode décrite plus haut et changer le code de transform pour qu'elle appelle cette méthode. 

Add the method LineDeleteCommand in the class StreamEditor, which return a new Object LineDeleteCommand with the right parameter.

Add the new method in the method Transform including the enum Action.

```java
public static LineDeleteCommand lineDelete(int nb) {
		if(nb<0)throw new IllegalArgumentException("Negative line");
		return new LineDeleteCommand(nb);
}

public void transform(LineNumberReader a, Writer w) throws IOException {
		Objects.requireNonNull(a);
		Objects.requireNonNull(w);
		String current_l = null;
		while((current_l  = a.readLine()) != null) {
			if((this.line != null)) {
				switch(this.line.deleteOrPrint(a.getLineNumber())) {
				case PRINT : w.append(current_l+'\n');
				case DELETE : continue;
				}
			}
			
		}
	}

public enum Action { DELETE, PRINT }
```

#### 5) Maintenant que l'on a bien préparé le terrain, on peut ajouter une nouvelle commande renvoyée par la méthode findAndDelete qui prend en paramètre un java.util.regex.Pattern telle que le code suivant fonctionne.

Re-implementation of findAndDelete in the same way as LineDelete, creation of a FindAndDeleteCommand(Pattern p) record, modification of the transform method by adding a condition in case there is a regex in command.

```java
	public static FindAndDeleteCommand findAndDelete(Pattern p) {
		Objects.requireNonNull(p);
		return new FindAndDeleteCommand(p);
	}

	public record FindAndDeleteCommand(Pattern p) {
		
		public FindAndDeleteCommand {
			Objects.requireNonNull(p);
		}
		
		public boolean ismatching(String stringToBeMatched) {
			Matcher matcher = p.matcher(stringToBeMatched);
			return matcher.find();
		}
	}

	public void transform(LineNumberReader a, Writer w) throws IOException {
		Objects.requireNonNull(a);
		Objects.requireNonNull(w);
		String current_l = null;
		while ((current_l = a.readLine()) != null) {
			if ((this.line != null)) {
				switch (this.line.deleteOrPrint(a.getLineNumber())) {
				case PRINT:
					w.append(current_l + '\n');
				case DELETE:
					continue;
				}
			}
			if (this.find != null) {
				if (!(find.ismatching(current_l))) w.append(current_l + '\n');	
			}
		}
	}
```

#### 6) Modifier votre code pour que les implantations des commandes renvoyées par les méthodes lineDelete et findAndDelete soit des lambdas. 

Added a functional interface representing a command.

```java 
	@FunctionalInterface
	interface Command {
		public Action apply(int i, String s);
	}

	private final Command command; //Modification du champ de la classe StreamEditor
```

Modification of the Transform method.

```java
public void transform(LineNumberReader a, Writer w) throws IOException {
		Objects.requireNonNull(a);
		Objects.requireNonNull(w);
		String current_l = null;
		while ((current_l = a.readLine()) != null) {
			
			if(command != null) {
				switch(command.apply(a.getLineNumber(), current_l)) {
				case PRINT: w.append(current_l + '\n');
				case DELETE: continue;
				}
			}
		}
	}
```

Changed findAndDelete/lineDelete methods to return lambdas.

```java
public static Command findAndDelete(Pattern p) {
		Objects.requireNonNull(p);
		return (i,s) -> {
			Matcher matcher = p.matcher(s.replaceAll("\\s", ""));
			return (matcher.find()) ? Action.DELETE : Action.PRINT;
		};
	}

	public static Command lineDelete(int nb) {
		if (nb < 0)
			throw new IllegalArgumentException("Negative line");
		return (i,s) -> {
			return ((i == nb) ? Action.DELETE : Action.PRINT);
		};
	}
```

#### 7)Modifier votre code sans introduire pour l'instant la commande substitute pour utiliser l'interface Action au lieu de l'enum. 

Modification of return values in the findAndDelete and lineDelete methods.

```java
return (matcher.find()) ? new Action.DeleteAction() : new Action.PrintAction(s);
return ((i == nb) ? new Action.DeleteAction() : new Action.PrintAction(s));
```

Modification of the switch in the transform method.

```java
while ((current_l = a.readLine()) != null) {
			if(command != null) {
				switch(command.apply(a.getLineNumber(), current_l)) {
				case Action.PrintAction x -> w.append(current_l + '\n');
				case Action.DeleteAction y -> w.append("");
				}
			}
	}
```

#### 8)Écrire le code de la méthode substitute et vérifier que les tests JUnit marqués "Q8" passent.

Add new method __substitute__ which returning a Command, it takes a Pattern p and String elm.

The method return the right string to return because there is 2 cases :

1. pattern match the current line so it returns a current line modified (it replace all occurence which matching the pattern with elm).
2. pattern match nothing so we return de current line.

```java
public static Command substitute(Pattern p, String elm) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(elm);
		return (i,s) -> {
			Matcher matcher = p.matcher(s);
			if(matcher.find()){
				var tmp = matcher.replaceAll(elm);
				return new Action.PrintAction(tmp);
			}
			return new Action.PrintAction(s);
		};
	}
```

#### 10) Modifier Command pour introduire la méthode d'instance andThen.

Add default method __andThen__ in the functional interface command.
It returns a Command corresponding to the new command called by the first.

```java
@FunctionalInterface
	public interface Command {
		public Action apply(int i, String s);

		default Command andThen(Command c) {
			Objects.requireNonNull(c);
			return (i,s) -> {
				var res = this.apply(i, s);
				return switch(res) {
				case Action.PrintAction x -> c.apply(i,x.text);
				case Action.DeleteAction y -> y;
				};
			};
		}
	}
```

#### 11) En conclusion, dans quel cas, à votre avis, va-t-on utiliser des records pour implanter de différentes façons une interface et dans quel cas va-t-on utiliser des lambdas ?

We use lambdas when we want to manipulate the same objects but with a different behavior.
We use records when we want to manipulate objects of the same "type" and the same behavior.