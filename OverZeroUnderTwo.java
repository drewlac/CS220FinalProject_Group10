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
			System.out.println("Some sorta error, must have got the coinflip wrong");
		} // end else
		
		//adds seven random cards to playerHand
		playerHand.add(gameDeck.getRandomCards(7));
		
		//adds seven random cards to cpuHand
		cpuHand.add(gameDeck.getRandomCards(7));
		
		//adds one random card to discard pile so there is one card to start with
		gamePile.addCard(gameDeck.getRandomCard());
		
		//player hand is getting 14 cards instead of 7, currently investigating
		System.out.println("The player has " + playerHand.getSize() + " cards. ");
		
	} // end method startGame

	//class Card has a method match() which checks if another card has equal value or color
	public static void playerGameplay() { // use to code player turn
		System.out.println("\nThe top of the card on the pile is:\n " + gamePile.getTopCard() + "\n");
		System.out.println("The player's hand is: \n");
		playerHand.printHand();
		
		System.out.println("The player has " + playerHand.getSize() + " cards. ");
		
		userInput();
		
		if(playerHand.getSize() == 0) {gameComplete = true;}
		playerTurn = false;
		cpuTurn = true;
	} 

	public static void cpuGameplay() {
	} // use to code CPU turn

	private static void userInput() {
		System.out.println("If you would like to DRAW, press D. If you would like to"
				+ " place a card, type the card you want to place. (Ex. Blue 5 or B5)");
		Scanner input = new Scanner(System.in);
		String choice = input.nextLine();
		// If the user types d or D, allow the user to draw a card.
		if (choice.equalsIgnoreCase("d")) {
			// (Put the code here to add a card to hand.)
			gameComplete = true;
		}
		// (Write this else...if later. Check the user input to determine if it matches
		// a card that he or she currently has. Possibly convert choice to lower case,
		// then compare result with current hand. Should we allow user to enter shortened
		// card entry, like B5 for blue five, as well as longer entry?)
		else if (choice.equalsIgnoreCase("")) {

		} else {
			// (Handle errors here.)
		}

		input.close();
	} // user inputs
	
	

} // end OverZeroUnderTwo Class
