import java.util.ArrayList;

public class Cards extends Card {
	private ArrayList<Card> cardList = new ArrayList<>();

	@Override
	public String toString() {
		return getColor() + " " + getValue();
	}
	
	public void print() {
		System.out.println(toString());
	}
	
}
