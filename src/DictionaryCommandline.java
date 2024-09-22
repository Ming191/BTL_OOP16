import java.util.Scanner;

public class DictionaryCommandline {
    DictionaryCommandline(Dictionary dictionary) {
        advanceDictionary(dictionary);
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
                System.out.println("Invalid option");
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
                    DictionaryManagement.showAllWords(dictionary);
                    break;
                case 5:
                    DictionaryManagement.dictionaryLookup(dictionary,sc);
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
