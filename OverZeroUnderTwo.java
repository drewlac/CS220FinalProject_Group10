import java.io.IOException;
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
		
		Scanner input = new Scanner(System.in);

		do { // game will be played inside of this do while loop, and to end the game we will
				// need to mark gameComplete as true

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
	public static void playerGameplay(Scanner input) { // use to code player turn
		System.out.println("\nThe top of the card on the pile is:\n" + gamePile.getTopCard() + "\n");
		
		//prints the player's hand
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
		
		if(playerHand.getSize() == 0) {gameComplete = true;}
		playerTurn = false;
		cpuTurn = true;
	} 

	public static void cpuGameplay() {
		//creates a hand of playable cards
		Hand playable = new Hand ();
		
		//checks cards in hand to see if they are playable
		for(int i =0; i<cpuHand.getSize();i++) {
			if(cpuHand.match(gamePile.getTopCard())) {
				//adds cards to the new hand
				playable.add(cpuHand.getCard(i));
			}
		}
		//if the list is 0 cpu draws a card and alerts player
		if(playable.getSize()==0) {
			cpuHand.add(gameDeck.getRandomCard());
			System.out.println("CPU drew a card");
		}
		
		//cpu plays random card from playable hand
		Random random = new Random();
		int cpuPlay = random.nextInt(playable.getSize()-1);
		
		gamePile.addCard(playable.getCard(cpuPlay));
		cpuHand.remove(playable.getCard(cpuPlay));
		
		//if cpu has one card left cpu alerts player
		if(cpuHand.getSize()==1) {
			System.out.println("CPU: Uno!");
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

	private static void userInput(Scanner input) {
		System.out.println("Type the number of the card you would like to play (Enter F to forfeit): ");
		
		//get user input for next int
		String choice;
		int selection;
		
		while(true) {
			try {
				choice = input.nextLine();
		
				// If the user types d or D, allow the user to draw a card.
				if (choice.equalsIgnoreCase("F")) {
					// Enter code to print that game is complete
					gameComplete = true;
				}//end if
		
				//switching input to a string
				selection = Integer.parseInt(choice);
		
				//Number must be greater than zero and no greater than the size of the hand
				if(selection < 0 || selection > playerHand.getSize()) {
					throw new RuntimeException();
				}
				else {
					if(playerHand.getCard(selection-1).match(gamePile.getTopCard())) {//if card matches top card
						//Playing the selected card
						gamePile.addCard(playerHand.getCard(selection-1));
						System.out.println("You played: " + playerHand.getCard(selection-1));
						playerHand.remove(playerHand.getCard(selection-1));
						break;
					}//end nested if
					
					else {
						System.out.println("Card does not match the color or value of the top card. Please select another card");
					}//end nested else	
				}//end else	
			}//end try
			catch(RuntimeException e){System.out.println("Answer must be an integer between 1 and your max amount of cards!");}
			}
		
		
		
		

		// (Write this else...if later. Check the user input to determine if it matches
		// a card that he or she currently has. Possibly convert choice to lower case,
		// then compare result with current hand. Should we allow user to enter shortened
		// card entry, like B5 for blue five, as well as longer entry?)
		

		//check if card matches
		
		//valid if user can play card, otherwise print only cards they can play
		
		//if user can't play a card, make them draw a card. 
		
	} // user inputs
	
	

} // end OverZeroUnderTwo Class
