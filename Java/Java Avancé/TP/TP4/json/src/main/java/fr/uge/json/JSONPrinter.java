package fr.uge.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSONPrinter {

  private static final ClassValue<List<Function<Record, String>>> CACHE = new ClassValue<>() {
    @Override
    protected List<Function<Record, String>> computeValue(Class<?> type) {
      Objects.requireNonNull(type);
      var tab = type.getRecordComponents();
      return Arrays.stream(tab).map(JSONPrinter::createJSON).toList();
    }
  };

  private static String escape(Object o) {
    return o instanceof String s ? "\"" + s + "\"": "" + o;
  }

  private static Object myInvoke(Method acc, Object obj){
    try {
      return acc.invoke(obj);
    } catch(InvocationTargetException | IllegalAccessException e) {
      var cause = e.getCause();
      if (cause instanceof RuntimeException rte) {
        throw rte;
      }
      if (cause instanceof Error error) {
        throw error;
      }
      throw new UndeclaredThrowableException(cause);
    }
  }

  private static Function<Record, String> createJSON(RecordComponent elem){
    return r -> "\"%s\": %s".formatted(simplifyField(elem), escape(myInvoke(elem.getAccessor(),r)));
  }

  public static String toJSON(Record elem) {
    Objects.requireNonNull(elem);
    return CACHE.get(elem.getClass()).stream().map(w -> w.apply(elem)).collect(Collectors.joining(",\n", "{\n", "\n}"));
  }

  private static String simplifyField(RecordComponent acc){
    var name = acc.getName();
    var annot = acc.getAnnotation(JSONProperty.class);
    if(annot == null) return name;
    var tmp = annot.value();
    return (tmp.isEmpty() ? name.replaceAll("_","-") : tmp);
  }
}