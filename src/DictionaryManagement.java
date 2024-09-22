import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class DictionaryManagement {

    public static void insertFromCommandLine(Dictionary dictionary, Scanner sc) {
        System.out.print("Nhập số lượng từ vựng: ");
        int num = sc.nextInt();
        sc.nextLine();

        for(int i = 0; i < num; i++) {
            System.out.println("Nhập vào từ thứ " + (i+1) + ":");
            String newWord = sc.nextLine();
            System.out.println("Nhập giải thích:");
            String meaning = sc.nextLine();
            Word newWordObj = new Word(newWord, meaning);
            dictionary.getWordList().add(newWordObj);
        }
        Collections.sort(dictionary.getWordList());
        System.out.println("Press OK to continue...");
        sc.nextLine();
    }

    public static void dictionaryLookup(Dictionary dictionary, Scanner sc){
        if(dictionary.getWordList().isEmpty()){
            System.out.println("Chưa có từ nào trong danh sách từ");
            System.out.println("Press OK to continue...");
            sc.nextLine();
            return;
        }
        System.out.print("Nhập từ tiếng anh cần tra cứu: ");
        String word=sc.nextLine();
        int pos = CommonFunc.binarySearch(dictionary.getWordList(),0,dictionary.getWordList().size(),word);
        if(pos == -1) {
            System.out.println("Không tìm thấy từ");
            return;
        }

        System.out.println("[" + pos + 1 + "]. " + dictionary.getWordList().get(pos).getWordTarget() + " : " + dictionary.getWordList().get(pos).getWordExplain());
        System.out.println("Press OK to continue...");
        sc.nextLine();
    }

    public static void updateWord(Dictionary dictionary, Scanner sc){;
        System.out.print("Nhập vào từ cần update: ");
        String word = sc.nextLine();
        int pos = CommonFunc.binarySearch(dictionary.getWordList(),0,dictionary.getWordList().size(),word);
        if(pos == -1) {
            System.out.println("Không tìm thấy từ: " + word);
        }
        else {
            System.out.println("Nghĩa ban đầu: " + dictionary.getWordList().get(pos).getWordExplain());
            System.out.print("Nhập ý nghĩa sửa lại: ");
            String newMeaning = sc.nextLine();
            dictionary.getWordList().get(pos).setWordExplain(newMeaning);
        }
        System.out.println("Press OK to continue...");
        sc.nextLine();
    }

    public static void removeWord(Dictionary dictionary, Scanner sc){
        System.out.print("Nhập từ muốn xóa: ");
        String word = sc.nextLine();
        int pos = CommonFunc.binarySearch(dictionary.getWordList(),0,dictionary.getWordList().size()-1,word);
        if(pos == -1) {
            System.out.println("Không tìm được từ: " + word);
        }
        else {
            System.out.println("Bạn có chắc chắn muốn xóa từ: " + word);
            System.out.println("Press Y/N to confirm:");
            char confirm = sc.nextLine().charAt(0);
            if(confirm == 'Y' || confirm == 'y') {
                dictionary.getWordList().remove(pos);
                System.out.println("Xoá thành công!");
            }
            else {
                System.out.println("Hủy thao tác xóa.");
            }
        }
            System.out.println("Press OK to continue...");
            sc.nextLine();
    }

    public static void dictionaryImportFromFile(Dictionary dictionary) {
        try{
            File wordFile = new File("dictionaries.txt");
            Scanner myReader = new Scanner(wordFile);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                String[] dataSplit = data.split(" : ");

                Word newWordObj = new Word(dataSplit[0], dataSplit[1]);
                dictionary.getWordList().add(newWordObj);
            }
            Collections.sort(dictionary.getWordList());
            myReader.close();
            System.out.println("Import thành công!");
        }
        catch (IOException e){
            System.out.println("Lỗi");
            e.printStackTrace();
        }
    }

    public static void dictionaryExportToFile(Dictionary dictionary) {
        try {
            FileWriter fileWriter = new FileWriter("dictionaries.txt",true);
            if(dictionary.getWordList().isEmpty()) {
                System.out.println("Không có từ trong từ điển");
                return;
            }
            for(Word word : dictionary.getWordList()) {
                if(CommonFunc.wordCheck("dictionaries.txt", word.getWordTarget())) {
                    continue;
                }
                fileWriter.write(word.getWordTarget() + " : " + word.getWordExplain() + "\n");
            }
            fileWriter.close();
            System.out.println("Export thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi! ");
            e.printStackTrace();
        }
    }
}
