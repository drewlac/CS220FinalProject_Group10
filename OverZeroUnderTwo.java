import java.util.Random;
import java.util.Scanner;

public class OverZeroUnderTwo {
	
	public static boolean gameComplete = false;
	public static boolean playerTurn = false;
	public static boolean cpuTurn = false;

	public static void main(String[] args) { // start main method
	
		
	startGame();
		
	do { //game will be played inside of this do while loop, and to end the game we will need to mark gameComplete as true
		
	if(playerTurn) {playerGameplay();} //code for players turn
	else if(cpuTurn) {cpuGameplay();} // code for cpu's turn
	else {} // catch errors and exceptions
		
		
	}
	while(!gameComplete);

		

	} // end main method
	
	public static void startGame() {
		Random rand = new Random();
		int coinflip = 0;
		coinflip = rand.nextInt(2)+1;
		
		if(coinflip == 1)
			playerTurn = true;
		else if(coinflip == 2)
			cpuTurn = true;
		else {
			System.out.println("Some sorta error, must have got the coinflip wrong");
		}//end else
	} // end method startGame
	
	public static void playerGameplay() {} //use to code player turn
	public static void cpuGameplay() {} //use to code cpu turn

} // end OverZeroUnderTwo Class
