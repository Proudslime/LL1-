package lexical;

import java.io.File;
import java.io.FileReader;

/**
 * 此程序是通过将文件的字符读取到字符数组中去，然后遍历数组，将读取的字符进行
 * 分类并输出
 * @author
 *
 */
public class WordAnalyze {
    private String keyWord[] = {"begin","call","const","do","end","if","odd","procedure","read","then","var","while","write"};
    private char ch;
    //判断是否是关键字
    boolean isKey(String str)
    {
        for(int i = 0;i < keyWord.length;i++)
        {
            if(keyWord[i].equals(str))
                return true;
        }
        return false;
    }
    //判断是否是字母
    boolean isLetter(char letter)
    {
        if((letter >= 'a' && letter <= 'z')||(letter >= 'A' && letter <= 'Z'))
            return true;
        else
            return false;
    }
    //判断是否是数字
    boolean isDigit(char digit)
    {
        if(digit >= '0' && digit <= '9')
            return true;
        else
            return false;
    }
    //词法分析
    void analyze(char[] chars)
    {
        String arr = "";
        int index = 0;
        for(int i = 0;i< chars.length;i++) {
            ch = chars[i];
            arr = "";
            if(ch == ' '||ch == '\t'||ch == '\n'||ch == '\r'){
            	continue;
            }
            else if(isLetter(ch)){
                while(isLetter(ch)||isDigit(ch)){    
                    arr += ch;
                    ch = chars[++i];
                }
                //回退一个字符
                i--;
                if(isKey(arr)){
                    //关键字
                    System.out.println("(3,'" + arr + "')");
                }
                else{
                    //标识符
                    System.out.println("(1,'" + index + "')");
                    index++;
                }
            }
            else if(isDigit(ch)||(ch == '.'))
            {
                while(isDigit(ch)||(ch == '.'&&isDigit(chars[++i])))
                {
                    if(ch == '.') i--;
                    arr = arr + ch;
                    ch = chars[++i];
                }
                //属于无符号常数
                System.out.println("(2,'" + arr + "')");
            }
            else switch(ch){
                //运算符
                case '+':System.out.println("(4,'" + ch + "')");break;
                case '-':System.out.println("(4,'" + ch + "')");break;
                case '*':System.out.println("(4,'" + ch + "')");break;
                case '/':System.out.println("(4,'" + ch + "')");break;
                //分界符
                case '(':System.out.println("(5,'" + ch + "')");break;
                case ')':System.out.println("(5,'" + ch + "')");break;
                case '[':System.out.println("(5,'" + ch + "')");break;
                case ']':System.out.println("(5,'" + ch + "')");break;
                case ';':System.out.println("(5,'" + ch + "')");break;
                case '{':System.out.println("(5,'" + ch + "')");break;
                case '}':System.out.println("(5,'" + ch + "')");break;
                //运算符
                case '=':{
                            ch = chars[++i];
                            if(ch == '=')System.out.println("(4,'==')");
                            else {
                                System.out.println("(4,'=')");
                                i--;
                            }
                         }break;
                case ':':{
                            ch = chars[++i];
                            if(ch == '=')System.out.println("(4,':=')");
                            else {
                                System.out.println("(4,':')");
                                i--;
                            }
                         }break;
                case '>':{
                            ch = chars[++i];
                            if(ch == '=')System.out.println("(4,'>=')");
                            else {
                                System.out.println("(4,'>')");
                                i--;
                            }
                         }break;
                case '<':{
                            ch = chars[++i];
                            if(ch == '=')System.out.println("(4,'<=')");
                            else {
                                System.out.println("(4,'<')");
                                i--;
                            }
                         }break;
                //无识别
                default: System.out.println("error:" + ch);
            }
        }
    }
	public static void main(String[] args) throws Exception {
		String urlString = "F:\\Java_Programming\\Analysis_Lexical\\src\\file.txt";
	    File file = new File(urlString);//定义一个file对象，用来初始化FileReader
	    FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
	    int length = (int) file.length();
	    //这里定义字符数组的时候需要多定义一个,因为词法分析器会遇到超前读取一个字符的时候，如果是最后一个
	    //字符被读取，如果在读取下一个字符就会出现越界的异常
	    char buf[] = new char[length+1];
	    reader.read(buf);
	    reader.close();
	    new WordAnalyze().analyze(buf);
	    
	}
}