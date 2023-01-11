# Compte rendu TP4 Java Avancé

### Mathis MENAA
------

## Exercice 2: JSON Encoder

##### 1) Écrire la méthode toJSON qui prend en paramètre un java.lang.Record, utilise la réflexion pour accéder à l'ensemble des composants d'un record (java.lang.Class.getRecordComponent), sélectionne les accesseurs, puis affiche les couples nom du composant, valeur associée.

Get the components of the record which we apply a stream that will access the fields/values of the record and format them to obtain a JSON.
Don't forget to handle mistakes properly with a try/catch.

```java
public static String toJSON(Record elem) {
    var components = elem.getClass().getRecordComponents();
    return Arrays.stream(components).map(x -> {
      try {
        return "\"%s\": %s".formatted(x.getName(), escape(x.getAccessor().invoke(elem)));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        var cause = e.getCause();
        if (cause instanceof RuntimeException exception) {
          throw exception;
        }
        if (cause instanceof Error error) {
          throw error;
        }
        throw new UndeclaredThrowableException(e);
      }
    }).collect(Collectors.joining(",\n", "{\n", "\n}"));
  }
```

##### 2) Déclarez l'annotation JSONProperty visible à l'exécution et permettant d'annoter des composants de record, puis modifiez le code de toJSON pour n'utiliser que les propriétés issues de méthodes marquées par l'annotation JSONProperty.

Creation of an Annotation interface file that will define a JSONproperty with a default method.
Add in the map function the read of an annotation if it exists.

```java
@Target(ElementType.RECORD_COMPONEN)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONProperty {
    String value() default "";
}

public static String toJSON(Record elem) {
    var components = elem.getClass().getRecordComponents();
    return Arrays.stream(components)
            .map(x -> "\"%s\": %s".formatted((x.getAnnotation(JSONProperty.class) == null ? x.getName() : x.getAnnotation(JSONProperty.class).value()), escape(myInvoke(x.getAccessor(),elem))))
            .collect(Collectors.joining(",\n", "{\n", "\n}"));
  }
```

##### 3) En fait, on veut aussi gérer le fait que l'annotation peut ne pas être présente et aussi le fait que si l'annotation est présente mais sans valeur spécifiée alors le nom du composant est utilisé avec les '_' réécrits en '-'. Modifier le code dans JSONPrinter et la déclaration de l'annotation en conséquence.

Add new method simplifyField which return the right value of the name of the field in the different cases.

```java
public static String toJSON(Record elem) {
        var components = elem.getClass().getRecordComponents();
        return Arrays.stream(components)
        .map(x -> "\"%s\": %s".formatted(simplifyField(x.getAccessor()), escape(myInvoke(x.getAccessor(),elem))))
        .collect(Collectors.joining(",\n", "{\n", "\n}"));
}

private static String simplifyField(RecordComponent acc){
    var name = acc.getName();
    var annot = acc.getAnnotation(JSONProperty.class);
    if(annot == null) return name;
    var tmp = annot.value();
    return (tmp.isEmpty() ? name.replaceAll("_","-") : tmp);
  }
```

##### 4) En fait, l'appel à getRecordComponents est lent ; regardez la signature de cette méthode et expliquez pourquoi...

Initialize an array makes the method slow, it costs a lot.
And also, we had to make a defensive copy.

##### 5) Nous allons donc limiter les appels à getRecordComponents en stockant le résultat de getRecordComponents dans un cache pour éviter de faire l'appel à chaque fois qu'on utilise toJSON. Utilisez la classe java.lang.ClassValue pour mettre en cache le résultat d'un appel à getRecordComponents pour une classe donnée.

Creation of a ClassValue field that will allow to pre-calculate some elements to optimize our code.
Here, the CACHE pre-calculate the call of the method getRecordComponents.

```java
private static final ClassValue<RecordComponent[]> CACHE = new ClassValue<>() {
    @Override
    protected RecordComponent[] computeValue(Class<?> type) {
      Objects.requireNonNull(type);
      return type.getRecordComponents();
    }
  };
```

##### 6) En fait, on peut mettre en cache plus d'informations que juste les méthodes, on peut aussi pré-calculer le nom des propriétés pour éviter d'accéder aux annotations à chaque appel. Écrire le code qui pré-calcule le maximum de choses pour que l'appel à toJSON soit le plus efficace possible.

Create new method createJSON which based on a RecordComponent will format the data of the component in the Json format thanks to a lambda.
Upgrade of the CACHE by returning a stream applying the getRecordComponents + map(JSONPrinter::createJSON).

```java
private static final ClassValue<List<Function<Record, String>>> CACHE = new ClassValue<>() {
    @Override
    protected List<Function<Record, String>> computeValue(Class<?> type) {
      Objects.requireNonNull(type);
      var tab = type.getRecordComponents();
      return Arrays.stream(tab).map(JSONPrinter::createJSON).toList();
    }
  };

private static Function<Record, String> createJSON(RecordComponent elem){
        Objects.requireNonNull(elem);
        return r -> "\"%s\": %s".formatted(simplifyField(elem), escape(myInvoke(elem.getAccessor(),r)));
    }
```