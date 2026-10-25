package src;
import java.util.*;
import java.io.*;

public class Setup {
    public static void deck() throws IOException 
    {
        // --- FIXED FOR EXECUTABLE PACKAGING ---
        // Reads the file out of the packed .jar/.exe resource bundle instead of the disk file system
        InputStream inputStream = Setup.class.getResourceAsStream(Main.file);

        
        if (inputStream == null) {
            throw new FileNotFoundException("Could not find game configuration resource file: " + Main.file);
        }
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Scanner reader = new Scanner(bufferedReader);
        
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
        if(Main.discard.get(0).charAt(0) == 'W'){
            Random random = new Random();
            int randomNum = random.nextInt(4);
            switch(randomNum){
                case 0:
                    Main.colour = "R";
                    break;
                case 1:
                    Main.colour = "Y";
                    break;
                case 2:
                    Main.colour = "G";
                    break;
                case 3:
                    Main.colour = "B";
                    break;
            }
        }
        Main.cards.remove(0);
    }

    public static String unoGame(){
        Scanner scanning = new Scanner(System.in);
        int number = 0;
        while(true){
            Main.clear();
            System.out.println("Which game do you want to play?");
            System.out.println("1 for standard, 2 for no mercy, 3 for uno all wild");
            if(scanning.hasNextInt()){
                number = scanning.nextInt();
                if(number > 0 && number < 4){
                    break;
                }else{
                    System.out.println("Please input a number from 1-3");
                    try{
                        Thread.sleep(1500);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    Main.clear();
                }
            }else{
                scanning.next();
                System.out.println("Please input a valid integer");
                try{
                    Thread.sleep(1500);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                Main.clear();
            }
        }
        switch (number){
            case 1:
                return "Uno.txt";
            case 2:
                return "UnoNoMercy.txt";
            case 3:
                return "UnoAllWild.txt";
        }
        return "testing.txt";
    }
}
