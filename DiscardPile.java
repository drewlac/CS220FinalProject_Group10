import java.util.ArrayList;

public class DiscardPile {

	private static ArrayList<Card> discard1 = new ArrayList<>(76);
	
	public void addCard(Card c) {
		discard1.add(c);
	}
	
	public Card getTopCard() {
		Card c = new Card();
		
		c = discard1.get(discard1.size()-1);
		
		return c;
	}
}
