// The Card class stores the number and color of each card object in the game.
public class Card implements Comparable<Card> {

	private int value;
	private String color;

	private final int[] valueArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }; // 0-9
	private final String[] colorArray = { "Red", "Blue", "Green", "Yellow", "Wild" };
	private final String[] specialtyCardArray = { "Skip", "Reverse", "Plus Two", "Card", "Plus Four" };

	public Card() {
		value = 0;
		color = "";
	}
	
	public Card (String color){
		this.color = color;
	}
	
	public Card(int value, String color) {
		this.value = value;
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	public void setValue(int i) {
		value = i;
	}

	public void setColor(String s) {
		color = s;
	}

	public int[] getValueArray() {
		return valueArray;
	}

	public String[] getColorArray() {
		return colorArray;
	}

	public String toString() {
		// SKIP is card number 10, REVERSE is 11, PLUS2 is 12, WILD is 13, WILDPLUS4 is
		// 14.
		if (value == 10) {
			return color + " " + specialtyCardArray[0];
		} else if (value == 11) {
			return color + " " + specialtyCardArray[1];
		} else if (value == 12) {
			return color + " " + specialtyCardArray[2];
		} else if (value == 13) {
			return colorArray[4] + " " + specialtyCardArray[3];
		} else if (value == 14) {
			return colorArray[4] + " " + specialtyCardArray[4];
		} else {
			return color + " " + value;
		}
	}

	public boolean match(Card c) {
		boolean returnBool = false;
		// If the Card is a Wild card, it matches all other colors.
		if (this.getValue() == 13 || this.getValue() == 14) {
			return true;
		} else if (value == c.getValue()) {
			returnBool = true;
		} else if (color == c.getColor()) {
			returnBool = true;
		}
		return returnBool;
	}

	public int compareTo(Card other) {
		// sort by color first
		if (color.equalsIgnoreCase(other.getColor())) {
			return Integer.compare(value, other.getValue());
		} else {
			return color.compareTo(other.getColor());
		}
	}

} // end Card Class
