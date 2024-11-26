// DeckTester allows the team to test the functionality of the program.
public class DeckTester {

	public static void main(String[] args) {

		Deck deck1 = new Deck();

		Hand hand1 = new Hand();
		Hand hand2 = new Hand();
		 
		hand1.add(deck1.getRandomCards(5));
		hand2.add(deck1.getRandomCards(5));
		
		System.out.println("Hand 1: ");
		hand1.printHand();
		System.out.println("Hand 2: ");
		hand2.printHand();

		Hand.switchHands(hand1, hand2);
		
		System.out.println("************************************");
		
		//hand1.add(deck1.getRandomCards(1));

		
		System.out.println("Hand 1: ");
		hand1.printHand();
		System.out.println("Hand 2: ");
		hand2.printHand();

	}

}
