import java.util.List;
import java.util.Scanner;

public class DictionaryCommandline {
    DictionaryCommandline(Dictionary dictionary) {
        advanceDictionary(dictionary);
    }

    public static void showAllWords(Dictionary dictionary) {
        System.out.printf("%-5s | %-20s | %-20s%n", "No", "English", "Vietnamese");
        System.out.println("------------------------------------------------");
        if(dictionary.getWordList().isEmpty()) {
            System.out.println("Từ điển trống!");
        }
        int index = 1;
        for (Word word : dictionary.getWordList()) {
            System.out.printf("%-5d | %-20s | %-20s%n", index++,
                    word.getWordTarget(), word.getWordExplain());
        }
    }

    public static void dictionarySearcher(Dictionary dictionary, Scanner sc) {
        System.out.print("Nhập vào từ muốn search:");
        String s = sc.nextLine().toLowerCase();
        boolean found = false;

        for (Word word : dictionary.getWordList()) {
            if (word.getWordTarget().toLowerCase().startsWith(s)) {
                System.out.printf("%-20s | %-20s%n", word.getWordTarget(), word.getWordExplain());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Không tìm được từ cần tìm.");
        }
    }

    public static void advanceDictionary(Dictionary dictionary) {
        int lastRequest;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("========================");
            System.out.println("0. Exit");
            System.out.println("1. Add");
            System.out.println("2. Remove");
            System.out.println("3. Update");
            System.out.println("4. Display");
            System.out.println("5. Look Up");
            System.out.println("6. Search");
            System.out.println("7. Game");
            System.out.println("8. Import from file");
            System.out.println("9. Export to file");
            System.out.println("========================");
            System.out.print("Your action: ");
            lastRequest = sc.nextInt();
            sc.nextLine();
            if(lastRequest > 9 || lastRequest < 0) {
                System.out.println("Action not supported");
                continue;
            }
            switch (lastRequest) {
                case 1:
                    DictionaryManagement.insertFromCommandLine(dictionary,sc);
                    break;
                case 2:
                    DictionaryManagement.removeWord(dictionary,sc);
                    break;
                case 3:
                    DictionaryManagement.updateWord(dictionary,sc);
                    break;
                case 4:
                    showAllWords(dictionary);
                    break;
                case 5:
                    DictionaryManagement.dictionaryLookup(dictionary, sc);
                    break;
                case 6:
                    dictionarySearcher(dictionary, sc);
                    break;
                case 8:
                    DictionaryManagement.dictionaryImportFromFile(dictionary);
                    break;
                case 9:
                    DictionaryManagement.dictionaryExportToFile(dictionary);
                    break;
                default:
                    break;
            }
            if(lastRequest == 0) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }
}
//