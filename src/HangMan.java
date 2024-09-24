import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HangMan {
    private static String secretWord;
    private static String guessedWord;
    private static int badGuesses;
    private static int hiddenLetters;
    private static int status;
    private static StringBuilder guessedWordBuilder;

    public static String getSecretWord() {
        return secretWord;
    }

    HangMan(Scanner sc, Dictionary dictionary) {
        secretWord = getRandomWord(dictionary.getWordList());
        guessedWord = new String(new char[secretWord.length()]).replace('\0', '_');
        badGuesses = 0;
        hiddenLetters = secretWord.length();
        guessedWordBuilder = new StringBuilder(guessedWord);
    }

    public static void Update (char input){
        status = GameConstants.BAD_GUESS;
        for(int i = 0; i<secretWord.length();i++){
            if(guessedWordBuilder.charAt(i) == '_' && secretWord.toLowerCase().charAt(i) == input){
                guessedWordBuilder.setCharAt(i, input);
                status = GameConstants.GOOD_GUESS;
                hiddenLetters--;
            }
        }
        if(status == GameConstants.BAD_GUESS) {
            badGuesses++;
        }
    }

    public static boolean won() {
        return hiddenLetters == 0;
    }

    public static boolean lost() {
        return badGuesses == GameConstants.MAX_BAD_GUESS;
    }

    public static boolean isOver() {
        return won() || lost();
    }

    public static boolean wannaContinue(Scanner sc) {
        System.out.println("Do you wanna continue?");
        System.out.println("[Y/N]: ");
        char choice = sc.nextLine().charAt(0);
        status = GameConstants.START;
        return (choice == 'Y' || choice == 'y');
    }

    public static void render() {
        System.out.println("========================");
        if(status == GameConstants.GOOD_GUESS){
            System.out.println("Good guess!");
        }
        else if(status == GameConstants.BAD_GUESS){
            System.out.println("Bad guess!");
        }
        System.out.println("Guessed word: " + guessedWordBuilder.toString());
        System.out.println("Bad guesses: " + badGuesses);

        if(won()) {
            System.out.println("You won!");
        } else if(lost()) {
            System.out.println("You lost!");
        }
        System.out.println("========================");
    }

    public static char getInput(Scanner sc) {
        System.out.println("Enter a letter: ");
        String input = sc.nextLine();
        return input.toLowerCase().charAt(0);
    }

    public static String getRandomWord(List<Word> wordList) {
        Random random = new Random();
        int randomIndex = random.nextInt(wordList.size());
        return wordList.get(randomIndex).getWordTarget();
    }
}

class GameConstants {
    public static final int  START = 0;
    public static final int  GOOD_GUESS = 1;
    public static final int  BAD_GUESS = 2;
    public static final int  MAX_BAD_GUESS = 5;
}
