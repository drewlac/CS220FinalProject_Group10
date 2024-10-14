
public class DeckTester {

	public static void main(String[] args) {

	Deck deck1 = new Deck();
	
	Hand hand1 = new Hand();
	
	hand1.add(deck1.getRandomCard());
	
	hand1.printHand();
		
	}

}
