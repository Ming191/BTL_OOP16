import java.util.Scanner;

public class DictionaryManagement {
    public void insertFromCommandLine(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so luong tu vung: ");
        int num = sc.nextInt();

        for (int i = 0; i < num; i++) {
            System.out.println("Nhap vao tu thu " + (i+1) + ":");
            String newWord = sc.nextLine();
            System.out.println("Nhap giai thich:");
            String Meaning = sc.nextLine();
        }
        sc.close();
    }

    /*public void dictionaryLookup(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap tu tieng anh can tra cuu" + ":");
        String word=sc.nextLine();
    }*/

    public void updateWord(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap vao tu can update:");
        String word=sc.nextLine();

        //look up and update here ...

    }

    public void deleteWord(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Nhap tu muon xoa:");
        String word = sc.nextLine();

        //lookup and delete...
    }

}
