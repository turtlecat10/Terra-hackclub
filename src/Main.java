package src;
import java.util.*;
import java.io.*;
public class Main {
    public static ArrayList<String> cards = new ArrayList<>();
    public static ArrayList<String> discard = new ArrayList<>();
    public static ArrayList<ArrayList<String>> hands = new ArrayList<>();
    public static ArrayList<String> playerNames = new ArrayList<>();
    public static ArrayList<Integer> selectedCards = new ArrayList<>();
    public static ArrayList<String> tempDiscard = new ArrayList<>();
    public static ArrayList<String> order = new ArrayList<>();
    public static ArrayList<String> losers = new ArrayList<>();
    public static String colour = "";
    public static String tempColour = "";
    public static String file = Setup.unoGame();
    public static int maxHandSize = 25;
    public static int numPlayers;
    public static boolean running = true;
    public static int direction = 1;
    public static int selected = 0;
    public static int turnOrder = 0;
    public static int adding = 0;
    public static int wildSelected = 0;
    public static boolean wildTime = false;
    public static int skipTurn = 0;
    public static boolean prevDeclaredUno = false;
    public static boolean declaredUno = false;
    public static boolean unoed = false;
    public static int prevOrder = 0;
    public static boolean pass = false;
    public static int numRemain = 1;
    public static boolean removed = false;
    public static boolean turnAgain = false;
    public static boolean colourRoulette = false;
    public static boolean seven = false;
    public static int target;
    public static boolean chosen;
    public static void clear(){
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                // Windows packaged .exe clear execution fallback
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Standard Unix/Linux/macOS ANSI clear
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Safe fallback if environment stream is blocked
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    public static void displayWinners(){
        clear();
        for(int i = 0; i < order.size(); i++){
            System.out.println((i+1)+": "+order.get(i));
        }
        System.out.println((order.size()+1)+": "+playerNames.get(0));
    }

    public static void displayLosers(){
        for(int i = 0; i < losers.size(); i++){
            System.out.println((order.size()+1+i)+": "+losers.get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        // --- NATIVE PACKAGED WINDOWS CONSOLE ALLOCATION ---
        // Forces a command window attach if the packaged .exe spawns as a detached GUI process
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            if (System.console() == null) {
                try {
                    // Re-opens and attaches a fresh CMD window to host the packaged app
                    new ProcessBuilder("cmd", "/c", "echo Off && cls").inheritIO().start();
                } catch (Exception e) {
                    // Suppressed, fallback to standard stream
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        Setup.deck();
        Setup.players(scanner);
        Setup.shuffle();
        Setup.hand();
        KeyListenerUno listenerWindow = new KeyListenerUno();
        
        while(running){
            Run.turn(scanner);
        }
        
        displayWinners();
        displayLosers();
        scanner.close();
    }
}