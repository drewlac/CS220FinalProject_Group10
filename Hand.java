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
	
	public void clearHand() {
		hand1.clear();
	}
	
	private ArrayList<Card> getHandArray(){
		return hand1;
	}

	public static void switchHands(Hand first, Hand second) {
		//create a copy of the first list
		ArrayList<Card> copyFirst = new ArrayList<>(10);
		copyFirst.addAll(first.getHandArray());
		
		//create a copy of the second list
		ArrayList<Card> copySecond = new ArrayList<>(10);
		copySecond.addAll(second.getHandArray());
	
		//clear both original hands
		first.clearHand();
		second.clearHand();
		
		//add second to first and first to second
		first.add(copySecond);
		second.add(copyFirst);	
	
	}//end switch hands method

} // end class hand
