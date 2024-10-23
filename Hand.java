import java.util.ArrayList;

// The Hand class manages the CPU's and player's current hand of cards.
public class Hand {

	private static ArrayList<Card> hand1 = new ArrayList<>(10);

	public void add(Card c) {
		if (c.getColor() == "" && c.getValue() == 0) {
			System.out.println("Deck is empty! Cannot add to hand.");
		} else {
			hand1.add(c);
		}
	}
	
	public void add(ArrayList<Card> cards) {
		
		for(Card c: cards) {
		
		
		if (c.getColor() == "" && c.getValue() == 0) {
			System.out.println("Deck is empty! Cannot add to hand.");
			} 
		
		else {
			hand1.add(c);
			}
		
		}
	}//end add overload method

	public void remove(Card c) {
		hand1.remove(c);
	}

	public void printHand() {
		for (int i = 0; i < hand1.size(); i++) {
			System.out.println(hand1.get(i).toString());
		}
	}
	
	public int getSize() {
		return hand1.size();
	}

} // end class hand
