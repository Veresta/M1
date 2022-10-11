package fr.uge.slice;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SliceTest {
  @Nested
  public class Q1 {
    @Test
    public void sliceArray() {
      String[] array = new String[] { "foo", "bar" };
      Slice<String> slice = Slice.array(array);
      assertAll(
          () -> assertEquals(2, slice.size()),
          () -> assertEquals("foo", slice.get(0)),
          () -> assertEquals("bar", slice.get(1))
      );
    }

    @Test
    public void sliceArrayAllowNull() {
      Integer[] array = new Integer[] { 6, 7, null };
      Slice<Integer> slice = Slice.array(array);
      assertAll(
          () -> assertEquals(3, slice.size()),
          () -> assertEquals(6, slice.get(0)),
          () -> assertEquals(7, slice.get(1)),
          () -> assertNull(slice.get(2))
      );
    }

    @Test
    public void sliceArrayEmpty() {
      var array = new Object[] { };
      var slice = Slice.array(array);
      assertEquals(0, slice.size());
    }

    @Test
    public void sliceArrayIsAView() {
      var array = new String[] { "foo", "bar", "baz" };
      var slice = Slice.array(array);
      array[0] = "zorg";
      assertAll(
          () -> assertEquals(3, slice.size()),
          () -> assertEquals("zorg", slice.get(0)),
          () -> assertEquals("bar", slice.get(1)),
          () -> assertEquals("baz", slice.get(2))
      );
    }

    @Test
    public void sliceArrayPrecondition() {
      assertThrows(NullPointerException.class, () -> Slice.array(null));
    }

    @Test
    public void sliceArrayGetPreconditions() {
      var array = new Double[] { 42.5 };
      var slice = Slice.array(array);
      assertAll(
          () -> assertThrows(IndexOutOfBoundsException.class, () -> slice.get(-1)),
          () -> assertThrows(IndexOutOfBoundsException.class, () -> slice.get(1))
      );
    }

    @Test
    public void onlyPermittedImplementations() {
      assertNotNull(Slice.class.getPermittedSubclasses());
    }

    @Test
    public void sliceArrayImplementationConstructorNotPublic() {
      var subclasses = Slice.class.getPermittedSubclasses();
      if (subclasses == null) {
        return;
      }
      for(var clazz: subclasses) {
        assertEquals(0, clazz.getConstructors().length);
      }
    }

    @Test
    public void sliceArrayImplementationDeclaredFinal() {
      var subclasses = Slice.class.getPermittedSubclasses();
      if (subclasses == null) {
        return;
      }
      for(var clazz: subclasses) {
        assertTrue(Modifier.isFinal(clazz.getModifiers()));
      }
    }
  }
}