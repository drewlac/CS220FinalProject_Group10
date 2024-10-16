import java.util.ArrayList;
import java.util.Random;


public class Deck {

private static ArrayList<Card> deck1 = new ArrayList<>(40);
private final String[] colorArray = {"Red", "Blue", "Green", "Yellow"};
private int deckCount;

public Deck() { //automatically creates a default deck with standard cards
	for(int j = 0; j <= 3; j++) { // i iterates between integer 0 thru 3 including 3 to represent color
	for(int i = 0; i <= 9; i++) { //i iterates between integers 0 thru 9 including 9
	
	deck1.add(newCard(i,colorArray[j]));
		
	} 
	
	deckCount = 40;
}
	
	
}//end construct

private Card newCard(int i, String string) {
	return new Card(i, string);
}

public ArrayList<Card> getDeck(){
	return deck1;
}	

public void printDeck() {
	
	for(int i = 0; i < deck1.size(); i++) {
		deck1.get(i).toString2();
	}
}

public Card getRandomCard() { //returns random card and removes it from the deck, presumable to put it in a hand
	Card c = new Card();
	int randomInt; 
	
	if(!deck1.isEmpty()) {
	randomInt = ranInt(deck1.size()-1,0);
	c = deck1.get(randomInt);
	deck1.remove(randomInt);
	deckCount--;
	}
	else {System.out.println("Deck is empty!");}
	
	return c;
}

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

	
}//end class deck
