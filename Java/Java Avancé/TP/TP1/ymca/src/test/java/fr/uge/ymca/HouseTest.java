package fr.uge.ymca;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

public class HouseTest {
	@Nested
	  public class Q1 {
	    @Test
	    public void villagePeople() {
	      var lee = new VillagePeople("Lee", Kind.BIKER);
	      var felipe = new VillagePeople("Felipe", Kind.NATIVE);
	      assertAll(
	          () -> assertEquals("Lee", lee.name()),
	          () -> assertEquals("Felipe", felipe.name()),
	          () -> assertEquals(Kind.BIKER, lee.kind()),
	          () -> assertEquals(Kind.NATIVE, felipe.kind())
	      );
	    }

	    @Test
	    public void villagePeopleToString() {
	      var alex = new VillagePeople("Alex", Kind.GI);
	      var mark = new VillagePeople("Mark", Kind.CONSTRUCTION);
	      assertAll(
	          () -> assertEquals("Alex (GI)", "" + alex),
	          () -> assertEquals("Mark (CONSTRUCTION)", "" + mark)
	      );
	    }

	    @Test
	    public void preconditions() {
	      assertAll(
	          () -> assertThrows(NullPointerException.class, () -> new VillagePeople(null, Kind.COP)),
	          () -> assertThrows(NullPointerException.class, () -> new VillagePeople("Peter", null))
	      );
	    }

	    @Test
	    public void modifiers() {
	      assertTrue(
	          isPublic(VillagePeople.class.getModifiers()) && isFinal(VillagePeople.class.getModifiers()));
	      assertTrue(
	          Arrays.stream(VillagePeople.class.getDeclaredFields())
	              .allMatch(field -> isPrivate(field.getModifiers()) && isFinal(field.getModifiers())));
	    }
	  }
	
	@Nested
	  public class Q2 {
	    @Test
	    public void house() {
	      var house = new House();
	      var david = new VillagePeople("David", Kind.COWBOY);
	      house.add(david);
	      assertEquals("House with David", "" + house);
	    }

	    @Test
	    public void houseWithSeveralVillagePeople() {
	      var house = new House();
	      var david = new VillagePeople("David", Kind.COWBOY);
	      var victor = new VillagePeople("Victor", Kind.COP);
	      house.add(david);
	      house.add(victor);
	      assertEquals("House with David, Victor", "" + house);
	    }

	    @Test
	    public void emptyHouse() {
	      var house = new House();
	      assertEquals("Empty House", "" + house);
	    }

	    @Test
	    public void preconditions() {
	      var house = new House();
	      assertThrows(NullPointerException.class, () -> house.add(null));
	    }

	    @Test
	    public void modifiers() {
	      assertTrue(
	          isPublic(House.class.getModifiers()) && isFinal(House.class.getModifiers()));
	      assertTrue(
	          Arrays.stream(House.class.getDeclaredFields())
	              .allMatch(field -> isPrivate(field.getModifiers()) && isFinal(field.getModifiers())));
	    }

	    @Test
	    public void publicMethods() {
	      var houseMethodNames = Arrays.stream(House.class.getMethods())
	          .filter(m -> m.getDeclaringClass() != Object.class)
	          .map(Method::getName)
	          .toList();
	      var expectedNames = Set.of("add", "toString", "averagePrice", "addDiscount", "removeDiscount", "priceByDiscount");
	      assertTrue(expectedNames.containsAll(houseMethodNames), "" + houseMethodNames);
	    }
	  }
	
	@Nested
	  public class Q3 {
	    @Test
	    public void houseNameProperlySorted() {
	      var house = new House();
	      house.add(new VillagePeople("Lee", Kind.BIKER));
	      house.add(new VillagePeople("Felipe", Kind.NATIVE));
	      house.add(new VillagePeople("Alex", Kind.GI));
	      house.add(new VillagePeople("Mark", Kind.CONSTRUCTION));
	      house.add(new VillagePeople("David", Kind.COWBOY));
	      house.add(new VillagePeople("Victor", Kind.COP));
	      assertEquals("House with Alex, David, Felipe, Lee, Mark, Victor", "" + house);
	    }
	  }
	
	@Nested
	  public class Q5 {
	    @Test
	    public void averagePriceVillagePeople() {
	      var house = new House();
	      house.add(new VillagePeople("Lee", Kind.BIKER));
	      house.add(new VillagePeople("Felipe", Kind.NATIVE));
	      assertEquals(100.0, house.averagePrice(), 0.00001);
	    }

	    @Test
	    public void averagePriceMinions() {
	      var house = new House();
	      house.add(new Minion("Stuart"));
	      house.add(new Minion("Kevin"));
	      house.add(new Minion("Bob"));
	      assertEquals(1.0, house.averagePrice(), 0.00001);
	    }

	    @Test
	    public void averagePriceFullHouse() {
	      var house = new House();
	      house.add(new VillagePeople("Lee", Kind.BIKER));
	      house.add(new VillagePeople("Felipe", Kind.NATIVE));
	      house.add(new VillagePeople("Alex", Kind.GI));
	      house.add(new VillagePeople("Mark", Kind.CONSTRUCTION));
	      house.add(new VillagePeople("David", Kind.COWBOY));
	      house.add(new VillagePeople("Victor", Kind.COP));
	      house.add(new Minion("Stuart"));
	      house.add(new Minion("Kevin"));
	      house.add(new Minion("Bob"));
	      assertEquals(67.0, house.averagePrice(), 0.00001);
	    }

	    @Test
	    public void averagePriceEmptyHouse() {
	      var house = new House();
	      assertTrue(Double.isNaN(house.averagePrice()));
	    }
	  }
	
	@Nested
	  public class Q6 {
	    @Test
	    public void commonSupertypeHasOnlyOneMethodNamedName() {
	      assertAll(
	          () -> assertEquals(1, VillagePeople.class.getInterfaces()[0].getMethods().length),
	          () -> assertEquals(1, Minion.class.getInterfaces()[0].getMethods().length),
	          () -> assertEquals("name", VillagePeople.class.getInterfaces()[0].getMethods()[0].getName()),
	          () -> assertEquals("name", Minion.class.getInterfaces()[0].getMethods()[0].getName())
	      );
	    }
	  }
	
	@Nested
	  public class Q7 {
	    @Test
	    public void commonSupertypeIsSealed() {
	      assertAll(
	          () -> assertEquals(List.of(VillagePeople.class.getInterfaces()), List.of(Minion.class.getInterfaces())),
	          () -> assertEquals(1, VillagePeople.class.getInterfaces().length),
	          () -> assertEquals(1, Minion.class.getInterfaces().length),
	          () -> assertTrue(VillagePeople.class.getInterfaces()[0].isSealed()),
	          () -> assertTrue(Minion.class.getInterfaces()[0].isSealed())
	      );
	    }
	  }
	
	@Nested
	  public class Q8 {
	    @Test
	    public void discountWithOnlyVillagePeople() {
	      var house = new House();
	      house.addDiscount(Kind.NATIVE);
	      house.add(new VillagePeople("Lee", Kind.BIKER));
	      house.add(new VillagePeople("Felipe", Kind.NATIVE));
	      assertEquals(60.0, house.averagePrice(), 0.00001);
	    }

	    @Test
	    public void discount() {
	      var house = new House();
	      house.addDiscount(Kind.BIKER);
	      house.add(new VillagePeople("Lee", Kind.BIKER));
	      house.add(new VillagePeople("Felipe", Kind.NATIVE));
	      house.add(new Minion("Stuart"));
	      house.add(new Minion("Kevin"));
	      assertEquals(30.5, house.averagePrice(), 0.00001);
	    }
	    
	    @Nested
	    public class Q9 {
	      @Test
	      public void discount() {
	        var house = new House();
	        house.addDiscount(Kind.BIKER);
	        house.add(new VillagePeople("Lee", Kind.BIKER));
	        house.add(new VillagePeople("Felipe", Kind.NATIVE));
	        house.add(new Minion("Stuart"));
	        house.add(new Minion("Kevin"));
	        house.removeDiscount(Kind.BIKER);
	        assertEquals(50.5, house.averagePrice(), 0.00001);
	      }

	      @Test
	      public void preconditions() {
	        assertAll(
	            () -> assertThrows(NullPointerException.class, () -> new House().removeDiscount(null)),
	            () -> assertThrows(IllegalStateException.class, () -> new House().removeDiscount(Kind.CONSTRUCTION)),
	            () -> assertThrows(IllegalStateException.class, () -> {
	              var house = new House();
	              house.addDiscount(Kind.COWBOY);
	              house.addDiscount(Kind.COWBOY);
	              house.removeDiscount(Kind.COWBOY);
	              house.removeDiscount(Kind.COWBOY);
	            })
	        );
	      }
	    }

	    @Test
	    public void discountCanAddTheSameDiscountTwice() {
	      var house = new House();
	      house.addDiscount(Kind.COP);
	      house.addDiscount(Kind.COP);
	      house.add(new VillagePeople("Victor", Kind.COP));
	      assertEquals(20.0, house.averagePrice(), 0.00001);
	    }

	    @Test
	    public void precondition() {
	      var house = new House();
	      assertThrows(NullPointerException.class, () -> house.addDiscount(null));
	    }
	  }
}