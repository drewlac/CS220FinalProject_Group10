
public class DeckTester {

	public static void main(String[] args) {

	Deck deck1 = new Deck();
	
	Hand hand1 = new Hand();
	
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	hand1.add(deck1.getRandomCard());
	
	System.out.println("There are " + deck1.getDeckCount() + " cards left in the deck");
	
	hand1.printHand();
		
	}

}
