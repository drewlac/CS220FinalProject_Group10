// DeckTester allows the team to test the functionality of the program.
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

		deck1.printDeckCount();

		hand1.printHand();

	}

}
