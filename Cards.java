import java.util.ArrayList;

public abstract class Cards extends Card {
	private ArrayList<Card> cardList = new ArrayList<>();

	public abstract String toString();
	
	abstract public void print();

	public ArrayList<Card> getCardList() {
		return cardList;
	}
	
}
