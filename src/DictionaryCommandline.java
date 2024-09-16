import java.util.Dictionary;
import java.util.Scanner;

public class DictionaryCommandline {
    private DictionaryManagement dictionaryManagement;
    private State appState;

    DictionaryCommandline() {
        appState = new State();
        dictionaryAdvance();
    }

    public void showAllWords() {

    }

    public void dictionaryAdvance() {
        System.out.println(
                "======================================================"
        );
        System.out.println(
                "Welcome to My Application!"
        );

        appState.printStatesList();

        System.out.println(
                "======================================================"
        );
        System.out.println(
                "YOUR ACTION: "
        );

        Scanner sc = new Scanner(System.in);
        int request = sc.nextInt();
        while (request > 10 || request < 0) {
            System.out.println("ACTION IS NOT SUPPORTED");
            System.out.println(
                    "YOUR ACTION: "
            );
            request = sc.nextInt();
        }
        appState.setCurrentState(request);
    }
}
