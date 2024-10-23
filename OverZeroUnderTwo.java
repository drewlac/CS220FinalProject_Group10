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
	} // end method startGame

	public static void playerGameplay() {
	} // use to code player turn

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
