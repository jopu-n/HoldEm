import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;

/**
 * @author Johannes Natunen
 * @version 1.0
 * 
 * A simplified Texas Hold 'em game. Still has some issues and not completely working
 * (for example, a player should be able to start a new game without restarting the program).
 * Still recognizable as a Texas hold 'em game.
 * 
 * If the player's points exceed 30, they win.
 * If the player's points to or below 0, they lose.
 * 
 */
public class HoldEm2 {
    
    public static final String[] cardClass = 
    {"C","C","C","C","C","C","C","C","C","C","C","C","C", // C = Clubs / Risti
    "D","D","D","D","D","D","D","D","D","D","D","D","D", // D = Diamonds / Ruutu
    "H","H","H","H","H","H","H","H","H","H","H","H","H", // H = Hearts / Hertta
    "S","S","S","S","S","S","S","S","S","S","S","S","S"}; // S = Spades / Pata

    public static final int[] cardValue = 
    {2,3,4,5,6,7,8,9,10,11,12,13,14, // Value of a clubs card at a specific index
    2,3,4,5,6,7,8,9,10,11,12,13,14, // Value of a diamonds card at a specific index
    2,3,4,5,6,7,8,9,10,11,12,13,14, // Value of a hearts card at a specific index
    2,3,4,5,6,7,8,9,10,11,12,13,14}; // Value of a spades card at a specific index

    public static String[] playerCardClass = new String[2]; //The player's card colours 
    public static String[] cpuCardClass = new String[2];    // The computer's card colours
    public static String[] tableCardClass = new String[5];  // The table's card colours
    public static int[] playerCardValue = new int[2];   // The player's card values
    public static int[] cpuCardValue = new int[2];  // The computer's card values
    public static int[] tableCardValue = new int[5]; // The table's card values

    public static int totalScore = 10;  // The total score. Default amount is 10.

    public static final Random rnd = new Random();  
    public static final Scanner scan = new Scanner(System.in);

    /**
     * This method assigns cards for the player, 
     * the computer and the table. 
     * The cards are assigned randomly, and multiple
     * pieces of the same card can be assigned.
     * 
     */
    public static void assignCards(){
        for (int i = 0; i < 5; i++){
            if (i < 2){
                int rndNum = rnd.nextInt(cardClass.length - 1);
                playerCardClass[i] = cardClass[rndNum];
                playerCardValue[i] = cardValue[rndNum];
                rndNum = rnd.nextInt(cardClass.length - 1);
                cpuCardClass[i] = cardClass[rndNum];
                cpuCardValue[i] = cardValue[rndNum];
            }
            int rndNum = rnd.nextInt(cardClass.length - 1);
            tableCardClass[i] = cardClass[rndNum];
            tableCardValue[i] = cardValue[rndNum];
        }
    }


    /**
     * This method checks if there are any "pairs", "three of a kinds"
     * or "four of a kinds".
     * 
     * @param type Defines if the program will be handling the player's or the computer's cards.
     * @return Returns a value of 0, 1, 4 or 8, according to how valuable the cards given are. 
     */
    public static int ruleSameCards(String type){
        
        // Do we want to use the player's hand or the computer's hand? 
        int[] cardValue = new int[2]; // A copy of the player's or the computer's hand, which will be used later
        if (type.equals("player")) cardValue = playerCardValue.clone();
        else if (type.equals("cpu")) cardValue = cpuCardValue.clone();

        int pair = 0;
        int pair2 = 0;
            
        if (cardValue[0] == cardValue[1]) pair++; // To check if the player already has a pair in their hand
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 5; j++){
                if (cardValue[0] == cardValue[1] && i == 1) break; // We do not want to count the same number's pairs twice
                if (cardValue[i] == tableCardValue[j]){
                    if(cardValue[i] == cardValue[0]) pair++;
                    if(cardValue[i] == cardValue[1]) pair2++;
                } 
            }
        }
        // Are there any four of a kinds?
        if (pair > 2 || pair2 > 2){ 
            if (type.equals("player")) System.out.println("Pelaajalla on neljä samaa korttia!" + "\n" + "-----------------------------");
            if (type.equals("cpu")) System.out.println("Tietokoneella on neljä samaa korttia!" + "\n" + "-----------------------------");
            return 8;
        }
        // Are there any three of a kinds?
        else if (pair > 1 || pair2 > 1){
            if (type.equals("player")) System.out.println("Pelaajalla on kolme samaa korttia!" + "\n" + "-----------------------------");
            if (type.equals("cpu")) System.out.println("Tietokoneella on kolme samaa korttia!" + "\n" + "-----------------------------");
            return 4;
        }
        // Are there any pairs?
        else if (pair > 0 || pair2 > 0){
            if (type.equals("player")) System.out.println("Pelaajalla on pari!" + "\n" + "-----------------------------");
            if (type.equals("cpu")) System.out.println("Tietokoneella on pari!" + "\n" + "-----------------------------");
            return 1;
        }
        
        return 0; // Return the value 0 if there are no matching cards.
    }

    /**
     * This method checks if there are any "two pairs" or "full houses". 
     * Full house means that there is at least one pair and one three of a kind.
     * 
     * @param type Defines if the program will be handling the player's or the computer's cards.
     * @return Returns a value of 0, 3 or 7, according to how valuable the cards given are. 
     */
    public static int ruleTwoPairs(String type){
        // Do we want to use the player's hand or the computer's hand? 
        int[] cardValue = new int[2]; // A copy of the player's or the computer's hand, which will be used later
        if (type.equals("player")) cardValue = playerCardValue.clone();
        else if (type.equals("cpu")) cardValue = cpuCardValue.clone();

        int pair1 = 0;
        int pair2 = 0;
            
        if (cardValue[0] == cardValue[1]) return 0; // If the player has two same cards in their hand, they can't have two pairs or a full house
        
        for (int j = 0; j < 5; j++){
            if (cardValue[0] == tableCardValue[j]) pair1++;
        }
        for (int j = 0; j < 5; j++){
            if (cardValue[1] == tableCardValue[j]) pair2++;
        }
        
        if (pair1 == 1 && pair2 == 1){
            if (type.equals("player")) System.out.println("Pelaajalla on kaksi paria!" + "\n" + "-----------------------------");
            if (type.equals("cpu")) System.out.println("Tietokoneella on kaksi paria!" + "\n" + "-----------------------------");
            return 3;
        }
        if (pair1 >= 1 && pair2 > 1 || pair1 > 1 && pair2 >= 1){
            if (type.equals("player")) System.out.println("Pelaajalla on täyskäsi!" + "\n" + "-----------------------------");
            if (type.equals("cpu")) System.out.println("Tietokoneella on täyskäsi!" + "\n" + "-----------------------------");
            return 7;
        }
        

        return 0;
    }

    /**
     * This method checks if there are any "straights" in the given cards.
     * 
     * @param type Defines if the program will be handling the player's or the computer's cards.
     * @return Returns a value of 0 or 5, according to how valuable the cards given are. 
     */
    public static int ruleStraight(String type){
        // Do we want to use the player's hand or the computer's hand? 
        int[] cardValue = new int[2]; // A copy of the player's or the computer's hand, which will be used later
        if (type.equals("player")) cardValue = playerCardValue.clone();
        else if (type.equals("cpu")) cardValue = cpuCardValue.clone();

        int[] combined = new int[7]; // A new array, where we will mash together the cards in hand and the cards in the table
        int combinedPos = 0; // Used to define the location of the next item in the combined array
        for(int number : cardValue){
            combined[combinedPos] = number;
            combinedPos++;
        }
        for(int number : tableCardValue){
            combined[combinedPos] = number;
            combinedPos++;
        }
        Arrays.sort(combined); // Sort the array to be able to check for consecutive numbers

        for (int i = 0; i < 3; i++){
            if (combined[i] + 1 == combined[i+1]){
                if (combined[i] + 2 == combined[i+2]){          //  This is to check whether the numbers are 
                    if (combined[i] + 3 == combined[i+3]){      //  consecutive in the array. 
                        if (combined[i] + 4 == combined[i+4]){
                            if (type.equals("player")) System.out.println("Pelaajalla on suora!" + "\n" + "-----------------------------");
                            if (type.equals("cpu")) System.out.println("Tietokoneella on suora!" + "\n" + "-----------------------------");
                            return 5;
                        }
                    }
                }
            }
        }
        return 0;
    }


    /**
     * This method checks if there are any "flushes" in the given cards.
     * A flush means that there are 5 cards of the same colour on either 
     * side's hand, combined with the cards on the table.
     * 
     * @param type Defines if the program will be handling the player's or the computer's cards.
     * @return Returns a value of 0 or 6, according to how valuable the cards given are. 
     */
    public static int ruleFlush(String type){
        
        // Do we want to use the player's hand or the computer's hand? 
        
        String[] cardClass = new String[2]; // A copy of the player's or the computer's hand, which will be used later
        if (type.equals("player")) cardClass = playerCardClass.clone();
        else if (type.equals("cpu")) cardClass = cpuCardClass.clone();
            
        int flush = 0; // To count how many of the same card we have

        if (cardClass[0].equals(cardClass[1])) flush++; // To check if the player already has an equal colour in their hand
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 5; j++){
                if (cardClass[0].equals(cardClass[1]) && i == 1) break; // We do not want to count the same colour's equals twice
                
                if (cardClass[i].equals(tableCardClass[j])){
                    flush++; // counts the same colour cards.
                    
                    if(flush == 4){ 
                        if (type.equals("player")) System.out.println("Pelaajalla on väri!" + "\n" + "-----------------------------");
                        if (type.equals("cpu")) System.out.println("Tietokoneella on väri!" + "\n" + "-----------------------------");
                        return 6;
                    }
                } 
            }
        flush = 0;
        }
        return 0;
    }

    /**
     * If there is both a flush and a straight in the given cards,
     * it is called a "Straight Flush". It is the most valuable of
     * any possible combination of cards.
     * 
     * @param type Defines if the program will be handling the player's or the computer's cards.
     * @param flush A parameter originally from the flush method to define if there is a flush in the given cards
     * @param straight A parameter originally from the straight method to define if there is a straight in the given cards
     * @return Returns a value of 0 or 9, according to how valuable the cards given are. 
     */

    public static int ruleStraightFlush(String type, int flush, int straight){
        if (flush != 0 && straight != 0){
            if (type.equals("player")) System.out.println("Pelaajalla on värisuora!" + "\n" + "-----------------------------");
            if (type.equals("cpu")) System.out.println("Tietokoneella on värisuora!" + "\n" + "-----------------------------");
            return 9;
        }
        return 0;

    }
    

    /**
     * This method compares cards using different rules. The more valuable
     * hand will be declared the winner of the round.
     * 
     * @return Returns the player's score after comparing their cards with the computer's
     * @throws InterruptedException For Thread.sleep() that is used to slow down the process a bit for better understanding
     */
    public static int compareCards() throws InterruptedException{
        int playerHandValue = 0;    // The better the hand is, the higher its
        int cpuHandValue = 0;       // value is. These will be compared in the end.

        int playerSameCards = ruleSameCards("player");                  // Check if there are pairs or
        int cpuSameCards = ruleSameCards("cpu");                        // three / four of a kinds
        if (playerSameCards > playerHandValue) playerHandValue = playerSameCards;
        if (cpuSameCards > cpuHandValue) cpuHandValue = cpuSameCards;
        Thread.sleep(1000);

        int playerTwoPairs = ruleTwoPairs("player");                    // Check if there are two pairs
        int cpuTwoPairs = ruleTwoPairs("cpu");                          // or full houses 
        if (playerTwoPairs > playerHandValue) playerHandValue = playerTwoPairs;
        if (cpuTwoPairs > cpuHandValue) cpuHandValue = cpuTwoPairs;
        Thread.sleep(1000);

        int playerStraight = ruleStraight("player");                   // Check if there are straights
        int cpuStraight = ruleStraight("cpu");
        if (playerStraight > playerHandValue) playerHandValue = playerStraight;
        if (cpuStraight > cpuHandValue) cpuHandValue = cpuStraight;
        Thread.sleep(1000);
 
        int playerFlush = ruleFlush("player");                          // Check if there are flushes
        int cpuFlush = ruleFlush("cpu");
        if (playerFlush > playerHandValue) playerHandValue = playerFlush;
        if (cpuFlush > cpuHandValue) cpuHandValue = cpuFlush;
        Thread.sleep(1000);

        int playerStraightFlush = ruleStraightFlush("player", playerFlush, playerStraight);    // Check if there are
        int cpuStraightFlush = ruleStraightFlush("cpu", cpuFlush, cpuStraight);                // straight flushes
        if (playerStraightFlush > playerHandValue) playerHandValue = playerStraightFlush;
        if (cpuStraightFlush > cpuHandValue) cpuHandValue = cpuStraightFlush;
        Thread.sleep(1000);
        
        // Comparing cards. The higher value hand will win the game.

        if (playerHandValue > cpuHandValue){
            System.out.println("Voitit kierroksen! +5 pistettä.");
            return 5;
        }
        if (playerHandValue < cpuHandValue){
            System.out.println("Tietokone voitti kierroksen. -5 pistettä.");
            return -5;
        }
        if (playerHandValue == cpuHandValue){   // Comparison of the value of individual cards, in case of a tie.
            System.out.println("Tasapeli! Suuriarvoisin kortti voittaa.");
            Arrays.sort(playerCardValue);
            Arrays.sort(cpuCardValue);
            Thread.sleep(1000);
            if (playerCardValue[1] > cpuCardValue[1]){
                System.out.println("Voitit kierroksen! +5 pistettä.");
                return 5;
            }
            else if (playerCardValue[1] < cpuCardValue[1]){
                System.out.println("Tietokone voitti kierroksen. -5 pistettä.");
                return -5;
            }
            else{
                System.out.println("Tasapeli. Et voittanut tai hävinnyt pisteitä.");
                return 0;
            }
        }
        return 0; // If, for some reason, none of the above happen, a score of 0 will be returned.
        
    }


    /**
     * The main game method, where all the other methods required to run the game will be called from.
     * The player has an option to quit at any stage of the game. If the player quits early,
     * they will be punished for fewer points in comparison to a loss after comparing the cards.
     * 
     * @return Returns the player's score for the round, which will then modify the total score parameter.
     * @throws InterruptedException For Thread.sleep() that is used to slow down the process a bit for better understanding
     */
    public static int game() throws InterruptedException{
        int score = 0;
        assignCards(); // Assignation of all the cards
        System.out.println("Korttisi ovat:");   // Display the cards of the player.
        for (int i = 0; i < 2; i++){            // The player has the option to quit at this point.
            System.out.print(playerCardClass[i] + playerCardValue[i] + " ");
        }
        System.out.println( "\n" + "-----------------------------" + "\n" + "Haluatko jatkaa? y/n");
        String continueGame = scan.nextLine();
        if(continueGame.equals("n")){
            score--;
            System.out.println("-----------------------------" + "\n" + "Hävisit kierroksen. " + score + " Pistettä.");
            return score;
        }
        
        // Displaying 3 cards on the table at first. The player has the option to quit at any point.

        for (int i = 3; i < 6; i++){
            System.out.println("-----------------------------" + "\n" + "Korttisi ovat:");
            for (int j = 0; j < 2; j++){
                System.out.print(playerCardClass[j] + playerCardValue[j] + " ");
            }
            System.out.println("\n" + "-----------------------------" + "\n" + "Paljastetaan pöydällä oleva kortti: ");
            for (int j = 0; j < i ; j++){
                System.out.print(tableCardClass[j] + tableCardValue[j] + " ");
            }
            System.out.println("\n" + "Haluatko jatkaa? y/n");
            continueGame = scan.nextLine();
            if(continueGame.equals("n")){
                score = score - i;
                System.out.println("-----------------------------" + "\n" + "Hävisit kierroksen. " + score + " Pistettä.");
                return score;
            }
        }

        // Display the cards before comparing

        System.out.println( "-----------------------------" + "\n" + "Korttisi ovat:");
        for (int j = 0; j < 2; j++){
            System.out.print(playerCardClass[j] + playerCardValue[j] + " ");
        }
        System.out.println("\n" + "-----------------------------" + "\n" + "Pöydällä olevat kortit ovat: ");
        for (int j = 0; j < 5 ; j++){
            System.out.print(tableCardClass[j] + tableCardValue[j] + " ");
        }
        System.out.println("\n" + "-----------------------------" + "\n" + "Tietokoneen kortit ovat: ");
        for (int j = 0; j < 2 ; j++){
            System.out.print(cpuCardClass[j] + cpuCardValue[j] + " ");
        }
        System.out.print("\n");
        Thread.sleep(1000);
        score = compareCards();
        Thread.sleep(1000);
        return score;
    }


    /**
     * The program's main method. The player has the option to play the game,
     * look at past results or quit the game.
     * 
     * This method also writes down the results of the player for later inspection.
     * 
     * 
     * @param args unused
     * @throws InterruptedException For Thread.sleep() that is used to slow down the process a bit for better understanding
     * @throws IOException If writing the file cannot be done for some reason.
     * @throws FileNotFoundException If reading the file cannot be done for some reason.
     */
    public static void main(String[] args) throws InterruptedException, IOException, FileNotFoundException {
        
        System.out.println("Tervetuloa Texas Hold 'emiin. " + "\n" + "Paina 1, jos haluat pelata peliä." + "\n" + "Paina 2, jos haluat tarkastella aikaisempia tuloksia. "
         + "\n" + "Paina mitä tahansa muuta poistuaksesi pelistä. ");
        String i = scan.nextLine();

        if (i.equals("1")){
            while (true){
            totalScore = totalScore + game();
            System.out.println("-----------------------------" + "\n" + "Sinulla on tällä hetkellä " + totalScore + " pistettä." + "\n" + "-----------------------------");
            Thread.sleep(1000);
            if (totalScore >= 30){
                System.out.println("Voitit! "); 
                break;
            }
            if (totalScore <= 0){
                System.out.println("Hävisit! ");
                break;
            }
            System.out.println("Aloitetaan uusi kierros! "+ "\n" + "-----------------------------");
            Thread.sleep(1000);
            }
            try{
                PrintWriter writer = new PrintWriter(new FileWriter("HoldEmData.txt", true));
                if (totalScore >= 30){
                    writer.write("Voitto" + "\n");
                    writer.close();
                }
                if (totalScore <= 0){
                    writer.write("Häviö" + "\n");
                    writer.close();
                }
            
            } catch (IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else if (i.equals("2")){
            try{
                File data = new File("HoldEmData.txt");
                Scanner datascan = new Scanner(data);
                while(datascan.hasNextLine()){
                    String line = datascan.nextLine();
                    System.out.println(line);
                }
                datascan.close();
            } catch(FileNotFoundException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }
        else{
            System.exit(0);
        }
    }
}
