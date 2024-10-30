import java.util.ArrayList;
import java.util.Random;

// The Deck class functions as the draw pile for the game. That is, this class
// assembles each card within the game into a draw pile, or deck.
public class Deck extends Cards{

	private static ArrayList<Card> deck1 = new ArrayList<>(76);
	private final String[] colorArray = { "Red", "Blue", "Green", "Yellow" };
	private int deckCount;

	public Deck() { // automatically creates a default deck with standard cards
		for (int j = 0; j <= 3; j++) { // j iterates between integer 0 through 3, including 3,
			// to represent color.
			for (int i = 0; i <= 9; i++) { // i iterates between integers 0 through 9, including 9.

				// If i is not a 0 card, ensure that there are two of each number card for each
				// color.
				if (i != 0) {
					deck1.add(newCard(i, colorArray[j]));
					deck1.add(newCard(i, colorArray[j]));
				} // end if
					// If i is a 0 card, only add one 0 card per color.
				else {
					deck1.add(newCard(i, colorArray[j]));
				}

			} // end for (nested)
		} // end outer for
		deckCount = 76;

	}// end constructor

	private Card newCard(int i, String string) {
		return new Card(i, string);
	}

	public ArrayList<Card> getDeck() {
		return deck1;
	}

	public void printDeck() {

		for (int i = 0; i < deck1.size(); i++) {
			deck1.get(i).toString();
		}
	}

	// Returns random card and removes it from the deck, presumed to put it in a
	// hand.
	public Card getRandomCard() {
		Card c = new Card();
		int randomInt;

		if (!deck1.isEmpty()) {
			randomInt = ranInt(deck1.size() - 1, 0);
			c = deck1.get(randomInt);
			deck1.remove(randomInt);
			deckCount--;
		} else {
			System.out.println("Deck is empty!");
		}

		return c;
	}//end method getRandomCard
	
	//this method is always returning 7 extra cards
	public ArrayList<Card> getRandomCards(int i){
		ArrayList<Card> returnList = new ArrayList<Card>();
		Card c = new Card();
		int randomInt;

		for(int j = 0; j < i; j++) {
		
		if (!deck1.isEmpty()) {
			randomInt = ranInt(deck1.size() - 1, 0);
			c = deck1.get(randomInt);
			returnList.add(c);
			deck1.remove(randomInt);
			deckCount--;
			//System.out.println(j);
			} 
		
		else {
			System.out.println("Deck is empty!");
			}
		} // end for loop
		return returnList;
	} // end getRandomCards method

	public static int ranInt(int max, int min) {
		int returnNum = 0;

		Random rand = new Random();

		returnNum = rand.nextInt((max - min) + 1) + min;

		return returnNum;
	}

	public int getDeckCount() {
		return deckCount;
	}

	public void printDeckCount() {
		System.out.println("There are " + deckCount + " cards left in the deck");
	}

	public boolean isEmpty() {
		boolean b = false;
		if (deckCount == 0) {
			b = true;
		}
		return b;
	}

	public void reshuffle() {
		for (int j = 0; j <= 3; j++) { // j iterates between integer 0 through 3, including 3, to represent color.
			for (int i = 0; i <= 9; i++) { // i iterates between integers 0 through 9, including 9.

				if (i != 0) { // If the card value is not 0, ensure that there are two of each number card for
								// each color.
					deck1.add(newCard(i, colorArray[j]));
					deck1.add(newCard(i, colorArray[j]));
				} // end if
				else { // If the card value is 0, ensure that there is only one 0 card for each color.
					deck1.add(newCard(i, colorArray[j]));
				}

			} // end for (nested)
		} // end outer for
		deckCount = 76;
	}

}// end class deck
