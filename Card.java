// The Card class stores the number and color of each card object in the game.
public class Card {

	private int value;
	private String color;

	private final int[] valueArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }; // 0-9
	private final String[] colorArray = { "Red", "Blue", "Green", "Yellow", "Wild" };
	private final String[] specialtyCardArray = { "Skip", "Reverse", "PlusTwo", "PlusFour" };

	public Card() {
		value = 0;
		color = "";
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
		if (value == 10) {
			return color + " " + specialtyCardArray[0];
		} else if (value == 11) {
			return color + " " + specialtyCardArray[1];
		} else if (value == 12) {
			return color + " " + specialtyCardArray[2];
		} else if (value == 12) {
			return color + " " + specialtyCardArray[3];
		} else {
			return color + " " + value;
		}
	}

	public boolean match(Card c) {
		boolean returnBool = false;

		if (value == c.getValue()) {
			returnBool = true;
		} else if (color == c.getColor()) {
			returnBool = true;
			// Might need to fix this later. We want the Wild card to equal all colors,
			// making it a positive match.
		} else if (color.equalsIgnoreCase("Wild")) {
			returnBool = true;

		}
		return returnBool;
	}

} // end Card Class
