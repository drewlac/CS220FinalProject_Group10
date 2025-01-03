import java.util.ArrayList;
import java.util.Random;

// The Deck class functions as the draw pile for the game. That is, this class
// assembles each card within the game into a draw pile, or deck.
public class Deck extends Cards{

	private ArrayList<Card> deck1 = new ArrayList<>(108);
	private final String[] colorArray = { "Red", "Blue", "Green", "Yellow", "Wild" };
	private int deckCount;

	public Deck() { // automatically creates a default deck with standard cards
		for (int j = 0; j <= 3; j++) { // j iterates between integer 0 through 3, including 3,
			// to represent color.
			for (int i = 0; i <= 14; i++) { // i iterates between integers 0 through 14, including 14.

				// If i is not a 0 card, ensure that there are two of each number card for each
				// color.
				if (i != 0 && i <= 9) {
					deck1.add(newCard(i, colorArray[j]));
					deck1.add(newCard(i, colorArray[j]));
				} // end if
					// If i is greater than 9, it begins to assign specialty cards. 2 of each color,
					// except +4 cards.
					// SKIP is card number 10, REVERSE is 11, PLUS2 is 12, WILD is 13, WILDPLUS4 is 14.
				else if (i > 9 && i < 13) {
					deck1.add(newCard(i, colorArray[j]));
					deck1.add(newCard(i, colorArray[j]));
				}
				// If i is a WILD card, only add four of each.
				else if (i >= 13 && i <= 14) {
					deck1.add(newCard(i, colorArray[4]));
				}
				// If i is a 0 card, only add one 0 card per color.
				else {
					deck1.add(newCard(i, colorArray[j]));
				}

			} // end for (nested)
		} // end outer for
		deckCount = 108;

	}// end constructor
	
	@Override
	public String toString() { 
		String returnString = "Deck:\n";
		for(Card c : deck1) {
			returnString += c.toString() + "\n";
		}
		return returnString;
	}
	
	@Override
	public void print() {
		System.out.println(this.toString());
	} 

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
			for (int i = 0; i <= 14; i++) { // i iterates between integers 0 through 14, including 14.

				if (i != 0) { // If the card value is not 0, ensure that there are two of each number card for
								// each color.
					deck1.add(newCard(i, colorArray[j]));
					deck1.add(newCard(i, colorArray[j]));
				} else if (i > 9 && i < 13) { // adds specialty cards to re-shuffle
					deck1.add(newCard(i, colorArray[j]));
					deck1.add(newCard(i, colorArray[j]));
				} // end if
				else if (i >= 13 && i <= 14){
					deck1.add(newCard(i, colorArray[4]));
				}
				else { // If the card value is 0, ensure that there is only one 0 card for each color.
					deck1.add(newCard(i, colorArray[j]));
				}

			} // end for (nested)
		} // end outer for
		deckCount = 108;
	}
	
	public int getSize() {
		return deck1.size();
	}

}// end class deck
