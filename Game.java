/**
 * imports
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Francisco Cortes
 *         Tony Melano
 *         Mikey Cho
 *
 */
public class Game {

    /**
     * @param args
     */
    
    public static int wordLength;
    public static char[] word = null;
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        File dictionaryFile = new File("dictionary.txt");
        Scanner fileScanner = null;
        boolean continueBol = true;
        Scanner keyBoard = new Scanner(System.in);
        ArrayList<String> dictionaryList = new ArrayList<String>();
        ArrayList<String> baseList;
        
        
        
        try
        {
            fileScanner = new Scanner(dictionaryFile);
        }
        catch (FileNotFoundException e)
        {           
            System.out.println("Wrong file settup.");
        }
        
        
        
        while (fileScanner.hasNext()){
            dictionaryList.add(fileScanner.next());
        }
        
        
        
        while (continueBol){
            System.out.println("\f");
            baseList = (ArrayList<String>) dictionaryList.clone();
            int guessCount = 10;
            char[] wordsUsed = new char[100];
            int counter = 0;
           
            //Takes user input to 
            boolean validInput;
            do{
                validInput = false;
                System.out.println("How many letters are in the word.");
                wordLength = keyBoard.nextInt();
                if(wordLength > 1 && wordLength < 16)
                    validInput = true;
                else
                    System.out.println("Invalid Input");
            }while(!validInput);
            
                //reduces to words with wordLength
            word = new char[wordLength];
            reducer(baseList);
            for(int i=0;i<wordLength;i++){
                    word[i]='_';
            }
            
            boolean gameEnd = false; //initiallizes game
            
            while(!gameEnd){
                gameEnd = true;
                //print for prompt and
                System.out.println("Choose a letter. Words chosen: ");
                for(int i = 0; i < wordsUsed.length; i++){
                    if(Character.isLetter(wordsUsed[i]))
                        System.out.print(wordsUsed[i] +  ", ");
                }
                System.out.println();
                //reducing and storing character typed in
                String letterInput = keyBoard.next();
                char character = letterInput.charAt(0);
                baseList = reducer(baseList, character);
                wordsUsed[counter] = character;
                counter++;
                //checking whether to continue
                boolean underScore = false;                
                for(int k = 0; k < wordLength; k++){
                    if(word[k] == '_'){
                        underScore = true;
                    }
                    if(word[k] == character)
                        guessCount++;
                }
                print();
                if (underScore && guessCount != 0)
                    gameEnd = false;
                //Result printer
                if(gameEnd){
                    if(underScore && guessCount == 0){
                        System.out.println("You Lose. Word was : ");
                        Random rand = new Random();
                        System.out.print(baseList.get(rand.nextInt(baseList.size())));
                        System.out.println();
                    }
                    else
                        System.out.println("You Win");
                    }
                //print result and decrement guesses.
                guessCount--;
                
            }
        
            //Continues if the user wants to.
            System.out.println("Continue?");
            if(keyBoard.next().equalsIgnoreCase("no"))
                continueBol = false;        
        }
        
        keyBoard.close();
        fileScanner.close();
    
        
    }
    //reduces based on size
    public static void reducer(ArrayList<String> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).length() != wordLength){
                list.remove(i);
                i--;
            }
        }
    }
    //reduces arrayList and returns 
    public static ArrayList<String> reducer(ArrayList<String> list, char letter){
        Map<String, ArrayList<String>> mapable = new HashMap<String, ArrayList<String>>(50);
        
        String skeleKey = null;
        for (int i = 0; i < list.size(); i++){
            skeleKey = skel(list.get(i), letter);//gets key for map using letter
            if(mapable.containsKey(skeleKey)){//adds letter to maplocation if exists else creates new one
                ArrayList<String> temp = mapable.get(skeleKey);
                temp.add(list.get(i));
                mapable.put(skeleKey, temp);
            }else {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(list.get(i));
                mapable.put(skeleKey, temp);
            } 
        }
        
        
        
        ArrayList<String> finalList = findMax(mapable, letter);//gets an arrayList
        return  finalList;
    }
    
    //finds the max using the map and letter
    public static ArrayList<String> findMax(Map<String, ArrayList<String>> master, char letter){
        ArrayList<String> max = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        Iterator<String> i = master.keySet().iterator();
        //initializes max with first
        String skeleton1 = i.next();
        max = master.get(skeleton1);
        for(int j = 0; j<wordLength; j++){
            if(word[j] == '_')
                word[j] = skeleton1.charAt(j);
        }
        //checks each map location and sets array maxWord based on results
        while(i.hasNext()){
            String skeleton = i.next();
            temp = master.get(skeleton);
            if(temp.size() > max.size()){
                max = temp;
                for(int j = 0; j<wordLength; j++){
                    if(word[j] == '_')
                        word[j] = skeleton.charAt(j);
                    else if(word[j] == letter && skeleton.charAt(j) != letter)
                        word[j] = '_';
                }
            }
        }

        return max;
    }
    //prints word
    public static void print(){
        if(word==null)
            return;
        for(int i=0;i<wordLength;i++){
            if(word[i]=='_')
                System.out.print("_ ");
            else
                System.out.print(word[i]+" ");
                
        }
        System.out.println();
    }
    //method for finding key string
    public static String skel(String s, char c){
        String skel="";
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==c)
                skel = skel + c;
            else
                skel = skel +"_";  
        }
        return skel;
    }   
}
