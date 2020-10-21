import java.util.Scanner;

public class StringCompression {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String output  = getCompress(str);

        output = output.length() >=str.length() ? str: output;
        System.out.println(output);
    }

    /**
     *  O(1)
     * @param str
     * @return
     * */
    private static String getCompress(String str){
        StringBuilder stringBuilder = new StringBuilder();
        int mostRecentCharCount = 1;
        char mostRecentChar = str.charAt(0);
        stringBuilder.append(mostRecentChar);
        for(int i=1; i < str.length(); i++){
            char temp = str.charAt(i);
            if(mostRecentChar == temp) {
                mostRecentCharCount++;
            } else {
                if(mostRecentCharCount>1){
                    stringBuilder.append(mostRecentCharCount);
                }

                mostRecentCharCount = 1;
                mostRecentChar = temp;
                stringBuilder.append(temp);
            }
        }

        if(mostRecentCharCount>1){
            stringBuilder.append(mostRecentCharCount);
        }
        return stringBuilder.toString();
    }
}


