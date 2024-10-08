
public class Card {

	private int value;
	private String color;
	
	private final int[] valueArray = {0,1,2,3,4,5,6,7,8,9}; //0-9
	private final String[] colorArray = {"Red", "Blue", "Green", "Yellow"};
	
	
	public Card() {}
	
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
	
} //end Card Class
