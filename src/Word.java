// Package not detected, please report project structure on CodeTogether Live's GitHub Issues

public class Word implements Comparable<Word> {
    private String wordTarget;
    private String wordExplain;
    Word(String wordTarget, String wordExplain){
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
    }

    public String getWordTarget() {
        return wordTarget;
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public String getWordExplain() {
        return wordExplain;
    }

    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }

    @Override
    public int compareTo(Word o) {
        return this.wordTarget.compareTo(o.wordTarget);
    }
}
