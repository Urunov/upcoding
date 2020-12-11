/**
 * @Author: Urunov
 * Date: 21.10.2020
 *
 * */



public class IsUnique {


    public static boolean isUniqueChars(String message){
        if(message.length() > 128){
            return false;
        }

        boolean[] char_set = new boolean[128];
        for(int i=0; i < message.length(); i++){
            int val = message.charAt(i);
            if(char_set[val]) return false;
            char_set[val] = true;
        }
        return true;
    }


    public static void main(String[]args) {

        String[] words = {"abds", "hello", "my names", "Namoz"};
        for(String word: words){
            System.out.println(word + " : " + isUniqueChars(word));
        }
    }


}
