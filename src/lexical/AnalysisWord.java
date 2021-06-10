package lexical;

import java.util.Stack;

public class AnalysisWord {

	public static final int identifier = 1;		//标识符
	public static final int constant = 2;		//常数
	public static final int reserved = 3;		//保留字
	public static final int operator = 4;		//运算符
	public static final int boundary = 5;		//界符
	public static final int NULLFONT = 0;
	
	/*
	 * 将输入的String分割成char数组
	 * 再运算
	 */
	public char[] splitCharacters(String words) {
		if (words.equals("") || words == null)
			return null;
		
		char[] tempWords = words.toCharArray();
		return tempWords;
	}
	
	public boolean recognition(char[] chars) {
		
		String tempString = "";
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(-1);
		int index = 0;
		for (int i = 0; i < chars.length - 1; i++) {
			char ch = chars[i];
			tempString = "";
			if (isBlankSpace(ch)) {
				continue;
			} else if (isLetter(ch)) {
				while (isLetter(ch) || isDigit(ch)) {
					tempString += ch;
					ch = chars[++i];
				}
				
				i--;
				if (!(isReserved(tempString) == reserved)) {
					
					if (stack.peek() == identifier) {
						System.out.println("错误，" + tempString + "保留字输入有误.");
					} else {
						stack.push(identifier);
						System.out.println("(" + identifier + "," + index + ")");
						index++;
					}
				} else {
					stack.push(reserved);
					System.out.println("(" + reserved + "," + tempString + ")");
				}
			} else if (isDigit(ch) || ch == '.') {
				while (isDigit(ch) || ( ch == '.' && isDigit(chars[++i]) )) {
					if (ch == '.') i--;
					tempString += ch;
					ch = chars[++i];
				}
				System.out.println("(" + constant + "," + tempString + ")");
			} else {
				switch (finallAnalysis(ch)) {
				case operator:
					tempString += ch;
					System.out.println("(" + operator + "," + tempString + ")");
					stack.push(operator);
					break;
				case boundary:
					tempString += ch;
					System.out.println("(" + boundary + "," + tempString + ")");
					stack.push(boundary);
					break;
				case operator + operator:
					tempString += ch;
					if (chars[i+1] == '=') {
						tempString += chars[i+1];
						i++;
					}
					System.out.println("(" + operator + "," + tempString + ")");
					stack.push(operator);
					break;
				case NULLFONT:
					System.out.println("错误，"+ ch +"符号输入有误，请重新输入");
					stack.push(NULLFONT);
					break;
				default:
					break;
				}
			}
		}
		
		
		return false;
	}
	
	public boolean isLetter(char c) {
		if ( c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isBlankSpace(char c) {
		if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
			return true;
		} else {
			return false;
		}	
	}
	
	/*
	 * 判断是否为保留字
	 * JAVA代码
	 */
	public int isReserved(String s) {
		String[] camp = {"abstract","assert","boolean","break",
				"byte","case","catch","char","class",
				"const","continue","default","do",
				"double","else","enum","extends",
				"final","finally","float","for",
				"goto","if","implements","import",
				"instanceof","int","interface","long",
				"native","new","package","private",
				"protected","public","return","short",
				"static","strictfp","super","switch",
				"synchronized","then","this","throw","throws",
				"transient","try","void","volatile",
				"while","true","false","null"};
		for (int i = 0; i < camp.length; i++) {
			if (s.equals(camp[i])) {
				return reserved;
			}
		}
		return NULLFONT;
	}
	
	public int finallAnalysis(char c) {
		if (c == '+' || c =='-' || c == '*' || c == '/') {
			return operator;
		} else if (
				c == '(' || c == ')' || c == '[' ||
				c == ']' || c == ';' || c == '{' ||
				c == '}') {
			return boundary;
		} else if (
				c == '=' || c == ':' || c == '<' ||
				c == '>') {
			return operator + operator;
		} 
		else {
			return NULLFONT;
		}
	}

}
