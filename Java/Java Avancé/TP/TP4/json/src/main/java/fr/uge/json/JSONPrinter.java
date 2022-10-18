package fr.uge.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JSONPrinter {

  private static String escape(Object o) {
    return o instanceof String s ? "\"" + s + "\"": "" + o;
  }

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

  public static String toJSON2(Person person) {
    return """
      {
        "firstName": "%s",
        "lastName": "%s"
      }
      """.formatted(person.firstName(), person.lastName());
  }

  public static void main(String[] args) {
    var i = new Person("Mathis", "MENAA");
    System.out.println(toJSON(i));
    System.out.println(toJSON2(i));
  }
}