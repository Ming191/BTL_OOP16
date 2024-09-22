import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private List<Word> wordList;

    Dictionary() {
        wordList = new ArrayList<>();
    }

    Dictionary(Word word) {
        wordList = new ArrayList<>();
        wordList.add(word);
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    public List<Word> getWordList() {
        return wordList;
    }
}
