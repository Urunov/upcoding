/**
 * Created by:
 * User: hamdamboy
 * Project: CTCI Data Structure and Algorithm
 * Github: @urunov
 */
public class OneAway {
    public static int setChar(char[] array, char c, int index, int count) {
        array[index] = c;
        index++;
        char[] cnt = String.valueOf(count).toCharArray();
        for (char x : cnt) {
            array[index] = x;
            index++;
        }
        return index;
    }


    public static int countCompression(String message) {
        if (message == null || message.isEmpty()) return 0;

        char last = message.charAt(0);
        int size = 0;
        int count = 1;
        for (int i = 1; i < message.length(); i++) {
            if (message.charAt(i) == last) {
                count++;
            } else {
                last = message.charAt(i);
                size += 1 + String.valueOf(count).length();
                count = 1;
            }
        }
        size += 1 + String.valueOf(count).length();
        return size;
    }

    public static String compressBad(String message){
        int size = countCompression(message);
        if(size >= message.length()){
            return message;
        }

        String mymessage = "";
        char last = message.charAt(0);
        int count = 1;
        for(int i=1; i< message.length(); i++) {
            if(message.charAt(i) ==last){
                count++;
            } else {
                mymessage +=last + " " + count;
                last = message.charAt(i);
                count =1;
            }
        }

        return mymessage + last + count;
    }

    public static String compressBetter(String message) {
        int size = countCompression(message);
        if(size >=message.length()){
            return message;
        }
        StringBuffer stringBuffer = new StringBuffer();
        char last = message.charAt(0);
        int count = 1;
        for(int i=1; i< message.length(); i++){
            if(message.charAt(i) == last){
                count++;
            } else {
                stringBuffer.append(last);
                stringBuffer.append(count);
                last = message.charAt(i);
                count = 1;
            }
        }
        stringBuffer.append(last);
        stringBuffer.append(count);
        return stringBuffer.toString();
    }


    public static String compressAlternate(String message){
        int size = countCompression(message);
        if(size >= message.length()){
            return message;
        }

        char []array = new char[size];
        int index = 0;
        char last = message.charAt(0);
        int count = 1;
        for(int i=1; i <message.length(); i++){
            if(message.charAt(i) == last) {
                count++;
            } else {
                index = setChar(array, last, index, count);
                last = message.charAt(i);
                count = 1;
            }
        }
        index = setChar(array, last, index, count);
        return String.valueOf(array);
    }

    public static void main(String []args){
        String message = "aabbccccddde";
        int c = countCompression(message);
        String message1 = compressAlternate(message);
        String t = compressBetter("");
        System.out.println("Compression: " + t);
        System.out.println("Old String (len = " + message.length() + " ):" + message);
        System.out.println("New Stirng (len = " + message1.length() + " ):" + message1);
        System.out.println("Potential Compression: " +c);
    }
}