import java.util.ArrayList;

public class Hand {

	private static ArrayList<Card> hand1 = new ArrayList<>(10);
	
	public void add(Card c) {
		hand1.add(c);
	}
	
	public void remove(Card c) {
		hand1.remove(c);
	}
	
	public void printHand() {
		for(int i = 0; i<hand1.size();i++) {
			hand1.get(i).toString2();
		}
	}
	
} // end class hand