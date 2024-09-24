import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CommonFunc {
    public static int binarySearch(List<Word> wordList, int low, int high, String wordTarget) {
        if(high >= low) {
            int mid = low + (high - low)/2;

            if(wordList.get(mid).getWordTarget().equals(wordTarget)) {
                return mid;
            } else if(wordList.get(mid).getWordTarget().compareTo(wordTarget) > 0) {
                return binarySearch(wordList, low, mid-1, wordTarget);
            } else {
                return binarySearch(wordList, mid+1, high, wordTarget);
            }
        }
        return -1;
    }

    public static Set<String> existingWords(String fileName) {
        Set<String> res = new HashSet<>();
        try (Scanner sc = new Scanner(new File(fileName))) {
            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] dataSplit = data.split(" : ");
                if(!dataSplit[0].isEmpty()) {
                    res.add(dataSplit[0]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String formatWord(String word) {
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }
}
