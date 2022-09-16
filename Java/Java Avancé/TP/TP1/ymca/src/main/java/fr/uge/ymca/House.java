package fr.uge.ymca;

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
			//res+=b.tarif();
			res+=tarif2(b);
		}
		return res/house.size();
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
	

}
