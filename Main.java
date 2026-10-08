import java.util.*;
import java.io.*;
public class Main {
    public static ArrayList<String> cards = new ArrayList<>();
    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String file = "Uno.txt";
        FileReader fileReader = new FileReader(file);
        Scanner reader = new Scanner(fileReader);
        while(reader.hasNextInt()){
            int numberOfCards = reader.nextInt();
            String cardType = reader.nextLine();
            if(cardType.charAt(1) != 'W'){
                for(int i = numberOfCards; i > 0; i--){
                    cards.add("G"+cardType.substring(1,cardType.length()));
                    cards.add("R"+cardType.substring(1,cardType.length()));
                    cards.add("B"+cardType.substring(1,cardType.length()));
                    cards.add("Y"+cardType.substring(1,cardType.length()));
                }
            } else {
                for(int i = numberOfCards; i > 0; i--){
                    cards.add(cardType.substring(1,cardType.length()));
                }
            }
        }
        System.out.println(cards);
        System.out.println(cards.size());
        reader.close();
        scanner.close();
    }
}
