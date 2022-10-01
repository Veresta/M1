# Compte rendu TP2 Java Avancé

### Mathis MENAA
------

## Exercice 2: 

#### 1) Dans un premier temps, on va créer une classe StreamEditor dans le package fr.uge.sed avec une méthode d'instance transform qui prend en paramètre un LineNumberReader et un Writer et écrit, ligne à ligne, le contenu du LineNumberReader dans le Writer.

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


```java
public static void main(String[] args) {
		File file = new File(args[0]);
		try (Reader reader = new FileReader(file)) {
			LineNumberReader lnr = new LineNumberReader(reader);
			OutputStream os = new FileOutputStream(file);
			var command = StreamEditor.lineDelete(2);
		    var editor = new StreamEditor(command);
		    editor.transform(lnr, new OutputStreamWriter(os,StandardCharsets.UTF_8));		    
		    os.close();
		    reader.close();
        } catch (FileNotFoundException e) {
			throw new AssertionError();
		} catch (IOException e) {
			throw new AssertionError();
		}
	}
```

#### 4) Changer le code pour que le record LineDeleteCommand possède la méthode décrite plus haut et changer le code de transform pour qu'elle appelle cette méthode. 

```java
public static LineDeleteCommand lineDelete(int nb) {
		if(nb<0)throw new IllegalArgumentException("Negative line");
		return new LineDeleteCommand(nb);
	}
```