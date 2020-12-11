/**
 * @AUTHOR: Hamdamboy
 * Date: 21.10.2020
 *
 * */

import java.util.Arrays;

public class CheckPermutation {

    public static String sort(String s){
        char[] content = s.toCharArray();
        Arrays.sort(content);
        return new String(content);
    }

    public static boolean permutation(String s, String t){
        return sort(s).equals(sort(t));
    }

    public static void main(String[]args){
        String [][] pairs = {{"apple", "papel"}, {"carrot", "tarroc"}, {"hello", "illoh"}};

        for(String[] pair: pairs){
            String word1 = pair[0];
            String word2 = pair[1];
            boolean anagram = permutation(word1, word2);

            System.out.println(word1 + ", " + word2 + ":" + anagram);
        }
    }
}

