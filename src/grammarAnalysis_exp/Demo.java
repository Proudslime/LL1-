package grammarAnalysis_exp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Demo {
	public static void main(String[] args) {
		
		/*
		 * 笔记：发现first集合的添加发生了和follow集合一样的问题
		 * 加一下添加时判断是否为空就可以了
		 * 上述问题已经解决
		 * 现在想加入布尔值判断是否已经计算完毕
		 * 现在预想在每次递归计算follow集合的结束之后将布尔值改为正值
		 * 但是扳机还没有找好，所以明天的主要内容就是添加的扳机
		 * 那么不妨设想一下，合适的扳机到底应该在什么时候添加才是比较好的
		 * 我初步设想是在出现要计算follow的计算结束的时候添加
		 * 那么涉及到follow集合的计算主要有两个部分，一个是非终结符集合的全部遍历计算，另一个是在前者的遍历过程中需要添加follow值的时候
		 * 01点59分2021年4月29日
		 */
    	
    	/*
    	 * 2021年5月5日20点09分
    	 * 1.现在添加了LL(1)文法的判断，能够通过得出的select集合判断出不是LL(1)文法，但是没有添加更改成LL(1)的方法
    	 * 2.还没有试试添加左递归会不会出现问题
    	 * 3.能判断输入的字符串该文法能不能推出
    	 */
		ArrayList<String> gsArray = readLL();
		Grammar grammar = new Grammar(gsArray,"E");
		grammar.analysis();
		
		String inputLeft = "i+i*i#";
		Analyzer analyzer = new Analyzer(grammar, "E", inputLeft);
		analyzer.analyze();
	}
	
	public static ArrayList<String> readLL() {
		
		ArrayList<String> arrayList = new ArrayList<String>();
		System.out.println("选择从1:文件读取还是2:控制台输入");
		Scanner input = new Scanner(System.in);
		String n = input.nextLine();
		while (!n.equals("0")) {
			
			if (n.equals("1")) {
				System.out.println("选择文件读取->");
				File file = new File("LL(1)input.txt");
				try {
					Scanner scanner = new Scanner(file);
					while (scanner.hasNextLine()) {
						arrayList.add(scanner.nextLine());
					}
					scanner.close();
					return arrayList;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("文件不存在");
					e.printStackTrace();
				}
			} else if (n.equals("2")) {
				System.out.println("选择控制台输入->");
				Scanner scanner = new Scanner(System.in);
				while (!scanner.nextLine().equals("quit")) {
					arrayList.add(scanner.nextLine());
				}
				scanner.close();
				return arrayList;
			} else {
				System.out.println("选择输入错误，请重新输入");
				n = input.nextLine();
			}
			
		}
		
		return null;
	}
}
