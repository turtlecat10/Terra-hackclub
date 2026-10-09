import java.util.*;
import java.io.*;
public class Main {
    public static ArrayList<String> cards = new ArrayList<>();
    public static ArrayList<String> discard = new ArrayList<>();
    public static ArrayList<ArrayList<String>> hands = new ArrayList<>();
    public static ArrayList<String> playerNames = new ArrayList<>();
    public static String file = "Uno.txt";
    public static int numPlayers;
    public static boolean running = true;
    public static int direction = 1;
    public static int selected = 0;
    public static int turnOrder = 0;
    public static void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        Setup.deck();
        Setup.players(scanner);
        Setup.shuffle();
        Setup.hand();
        while(running){
            Run.turn(scanner);
        }
        scanner.close();
    }
}
