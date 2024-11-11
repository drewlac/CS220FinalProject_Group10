import java.util.ArrayList;

// The purpose of this class is to store the top card of the discard pile. Once
// a new card is discarded, the top card is now that card.
public class DiscardPile extends Cards{

	// discard1 ArrayList holds up to the total amount of cards in the deck.
	private static ArrayList<Card> discard1 = new ArrayList<>(76);

	// addCard() method adds a card to the discard1 ArrayList.
	public void addCard(Card c) {
		discard1.add(c);
	}

	// getTopCard() method is used to retrieve the top card of the discard pile.
	public Card getTopCard() {
		Card c = new Card();

		c = discard1.get(discard1.size() - 1);

		return c;
	}
	
	@Override
	public void print() {}
	
	@Override
	public String toString() {
		return "";
	}
}
