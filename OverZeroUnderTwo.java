
//import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

// This is the main class for the game. Once functionality is complete, the player 
// plays one or more games of OverZeroUnderTwo against the CPU.
public class OverZeroUnderTwo {

	private static boolean gameComplete = false;
	private static boolean playerTurn = false;
	private static boolean cpuTurn = false;
	private static Hand playerHand = new Hand(); // static variable for playerHand, placed here so all methods can use
													// it
	private static Hand cpuHand = new Hand(); // static variable for cpuHand, placed here so all methods can use it
	private static Deck gameDeck = new Deck(); // static variable for the deck the game is played on
	private static DiscardPile gamePile = new DiscardPile(); // static variable for game discard pile

	public static void main(String[] args) { // start main method

		startGame();

		Scanner input = new Scanner(System.in);

		do { // game will be played inside of this do while loop, and to end the game we will
				// need to mark gameComplete as true

			// make sure there are cards in gamedeck
			if (gameDeck.isEmpty()) {
				gameDeck.reshuffle();
			}

			if (playerTurn) {
				playerGameplay(input);
			} // code for players turn
			else if (cpuTurn) {
				cpuGameplay();
			} // code for cpu's turn
			else {
			} // catch errors and exceptions

		} while (!gameComplete);

		System.out.println("\nGame is over!");
		input.close();
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

		// adds seven random cards to playerHand
		for (int i = 0; i < 7; i++) {
			playerHand.add(gameDeck.getRandomCard());
		}

		// adds seven random cards to cpuHand
		for (int i = 0; i < 7; i++) {
			cpuHand.add(gameDeck.getRandomCard());
		}

		// adds one random card to discard pile so there is one card to start with
		gamePile.addCard(gameDeck.getRandomCard());

	} // end method startGame

	// class Card has a method match() which checks if another card has equal value
	// or color
	public static void playerGameplay(Scanner input) { // use to code player turn
		System.out.println("\nThe top of the card on the pile is: " + gamePile.getTopCard() + "\n");
		
		//prints the player's hand
		//add function to sort player's hand
		System.out.println("The player's hand is:");
		playerHand.printHand();
		
		System.out.println("\nThe player has " + playerHand.getSize() + " cards. ");
		
		//checking if any cards match
		int matches = 0;
		for(int i = 0; i < playerHand.getSize(); i++) {
			if(playerHand.getCard(i).match(gamePile.getTopCard())) {
				matches++;
			}
		} //if there are matches, getting user input
		if(matches > 0) {
			userInput(input);
		}
		else { //if there are no matches, drawing one card
			System.out.println("You have no matches so you drew 1 card");
			playerHand.add(gameDeck.getRandomCard());
		}
		
		if(playerHand.getSize() == 1) {
			System.out.print("Any additional information? ");
			String answer = input.nextLine();
			if(answer.strip().equalsIgnoreCase("uno")) {
				System.out.println("You successfully called uno!");
			}
			else if (!answer.strip().equalsIgnoreCase("uno") || answer.isEmpty()) {
				System.out.println("You forgot to call uno, drawing another card!");
				playerHand.add(gameDeck.getRandomCard());
			}
		}
		
		//ends game if player's hand is empty
		if(playerHand.getSize() == 0) {
			gameComplete = true;
			System.out.println("You win!");
		}
		
		// if value is NOT equal to 10 (skip), then code runs.
		// if it IS equal, code does not run, and the opposing player's next turn is skipped.
		if (gamePile.getTopCard().getValue() != 10) {
		//switch turns and wait 1 second
		playerTurn = false;
		cpuTurn = true;
		}
		// If you played a skip card and one or more of your cards matches the top card.
		if (gamePile.getTopCard().getValue() == 10 && matches > 0) {
			System.out.println("You skipped CPU's turn.");
		}
		
		try {
			Thread.sleep(1000);
		}
		catch(InterruptedException c) {c.printStackTrace();}
	}

	public static void cpuGameplay() {
		// creates a hand of playable cards
		int playableCount = 0;

		// checks cards in hand to see if they are playable
		for (int i = 0; i < cpuHand.getSize(); i++) {
			if (cpuHand.getCard(i).match(gamePile.getTopCard())) {
				// adds cards to the new hand
				playableCount++;
			}
		}

		// System.out.println("Playable Count: " + playableCount); //used for
		// troubleshooting

		boolean cardPlayed = false;
		Random random = new Random();
		double coinflip;

		while (true) {
			if (cpuHand.getSize() == 0) { // if CPU has no cards, end game
				gameComplete = true;
				System.out.println("CPU Wins!");
				break;
			} // end if

			// if the list is 0 cpu draws a card and alerts player
			else if (playableCount <= 0) {
				cpuHand.add(gameDeck.getRandomCard());
				System.out.println("CPU drew a card");
				break;
			} // end else if

			else {
				// cpu flips a coin to see if they play each playable card
				for (int i = 0; i < cpuHand.getSize(); i++) {
					coinflip = random.nextDouble();
					// System.out.println("Coinflip: " + coinflip); //used for troubleshooting
					if (cpuHand.getCard(i).match(gamePile.getTopCard()) && coinflip < 0.5) {// if cards match and
																							// coinflip is good
						gamePile.addCard(cpuHand.getCard(i)); // play card to game pile
						System.out.println("Cpu played: " + cpuHand.getCard(i)); // say which card cpu plyaed
						cpuHand.remove(cpuHand.getCard(i));// remove it from cpuHand
						if (cpuHand.getSize() == 1) {
							System.out.println("CPU: Uno!");
						} // print out uno if only one card left
						cardPlayed = true;
						break;
					} // end if
				} // end for loop

				if (cardPlayed) {
					break;
				}

			} // end else
		} // end while

		// if value is NOT equal to 10 (skip), then code runs.
		// if it IS equal, code does not run, and the opposing player's next turn is
		// skipped.
		if (gamePile.getTopCard().getValue() != 10) {
			// CPU ends turn, happens regardless. Switches turn and waits 1 second
			cpuTurn = false;
			playerTurn = true;
		}

		if (gamePile.getTopCard().getValue() == 10) {
			System.out.println("CPU skipped your turn.");
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

	} // use to code CPU turn

	private static void userInput(Scanner input) {

		// get user input for next int
		String choice;
		int selection;

		while (true) {
			try {
				System.out.print("Type the number of the card you would like to play (Enter F to forfeit): ");
				choice = input.nextLine();

				// If the user types d or D, allow the user to draw a card.
				if (choice.strip().equalsIgnoreCase("F")) {
					// Enter code to print that game is complete
					gameComplete = true;
					break;
				} // end if

				// to prevent NumberFormatException
				else if (choice.isEmpty()) {
					choice = input.next();
				}

				// switching input to a string
				selection = Integer.parseInt(choice);

				// Number must be greater than zero and no greater than the size of the hand
				if (selection < 0 || selection > playerHand.getSize()) {
					throw new RuntimeException();
				} else {
					if (playerHand.getCard(selection - 1).match(gamePile.getTopCard())) {// if card matches top card
						// Playing the selected card
						gamePile.addCard(playerHand.getCard(selection - 1));
						System.out.println("You played: " + playerHand.getCard(selection - 1));
						playerHand.remove(playerHand.getCard(selection - 1));
						break;
					} // end nested if

					else {
						System.out.println(
								"Card does not match the color or value of the top card. Please select another card");
					} // end nested else
				} // end else
			} // end try
			catch (RuntimeException e) {
				System.out.println("Answer must be an integer between 1 and your max amount of cards!\n");
			} // end catch
		} // end while loop


	} // user inputs

} // end OverZeroUnderTwo Class
