import java.util.Random;
import java.util.Scanner;


// This is the main class for the game. Once functionality is complete, the player 
// plays one or more games of OverZeroUnderTwo against the CPU.
public class OverZeroUnderTwo {

	public static boolean gameComplete = false;
	public static boolean playerTurn = false;
	public static boolean cpuTurn = false;
	private static Hand playerHand = new Hand(); // static variable for playerHand, placed here so all methods can use it
	private static Hand cpuHand = new Hand(); //static variable for cpuHand, placed here so all methods can use it
	private static Deck gameDeck = new Deck(); //static variable for the deck the game is played on
	private static DiscardPile gamePile = new DiscardPile(); // static variable for game discard pile 
	
	public static void main(String[] args) { // start main method

		
		
		startGame();

		do { // game will be played inside of this do while loop, and to end the game we will
				// need to mark gameComplete as true

			if (playerTurn) {
				playerGameplay();
			} // code for players turn
			else if (cpuTurn) {
				cpuGameplay();
			} // code for cpu's turn
			else {
			} // catch errors and exceptions

		} while (!gameComplete);
		
		System.out.println("\nGame is over!");

	} // end main method

	// startGame() method begins the game and determine which player goes first.
	// The current implementation mimics a coin flip to determine the turn order.
	public static void startGame() {
		System.out.println("Game is starting");
		Random rand = new Random();
		int coinflip = 0;
		coinflip = rand.nextInt(2) + 1;

		if (coinflip == 1) {
			playerTurn = true;
			System.out.println("Player turn is first!");
		} else if (coinflip == 2) {
			cpuTurn = true;
			System.out.println("CPU turn is first!");
		} else {
			System.out.println("Some sorta error, must have got the coinflip wrong :( ");
		} // end else
		
		//adds seven random cards to playerHand
		for(int i = 0; i < 7; i++) {
			playerHand.add(gameDeck.getRandomCard());
		}
		
		
		//adds seven random cards to cpuHand
		for(int i = 0; i < 7; i++) {
			cpuHand.add(gameDeck.getRandomCard());
		}
		
		//adds one random card to discard pile so there is one card to start with
		gamePile.addCard(gameDeck.getRandomCard());
		

	} // end method startGame

	//class Card has a method match() which checks if another card has equal value or color
	public static void playerGameplay() { // use to code player turn
		System.out.println("\nThe top of the card on the pile is:\n" + gamePile.getTopCard() + "\n");
		
		//prints the player's hand
		System.out.println("The player's hand is: \n");
		playerHand.printHand();
		
		System.out.println("The player has " + playerHand.getSize() + " cards. ");
		
		userInput();
		
		if(playerHand.getSize() == 0) {gameComplete = true;}
		playerTurn = false;
		cpuTurn = true;
	} 

	public static void cpuGameplay() {
		//creates a hand of playable cards
		Hand playable = new Hand ();
		
		//checks cards in hand to see if they are playable
		for(int i =0; i<playerHand.getSize();i++) {
			if(playerHand.match(gamePile.getTopCard())) {
				//adds cards to the new hand
				playable.add(playerHand.getCard(i));
			}
		}
		//if the list is 0 cpu draws a card and alerts player
		if(playable.getSize()==0) {
			cpuHand.add(gameDeck.getRandomCard());
			System.out.println("CPU drew a card");
		}
		
		//cpu plays random card from playable hand
		Random random = new Random();
		int cpuPlay = random.nextInt(playable.getSize())+1;
		
		gamePile.addCard(playable.getCard(cpuPlay));
		cpuHand.remove(playable.getCard(cpuPlay));
		
		//if cpu has one card left cpu alerts player
		if(cpuHand.getSize()==1) {
			System.out.println("CPU has one card Left!");
		}
		//if cpu hand is empty game ends
		if (cpuHand.getSize()== 0) {
			gameComplete = true;
		}
		
		//cpu ends turn
		cpuTurn = false;
		playerTurn = true;
		
		//forgot to actually play cards 
		
	} // use to code CPU turn

	private static void userInput() {
		System.out.println("If you would like to FORFEIT, type F. If you would like to"
				+ " place a card, type the card you want to place. (Ex. Blue 5 or B5)");
		Scanner input = new Scanner(System.in);
		//Print hand information
		
		String choice = input.nextLine();
		// If the user types d or D, allow the user to draw a card.
		if (choice.equalsIgnoreCase("F")) {
			// Enter code to print that game is complete
			gameComplete = true;
		}
		// (Write this else...if later. Check the user input to determine if it matches
		// a card that he or she currently has. Possibly convert choice to lower case,
		// then compare result with current hand. Should we allow user to enter shortened
		// card entry, like B5 for blue five, as well as longer entry?)
		

		//check if card matches
		
		//valid if user can play card, otherwise print only cards they can play
		
		//if user can't play a card, make them draw a card. 
		
		input.close();
	} // user inputs
	
	

} // end OverZeroUnderTwo Class
