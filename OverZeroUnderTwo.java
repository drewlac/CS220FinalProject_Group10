
//import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

// This is the main class for the game. Once functionality is complete, the player 
// plays one or more games of OverZeroUnderTwo against the CPU.
public class OverZeroUnderTwo {
	private static int stackCount = 0;
	private static boolean gameComplete = false;
	private static boolean playerTurn = false;
	private static boolean cpu1Turn = false;
	private static boolean cpu2Turn = false;
	private static boolean cpu3Turn = false;

	private static boolean draw = false;
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

		System.out.print("No bluffing (Y/N): ");
		boolean noBluffing = getYesNo(input);

		System.out.print("Stacking Rule (Y/N): ");
		boolean stacking = getYesNo(input);

		// used to pass these values into methods
		boolean[] partyRules = { sevenZero, noBluffing, stacking };
		/*
		 * partyRules[0] = sevenZero rule 
		 * partyRules[1] = no bluffing rule
		 * partyRules[2] = stacking rule
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
				System.out.print("\nError, input must be 1 or 2. Enter again: ");
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
		System.out.println("\nGame is starting");
		Random rand = new Random();
		int coinflip = 0;
		coinflip = rand.nextInt(2) + 1;

		if (coinflip == 1) {
			playerTurn = true;
			System.out.println("Player's turn is first!");
		} else if (coinflip == 2) {
			cpu1Turn = true;
			System.out.println("CPU 1's turn is first!");
		} else {
			System.out.println("Some sort of error, must have got the coinflip wrong :( ");
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

		//gamePile.addCard(new Card(11, "Red")); // Tester code to see what happens
		// when a specialty card is the first card placed on the discard pile in a game.
		// System.out.println(gamePile.getTopCard()); Tester print statement to see the
		// first card placed on the discard pile.
		// If the top card at the beginning of the game is a Wild card, randomly assign
		// a color value to the top card.
		// *** Wild card or Wild Plus Four card - First card - Single-Player ***
		if (gamePile.getTopCard().getValue() == 13 || gamePile.getTopCard().getValue() == 14) {
			int colorChoice = 0;
			Random rand1 = new Random();
			colorChoice = rand1.nextInt(4) + 1; // Generate random number between 1 and 4.
			if (gamePile.getTopCard().getValue() == 13) {
				System.out.println("The first card of the game was a Wild card.");
			}
			if (gamePile.getTopCard().getValue() == 14 && playerTurn) {
				System.out
						.println("First card was a Wild Plus Four card.\nYou drew 4 cards and your turn was skipped.");
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				playerTurn = false;
				cpu1Turn = true;
			} else if (gamePile.getTopCard().getValue() == 14 && cpu1Turn) {
				System.out.println(
						"First card was a Wild Plus Four card.\nCPU 1 drew 4 cards and their turn was skipped.");
				for (int i = 0; i < 4; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				cpu1Turn = false;
				playerTurn = true;
			}
			if (colorChoice == 1) {
				System.out.println("The first card is now a Blue Card.\n");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 2) {
				System.out.println("The first card is now a Green Card.\n");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 3) {
				System.out.println("The first card is now a Red Card.\n");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 4) {
				System.out.println("The first card is now a Yellow Card.\n");
				gamePile.addCard(new Card("Yellow"));
			}
		}

		// If the skip card is the first card on the discard pile, skip the first
		// player's turn.
		// *** Skip card - First card - Single-Player ***
		if (gamePile.getTopCard().getValue() == 10) {
			if (playerTurn) {
				System.out.println("First card was a Skip card. Your turn was skipped.");
				playerTurn = false;
				cpu1Turn = true;
			} else if (cpu1Turn) {
				System.out.println("First card was a Skip card. CPU 1's turn was skipped.");
				cpu1Turn = false;
				playerTurn = true;
			}
		}
		// If the reverse card is the first card on the discard pile, reverse the turn
		// order and continue. Because the turn order is clockwise by default, the first
		// card should only switch the order to counter-clockwise.
		// *** Reverse card - First card - Single-Player ***
		if (gamePile.getTopCard().getValue() == 11) {
			if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1) {
				gameFlow = 2;
				System.out.println("The first card was a Reverse card.");
				System.out.println("The game order was switched to counter-clockwise.");
			}
		}
		// If the Plus 2 card is the first card on the discard pile, make the first
		// player draw 2 cards and skip their turn.
		// *** Plus 2 card - First card - Single-Player ***
		if (gamePile.getTopCard().getValue() == 12) {
			if (playerTurn) {
				System.out.println("First card was a Plus Two card.\nYou drew 2 cards and your turn was skipped.");
				for (int i = 0; i < 2; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				playerTurn = false;
				cpu1Turn = true;
			} else if (cpu1Turn) {
				System.out.println("First card was a Plus Two card.\nCPU 1 drew 2 cards and their turn was skipped.");
				for (int i = 0; i < 2; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				cpu1Turn = false;
				playerTurn = true;
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

		// *** STACKING ***
		if (stackCount > 0 && partyRules[2]) {
			if (playerHand.getStackableCount() > 0) {
				System.out.println("Stack has " + stackCount + " cards");
				System.out.print("Do you want to add to stack? (Y/N)");
				boolean answer = getYesNo(input);
				if (answer) {
					while (true) {
						try {
							ArrayList<Card> stackList = playerHand.getStackableArray(playerHand);
							for (int i = 0; i < stackList.size(); i++) {
								System.out.println("-Card " + (i + 1) + ": " + stackList.get(i).toString());
							}
							System.out.println("Enter the number of the card you would like to play");
							int choice = input.nextInt();
							if (choice < 1 || choice > stackList.size()) {
								throw new RuntimeException();
							}
							// *** PLAY CARD AND DISCARD IT, ADD TO STACKING ***
							gamePile.addCard(stackList.get(choice - 1));
							System.out.println("You played: " + stackList.get(choice - 1));
							playerHand.remove(stackList.get(choice - 1));

							// *** ADD TO STACK COUNT ***
							if (stackList.get(choice - 1).getValue() == 12) {
								System.out.println("+2 added to stack");
								stackCount += 2;
							} else if (stackList.get(choice - 1).getValue() == 14) {
								System.out.println("+4 added to stack");
								stackCount += 4;
							}
							break;
						} catch (RuntimeException e) {
							System.out.println("Choice must be between 1 and max cards");
						} // end catch
					} // end while
				} // end if answer yes
				else { // if answer no
					for (int i = 0; i < stackCount; i++) {
						playerHand.add(gameDeck.getRandomCard());
					}
					System.out.println("You didn't stack and drew " + stackCount + " cards!");
					stackCount = 0;
				} // end else
			} // end if stackable cards
			else {
				for (int i = 0; i < stackCount; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You didn't stack and drew " + stackCount + " cards!");
				stackCount = 0;
			}
			// *** SWITCH TURNS ***
			if (gameFlow == 1) { // clockwise
				playerTurn = false;
				cpu1Turn = true;
			} else if (gameFlow == 2) { // counter clockwise
				playerTurn = false;
				cpu1Turn = true;
			}
		} else { // *** OTHERWISE, PLAY GAME NORMALLY *** END STACKING ***

			// Set draw to false at the beginning of the player's turn.
			draw = false;
			System.out.println("\nThe top card on the pile is: " + gamePile.getTopCard() + "\n");

			// Permanently add a Wild Plus Four to player hand every turn for testing
			// purposes.
			// playerHand.add(new Card(14, "Wild"));
			// prints the player's hand
			// add function to sort player's hand
			System.out.println("The player's hand is:");
			playerHand.sort();
			playerHand.printHand();

			if (playerHand.getSize() > 1) {
				System.out.println("\nThe player has " + playerHand.getSize() + " cards. ");
			} else if (playerHand.getSize() == 1) {
				System.out.println("\nThe player has " + playerHand.getSize() + " card. ");
			}
			// checking if any cards match
			int matches = 0;
			int wildPlus4Count = 0;
			for (int i = 0; i < playerHand.getSize(); i++) {
				if (playerHand.getCard(i).match(gamePile.getTopCard())) {
					matches++;
				}
				if(playerHand.getCard(i).getValue() == 14 || playerHand.getCard(i).getValue() == 13) {
					wildPlus4Count++;
				}
			} // if there are matches, getting user input *** PLAYER PLAYS CARD HERE ***
			int[] matchAmounts = {matches, wildPlus4Count};
			if (matches > 0) {
				userInput(input, playerHand, matchAmounts, partyRules);
			} else { // if there are no matches, drawing one card
				System.out.println("You have no matches, so you drew 1 card.");
				playerHand.add(gameDeck.getRandomCard());
			}

			if (playerHand.getSize() == 1) {
				System.out.print("Any additional information? ");
				String answer = input.next();
				if (answer.strip().equalsIgnoreCase("uno")) {
					System.out.println("You successfully called Uno!");
				} else if (!answer.strip().equalsIgnoreCase("uno") || answer.isEmpty()) {
					System.out.println("You forgot to call Uno, drawing another card!");
					playerHand.add(gameDeck.getRandomCard());
				}
			}

			// ends game if player's hand is empty *** CATCH AFTER PLAYER HAS PLAYED Card
			if (playerHand.getSize() == 0) {
				gameComplete = true;
				System.out.println("You win!");
			}

			// *** 0-7 Rule ***
			else if (partyRules[0]
					&& (gamePile.getTopCard().getValue() == 0 || gamePile.getTopCard().getValue() == 7)) {
				Hand.switchHands(playerHand, cpuHand);
				System.out.println("0-7 Rule Activated: Hands Switched");
			}

			// *** Reverse card - Player - Single-player ***
			else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && matches > 0 && draw == false) {
				gameFlow = 2;
				playerTurn = false;
				cpu1Turn = true;
				System.out.println("The game order was switched to counter-clockwise.");
			} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && matches > 0 && draw == false) {
				gameFlow = 1;
				playerTurn = false;
				cpu1Turn = true;
				System.out.println("The game order was switched to clockwise.");
			}

			// *** Wild card - Player - Single-player ***
			else if (gamePile.getTopCard().getValue() == 13 && matches > 0 && draw == false) {
				while (true) {
					try {
						int colorChoice = 0;
						System.out.println("1: Blue\n2: Green\n3: Red\n4: Yellow");
						System.out.print("\nEnter a color number (1-4): ");
						input.nextLine();
						colorChoice = input.nextInt();
						if (colorChoice == 1) {
							System.out.println("You played a Blue Wild card.");
							gamePile.addCard(new Card("Blue"));
							cpu1Turn = true;
							playerTurn = false;
							break;
						} else if (colorChoice == 2) {
							System.out.println("You played a Green Wild card.");
							gamePile.addCard(new Card("Green"));
							cpu1Turn = true;
							playerTurn = false;
							break;
						} else if (colorChoice == 3) {
							System.out.println("You played a Red Wild card.");
							gamePile.addCard(new Card("Red"));
							cpu1Turn = true;
							playerTurn = false;
							break;
						} else if (colorChoice == 4) {
							System.out.println("You played a Yellow Wild card.");
							gamePile.addCard(new Card("Yellow"));
							cpu1Turn = true;
							playerTurn = false;
							break;
						} else {
							throw new InputMismatchException();
						} // End else
					} catch (InputMismatchException e) {
						System.out.print("Answer must be an integer between 1 and 4!\n");
					} // end catch
				} // End while
			} // End if *** END WILD CARD ***

			// If you played a skip card and one or more of your cards matches the top card,
			// print the skip statement.
			// NOTE: This skip statement shows up if the last card played before winning is
			// a skip card.
			// *** Skip card - Player - Single-player ***
			else if (gamePile.getTopCard().getValue() == 10 && matches > 0 && draw == false && gameComplete == false) {
				System.out.println("You skipped CPU's turn.");
				if (matches > 0) {
					playerTurn = true;
					cpu1Turn = false;
				} else if (matches == 0) {
					playerTurn = false;
					cpu1Turn = true;
				}
			}

			// Player plays a plus2 card. Force CPU to draw 2 random cards.
			// *** Plus Two card - Player - Single-player ***
			else if (gamePile.getTopCard().getValue() == 12 && playerTurn && matches > 0 && draw == false) {
				if (partyRules[2]) {
					System.out.println("Added +2 to the stack!");
					stackCount += 2;
					
					// *** SWITCH TURNS ***
					if (gameFlow == 1) { // clockwise
						playerTurn = false;
						cpu1Turn = true;
					} else if (gameFlow == 2) { // counter clockwise
						playerTurn = false;
						cpu1Turn = true;
					}
				} else {
					for (int i = 0; i < 2; i++) {
						cpuHand.add(gameDeck.getRandomCard());
					}
				
				System.out.println("CPU 1 drew 2 cards and their turn was skipped.");
				}
			}
			// Player plays a Wild Plus Four card. Change the color to one of the four
			// colors, add four cards to CPU's hand, and skip CPU's turn. Keep asking for
			// valid input of 1-4 if the user enters a wrong number or input type.
			// *** Wild Plus Four card - Player - Single-player ***
			else if (gamePile.getTopCard().getValue() == 14 && playerTurn && matches > 0 && draw == false) {
				while (true) {
					try {
						int colorChoice = 0;
						System.out.println("1: Blue\n2: Green\n3: Red\n4: Yellow");
						System.out.print("\nEnter a color number (1-4): ");
						input.nextLine();
						colorChoice = input.nextInt();
						if (colorChoice == 1) {
							System.out.println("You played a Blue Wild Plus Four card.");
							gamePile.addCard(new Card("Blue"));
							if (partyRules[2] == false) {
								for (int i = 0; i < 4; i++) { // adds 4 cards
									cpuHand.add(gameDeck.getRandomCard());
								}

								System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							}
							break;
						} else if (colorChoice == 2) {
							System.out.println("You played a Green Wild Plus Four card.");
							gamePile.addCard(new Card("Green"));
							if (partyRules[2] == false) {
								for (int i = 0; i < 4; i++) { // adds 4 cards
									cpuHand.add(gameDeck.getRandomCard());
								}

								System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							}
							break;
						} else if (colorChoice == 3) {
							System.out.println("You played a Red Wild Plus Four card.");
							gamePile.addCard(new Card("Red"));
							if (partyRules[2] == false) {
								for (int i = 0; i < 4; i++) { // adds 4 cards
									cpuHand.add(gameDeck.getRandomCard());
								}

								System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							}
							break;
						} else if (colorChoice == 4) {
							System.out.println("You played a Yellow Wild Plus Four card.");
							gamePile.addCard(new Card("Yellow"));
							if (partyRules[2] == false) {
								for (int i = 0; i < 4; i++) { // adds 4 cards
									cpuHand.add(gameDeck.getRandomCard());
								}

								System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
							}
							break;
						} else {
							throw new InputMismatchException();
						} // End else
					} catch (InputMismatchException e) {
						System.out.print("Answer must be an integer between 1 and 4!\n");
					} // end catch
				} // End while
				if (partyRules[2]) {
					System.out.println("Added +4 cards to the stack");
					stackCount += 4;
					if (gameFlow == 1) { // clockwise
						playerTurn = false;
						cpu1Turn = true;
					} else if (gameFlow == 2) { // counter clockwise
						playerTurn = false;
						cpu1Turn = true;
					}
				}
			} // else if (!(gamePile.getTopCard().getValue() == 10)) { // no skip played,
				// switch turns
			else {
				// switch turns and wait 1 second
				if (gameFlow == 1) { // clockwise
					playerTurn = false;
					cpu1Turn = true;
				} else if (gameFlow == 2) { // counter clockwise
					playerTurn = false;
					cpu1Turn = true;
				}
			}

		} // End big else

		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

	} // End playerGameplay1CPU method.

	public static void cpu1Gameplay1CPU(Hand cpuHand, Hand playerHand, boolean[] partyRules) {
		
		// *** STACKING ***
		if (stackCount > 0 && partyRules[2]) {
			
			try {
			
				if (cpuHand.getStackableCount() > 0) {
					ArrayList<Card> stackList = cpuHand.getStackableArray(cpuHand);
	
					// *** PLAY FIRST STACKABLE CARD ***
					// .get(0) is causing bugs *************************8
					gamePile.addCard(stackList.get(0)); // play card to game pile
					System.out.println("CPU 1 played: " + stackList.get(0)); // say which card cpu played
					cpuHand.remove(stackList.get(0));// remove it from cpuHand
	
					// *** ADD VALUE TO STACK COUNT ***
					if (stackList.get(0).getValue() == 12) {
						System.out.println("+2 added to stack");
						stackCount += 2;
					} else if (stackList.get(0).getValue() == 14) {
						System.out.println("+4 added to stack");
						stackCount += 4;
					}
	
				} else { // *** ADD STACK TO CPU HAND AND RESET IT ***
					for (int i = 0; i < stackCount; i++) {
						cpuHand.add(gameDeck.getRandomCard());
					}
					System.out.println("CPU didn't stack and drew " + stackCount + " cards!");
					stackCount = 0;
				}
	
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu1Turn = false;
					playerTurn = true;
				} else if (gameFlow == 2) { // counter clockwise
					cpu1Turn = false;
					playerTurn = true;
				}
			}
			
			catch(IndexOutOfBoundsException e) {
				for (int i = 0; i < stackCount; i++) {
					cpuHand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU didn't stack and drew " + stackCount + " cards!");
				stackCount = 0;
				
				if (gameFlow == 1) { // clockwise
					cpu1Turn = false;
					playerTurn = true;
				} else if (gameFlow == 2) { // counter clockwise
					cpu1Turn = false;
					playerTurn = true;
				}
			}
			
		} else { // *** ELSE JUST PLAY REGULAR GAME

			// creates a hand of playable cards
			// boolean stackable;
			// if (partyRules[2] && (gamePile.getTopCard().getValue() == 12 ||
			// gamePile.getTopCard().getValue() == 14))
			// stackable = true;
			// else
			// stackable = false;
			int playableCount = 0;
			int wildPlus4Amount = 0;
			// cpuHand.add(new Card(14, "Wild")); // Added tester code so the CPU always has
			// a
			// Wild Plus Four card.
			// checks cards in hand to see if they are playable
			for (int i = 0; i < cpuHand.getSize(); i++) {
				if (cpuHand.getCard(i).match(gamePile.getTopCard())) {
					// adds cards to the new hand
					playableCount++;
				}
				if (cpuHand.getCard(i).getValue() ==  14 || cpuHand.getCard(i).getValue() == 13) {
					wildPlus4Amount++;
				}
			}

			// System.out.println("Playable Count: " + playableCount); //used for
			// troubleshooting

			boolean cardPlayed = false;
			Random random = new Random();
			double coinflip;

			while (true) { // *** CPU IS PLAYING CARD HERE ***
				if (cpuHand.getSize() == 0) { // if CPU has no cards, end game
					gameComplete = true;
					System.out.println("CPU 1 Wins!");
					break;
				} // end if

				// if the list is 0 cpu draws a card and alerts player
				else if (playableCount <= 0) {
					cpuHand.add(gameDeck.getRandomCard());
					System.out.println("CPU 1 drew a card.");
					break;
				} // end else if

				else {
					// cpu flips a coin to see if they play each playable card
					for (int i = 0; i < cpuHand.getSize(); i++) {
						coinflip = random.nextDouble();
						// System.out.println("Coinflip: " + coinflip); //used for troubleshooting
						if(partyRules[1] && playableCount > wildPlus4Amount && cpuHand.getCard(i).getValue()==14) {
							System.out.println("CPU: I can't bluff, trying again!");
						} // skipping bluffing if no bluff rule is activated
						else if (cpuHand.getCard(i).match(gamePile.getTopCard()) && coinflip < 0.5) {// if cards match and
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
			} // end while *** CPU CARD IS PLAYED ***

			if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played
											// to
				// catch a bug
				gameComplete = true;
				System.out.println("CPU 1 Wins!");
			}

			// *** 0-7 Rule ***
			else if (partyRules[0]
					&& (gamePile.getTopCard().getValue() == 0 || gamePile.getTopCard().getValue() == 7)) {
				Hand.switchHands(playerHand, cpuHand);
				System.out.println("0-7 Rule Activated: Hands Switched");
			}

			// *** Wild card - CPU1 - Single-player ***
			else if (gamePile.getTopCard().getValue() == 13 && playableCount > 0) {
				int colorChoice = 0;
				Random rand = new Random();
				colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

				// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
				// to confirm CPU can play only the four valid colors.
				if (colorChoice == 1) {
					System.out.println("CPU 1 played a Blue Wild Card.");
					gamePile.addCard(new Card("Blue"));
				} else if (colorChoice == 2) {
					System.out.println("CPU 1 played a Green Wild Card.");
					gamePile.addCard(new Card("Green"));
				} else if (colorChoice == 3) {
					System.out.println("CPU 1 played a Red Wild Card.");
					gamePile.addCard(new Card("Red"));
				} else if (colorChoice == 4) {
					System.out.println("CPU 1 played a Yellow Wild Card.");
					gamePile.addCard(new Card("Yellow"));
				}
				cpu1Turn = false;
				playerTurn = true;
			}

			// *** Reverse card - CPU1 - Single-player ***
			else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && playableCount > 0) {
				gameFlow = 2;
				cpu1Turn = false;
				playerTurn = true;
				System.out.println("The game order was switched to counter-clockwise.");
			} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && playableCount > 0) {
				gameFlow = 1;
				cpu1Turn = false;
				playerTurn = true;
				System.out.println("The game order was switched to clockwise.");
			}

			// if value is NOT equal to 10 (skip), then code runs.
			// if it IS equal, code does not run, and the opposing player's next turn is
			// skipped.
			// *** Skip card - CPU1 - Single-player ***
			else if (gamePile.getTopCard().getValue() == 10 && playableCount > 0) {
				System.out.println("CPU 1 skipped your turn.");
				if (playableCount > 0) {
					cpu1Turn = true;
					playerTurn = false;
				} else if (playableCount == 0) {
					cpu1Turn = false;
					playerTurn = true;
				}
			}

			// *******Stacking********* Currently broken
			/*
			 * // WIP //Checks to see if party rules are in effect, if the previous player
			 * // played a stackable card else if (partyRules[2] && stackable) {
			 * System.out.println("Stacking check");// testing to tell us if this is run if
			 * ((gamePile.getTopCard().getValue() == 12 || gamePile.getTopCard().getValue()
			 * == 14)) { // <-- I think // this is the // problem if
			 * (gamePile.getTopCard().getValue() == 12) { stackCount += 2;
			 * System.out.println("+2 added to stack"); } else { stackCount += 4;
			 * System.out.println("+4 added to stack"); } } // if the card played is not
			 * stackable then the current player draws stackCount // number of cards and
			 * informs player else { for (int n = 0; n < stackCount; n++) {
			 * cpuHand.add(gameDeck.getRandomCard());
			 * System.out.println("CPU didn't stack and drew " + stackCount + " cards!"); }
			 * stackCount = 0; } System.out.println(stackable + " " + stackCount);// testing
			 * to see if stuff is working // change turn cpu1Turn = false; playerTurn =
			 * true; }
			 */

			// *** Plus Two card - CPU1 - Single-player ***
			else if (gamePile.getTopCard().getValue() == 12 && playableCount > 0) { // plus2
				// Forces player to draw 2 random cards.
				if (partyRules[2]) {
					System.out.println("+2 added to stack");
					stackCount += 2;
				} else {
					for (int i = 0; i < 2; i++) {
						playerHand.add(gameDeck.getRandomCard());
					}
					System.out.println("You drew 2 cards and your turn was skipped.");
				}
			}

			// CPU plays Wild Plus Four card. It then randomly chooses a color, forces the
			// player to draw 4 cards, and skips the player's turn.
			else if (gamePile.getTopCard().getValue() == 14 && playableCount > 0) {
				int colorChoice = 0;
				Random rand = new Random();
				colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

				// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
				// to confirm CPU can play only the four valid colors.
				if (colorChoice == 1) {
					System.out.println("CPU 1 played a Red Wild Plus Four Card.");
					gamePile.addCard(new Card("Red"));
					if (partyRules[2] == false) {
						for (int i = 0; i < 4; i++) {
							playerHand.add(gameDeck.getRandomCard());
						}

						System.out.println("You drew 4 cards and your turn was skipped.");
					}
				} else if (colorChoice == 2) {
					System.out.println("CPU 1 played a Blue Wild Plus Four Card.");
					gamePile.addCard(new Card("Blue"));
					if (partyRules[2] == false) {
						for (int i = 0; i < 4; i++) {
							playerHand.add(gameDeck.getRandomCard());
						}

						System.out.println("You drew 4 cards and your turn was skipped.");
					}
				} else if (colorChoice == 3) {
					System.out.println("CPU 1 played a Green Wild Plus Four Card.");
					gamePile.addCard(new Card("Green"));
					if (partyRules[2] == false) {
						for (int i = 0; i < 4; i++) {
							playerHand.add(gameDeck.getRandomCard());
						}

						System.out.println("You drew 4 cards and your turn was skipped.");
					}
				} else if (colorChoice == 4) {
					System.out.println("CPU 1 played a Yellow Wild Plus Four Card.");
					gamePile.addCard(new Card("Yellow"));
					if (partyRules[2] == false) {
						for (int i = 0; i < 4; i++) {
							playerHand.add(gameDeck.getRandomCard());
						}

						System.out.println("You drew 4 cards and your turn was skipped.");
					}
				}
				if (partyRules[2]) {
					System.out.println("Added +4 cards to the stack");
					stackCount += 4;
				}
				
				if (gameFlow == 1) { // clockwise
					cpu1Turn = false;
					playerTurn = true;
				} else if (gameFlow == 2) { // counter clockwise
					cpu1Turn = false;
					playerTurn = true;
				}

			} // else if (!(gamePile.getTopCard().getValue() == 10 && playableCount < 1)) { //
				// no skip played, switch turns
			else {
				// cpu ends turn, happens regardless. Switches turn and waits 1 second
				if (gameFlow == 1) { // clockwise
					cpu1Turn = false;
					playerTurn = true;
				} else if (gameFlow == 2) { // counter clockwise
					cpu1Turn = false;
					playerTurn = true;
				}
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}
		// Test code to see how many cards CPU has.
		if (cpuHand.getSize() > 1) {
			System.out.println("CPU 1 has " + cpuHand.getSize() + " cards.");
		} else if (cpuHand.getSize() == 1) {
			System.out.println("CPU 1 has " + cpuHand.getSize() + " card.");
		}
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
				} // code for player's turn
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
			System.out.println("Player's turn is first!");
		} else if (coinflip == 2) {
			cpu1Turn = true;
			System.out.println("CPU 1's turn is first!");
		} else if (coinflip == 3) {
			cpu2Turn = true;
			System.out.println("CPU 2's turn is first!");
		} else if (coinflip == 4) {
			cpu3Turn = true;
			System.out.println("CPU 3's turn is first!");
		} else {
			System.out.println("Some sort of error, must have got the coinflip wrong :( ");
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
		// gamePile.addCard(new Card(12, "Red")); // Tester code to see what happens
		// when a specialty card is the first card placed on the discard pile in a game.

		// If the top card at the beginning of the game is a Wild or Wild Plus Four
		// card, randomly assign a color value to the top card.
		// *** Wild card or Wild Plus Four card - First card - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 13 || gamePile.getTopCard().getValue() == 14) {
			int colorChoice = 0;
			Random rand2 = new Random();
			colorChoice = rand2.nextInt(4) + 1; // Generate random number between 1 and 4.
			if (gamePile.getTopCard().getValue() == 13) {
				System.out.println("The first card of the game was a Wild card.");
			}
			if (gamePile.getTopCard().getValue() == 14 && playerTurn) {
				System.out
						.println("First card was a Wild Plus Four card.\nYou drew 4 cards and your turn was skipped.");
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				playerTurn = false;
				cpu1Turn = true;
			} else if (gamePile.getTopCard().getValue() == 14 && cpu1Turn) {
				System.out.println(
						"First card was a Wild Plus Four card.\nCPU 1 drew 4 cards and their turn was skipped.");
				for (int i = 0; i < 4; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				cpu1Turn = false;
				cpu2Turn = true;
			} else if (gamePile.getTopCard().getValue() == 14 && cpu2Turn) {
				System.out.println(
						"First card was a Wild Plus Four card.\nCPU 2 drew 4 cards and their turn was skipped.");
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				cpu2Turn = false;
				cpu3Turn = true;
			} else if (gamePile.getTopCard().getValue() == 14 && cpu3Turn) {
				System.out.println(
						"First card was a Wild Plus Four card.\nCPU 3 drew 4 cards and their turn was skipped.");
				for (int i = 0; i < 4; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				cpu3Turn = false;
				playerTurn = true;
			}
			if (colorChoice == 1) {
				System.out.println("The first card is now a Blue Card.\n");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 2) {
				System.out.println("The first card is now a Green Card.\n");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 3) {
				System.out.println("The first card is now a Red Card.\n");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 4) {
				System.out.println("The first card is now a Yellow Card.\n");
				gamePile.addCard(new Card("Yellow"));
			}
		} // End Wild if

		// If the skip card is the first card on the discard pile, skip the first
		// player's turn.
		// *** Skip card - First card - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 10) {
			if (playerTurn) {
				System.out.println("First card was a Skip card. Your turn was skipped.");
				playerTurn = false;
				cpu1Turn = true;
			} else if (cpu1Turn) {
				System.out.println("First card was a Skip card. CPU 1's turn was skipped.");
				cpu1Turn = false;
				cpu2Turn = true;
			} else if (cpu2Turn) {
				System.out.println("First card was a Skip card. CPU 2's turn was skipped.");
				cpu2Turn = false;
				cpu3Turn = true;
			} else if (cpu3Turn) {
				System.out.println("First card was a Skip card. CPU 3's turn was skipped.");
				cpu3Turn = false;
				playerTurn = true;
			}
		} // End Skip if

		// If the reverse card is the first card on the discard pile, reverse the turn
		// order and continue. Because the turn order is clockwise by default, the first
		// card should only switch the order to counter-clockwise.
		// *** Reverse card - First card - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 11) {
			if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1) {
				gameFlow = 2;
				System.out.println("The first card was a Reverse card.");
				System.out.println("The game order was switched to counter-clockwise.");
			}
		} // End Reverse if

		// If the Plus 2 card is the first card on the discard pile, make the first
		// player draw 2 cards and skip their turn.
		// *** Plus 2 card - First card - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 12) {
			if (playerTurn) {
				System.out.println("First card was a Plus Two card.\nYou drew 2 cards and your turn was skipped.");
				for (int i = 0; i < 2; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				playerTurn = false;
				cpu1Turn = true;
			} else if (cpu1Turn) {
				System.out.println("First card was a Plus Two card.\nCPU 1 drew 2 cards and their turn was skipped.");
				for (int i = 0; i < 2; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				cpu1Turn = false;
				cpu2Turn = true;
			} else if (cpu2Turn) {
				System.out.println("First card was a Plus Two card.\nCPU 2 drew 2 cards and their turn was skipped.");
				for (int i = 0; i < 2; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				cpu2Turn = false;
				cpu3Turn = true;
			} else if (cpu3Turn) {
				System.out.println("First card was a Plus Two card.\nCPU 3 drew 2 cards and their turn was skipped.");
				for (int i = 0; i < 2; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				cpu3Turn = false;
				playerTurn = true;
			}
		} // End Plus 2 if

	} // end method startGame3CPU method.

	public static void resetGame3CPU(Hand playerHand, Hand cpu1Hand, Hand cpuHand2, Hand cpuHand3) {

		playerHand.removeAllCards();
		cpu1Hand.removeAllCards();
		cpuHand2.removeAllCards();
		cpuHand3.removeAllCards();
		gameDeck.reshuffle();
	} // End resetGame3CPU method.

	// Used to code player turn in multiplayer.
	public static void playerGameplay3CPU(Scanner input, Hand playerHand, Hand cpu1Hand, Hand cpu2Hand, Hand cpu3Hand, boolean[] partyRules) {
		
		// *** STACKING ***
		if (stackCount > 0 && partyRules[2]) {
			if (playerHand.getStackableCount() > 0) {
				System.out.println("Stack has " + stackCount + " cards");
				System.out.print("Do you want to add to stack? (Y/N)");
				boolean answer = getYesNo(input);
				if (answer) {
					while (true) {
						try {
							ArrayList<Card> stackList = playerHand.getStackableArray(playerHand);
							for (int i = 0; i < stackList.size(); i++) {
								System.out.println("-Card " + (i + 1) + ": " + stackList.get(i).toString());
							}
							System.out.println("Enter the number of the card you would like to play");
							int choice = input.nextInt();
							if (choice < 1 || choice > stackList.size()) {
								throw new RuntimeException();
							}
							// *** PLAY CARD AND DISCARD IT, ADD TO STACKING ***
							gamePile.addCard(stackList.get(choice - 1));
							System.out.println("You played: " + stackList.get(choice - 1));
							playerHand.remove(stackList.get(choice - 1));

							// *** ADD TO STACK COUNT ***
							if (stackList.get(choice - 1).getValue() == 12) {
								System.out.println("+2 added to stack");
								stackCount += 2;
							} else if (stackList.get(choice - 1).getValue() == 14) {
								System.out.println("+4 added to stack");
								stackCount += 4;
							}
							break;
						} catch (RuntimeException e) {
							System.out.println("Choice must be between 1 and max cards");
						} // end catch
					} // end while
				} // end if answer yes
				else { // if answer no
					for (int i = 0; i < stackCount; i++) {
						playerHand.add(gameDeck.getRandomCard());
					}
					System.out.println("You didn't stack and drew " + stackCount + " cards!");
					stackCount = 0;
				} // end else
			} // end if stackable cards
			else {
				for (int i = 0; i < stackCount; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You didn't stack and drew " + stackCount + " cards!");
				stackCount = 0;
			}
			
			if (gameFlow == 1) { // clockwise
				playerTurn = false;
				cpu3Turn = false;
				cpu1Turn = true;
			} 
			else if (gameFlow == 2) { // counter clockwise
				cpu1Turn = false;
				playerTurn = false;
				cpu3Turn = true;
			}

		}
		else { // *** OTHERWISE, PLAY GAME NORMALLY *** END STACKING ***
		
		
		// Set draw to false at the beginning of the player's turn.
		draw = false;
		System.out.println("\nThe top card on the pile is: " + gamePile.getTopCard() + "\n");

		// prints the player's hand
		// add function to sort player's hand
		System.out.println("The player's hand is:");
		playerHand.sort();
		playerHand.printHand();

		if (playerHand.getSize() > 1) {
			System.out.println("\nThe player has " + playerHand.getSize() + " cards. ");
		} else if (playerHand.getSize() == 1) {
			System.out.println("\nThe player has " + playerHand.getSize() + " card. ");
		}

		// playerHand.add(new Card(11, "Red")); // Tester code for a Red Reverse card.
		// checking if any cards match
		int matches = 0;
		int wildPlus4Count = 0;
		for (int i = 0; i < playerHand.getSize(); i++) {
			if (playerHand.getCard(i).match(gamePile.getTopCard())) {
				matches++;	
			}
			if(playerHand.getCard(i).getValue()==14 || playerHand.getCard(i).getValue() == 13) {
				wildPlus4Count++;
			}
		} // if there are matches, getting user input
		int[] matchAmounts = {matches, wildPlus4Count};
		if (matches > 0) {
			userInput(input, playerHand, matchAmounts, partyRules);
		} else { // if there are no matches, drawing one card
			System.out.println("You have no matches, so you drew 1 card.");
			playerHand.add(gameDeck.getRandomCard());
		}

		if (playerHand.getSize() == 1) {
			System.out.print("Any additional information? ");
			String answer = input.next();
			if (answer.strip().equalsIgnoreCase("uno")) {
				System.out.println("You successfully called Uno!");
			} else if (!answer.strip().equalsIgnoreCase("uno") || answer.isEmpty()) {
				System.out.println("You forgot to call Uno, drawing another card!");
				playerHand.add(gameDeck.getRandomCard());
			}
		}
		
		// *** 0-7 Rule *** - Player 

		if (partyRules[0] && (gamePile.getTopCard().getValue() == 0 || gamePile.getTopCard().getValue() == 7)) {
			
			System.out.println("0-7 rule activated");
			
			if(gamePile.getTopCard().getValue() == 0) {
				
				if(gameFlow == 1) {
					System.out.println("Hands rotated clockwise!");
					Hand.switchHands(playerHand, cpu1Hand);
					Hand.switchHands(playerHand, cpu2Hand);
					Hand.switchHands(playerHand, cpu3Hand);

				}
				else if(gameFlow == 2) {
					System.out.println("Hans rotated counter-clockwise");
					Hand.switchHands(playerHand, cpu3Hand);
					Hand.switchHands(playerHand, cpu2Hand);
					Hand.switchHands(playerHand, cpu1Hand);
				}
				
			} // end 0 rule
			
			else if(gamePile.getTopCard().getValue() == 7) {
				System.out.println("-1: CPU 1's Hand");
				System.out.println("-2: CPU 2's Hand");
				System.out.println("-3: CPU 3's Hand");
				System.out.print("\n Enter which hand you would like to swap with: ");
				
				while(true) {
					try {
					
						int ans = input.nextInt();
						if(ans != 1 || ans != 2 || ans != 3) {
							throw new RuntimeException();
						}
						switch(ans) {
							case 1 -> Hand.switchHands(playerHand, cpu1Hand);
							case 2 -> Hand.switchHands(playerHand, cpu2Hand);
							case 3 -> Hand.switchHands(playerHand, cpu3Hand);
						}
						break; //end loop if successful
					}
					
					catch(RuntimeException e) {System.out.print("\nAnswer must be 1, 2, or 3. Try again : ");}
				
				} // end while loop
			
			} // end 7 rule
				
		}

		// *** Reverse card - Player - Multiplayer ***

		if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && matches > 0 && draw == false) {
			gameFlow = 2;
			System.out.println("The game order was switched to counter-clockwise.");
		} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && matches > 0 && draw == false) {
			gameFlow = 1;
			System.out.println("The game order was switched to clockwise.");
		}

		// *** Wild card - Player - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 13 && matches > 0 && draw == false) {
			while (true) {
				try {
					int colorChoice = 0;
					System.out.println("1: Blue\n2: Green\n3: Red\n4: Yellow");
					System.out.print("\nEnter a color number (1-4): ");
					input.nextLine();
					colorChoice = input.nextInt();
					// If gameFlow is 1, go to CPU1's turn after playing Wild card.
					if (gameFlow == 1) {
						if (colorChoice == 1) {
							System.out.println("You played a Blue Wild card.");
							gamePile.addCard(new Card("Blue"));
							break;
						} else if (colorChoice == 2) {
							System.out.println("You played a Green Wild card.");
							gamePile.addCard(new Card("Green"));
							break;
						} else if (colorChoice == 3) {
							System.out.println("You played a Red Wild card.");
							gamePile.addCard(new Card("Red"));
							break;
						} else if (colorChoice == 4) {
							System.out.println("You played a Yellow Wild card.");
							gamePile.addCard(new Card("Yellow"));
							break;
						}
					}
					// If gameFlow is 2, go to CPU3's turn.
					else if (gameFlow == 2) {
						if (colorChoice == 1) {
							System.out.println("You played a Blue Wild card.");
							gamePile.addCard(new Card("Blue"));
							break;
						} else if (colorChoice == 2) {
							System.out.println("You played a Green Wild card.");
							gamePile.addCard(new Card("Green"));
							break;
						} else if (colorChoice == 3) {
							System.out.println("You played a Red Wild card.");
							gamePile.addCard(new Card("Red"));
							break;
						} else if (colorChoice == 4) {
							System.out.println("You played a Yellow Wild card.");
							gamePile.addCard(new Card("Yellow"));
							break;
						}
					} else {
						throw new InputMismatchException();
					} // End else
				} catch (InputMismatchException e) {
					System.out.print("Answer must be an integer between 1 and 4!\n");
				} // end catch
			} // End while
		} // End if *** END WILD CARD ***

		// ends game if player's hand is empty
		if (playerHand.getSize() == 0) {
			gameComplete = true;
			System.out.println("You win!");
		}

		boolean skipped = false;
		// *** Skip card - Player - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 10 && matches > 0 && draw == false && gameComplete == false) {
			skipped = true;
			if (gameFlow == 1) {
				System.out.println("You skipped CPU 1's turn.");
			} else if (gameFlow == 2) {
				System.out.println("You skipped CPU 3's turn.");
			}
		}

		// *** Plus Two card - Player - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 12 && playerTurn && matches > 0 && draw == false) {
			skipped = true;
			if (gameFlow == 1) {
				for (int i = 0; i < 2; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 1 drew 2 cards and their turn was skipped.");
			} else if (gameFlow == 2) {
				for (int i = 0; i < 2; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 3 drew 2 cards and their turn was skipped.");
			}
		} // End Plus Two else if

		// *** Wild Plus Four card - Player - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 14 && playerTurn && matches > 0 && draw == false) {
			while (true) {
				try {
					skipped = true;
					int colorChoice = 0;
					System.out.println("1: Blue\n2: Green\n3: Red\n4: Yellow");
					System.out.print("\nEnter a color number (1-4): ");
					input.nextLine();
					colorChoice = input.nextInt();
					// If the gameFlow is 1, skip CPU1's turn and make CPU1 draw four cards.
					if (colorChoice == 1 && gameFlow == 1) {
						System.out.println("You played a Blue Wild Plus Four card.");
						gamePile.addCard(new Card("Blue"));
						for (int i = 0; i < 4; i++) {
							cpu1Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 2 && gameFlow == 1) {
						System.out.println("You played a Green Wild Plus Four card.");
						gamePile.addCard(new Card("Green"));
						for (int i = 0; i < 4; i++) {
							cpu1Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 3 & gameFlow == 1) {
						System.out.println("You played a Red Wild Plus Four card.");
						gamePile.addCard(new Card("Red"));
						for (int i = 0; i < 4; i++) {
							cpu1Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 4 && gameFlow == 1) {
						System.out.println("You played a Yellow Wild Plus Four card.");
						gamePile.addCard(new Card("Yellow"));
						for (int i = 0; i < 4; i++) {
							cpu1Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
						break;
					} // If the gameFlow is 2, skip CPU3's turn and make CPU3 draw four cards.
					else if (colorChoice == 1 && gameFlow == 2) {
						System.out.println("You played a Blue Wild Plus Four card.");
						gamePile.addCard(new Card("Blue"));
						for (int i = 0; i < 4; i++) {
							cpu3Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 2 && gameFlow == 2) {
						System.out.println("You played a Green Wild Plus Four card.");
						gamePile.addCard(new Card("Green"));
						for (int i = 0; i < 4; i++) {
							cpu3Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 3 & gameFlow == 2) {
						System.out.println("You played a Red Wild Plus Four card.");
						gamePile.addCard(new Card("Red"));
						for (int i = 0; i < 4; i++) {
							cpu3Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
						break;
					} else if (colorChoice == 4 && gameFlow == 2) {
						System.out.println("You played a Yellow Wild Plus Four card.");
						gamePile.addCard(new Card("Yellow"));
						for (int i = 0; i < 4; i++) {
							cpu3Hand.add(gameDeck.getRandomCard());
						}
						System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
						break;
					} else {
						throw new InputMismatchException();
					} // End else
				} catch (InputMismatchException e) {
					System.out.print("Answer must be an integer between 1 and 4!\n");
				} // end catch
			} // End while
		} // End Wild Plus Four if
		
		//put else end brack here

			// switch turns and wait 1 second
			if (gameFlow == 1) { // clockwise
				playerTurn = false;
				cpu3Turn = false;
				if (skipped) // if skip is played
					cpu2Turn = true;
				else // regular gameplay
					cpu1Turn = true;
			} else if (gameFlow == 2) { // counter clockwise
				cpu1Turn = false;
				playerTurn = false;
				if (skipped) // if skip is played
					cpu2Turn = true;
				else // regular gameplay
					cpu3Turn = true;
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}
	} // End playerGameplay3CPU method.

	public static void cpu1Gameplay3CPU(Hand cpuHand, Hand playerHand, Hand cpu2Hand, Hand cpu3Hand, boolean[] partyRules) {
		
		// *** STACKING *** 
		if (stackCount > 0 && partyRules[2]) {
			
			try {
			
				if (cpuHand.getStackableCount() > 0) {
					ArrayList<Card> stackList = cpuHand.getStackableArray(cpuHand);
	
					// *** PLAY FIRST STACKABLE CARD ***
					// .get(0) is causing bugs *************************8
					gamePile.addCard(stackList.get(0)); // play card to game pile
					System.out.println("CPU 1 played: " + stackList.get(0)); // say which card cpu played
					cpuHand.remove(stackList.get(0));// remove it from cpuHand
	
					// *** ADD VALUE TO STACK COUNT ***
					if (stackList.get(0).getValue() == 12) {
						System.out.println("+2 added to stack");
						stackCount += 2;
					} else if (stackList.get(0).getValue() == 14) {
						System.out.println("+4 added to stack");
						stackCount += 4;
					}
	
				} else { // *** ADD STACK TO CPU HAND AND RESET IT ***
					for (int i = 0; i < stackCount; i++) {
						cpuHand.add(gameDeck.getRandomCard());
					}
					System.out.println("CPU 1 didn't stack and drew " + stackCount + " cards!");
					stackCount = 0;
				}
	
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu1Turn = false;
					playerTurn = false;
					cpu2Turn = true;
					
				} 
				else if (gameFlow == 2) { // counter clockwise
					cpu1Turn = false;
					cpu2Turn = false;
					playerTurn = true;
				}
			}
			
			catch(IndexOutOfBoundsException e) {
				for (int i = 0; i < stackCount; i++) {
					cpuHand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU  1 didn't stack and drew " + stackCount + " cards!");
				stackCount = 0;
				
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu1Turn = false;
					playerTurn = false;
					cpu2Turn = true;
					
				} 
				else if (gameFlow == 2) { // counter clockwise
					cpu1Turn = false;
					cpu2Turn = false;
					playerTurn = true;
				}
			} // end catch
			
		} else { // *** ELSE JUST PLAY REGULAR GAME
		
		
		// creates a hand of playable cards
		int playableCount = 0;
		int wildPlus4Amount = 0;

		// checks cards in hand to see if they are playable
		for (int i = 0; i < cpuHand.getSize(); i++) {
			if (cpuHand.getCard(i).match(gamePile.getTopCard())) {
				// adds cards to the new hand
				playableCount++;
			}
			if (cpuHand.getCard(i).getValue() ==  14 || cpuHand.getCard(i).getValue() == 13) {
				wildPlus4Amount++;
			}
		}

		// cpuHand.add(new Card(10, "Red"));
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
				System.out.println("CPU 1 drew a card.");
				break;
			} // end else if

			else {
				// cpu flips a coin to see if they play each playable card
				for (int i = 0; i < cpuHand.getSize(); i++) {
					coinflip = random.nextDouble();
					
					if(partyRules[1] && playableCount > wildPlus4Amount && cpuHand.getCard(i).getValue()==14) {
						System.out.println("CPU 1: I can't bluff, trying again!");
					} // skipping bluffing if no bluff rule is activated

					else if (cpuHand.getCard(i).match(gamePile.getTopCard()) && coinflip < 0.5) {// if cards match and
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
		
		// *** 0-7 Rule *** - CPU1
		if (partyRules[0] && (gamePile.getTopCard().getValue() == 0 || gamePile.getTopCard().getValue() == 7)) {
			
			System.out.println("0-7 rule activated");
			
			if(gamePile.getTopCard().getValue() == 0) {
				
				if(gameFlow == 1) {
					System.out.println("Hands rotated clockwise!");
					Hand.switchHands(playerHand, cpuHand);
					Hand.switchHands(playerHand, cpu2Hand);
					Hand.switchHands(playerHand, cpu3Hand);

				}
				else if(gameFlow == 2) {
					System.out.println("Hans rotated counter-clockwise");
					Hand.switchHands(playerHand, cpu3Hand);
					Hand.switchHands(playerHand, cpu2Hand);
					Hand.switchHands(playerHand, cpuHand);
				}
				
			}// end 0 rule
		
			else if(gamePile.getTopCard().getValue() == 7) {
				
				Random rand = new Random();
				int ranInt = rand.nextInt(3 - 1 + 1) + 1;
				
				switch(ranInt) {
					case 1 -> Hand.switchHands(cpuHand, playerHand); //System.out.println("CPU 1 switch hands with the player!");
					case 2 -> Hand.switchHands(cpuHand, cpu2Hand);
					case 3 -> Hand.switchHands(cpuHand, cpu3Hand);
				}
				
				switch(ranInt) {
					case 1 -> System.out.println("CPU 1 switched hands with the player!");
					case 2 -> System.out.println("CPU 1 switched hands with the CPU 2!");
					case 3 -> System.out.println("CPU 1 switched hands with the CPU 3!");
				}
				
				
			} // end 7 rule

		
		} // end 0-7 rule
		


		// *** Reverse card - CPU1 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && playableCount > 0) {
			gameFlow = 2;
			System.out.println("The game order was switched to counter-clockwise.");
		} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && playableCount > 0) {
			gameFlow = 1;
			System.out.println("The game order was switched to clockwise.");
		}

		// *** Wild card - CPU1 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 13 && playableCount > 0) {
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			if (colorChoice == 1) {
				System.out.println("CPU 1 played a Blue Wild Card.");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 2) {
				System.out.println("CPU 1 played a Green Wild Card.");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 3) {
				System.out.println("CPU 1 played a Red Wild Card.");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 4) {
				System.out.println("CPU 1 played a Yellow Wild Card.");
				gamePile.addCard(new Card("Yellow"));
			}
		} // End Wild card if

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 1 Wins!");
		}

		boolean skipped = false;
		// *** Skip card - CPU1 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 10 && playableCount > 0) {
			skipped = true;
			// skip code for CLOCKWISE order
			if (gameFlow == 1) {
				System.out.println("CPU 1 skipped CPU 2's turn.");
			}
			// skip code for COUNTER-CLOCKWISE order
			if (gameFlow == 2) {
				System.out.println("CPU 1 skipped your turn.");
			}
		}

		// *** Plus Two card - CPU1 - Multiplayer ***
		else if (gamePile.getTopCard().getValue() == 12 && playableCount > 0) {
			skipped = true;
			if (gameFlow == 1) {
				// Forces CPU 2 to draw 2 random cards.
				for (int i = 0; i < 2; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 2 cards and their turn was skipped.");
			} else if (gameFlow == 2) {
				// Forces player to draw 2 random cards.
				for (int i = 0; i < 2; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 2 cards and your turn was skipped.");
			}
		} // End Plus Two else if

		// *** Wild Plus Four card - CPU1 - Multiplayer ***
		else if (gamePile.getTopCard().getValue() == 14 && playableCount > 0) {
			skipped = true;
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			// If gameFlow is 1, skip CPU2's turn and make them draw four cards.
			if (colorChoice == 1 && gameFlow == 1) {
				System.out.println("CPU 1 played a Blue Wild Plus Four Card.");
				gamePile.addCard(new Card("Blue"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 2 && gameFlow == 1) {
				System.out.println("CPU 1 played a Green Wild Plus Four Card.");
				gamePile.addCard(new Card("Green"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 3 && gameFlow == 1) {
				System.out.println("CPU 1 played a Red Wild Plus Four Card.");
				gamePile.addCard(new Card("Red"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 4 && gameFlow == 1) {
				System.out.println("CPU 1 played a Yellow Wild Plus Four Card.");
				gamePile.addCard(new Card("Yellow"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			}
			// If gameFlow is 2, skip player's turn and make them draw four cards.
			else if (colorChoice == 1 && gameFlow == 2) {
				System.out.println("CPU 1 played a Blue Wild Plus Four Card.");
				gamePile.addCard(new Card("Blue"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 2 && gameFlow == 2) {
				System.out.println("CPU 1 played a Green Wild Plus Four Card.");
				gamePile.addCard(new Card("Green"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 3 && gameFlow == 2) {
				System.out.println("CPU 1 played a Red Wild Plus Four Card.");
				gamePile.addCard(new Card("Red"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 4 && gameFlow == 2) {
				System.out.println("CPU 1 played a Yellow Wild Plus Four Card.");
				gamePile.addCard(new Card("Yellow"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			}
		} // End Wild Plus Four card if

			// cpu ends turn, happens regardless. Switches turn and waits 1 second
			if (gameFlow == 1) { // clockwise
				cpu1Turn = false;
				playerTurn = false;
				if (skipped)
					cpu3Turn = true;
				else
					cpu2Turn = true;
			} else if (gameFlow == 2) { // counter clockwise
				cpu1Turn = false;
				cpu2Turn = false;
				if (skipped)
					cpu3Turn = true;
				else
					playerTurn = true;
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

		// Test code to see how many cards CPU has.
		if (cpuHand.getSize() > 1) {
			System.out.println("CPU 1 has " + cpuHand.getSize() + " cards.");
		} else if (cpuHand.getSize() == 1) {
			System.out.println("CPU 1 has " + cpuHand.getSize() + " card.");
		}

	} // End cpu1Gameplay3CPU method.

	public static void cpu2Gameplay3CPU(Hand cpuHand, Hand playerHand, Hand cpu1Hand, Hand cpu3Hand, boolean[] partyRules) {
		
		// *** STACKING *** 
		if (stackCount > 0 && partyRules[2]) {
			
			try {
			
				if (cpuHand.getStackableCount() > 0) {
					ArrayList<Card> stackList = cpuHand.getStackableArray(cpuHand);
	
					// *** PLAY FIRST STACKABLE CARD ***
					// .get(0) is causing bugs *************************8
					gamePile.addCard(stackList.get(0)); // play card to game pile
					System.out.println("CPU 2 played: " + stackList.get(0)); // say which card cpu played
					cpuHand.remove(stackList.get(0));// remove it from cpuHand
	
					// *** ADD VALUE TO STACK COUNT ***
					if (stackList.get(0).getValue() == 12) {
						System.out.println("+2 added to stack");
						stackCount += 2;
					} else if (stackList.get(0).getValue() == 14) {
						System.out.println("+4 added to stack");
						stackCount += 4;
					}
	
				} else { // *** ADD STACK TO CPU HAND AND RESET IT ***
					for (int i = 0; i < stackCount; i++) {
						cpuHand.add(gameDeck.getRandomCard());
					}
					System.out.println("CPU 2 didn't stack and drew " + stackCount + " cards!");
					stackCount = 0;
				}
	
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu2Turn = false;
					cpu1Turn = false;
					cpu3Turn = true;
					
				} else if (gameFlow == 2) { // counter clockwise
					cpu2Turn = false;
					cpu3Turn = false;
					cpu1Turn = true;
				}
			}
			
			catch(IndexOutOfBoundsException e) {
				for (int i = 0; i < stackCount; i++) {
					cpuHand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU  2 didn't stack and drew " + stackCount + " cards!");
				stackCount = 0;
				
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu2Turn = false;
					cpu1Turn = false;
					cpu3Turn = true;
					
				} else if (gameFlow == 2) { // counter clockwise
					cpu2Turn = false;
					cpu3Turn = false;
					cpu1Turn = true;
				}
				
			} // end catch
			
		} else { // *** ELSE JUST PLAY REGULAR GAME
		
		
		
		// creates a hand of playable cards
		int playableCount = 0;
		int wildPlus4Amount = 0;


		// checks cards in hand to see if they are playable
		for (int i = 0; i < cpuHand.getSize(); i++) {
			if (cpuHand.getCard(i).match(gamePile.getTopCard())) {
				// adds cards to the new hand
				playableCount++;
			}
			if (cpuHand.getCard(i).getValue() ==  14 || cpuHand.getCard(i).getValue() == 13) {
				wildPlus4Amount++;
			}
		}

		// cpuHand.add(new Card(10, "Red"));
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
				System.out.println("CPU 2 drew a card.");
				break;
			} // end else if

			else {
				// cpu flips a coin to see if they play each playable card
				for (int i = 0; i < cpuHand.getSize(); i++) {
					coinflip = random.nextDouble();
					
					if(partyRules[1] && playableCount > wildPlus4Amount && cpuHand.getCard(i).getValue()==14) {
							System.out.println("CPU 2: I can't bluff, trying again!");
					} // skipping bluffing if no bluff rule is activated
					else if (cpuHand.getCard(i).match(gamePile.getTopCard()) && coinflip < 0.5) {// if cards match and
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
		
		// *** 0-7 Rule *** - CPU1
		if (partyRules[0] && (gamePile.getTopCard().getValue() == 0 || gamePile.getTopCard().getValue() == 7)) {
			
			System.out.println("0-7 rule activated");
			
			if(gamePile.getTopCard().getValue() == 0) {
				
				if(gameFlow == 1) {
					System.out.println("Hands rotated clockwise!");
					Hand.switchHands(playerHand, cpu1Hand);
					Hand.switchHands(playerHand, cpuHand);
					Hand.switchHands(playerHand, cpu3Hand);

				}
				else if(gameFlow == 2) {
					System.out.println("Hans rotated counter-clockwise");
					Hand.switchHands(playerHand, cpu3Hand);
					Hand.switchHands(playerHand, cpuHand);
					Hand.switchHands(playerHand, cpu1Hand);
				}
				
			}// end 0 rule
		
			else if(gamePile.getTopCard().getValue() == 7) {
				
				Random rand = new Random();
				int ranInt = rand.nextInt(3 - 1 + 1) + 1;
				
				switch(ranInt) {
					case 1 -> Hand.switchHands(cpuHand, playerHand); //System.out.println("CPU 1 switch hands with the player!");
					case 2 -> Hand.switchHands(cpuHand, cpu1Hand);
					case 3 -> Hand.switchHands(cpuHand, cpu3Hand);
				}
				
				switch(ranInt) {
					case 1 -> System.out.println("CPU 2 switched hands with the player!");
					case 2 -> System.out.println("CPU 2 switched hands with the CPU 1!");
					case 3 -> System.out.println("CPU 2 switched hands with the CPU 3!");
				}
				
				
			} // end 7 rule

		
		} // end 0-7 rule

		// *** Reverse card - CPU2 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && playableCount > 0) {
			gameFlow = 2;
			System.out.println("The game order was switched to counter-clockwise.");
		} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && playableCount > 0) {
			gameFlow = 1;
			System.out.println("The game order was switched to clockwise.");
		}

		// *** Wild card - CPU2 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 13 && playableCount > 0) {
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			if (colorChoice == 1) {
				System.out.println("CPU 2 played a Blue Wild Card.");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 2) {
				System.out.println("CPU 2 played a Green Wild Card.");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 3) {
				System.out.println("CPU 2 played a Red Wild Card.");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 4) {
				System.out.println("CPU 2 played a Yellow Wild Card.");
				gamePile.addCard(new Card("Yellow"));
			}
		} // End Wild card if

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 2 Wins!");
		}

		boolean skipped = false;
		// *** Skip card - CPU2 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 10 && playableCount > 0) {
			// skip code for CLOCKWISE order
			skipped = true;
			if (gameFlow == 1) {
				System.out.println("CPU 2 skipped CPU 3's turn.");
			}
			// skip code for COUNTER-CLOCKWISE order
			if (gameFlow == 2) {
				System.out.println("CPU 2 skipped CPU 1's turn.");
			}
		}

		// *** Plus Two card - CPU2 - Multiplayer ***
		else if (gamePile.getTopCard().getValue() == 12 && playableCount > 0) { // plus2
			skipped = true;
			if (gameFlow == 1) {
				// Forces CPU 3 to draw 2 random cards.
				for (int i = 0; i < 2; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 3 drew 2 cards and their turn was skipped.");
			} else if (gameFlow == 2) {
				// Forces CPU 1 to draw 2 random cards.
				for (int i = 0; i < 2; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 1 drew 2 cards and their turn was skipped.");
			}
		} // End Plus Two else if

		// *** Wild Plus Four card - CPU2 - Multiplayer ***
		else if (gamePile.getTopCard().getValue() == 14 && playableCount > 0) {
			skipped = true;
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			// If gameFlow is 1, skip CPU3's turn and make them draw four cards.
			if (colorChoice == 1 && gameFlow == 1) {
				System.out.println("CPU 2 played a Blue Wild Plus Four Card.");
				gamePile.addCard(new Card("Blue"));
				for (int i = 0; i < 4; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 2 && gameFlow == 1) {
				System.out.println("CPU 2 played a Green Wild Plus Four Card.");
				gamePile.addCard(new Card("Green"));
				for (int i = 0; i < 4; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 3 && gameFlow == 1) {
				System.out.println("CPU 2 played a Red Wild Plus Four Card.");
				gamePile.addCard(new Card("Red"));
				for (int i = 0; i < 4; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 4 && gameFlow == 1) {
				System.out.println("CPU 2 played a Yellow Wild Plus Four Card.");
				gamePile.addCard(new Card("Yellow"));
				for (int i = 0; i < 4; i++) {
					cpu3Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 3 drew 4 cards and their turn was skipped.");
			}
			// If gameFlow is 2, skip CPU1's turn and make them draw four cards.
			else if (colorChoice == 1 && gameFlow == 2) {
				System.out.println("CPU 2 played a Blue Wild Plus Four Card.");
				gamePile.addCard(new Card("Blue"));
				for (int i = 0; i < 4; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 2 && gameFlow == 2) {
				System.out.println("CPU 2 played a Green Wild Plus Four Card.");
				gamePile.addCard(new Card("Green"));
				for (int i = 0; i < 4; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 3 && gameFlow == 2) {
				System.out.println("CPU 2 played a Red Wild Plus Four Card.");
				gamePile.addCard(new Card("Red"));
				for (int i = 0; i < 4; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 4 && gameFlow == 2) {
				System.out.println("CPU 2 played a Yellow Wild Plus Four Card.");
				gamePile.addCard(new Card("Yellow"));
				for (int i = 0; i < 4; i++) {
					cpu1Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 1 drew 4 cards and their turn was skipped.");
			}
		} // End Wild Plus Four card else if

			// cpu ends turn, happens regardless. Switches turn and waits 1 second
			if (gameFlow == 1) { // clockwise
				cpu2Turn = false;
				cpu1Turn = false;
				if (skipped)
					playerTurn = true;
				else
					cpu3Turn = true;
			} else if (gameFlow == 2) { // counter clockwise
				cpu2Turn = false;
				cpu3Turn = false;
				if (skipped) // Skipped gameplay loop
					playerTurn = true;
				else // Regular gameplay loop
					cpu1Turn = true;
			}
		
		//end else goes here , code beyond is always executed
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

		// Test code to see how many cards CPU has.
		if (cpuHand.getSize() > 1) {
			System.out.println("CPU 2 has " + cpuHand.getSize() + " cards.");
		} else if (cpuHand.getSize() == 1) {
			System.out.println("CPU 2 has " + cpuHand.getSize() + " card.");
		}
	} // end cpu2Gameplay3CPU method.

	public static void cpu3Gameplay3CPU(Hand cpuHand, Hand playerHand, Hand cpu1Hand, Hand cpu2Hand, boolean[] partyRules) {
		
		// *** STACKING *** 
		if (stackCount > 0 && partyRules[2]) {
			
			try {
			
				if (cpuHand.getStackableCount() > 0) {
					ArrayList<Card> stackList = cpuHand.getStackableArray(cpuHand);
	
					// *** PLAY FIRST STACKABLE CARD ***
					// .get(0) is causing bugs *************************8
					gamePile.addCard(stackList.get(0)); // play card to game pile
					System.out.println("CPU 3 played: " + stackList.get(0)); // say which card cpu played
					cpuHand.remove(stackList.get(0));// remove it from cpuHand
	
					// *** ADD VALUE TO STACK COUNT ***
					if (stackList.get(0).getValue() == 12) {
						System.out.println("+2 added to stack");
						stackCount += 2;
					} else if (stackList.get(0).getValue() == 14) {
						System.out.println("+4 added to stack");
						stackCount += 4;
					}
	
				} else { // *** ADD STACK TO CPU HAND AND RESET IT ***
					for (int i = 0; i < stackCount; i++) {
						cpuHand.add(gameDeck.getRandomCard());
					}
					System.out.println("CPU 3 didn't stack and drew " + stackCount + " cards!");
					stackCount = 0;
				}
	
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu3Turn = false;
					cpu2Turn = false;
					playerTurn = true;
					
				} 
				else if (gameFlow == 2) { // counter clockwise
					cpu3Turn = false;
					playerTurn = false;
					cpu2Turn = true;
				}
			}
			
			catch(IndexOutOfBoundsException e) {
				for (int i = 0; i < stackCount; i++) {
					cpuHand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU  3 didn't stack and drew " + stackCount + " cards!");
				stackCount = 0;
				
				// *** SWITCH TURN ***
				if (gameFlow == 1) { // clockwise
					cpu3Turn = false;
					cpu2Turn = false;
					playerTurn = true;
					
				} 
				else if (gameFlow == 2) { // counter clockwise
					cpu3Turn = false;
					playerTurn = false;
					cpu2Turn = true;
				}
			} // end catch
			
		} else { // *** ELSE JUST PLAY REGULAR GAME
		
		
		
		
		// creates a hand of playable cards
		int playableCount = 0;
		int wildPlus4Amount = 0;


		// checks cards in hand to see if they are playable
		for (int i = 0; i < cpuHand.getSize(); i++) {
			if (cpuHand.getCard(i).match(gamePile.getTopCard())) {
				// adds cards to the new hand
				playableCount++;
			}
			if (cpuHand.getCard(i).getValue() ==  14 || cpuHand.getCard(i).getValue() == 13) {
				wildPlus4Amount++;
			}
		}

		// cpuHand.add(new Card(10, "Red"));
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
				System.out.println("CPU 3 drew a card.");
				break;
			} // end else if

			else {
				// cpu flips a coin to see if they play each playable card
				for (int i = 0; i < cpuHand.getSize(); i++) {
					coinflip = random.nextDouble();
					if(partyRules[1] && playableCount > wildPlus4Amount && cpuHand.getCard(i).getValue()==14) {
						System.out.println("CPU: I can't bluff, trying again!");
					} // skipping bluffing if no bluff rule is activated
					else if (cpuHand.getCard(i).match(gamePile.getTopCard()) && coinflip < 0.5) {// if cards match and
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
		
		// *** 0-7 Rule *** - CPU1
		if (partyRules[0] && (gamePile.getTopCard().getValue() == 0 || gamePile.getTopCard().getValue() == 7)) {
			
			System.out.println("0-7 rule activated");
			
			if(gamePile.getTopCard().getValue() == 0) {
				
				if(gameFlow == 1) {
					System.out.println("Hands rotated clockwise!");
					Hand.switchHands(playerHand, cpu1Hand);
					Hand.switchHands(playerHand, cpu2Hand);
					Hand.switchHands(playerHand, cpuHand);

				}
				else if(gameFlow == 2) {
					System.out.println("Hans rotated counter-clockwise");
					Hand.switchHands(playerHand, cpuHand);
					Hand.switchHands(playerHand, cpu2Hand);
					Hand.switchHands(playerHand, cpu1Hand);
				}
				
			}// end 0 rule
		
			else if(gamePile.getTopCard().getValue() == 7) {
				
				Random rand = new Random();
				int ranInt = rand.nextInt(3 - 1 + 1) + 1;
				
				switch(ranInt) {
					case 1 -> Hand.switchHands(cpuHand, playerHand); //System.out.println("CPU 1 switch hands with the player!");
					case 2 -> Hand.switchHands(cpuHand, cpu1Hand);
					case 3 -> Hand.switchHands(cpuHand, cpu2Hand);
				}
				
				switch(ranInt) {
					case 1 -> System.out.println("CPU 3 switched hands with the player!");
					case 2 -> System.out.println("CPU 3 switched hands with the CPU 1!");
					case 3 -> System.out.println("CPU 3 switched hands with the CPU 2!");
				}
				
				
			} // end 7 rule

		
		} // end 0-7 rule

		// *** Reverse card - CPU3 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 11 && gameFlow == 1 && playableCount > 0) {
			gameFlow = 2;
			System.out.println("The game order was switched to counter-clockwise.");
		} else if (gamePile.getTopCard().getValue() == 11 && gameFlow == 2 && playableCount > 0) {
			gameFlow = 1;
			System.out.println("The game order was switched to clockwise.");
		}

		// *** Wild card - CPU3 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 13 && playableCount > 0) {
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			if (colorChoice == 1) {
				System.out.println("CPU 3 played a Blue Wild Card.");
				gamePile.addCard(new Card("Blue"));
			} else if (colorChoice == 2) {
				System.out.println("CPU 3 played a Green Wild Card.");
				gamePile.addCard(new Card("Green"));
			} else if (colorChoice == 3) {
				System.out.println("CPU 3 played a Red Wild Card.");
				gamePile.addCard(new Card("Red"));
			} else if (colorChoice == 4) {
				System.out.println("CPU 3 played a Yellow Wild Card.");
				gamePile.addCard(new Card("Yellow"));
			}
		} // End Wild card if

		if (cpuHand.getSize() == 0) { // if CPU has no cards, end game **This check is added after cards are played to
										// catch a bug
			gameComplete = true;
			System.out.println("CPU 3 Wins!");
		}

		boolean skipped = false;
		// *** Skip card - CPU3 - Multiplayer ***
		if (gamePile.getTopCard().getValue() == 10 && playableCount > 0 && cpu3Turn) {
			// skip code for CLOCKWISE order
			skipped = true;
			if (gameFlow == 1) {
				System.out.println("CPU 3 skipped your turn.");
			}
			// skip code for COUNTER-CLOCKWISE order
			if (gameFlow == 2) {
				System.out.println("CPU 3 skipped CPU 2's turn.");
			}
		}

		// *** Plus Two card - CPU3 - Multiplayer ***
		else if (gamePile.getTopCard().getValue() == 12 && playableCount > 0) { // plus2
			skipped = true;
			if (gameFlow == 1) {
				// Forces player to draw 2 random cards.
				for (int i = 0; i < 2; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 2 cards and your turn was skipped.");
			} else if (gameFlow == 2) {
				// Forces CPU 2 to draw 2 random cards.
				for (int i = 0; i < 2; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 2 cards and their turn was skipped.");
			}
		} // End Plus Two else if

		// *** Wild Plus Four card - CPU3 - Multiplayer ***
		else if (gamePile.getTopCard().getValue() == 14 && playableCount > 0) {
			skipped = true;
			int colorChoice = 0;
			Random rand = new Random();
			colorChoice = rand.nextInt(4) + 1; // Generate random number between 1 and 4.

			// System.out.println("CPU colorChoice is " + colorChoice); Test print statement
			// to confirm CPU can play only the four valid colors.
			// If gameFlow is 1, skip player's turn and make them draw four cards.
			if (colorChoice == 1 && gameFlow == 1) {
				System.out.println("CPU 3 played a Blue Wild Plus Four Card.");
				gamePile.addCard(new Card("Blue"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 2 && gameFlow == 1) {
				System.out.println("CPU 3 played a Green Wild Plus Four Card.");
				gamePile.addCard(new Card("Green"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 3 && gameFlow == 1) {
				System.out.println("CPU 3 played a Red Wild Plus Four Card.");
				gamePile.addCard(new Card("Red"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			} else if (colorChoice == 4 && gameFlow == 1) {
				System.out.println("CPU 3 played a Yellow Wild Plus Four Card.");
				gamePile.addCard(new Card("Yellow"));
				for (int i = 0; i < 4; i++) {
					playerHand.add(gameDeck.getRandomCard());
				}
				System.out.println("You drew 4 cards and your turn was skipped.");
			}
			// If gameFlow is 2, skip CPU2's turn and make them draw four cards.
			else if (colorChoice == 1 && gameFlow == 2) {
				System.out.println("CPU 3 played a Blue Wild Plus Four Card.");
				gamePile.addCard(new Card("Blue"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 2 && gameFlow == 2) {
				System.out.println("CPU 3 played a Green Wild Plus Four Card.");
				gamePile.addCard(new Card("Green"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 3 && gameFlow == 2) {
				System.out.println("CPU 3 played a Red Wild Plus Four Card.");
				gamePile.addCard(new Card("Red"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			} else if (colorChoice == 4 && gameFlow == 2) {
				System.out.println("CPU 3 played a Yellow Wild Plus Four Card.");
				gamePile.addCard(new Card("Yellow"));
				for (int i = 0; i < 4; i++) {
					cpu2Hand.add(gameDeck.getRandomCard());
				}
				System.out.println("CPU 2 drew 4 cards and their turn was skipped.");
			}
		} // End Wild Plus Four card else if

			// cpu ends turn, happens regardless. Switches turn and waits 1 second
			if (gameFlow == 1) { // clockwise
				cpu3Turn = false;
				cpu2Turn = false;
				if (skipped)
					cpu1Turn = true;
				else
					playerTurn = true;
			} else if (gameFlow == 2) { // counter clockwise
				cpu3Turn = false;
				playerTurn = false;
				if (skipped)
					cpu1Turn = true;
				else
					cpu2Turn = true;
			}
			
		//put end bracket here, all code after is always executed	
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException c) {
			c.printStackTrace();
		}

		// Test code to see how many cards CPU has.
		if (cpuHand.getSize() > 1) {
			System.out.println("CPU 3 has " + cpuHand.getSize() + " cards.");
		} else if (cpuHand.getSize() == 1) {
			System.out.println("CPU 3 has " + cpuHand.getSize() + " card.");
		}
	} // End cpu3Gameplay3CPU method.

	public static void threeCPU(Scanner input, Hand playerHand, Hand cpu1Hand, Hand cpu2Hand, Hand cpu3Hand,
			boolean[] partyRules) {

		boolean playAgain = true;

		while (playAgain) {

			startGame3CPU(playerHand, cpu1Hand, cpu2Hand, cpu3Hand); // method to start game

			// If the player chooses to play a new game and the previous game was in
			// counter-clockwise order, reset the order to clockwise.
			if (!(gamePile.getTopCard().getValue() == 11) && gameFlow == 2) {
				gameFlow = 1;
			}
			do { // game will be played inside of this do while loop, and to end the game we will
					// need to mark gameComplete as true

				// make sure there are cards in gamedeck
				if (gameDeck.isEmpty()) {
					gameDeck.reshuffle();
				}

				if (gameFlow == 1 && gameComplete == false) {// clockwise gameplay

					if (playerTurn && gameComplete == false) {
						playerGameplay3CPU(input, playerHand, cpu1Hand, cpu2Hand, cpu3Hand, partyRules);
					} // Code for player's turn
					else if (cpu1Turn && gameComplete == false) {
						cpu1Gameplay3CPU(cpu1Hand, playerHand, cpu2Hand, cpu3Hand, partyRules);
					} // Code for CPU1's turn.
					else if (cpu2Turn && gameComplete == false) {
						cpu2Gameplay3CPU(cpu2Hand, playerHand, cpu1Hand, cpu3Hand, partyRules);
					} // Code for CPU2's turn.
					else if (cpu3Turn && gameComplete == false) {
						cpu3Gameplay3CPU(cpu3Hand, playerHand, cpu1Hand, cpu2Hand, partyRules);
					} // Code for CPU3's turn.
					else {
					} // catch errors and exceptions
				}

				else if (gameFlow == 2 && gameComplete == false) {// counter clockwise gameplay

					// Code for player's turn.
					if (playerTurn && gameComplete == false) {
						playerGameplay3CPU(input, playerHand, cpu1Hand, cpu2Hand, cpu3Hand, partyRules);
					}
					// Code for CPU3's turn.
					else if (cpu3Turn && gameComplete == false) {
						cpu3Gameplay3CPU(cpu3Hand, playerHand, cpu1Hand, cpu2Hand, partyRules);
					}
					// Code for CPU2's turn.
					else if (cpu2Turn && gameComplete == false) {
						cpu2Gameplay3CPU(cpu2Hand, playerHand, cpu1Hand, cpu3Hand, partyRules);
					}
					// Code for CPU1's turn.
					else if (cpu1Turn && gameComplete == false) {
						cpu1Gameplay3CPU(cpu1Hand, playerHand, cpu2Hand, cpu3Hand, partyRules);
					}

				}

			} while (!gameComplete);

			playAgain = againChecker(input);

		} // end playAgain loop
	} // End threeCPU method.

	private static void userInput(Scanner input, Hand playerHand, int[] matchAmounts, boolean[] partyRules) {

		// get user input for next int
		String choice;
		int selection;

		while (true) {
			try {
				System.out.print(
						"\nType the number of the card you would like to play (Enter F to forfeit or D to draw a card): ");
				choice = input.next();

				// If the user types f or F, allow the user to forfeit the game.
				if (choice.strip().equalsIgnoreCase("F")) {
					// Enter code to print that game is complete
					gameComplete = true;
					break;
				} // end if

				// If the user types d or D, allow the user to draw a card.
				else if (choice.strip().equalsIgnoreCase("D")) {
					playerHand.add(gameDeck.getRandomCard());
					System.out.println("You drew 1 card.");
					// Set draw to true, which avoids errors related to skip functionality. For
					// example, if the top card on the discard pile is a Blue Skip and the player
					// chooses to draw a card when they could have played a card from their hand,
					// the top card's value is still 10. This would wrongly execute skip code on the
					// CPU an extra time. This applies to Plus Two and Wild Plus Four cards as well,
					// since they skip the next player's turn automatically.
					draw = true;
					break;
				} // end else if

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
					if(partyRules[1] && (matchAmounts[0] > matchAmounts[1]) && playerHand.getCard(selection - 1).getValue()==14) {
						System.out.println("You are not allowed to bluff the wild plus 4 card. Please select another card!");
					}
					else if (playerHand.getCard(selection - 1).match(gamePile.getTopCard())) {// if card matches top card
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
				System.out.print("\nValue must be Y or N. Enter again: ");
			}
			if (loopCount == 10) {
				return false;
			}
		} // end while loop
	}// end getYesNo method

} // end OverZeroUnderTwo Class
