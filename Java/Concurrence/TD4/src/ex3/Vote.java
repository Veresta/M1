package ex3;

import java.util.HashMap;
import java.util.Objects;

public class Vote {
	
	private final HashMap<String,Integer> vote;
	private final Object lock = new Object();
	private final int nbVoteWaited;
	private int currentVote;
	private String winner = "";
	
	public Vote(int number) {
		if(number <= 0)throw new IllegalArgumentException();
		nbVoteWaited = number;
		vote = new HashMap<>();
	}
	
	private boolean is_full() {
		synchronized(lock) {
			return (currentVote == nbVoteWaited ? true : false);
		}
	}
	
	private String computeWinner() {
		synchronized(lock) {
			var score = -1;
		    String winner = null;
		    for (var e : vote.entrySet()) {
		      var key = e.getKey();
		      var value = e.getValue();
		      if (value > score || (value == score && key.compareTo(winner) < 0)) {
		        winner = key;
		        score = value;
		      }
		    }
		    return winner;	
		}	    
	}
	
	private void voteBis(String s) {
		synchronized(lock) {
			if(this.vote.containsKey(s)) vote.put(s, vote.get(s) + 1);
			else vote.put(s, 1);
			this.currentVote+=1;
		}
	}
	
	
	public String vote(String word) throws InterruptedException {
		Objects.requireNonNull(word);
		synchronized(lock) {
			voteBis(word);
			while(!(is_full())) {
				lock.wait();
			}
			lock.notifyAll();
			if(is_full() && winner.isEmpty()) winner = this.computeWinner(); 
			return winner;
		}
	}
}
