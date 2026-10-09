import java.util.*;
import java.io.*;
public class Setup {
    public static void deck() throws IOException 
    {
        FileReader fileReader = new FileReader(Main.file);
        Scanner reader = new Scanner(fileReader);
        while(reader.hasNextInt()){
            int numberOfCards = reader.nextInt();
            String cardType = reader.nextLine();
            if(cardType.charAt(1) != 'W'){
                for(int i = numberOfCards; i > 0; i--){
                    Main.cards.add("G"+cardType.substring(1,cardType.length()));
                    Main.cards.add("R"+cardType.substring(1,cardType.length()));
                    Main.cards.add("B"+cardType.substring(1,cardType.length()));
                    Main.cards.add("Y"+cardType.substring(1,cardType.length()));
                }
            } else {
                for(int i = numberOfCards; i > 0; i--){
                    Main.cards.add(cardType.substring(1,cardType.length()));
                }
            }
        }
        reader.close();
    }
    public static void players(Scanner playerScan)
    {
        boolean loop = true;
        System.out.println("How many players are there (2-10)?");
        while(loop){
            try {
                Main.numPlayers = playerScan.nextInt();
                loop = false;
            } catch (InputMismatchException e) {
                Main.clear();
                System.out.println("Please input a number");
                playerScan.nextLine();
            }
            if(Main.numPlayers < 2 || Main.numPlayers > 10){
                Main.clear();
                System.out.println("Please input an integer between 2-10");
                loop = true;
            }
        }
        Main.clear();
        playerScan.nextLine();
        for(int i = 1; i <= Main.numPlayers; i++){
            System.out.println("Player "+i+" what is your name?");
            Main.playerNames.add(playerScan.nextLine());
            Main.hands.add(new ArrayList<String>());
            Main.clear();
        }
    }
    public static void shuffle()
    {
        Collections.shuffle(Main.cards);
    }
    public static void hand()
    {
        for(int i = 0; i < Main.numPlayers; i++){
            for(int l = 0; l < 7; l++){
                Main.hands.get(i).add(Run.draw());
            }
        }
        Main.discard.add(Main.cards.get(0));
        Main.cards.remove(0);
    }
}
