package lexical;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class TestMain {
	
	public static void main(String args[]) throws IOException {
		
		System.out.println("选择 1：控制台输入。2：文件输入。");
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		int select = input.nextInt();
		
		if (select == 1) {
			
			@SuppressWarnings("resource")
			Scanner input2 = new Scanner(System.in);
			String string = input2.nextLine();
			
			string += " ";
			char[] buf = string.toCharArray();

			new AnalysisWord().recognition(buf);
			
		} else if (select == 2) {
			
			String urlString = "F:\\Java_Programming\\Analysis_Lexical\\src\\file.txt";
		    File file = new File(urlString);//定义一个file对象，用来初始化FileReader
		    FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
		    int length = (int) file.length();
		    //这里定义字符数组的时候需要多定义一个,因为词法分析器会遇到超前读取一个字符的时候，如果是最后一个
		    //字符被读取，如果在读取下一个字符就会出现越界的异常
		    char buf[] = new char[length+1];
		    reader.read(buf);
		    reader.close();
		    new AnalysisWord().recognition(buf);
		    
		} else {
			return;
		}
	    
	    
	}

}
