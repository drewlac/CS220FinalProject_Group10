// DeckTester allows the team to test the functionality of the program.
public class DeckTester {

	public static void main(String[] args) {

		Deck deck1 = new Deck();

		//Hand hand1 = new Hand();
		Hand hand2 = new Hand();
		
		
		

		
		hand2.add(deck1.getRandomCards(5));
		System.out.println("size: " + hand2.getSize());
		hand2.printHand();

		//deck1.printDeckCount();

		//hand1.printHand();

	}

}
