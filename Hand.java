import java.util.ArrayList;

// The Hand class manages the CPU's and player's current hand of cards.
public class Hand extends Cards{

	private ArrayList<Card> hand1 = new ArrayList<>(10);

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
			System.out.println("-Card " + (i+1) + ": " + hand1.get(i).toString());
		}
	}
	
	public Card getCard(int index) {
		return hand1.get(index);
	}
	
	public int getSize() {
		return hand1.size();
	}
	
	public String toString() { //needs to be completed
		return "";
	}
	
	public void print() {}//needs to be completed
	
	public void sort() {
		hand1.sort(null);
	}
	
	public void removeAllCards() {
		hand1.removeAll(hand1);
	}

} // end class hand
