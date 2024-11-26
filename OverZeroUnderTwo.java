
//import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

// This is the main class for the game. Once functionality is complete, the player 
// plays one or more games of OverZeroUnderTwo against the CPU.
public class OverZeroUnderTwo {

	private static boolean gameComplete = false;
	private static boolean playerTurn = false;
	private static boolean cpu1Turn = false;
	private static boolean cpu2Turn = false;
	private static boolean cpu3Turn = false;
	private static byte gameFlow = 1; // 1 for clockwise, 2 for counter clockwise
	private static Deck gameDeck = new Deck(); // static variable for the deck the game is played on
	private static DiscardPile gamePile = new DiscardPile(); // static variable for game discard pile

	public static void main(String[] args) { // start main method

		Scanner input = new Scanner(System.in); // initializing scanner object

		// generate hands that the methods use
		Hand playerHand = new Hand();
		Hand cpu1Hand = new Hand();

		// Party Rules Add On
		System.out.println("Optional Party Rules:");

		System.out.print("7-0 Rule (Y/N): ");
		boolean sevenZero = getYesNo(input);

		System.out.print("\nNo Bluffing (Y/N): ");
		boolean bluffing = getYesNo(input);

		System.out.print("\nStacking Rule (Y/N): ");
		boolean stacking = getYesNo(input);

		// used to pass these values into methods
		boolean[] partyRules = { sevenZero, bluffing, stacking };
		/*
		 * partyRules[0] = sevenZero rule partyRules[1] = No bluffing rule partyRules[2]
		 * = stacking rule
		 */

		// Initializing variables used by game
		byte gameMode;
		boolean singleCPU = true;
		// boolean playAgain = true;

		// For determining game mode
		System.out.println("\nOption 1: Play against 1 CPU: ");
		System.out.println("Option 2: Play against 3 CPUs:");
		System.out.print("Please enter 1 or 2 to select your option: ");

		while (true) { // getting input for gameMode
			try {
				gameMode = Byte.parseByte(input.next());
				if (!(gameMode == 1 || gameMode == 2)) {
					throw new RuntimeException();
				}
				break;
			} catch (RuntimeException E) {
				System.out.println("Error, input must be 1 or 2. Enter again: ");
			}
		}

		if (gameMode == 2) { // switch to 3 CPU Mode
			singleCPU = false;
		}

		if (singleCPU) {
			singleCPU(input, playerHand, cpu1Hand, partyRules);
		} else {
			Hand cpu2Hand = new Hand();
			Hand cpu3Hand = new Hand();
			threeCPU(input, playerHand, cpu1Hand, cpu2Hand, cpu3Hand, partyRules);
		}

		System.out.println("\nGame is over!");
		input.close();
	} // end main method

	public static void startGame1CPU(Hand playerHand, Hand cpu1Hand) {
		System.out.println("Game is starting");
		Random rand = new Random();
		int coinflip = 0;
		coinflip = rand.nextInt(2) + 1;

		if (coinflip == 1) {
			playerTurn = true;
			System.out.println("Player turn is first!");
		} else if (coinflip == 2) {
			cpu1Turn = true;
			System.out.println("CPU turn is first!");
		} else {
			System.out.println("Some sorta error, must have got the coinflip wrong :( ");
		} // end else

		resetGame1CPU(playerHand, cpu1Hand);// resets game since it can be played several times

		// adds seven random cards to playerHand
		for (int i = 0; i < 7; i++) {
			playerHand.add(gameDeck.getRandomCard());
		}

		// adds seven random cards to cpuHand
		for (int i = 0; i < 7; i++) {
			cpu1Hand.add(gameDeck.getRandomCard());
		}

		// adds one random card to discard pile so there is one card to start with
		gamePile.addCard(gameDeck.getRandomCard());

		// gamePile.addCard(new Card(13, "Wild")); //Tester code to see what happens
		// when
		// a Wild card is the first card placed on the discard pile in a game.
		// gamePile.addCard(new Card(14, "Wild"));
		// If the top card at the beginning of the game is a Wild card, randomly assign
		// a color value to the top card.
		if (gamePile.getTopCard().getValue() == 13 || gamePile.getTopCard().getValue() == 14) {
			int colorChoice = 0;
			Random rand1 = new Random();
			colorChoice = rand1.nextInt((4 - 1) + 1) + 1; // Generate random number between 1 and 4.
			System.out.println("The first card of the game was a Wild or Wild Plus Four card.");
			if (colorChoice == 1) {
				System.out.println("The first card is now a Red Card.\n");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 2) {
				System.out.println("The first card is now a Blue Card.\n");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 3) {
				System.out.println("The first card is now a Green Card.\n");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 4) {
				System.out.println("The first card is now a Yellow Card.\n");
				gamePile.addCard(new Card("Yellow"));
			}
		}
	} // End startGame1CPU method.

	private static void resetGame1CPU(Hand playerHand, Hand cpu1Hand) {
		playerHand.removeAllCards();
		cpu1Hand.removeAllCards();
		gameDeck.reshuffle();
	} // End resetGame1CPU method.
	
	// Use to code player turn.
	public static void playerGameplay1CPU(Scanner input, Hand playerHand, Hand cpuHand, boolean[] partyRules) {
		System.out.println("\nThe top card on the pile is: " + gamePile.getTopCard() + "\n");

		// Permanently add a Wild Plus Four to player hand every turn for testing
		// purposes.
		// playerHand.add(new Card(14, "Wild"));
		// prints the player's hand
		// add function to sort player's hand
		System.out.println("The player's hand is:");
		playerHand.sort();
		playerHand.printHand();

		System.out.println("\nThe player has " + playerHand.getSize() + " cards. ");

		// checking if any cards match
		int matches = 0;
		for (int i = 0; i < playerHand.getSize(); i++) {
			if (playerHand.getCard(i).match(gamePile.getTopCard())) {
				matches++;
			}
		} // if there are matches, getting user input *** PLAYER PLAYS CARD HERE ***
		if (matches > 0) {
			userInput(input, playerHand);
		} else { // if there are no matches, drawing one card
			System.out.println("You have no matches so you drew 1 card");
			playerHand.add(gameDeck.getRandomCard());
		}

		if (playerHand.getSize() == 1) {
			System.out.print("Any additional information? ");
			String answer = input.next();
			if (answer.strip().equalsIgnoreCase("uno")) {
				System.out.println("You successfully called uno!");
			} else if (!answer.strip().equalsIgnoreCase("uno") || answer.isEmpty()) {
				System.out.println("You forgot to call uno, drawing another card!");
				playerHand.add(gameDeck.getRandomCard());
			}
		}

		// ends game if player's hand is empty *** CATCH AFTER PLAYER HAS PLAYED Card
		if (playerHand.getSize() == 0) {
			gameComplete = true;
			System.out.println("You win!");
		}

		// reverse
		else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && matches > 0) {
			gameFlow = 2;
			System.out.println("The game order was switched to counter-clockwise.");
		} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && matches > 0) {
			gameFlow = 1;
			System.out.println("The game order was switched to clockwise.");
		}

		// *** Wild card ***
		else if (gamePile.getTopCard().getValue() == 13 && matches > 0) {
			while (true) {
				try {
					int colorChoice = 0;
					System.out.println("1: Red\n2: Blue\n3: Green\n4: Yellow");
					System.out.print("\nEnter a color number (1-4): ");
					input.nextLine();
					colorChoice = input.nextInt();
					if (colorChoice == 1) {
						System.out.println("You played a Red Wild card.");
						gamePile.addCard(new Card("Red"));
						break;
					} else if (colorChoice == 2) {
						System.out.println("You played a Blue Wild card.");
						gamePile.addCard(new Card("Blue"));
						break;
					} else if (colorChoice == 3) {
						System.out.println("You played a Green Wild card.");
						gamePile.addCard(new Card("Green"));
						break;
					} else if (colorChoice == 4) {
						System.out.println("You played a Yellow Wild card.");
						gamePile.addCard(new Card("Yellow"));
						break;
					} else {
						System.out.print("\nEnter a valid color number (1-4): ");
						colorChoice = input.nextInt();
						if (colorChoice == 1) {
							System.out.println("You played a Red Wild card.");
							gamePile.addCard(new Card("Red"));
							break;
						} else if (colorChoice == 2) {
							System.out.println("You played a Blue Wild card.");
							gamePile.addCard(new Card("Blue"));
							break;
						} else if (colorChoice == 3) {
							System.out.println("You played a Green Wild card.");
							gamePile.addCard(new Card("Green"));
							break;
						} else if (colorChoice == 4) {
							System.out.println("You played a Yellow Wild card.");
							gamePile.addCard(new Card("Yellow"));
							break;
						}
					} // End else
				} catch (InputMismatchException e) {
					System.out.print("\nAnswer must be an integer between 1 and 4!\n");
				} // end catch
			} // End while
		} // End if *** END WILD CARD ***

		// if value is NOT equal to 10 (skip), then code runs.
		// if it IS equal, code does not run, and the opposing player's next turn is
		// skipped.
		// If you played a skip card and one or more of your cards matches the top card,
		// print the skip statement.
		// NOTE: This skip statement shows up if the last card played before winning is
		// a skip card.
		else if (gamePile.getTopCard().getValue() == 10 && matches > 0 && gameComplete == false) {
			System.out.println("You skipped CPU's turn.");
			cpu1Turn = false;
			playerTurn = true;
		} // do nothing if skip cpu

		// Player plays a plus2 card. Force CPU to draw 2 random cards.
		else if (gamePile.getTopCard().getValue() == 12 && playerTurn && matches > 0) {
			cpuHand.add(gameDeck.getRandomCard());
			cpuHand.add(gameDeck.getRandomCard());

			System.out.println("CPU 1 drew 2 cards and their turn was skipped.");
		}
		// Player plays a Wild Plus Four card. Change the color to one of the four
		// colors, add four cards to CPU's hand, and skip CPU's turn. Keep asking for
		// valid input of 1-4 if the user enters a wrong number or input type.
		else if (gamePile.getTopCard().getValue() == 14 && playerTurn && matches > 0) {
			while (true) {
				try {
					int colorChoice = 0;
					System.out.println("1: Red\n2: Blue\n3: Green\n4: Yellow");
					System.out.print("\nEnter a color number (1-4): ");
					input.nextLine();
					colorChoice = input.nextInt();
					if (colorChoice == 1) {
						System.out.println("You played a Red Wild Plus Four card.");
						gamePile.addCard(new Card("Red"));
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 2) {
						System.out.println("You played a Blue Wild Plus Four card.");
						gamePile.addCard(new Card("Blue"));
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 3) {
						System.out.println("You played a Green Wild Plus Four card.");
						gamePile.addCard(new Card("Green"));
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 4) {
						System.out.println("You played a Yellow Wild Plus Four card.");
						gamePile.addCard(new Card("Yellow"));
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						cpuHand.add(gameDeck.getRandomCard());
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else {
						System.out.print("\nEnter a valid color number (1-4): ");
						colorChoice = input.nextInt();
						if (colorChoice == 1) {
							System.out.println("You played a Red Wild Plus Four card.");
							gamePile.addCard(new Card("Red"));
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							break;
						} else if (colorChoice == 2) {
							System.out.println("You played a Blue Wild Plus Four card.");
							gamePile.addCard(new Card("Blue"));
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							break;
						} else if (colorChoice == 3) {
							System.out.println("You played a Green Wild Plus Four card.");
							gamePile.addCard(new Card("Green"));
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							break;
						} else if (colorChoice == 4) {
							System.out.println("You played a Yellow Wild Plus Four card.");
							gamePile.addCard(new Card("Yellow"));
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							cpuHand.add(gameDeck.getRandomCard());
							System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							break;
						}
					} // End else
				} catch (InputMismatchException e) {
					System.out.print("\nAnswer must be an integer between 1 and 4!\n");
				} // end catch
			} // End while
		}
		if (!(gamePile.getTopCard().getValue() == 10)) { // no skip played, switch turns
			// switch turns and wait 1 second
			if (gameFlow == 1) { // clockwise
				playerTurn = false;
				cpu1Turn = true;
			} else if (gameFlow == 2) { // counter clockwise
				playerTurn = false;
				cpu1Turn = true;
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}
	} // End playerGameplay1CPU method.

	public static void cpu1Gameplay1CPU(Hand cpuHand, Hand playerHand, boolean[] partyRules) {
		// creates a hand of playable cards
		int playableCount = 0;
		// cpuHand.add(new Card(14, "Wild")); // Added tester code so the CPU always has
		// a
		// Wild Draw Four card.
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
				System.out.println("CPU 1 Wins!");
				break;
			} // end if

			// if the list is 0 cpu draws a card and alerts player
			else if (playableCount <= 0) {
				cpuHand.add(gameDeck.getRandomCard());
				System.out.println("CPU 1 drew a card");
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
						System.out.println("CPU 1 played: " + cpuHand.getCard(i)); // say which card cpu played
						cpuHand.remove(cpuHand.getCard(i));// remove it from cpuHand
						if (cpuHand.getSize() == 1) {
							System.out.println("CPU 1: Uno!");
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

		// Wild card
		if (gamePile.getTopCard().getValue() == 13 && playableCount > 0) {
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt((4 - 1) + 1) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			if (colorChoice == 1) {
				System.out.println("CPU 1 played a Red Wild Card.");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 2) {
				System.out.println("CPU 1 played a Blue Wild Card.");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 3) {
				System.out.println("CPU 1 played a Green Wild Card.");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 4) {
				System.out.println("CPU 1 played a Yellow Wild Card.");
				gamePile.addCard(new Card("Yellow"));
			}
		}

		// reverse
		if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && playableCount > 0) {
			gameFlow = 2;
			System.out.println("The game order was switched to counter-clockwise.");
		} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && playableCount > 0) {
			gameFlow = 1;
			System.out.println("The game order was switched to clockwise.");
		}

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 1 Wins!");
		}

		// if value is NOT equal to 10 (skip), then code runs.
		// if it IS equal, code does not run, and the opposing player's next turn is
		// skipped.

		else if (gamePile.getTopCard().getValue() == 10 && playableCount > 0) {
			System.out.println("CPU skipped your turn.");
		}

		else if (gamePile.getTopCard().getValue() == 12 && playableCount > 0) { // plus2
			// Forces player to draw 2 random cards.
			playerHand.add(gameDeck.getRandomCard());
			playerHand.add(gameDeck.getRandomCard());

			System.out.println("You drew 2 cards and your turn was skipped.");
		}
		// CPU plays Wild Plus Four card. It then randomly chooses a color, forces the
		// player to draw 4 cards, and skips the player's turn.
		else if (gamePile.getTopCard().getValue() == 14 && playableCount > 0) {
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt((4 - 1) + 1) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			if (colorChoice == 1) {
				System.out.println("CPU 1 played a Red Wild Draw Four Card.");
				gamePile.addCard(new Card("Red"));
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 2) {
				System.out.println("CPU 1 played a Blue Wild Draw Four Card.");
				gamePile.addCard(new Card("Blue"));
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 3) {
				System.out.println("CPU 1 played a Green Wild Draw Four Card.");
				gamePile.addCard(new Card("Green"));
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 4) {
				System.out.println("CPU 1 played a Yellow Wild Draw Four Card.");
				gamePile.addCard(new Card("Yellow"));
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				playerHand.add(gameDeck.getRandomCard());
				System.out.println("You drew 4 cards and your turn was skipped.");
			}
		} else { // no skip played, switch turns
					// cpu ends turn, happens regardless. Switches turn and waits 1 second
			if (gameFlow == 1) { // clockwise
				cpu1Turn = false;
				playerTurn = true;
			} else if (gameFlow == 2) { // counter clockwise
				cpu1Turn = false;
				playerTurn = true;
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}
		// Test code to see how many cards CPU has.
		System.out.println("CPU has " + cpuHand.getSize() + " cards.");
	} // End cpu1Gameplay1CPU method.

	public static void singleCPU(Scanner input, Hand playerHand, Hand cpu1Hand, boolean[] partyRules) {

		boolean playAgain = true;

		while (playAgain) {

			startGame1CPU(playerHand, cpu1Hand); // method to start game

			do { // game will be played inside of this do while loop, and to end the game we will
					// need to mark gameComplete as true

				// make sure there are cards in gamedeck
				if (gameDeck.isEmpty()) {
					gameDeck.reshuffle();
				}

				if (playerTurn && gameComplete == false) {
					playerGameplay1CPU(input, playerHand, cpu1Hand, partyRules);
				} // code for players turn
				else if (cpu1Turn && gameComplete == false) {
					cpu1Gameplay1CPU(cpu1Hand, playerHand, partyRules);
				} // code for cpu's turn
				else {
				} // catch errors and exceptions

			} while (!gameComplete);

			playAgain = againChecker(input);

		}

	} // End singleCPU method.

	// startGame() method begins the game and determine which player goes first.
	// The current implementation mimics a coin flip to determine the turn order.
	public static void startGame3CPU(Hand playerHand, Hand cpu1Hand, Hand cpu2Hand, Hand cpu3Hand) {
		System.out.println("\nGame is starting");
		Random rand = new Random();
		int coinflip = 0;
		coinflip = rand.nextInt(4) + 1;

		if (coinflip == 1) {
			playerTurn = true;
			System.out.println("Player turn is first!");
		} else if (coinflip == 2) {
			cpu1Turn = true;
			System.out.println("CPU 1 turn is first!");
		} else if (coinflip == 3) {
			cpu2Turn = true;
			System.out.println("CPU 2 turn is first!");
		} else if (coinflip == 4) {
			cpu3Turn = true;
			System.out.println("CPU 3 turn is first!");
		} else {
			System.out.println("Some sorta error, must have got the coinflip wrong :( ");
		} // end else

		resetGame3CPU(playerHand, cpu1Hand, cpu2Hand, cpu3Hand);// resets game since it can be played several times

		// adds seven random cards to playerHand
		for (int i = 0; i < 7; i++) {
			playerHand.add(gameDeck.getRandomCard());
		}

		// adds seven random cards to cpu1Hand
		for (int i = 0; i < 7; i++) {
			cpu1Hand.add(gameDeck.getRandomCard());
		}

		// adds seven random cards to cpu2Hand
		for (int i = 0; i < 7; i++) {
			cpu2Hand.add(gameDeck.getRandomCard());
		}

		// adds seven random cards to cpu3Hand
		for (int i = 0; i < 7; i++) {
			cpu3Hand.add(gameDeck.getRandomCard());
		}

		// adds one random card to discard pile so there is one card to start with
		gamePile.addCard(gameDeck.getRandomCard());

	} // end method startGame

	public static void resetGame3CPU(Hand playerHand, Hand cpu1Hand, Hand cpuHand2, Hand cpuHand3) {

		playerHand.removeAllCards();
		cpu1Hand.removeAllCards();
		cpuHand2.removeAllCards();
		cpuHand3.removeAllCards();
		gameDeck.reshuffle();
	} // End resetGame3CPU method.

	public static void playerGameplay3CPU(Scanner input, Hand playerHand) { // use to code player turn
		System.out.println("\nThe top card on the pile is: " + gamePile.getTopCard() + "\n");

		// prints the player's hand
		// add function to sort player's hand
		System.out.println("The player's hand is:");
		playerHand.sort();
		playerHand.printHand();

		System.out.println("\nThe player has " + playerHand.getSize() + " cards. ");

		// checking if any cards match
		int matches = 0;
		for (int i = 0; i < playerHand.getSize(); i++) {
			if (playerHand.getCard(i).match(gamePile.getTopCard())) {
				matches++;
			}
		} // if there are matches, getting user input
		if (matches > 0) {
			userInput(input, playerHand);
		} else { // if there are no matches, drawing one card
			System.out.println("You have no matches so you drew 1 card");
			playerHand.add(gameDeck.getRandomCard());
		}

		if (playerHand.getSize() == 1) {
			System.out.print("Any additional information? ");
			String answer = input.next();
			if (answer.strip().equalsIgnoreCase("uno")) {
				System.out.println("You successfully called uno!");
			} else if (!answer.strip().equalsIgnoreCase("uno") || answer.isEmpty()) {
				System.out.println("You forgot to call uno, drawing another card!");
				playerHand.add(gameDeck.getRandomCard());
			}
		}

		// ends game if player's hand is empty
		if (playerHand.getSize() == 0) {
			gameComplete = true;
			System.out.println("You win!");
		}

		boolean skipped = false;
		if (gamePile.getTopCard().getValue() == 10)
			skipped = true;

		// switch turns and wait 1 second
		if (gameFlow == 1) { // clockwise
			playerTurn = false;
			if (skipped) // if skip is played
				cpu2Turn = true;
			else // regular gameplay
				cpu1Turn = true;
		} else if (gameFlow == 2) { // counter clockwise
			playerTurn = false;
			if (skipped) // if skip is played
				cpu2Turn = true;
			else // regular gameplay
				cpu3Turn = true;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}
	} // End playerGameplay3CPU method.

	public static void cpu1Gameplay3CPU(Hand cpuHand) {
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
				System.out.println("CPU 1 Wins!");
				break;
			} // end if

			// if the list is 0 cpu draws a card and alerts player
			else if (playableCount <= 0) {
				cpuHand.add(gameDeck.getRandomCard());
				System.out.println("CPU 1 drew a card");
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
						System.out.println("CPU 1 played: " + cpuHand.getCard(i)); // say which card cpu played
						cpuHand.remove(cpuHand.getCard(i));// remove it from cpuHand
						if (cpuHand.getSize() == 1) {
							System.out.println("CPU 1: Uno!");
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

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 1 Wins!");
		}

		// skip code for CLOCKWISE order
		if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu1Turn == true && gameFlow == 1) {
			cpu2Turn = false;
			cpu3Turn = true;
			// cpu2Turn = true;
			System.out.println("cpu2 turn skipped");
		} else if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu2Turn == true) {
			cpu3Turn = false;
			playerTurn = true;
			// cpu3Turn = true;
			System.out.println("cpu3 turn skipped");
		} else if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu3Turn == true) {
			playerTurn = false;
			cpu1Turn = true;
			// playerTurn = true;
			System.out.println("your turn skipped");
		}

		// skip code for COUNTER-CLOCKWISE order
		if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu1Turn == true && gameFlow == 2) {
			cpu1Turn = false;
			playerTurn = true;
			System.out.println("CPU2 skipped CPU1's turn.");
		} else if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu2Turn == true) {
			cpu2Turn = false;
			cpu1Turn = true;
			System.out.println("CPU3 skipped CPU2's turn.");
		} else if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu3Turn == true) {
			cpu3Turn = false;
			cpu2Turn = true;
			System.out.println("You skipped CPU3's turn.");
		}

		// cpu ends turn, happens regardless. Switches turn and waits 1 second
		if (gameFlow == 1) { // clockwise
			cpu1Turn = false;
			cpu2Turn = true;
		} else if (gameFlow == 2) { // counter clockwise
			cpu1Turn = false;
			playerTurn = true;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

	} // End cpu1Gameplay3CPU method.

	private static void cpu2Gameplay3CPU(Hand cpuHand) {
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
				System.out.println("CPU 2 Wins!");
				break;
			} // end if

			// if the list is 0 cpu draws a card and alerts player
			else if (playableCount <= 0) {
				cpuHand.add(gameDeck.getRandomCard());
				System.out.println("CPU 2 drew a card");
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
						System.out.println("CPU 2 played: " + cpuHand.getCard(i)); // say which card cpu played
						cpuHand.remove(cpuHand.getCard(i));// remove it from cpuHand
						if (cpuHand.getSize() == 1) {
							System.out.println("CPU 2: Uno!");
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

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 2 Wins!");
		}

		// cpu ends turn, happens regardless. Switches turn and waits 1 second
		if (gameFlow == 1) { // clockwise
			cpu2Turn = false;
			cpu3Turn = true;
		} else if (gameFlow == 2) { // counter clockwise
			cpu2Turn = false;
			cpu1Turn = true;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

	}// end cpuGameplay2 method.

	private static void cpu3Gameplay3CPU(Hand cpuHand) {
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
				System.out.println("CPU 3 Wins!");
				break;
			} // end if

			// if the list is 0 cpu draws a card and alerts player
			else if (playableCount <= 0) {
				cpuHand.add(gameDeck.getRandomCard());
				System.out.println("CPU 3 drew a card");
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
						System.out.println("CPU 3 played: " + cpuHand.getCard(i)); // say which card cpu played
						cpuHand.remove(cpuHand.getCard(i));// remove it from cpuHand
						if (cpuHand.getSize() == 1) {
							System.out.println("CPU 3: Uno!");
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

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 3 Wins!");
		}

		// cpu ends turn, happens regardless. Switches turn and waits 1 second
		if (gameFlow == 1) { // clockwise
			cpu3Turn = false;
			playerTurn = true;
		} else if (gameFlow == 2) { // counter clockwise
			cpu3Turn = false;
			cpu2Turn = true;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

	} // End cpu3Gameplay method.

	public static void threeCPU(Scanner input, Hand playerHand, Hand cpu1Hand, Hand cpu2Hand, Hand cpu3Hand,
			boolean[] partyRules) {

		boolean playAgain = true;

		while (playAgain) {

			startGame3CPU(playerHand, cpu1Hand, cpu2Hand, cpu3Hand); // method to start game

			do { // game will be played inside of this do while loop, and to end the game we will
					// need to mark gameComplete as true

				// make sure there are cards in gamedeck
				if (gameDeck.isEmpty()) {
					gameDeck.reshuffle();
				}

				if (gameFlow == 1 && gameComplete == false) {// clockwise gameplay

					if (playerTurn && gameComplete == false) {
						playerGameplay3CPU(input, playerHand);
					} // code for players turn
					else if (cpu1Turn && gameComplete == false) {
						cpu1Gameplay3CPU(cpu1Hand);
					} // code for cpu1 turn
					else if (cpu2Turn && gameComplete == false) {
						cpu2Gameplay3CPU(cpu2Hand);
					} // code for cpu2 turn
					else if (cpu3Turn && gameComplete == false) {
						cpu3Gameplay3CPU(cpu3Hand);
					} // code for cpu3 turn
					else {
					} // catch errors and exceptions
				}

				else if (gameFlow == 2 && gameComplete == false) {// counter clockwise gameplay

					if (playerTurn && gameComplete == false) {
						playerGameplay3CPU(input, playerHand);
					} // code for players turn
					else if (cpu3Turn && gameComplete == false) {
						cpu1Gameplay3CPU(cpu3Hand);
					} // code for cpu3 turn
					else if (cpu2Turn && gameComplete == false) {
						cpu2Gameplay3CPU(cpu2Hand);
					} // code for cpu2 turn
					else if (cpu1Turn && gameComplete == false) {
						cpu1Gameplay3CPU(cpu1Hand);
					} // code for cpu1 turn

				}

			} while (!gameComplete);

			playAgain = againChecker(input);

		} // end playAgain loop
	} // End threeCPU method.

	private static void userInput(Scanner input, Hand playerHand) {

		// get user input for next int
		String choice;
		int selection;

		while (true) {
			try {
				System.out.print("\nType the number of the card you would like to play (Enter F to forfeit): ");
				choice = input.next();

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
								"\nCard does not match the color or value of the top card. Please select another card!");
					} // end nested else
				} // end else
			} // end try
			catch (RuntimeException e) {
				System.out.println("Answer must be an integer between 1 and your max amount of cards!\n");
			} // end catch
		} // end while loop

		// check if card matches

		// valid if user can play card, otherwise print only cards they can play

		// if user can't play a card, make them draw a card.

	} // End userInput method.

	public static boolean againChecker(Scanner scnr) {
		System.out.print("\nDo you want to play again? (Y/N): ");
		String input = scnr.next();

		if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
			gameComplete = false;
			return true;
		} else {
			return false;
		}

	} // End againChecker method.

	public static boolean getYesNo(Scanner input) {
		byte loopCount = 0;
		while (true) {
			loopCount++;
			try {
				String answer = input.next();
				if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
					return true;
				} else if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
					return false;
				} else {
					throw new RuntimeException();
				}
			} catch (RuntimeException e) {
				System.out.print("\nValue must be Y or N, Enter again: ");
			}
			if (loopCount == 10) {
				return false;
			}
		} // end while loop
	}// end getYesNo method

} // end OverZeroUnderTwo Class
