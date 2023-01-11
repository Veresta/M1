package fr.uge.ymca;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class House {
	
	private final List<Resident> house = new ArrayList<>();
	//private final Set<Kind> discounts = new HashSet<>();
	private final HashMap<Kind, Integer> discounts = new HashMap<>();
	
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
		case Minion x -> 1;
		case VillagePeople x -> this.getPriceVillagePeople(x, 100);
		};
	}
	
	

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
	/*private double getPriceVillagePeople(VillagePeople v, double price) {
		return (discounts.contains(v.kind()) ? price * 0.2 : price);
	}*/
	private double getPriceVillagePeople(VillagePeople v, int price) {
		return (discounts.containsKey(v.kind()) ? (price * ((100-discounts.get(v.kind())) * 0.01)) : price);
	}
	
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
}
