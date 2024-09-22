import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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

    public static boolean wordCheck(String fileName, String wordTarget) {
        Scanner sc = new Scanner(fileName);
        try {
            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] dataSplit = data.split(" : ");
                if(dataSplit[0].equals(wordTarget)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
