package grammarAnalysis_exp;

import java.util.ArrayList;
import java.util.Stack;

import javax.sound.midi.Soundbank;

public class Analyzer {
	private String start;
	private Stack<String> analyzeStack;
	private String inputLeft;
	private String useExp;
	
	private ArrayList<AnalyzeProduce> analyzeProduces;
	private Grammar LL_1;
	
	public Analyzer() {
		super();
		analyzeProduces = new ArrayList<AnalyzeProduce>();
		analyzeStack = new Stack<String>();
		analyzeStack.push("#");
	}

	public Analyzer(
			Grammar inputGrammar,
			String start,String inputLeft) {
		this();
		this.LL_1 = inputGrammar;
		this.start = start;
		this.inputLeft = inputLeft;
	}
	
	/*
	 * 15点06分-2021年5月1日
	 * 问题记录
	 * 最后的问题出现在当有A->EPCILON的时候，栈内应该不存储
	 * 所以可以设计一下判断
	 */
	public void analyze() {
		
		if (!LL_1.getIsLL()) {
			return;
		}
		
		System.out.println("====================\nLL(1)文法分析过程\n====================");
        System.out.println("开始符:" + start);
        System.out.println("序号\t\t符号栈\t\t\t输入串\t\t\t所用产生式");
		
		analyzeStack.push(start);
		int index = 0;
		while (!analyzeStack.peek().equals("#") && inputLeft.charAt(0) != '#') {
			index++;
			
			if (!analyzeStack.peek().equals(inputLeft.charAt(0) + "")) {
				
				String findString = TestUtil.findUseExp(
						LL_1.returnSelect(), LL_1.getExp(), 
						analyzeStack.peek(), inputLeft.charAt(0) + "");
				
				AnalyzeProduce analyzeProduce = new AnalyzeProduce(index,analyzeStack.toString(),inputLeft);
				
				if (findString.equals("")) {
					break;
				}
				
				print(analyzeStack,findString,inputLeft,0,index);
				
				if (!findString.equals(TestUtil.EPSILON)) {
					
					analyzeProduce.setUseExp(analyzeStack.peek() + "->" + findString);
					analyzeProduces.add(analyzeProduce);
					analyzeStack.pop();
					inputLeftIntoStack(findString);
					
					continue;
				} else if (findString.equals(TestUtil.EPSILON)) {
					analyzeProduce.setUseExp(analyzeStack.peek() + "->" + findString);
					analyzeProduces.add(analyzeProduce);
					analyzeStack.pop();
					
					continue;
				} else {
					analyzeProduce.setUseExp("no match");
					analyzeProduces.add(analyzeProduce);
					analyzeStack.pop();
					inputLeftIntoStack(findString);
					
					continue;
				}
			} 
			if (analyzeStack.peek().equals(inputLeft.charAt(0) + "")) {
				print(analyzeStack,inputLeft.charAt(0) + "",inputLeft,1,index);
				
				AnalyzeProduce analyzeProduce = new AnalyzeProduce(index, analyzeStack.toString(), inputLeft);
				analyzeProduce.setUseExp(inputLeft.charAt(0) + ":match");
				analyzeProduces.add(analyzeProduce);
				analyzeStack.pop();
				inputLeft = inputLeft.substring(1);
				continue;
			}
			
		}
		if (!(inputLeft.charAt(0) + "").equals("#")) {
			System.out.println("匹配失败，输入的字符串该文法推导不出来");
		} else {
			index += 1;
			print(analyzeStack, "#", "#", 3, index);
		}
	}
	
	
	private void inputLeftIntoStack(String findString) {
		// TODO Auto-generated method stub
		if (findString != null && findString.charAt(0) + "" != TestUtil.EPSILON) {
			for (int i = findString.length() - 1; i >= 0; i--) {
				analyzeStack.push(findString.charAt(i) + "");
			}
		}
	}

	public void print(Stack<String> analyzeStack2, String findString, String inputLeft, int mark, int index) {
		// TODO Auto-generated method stub
		switch (mark) {
		case 0:
			
			if (analyzeStack2.size() == 1) {
				System.out.println(
						index + "\t\t" + analyzeStack2.toString() + "\t\t\t\t" +
						inputLeft + "\t\t\t" +
						analyzeStack2.peek() + "->" + findString);
			} else if (analyzeStack2.size() == 2) {
				System.out.println(
						index + "\t\t" + analyzeStack2.toString() + "\t\t\t" +
						inputLeft + "\t\t\t" +
						analyzeStack2.peek() + "->" + findString);
			} else {
				System.out.println(
						index + "\t\t" + analyzeStack2.toString() + "\t\t" +
						inputLeft + "\t\t\t" +
						analyzeStack2.peek() + "->" + findString);
			}
			
			break;
		case 1:
			
			if (analyzeStack2.size() == 1) {
				System.out.println(
						index + "\t\t" + analyzeStack2.toString() + "\t\t\t\t" +
						inputLeft + "\t\t\t" +
						inputLeft.charAt(0) + ":match");
			} else if (analyzeStack2.size() == 2) {
				System.out.println(
						index + "\t\t" + analyzeStack2.toString() + "\t\t\t" +
						inputLeft + "\t\t\t" +
						inputLeft.charAt(0) + ":match");
			} else {
				System.out.println(
						index + "\t\t" + analyzeStack2.toString() + "\t\t" +
						inputLeft + "\t\t\t" +
						inputLeft.charAt(0) + ":match");
			}
			
			break;
			
		case 3:
			
			System.out.println(
					index + "\t\t" + analyzeStack2.toString() + "\t\t\t" +
					inputLeft + "\t\t\t" + "acc");
			
			break;
		default:
			break;
		}
	}

	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	public Stack<String> getAnalyzeStack() {
		return analyzeStack;
	}
	public void setAnalyzeStack(Stack<String> analyzeStack) {
		this.analyzeStack = analyzeStack;
	}
	public String getInputLeft() {
		return inputLeft;
	}
	public void setInputLeft(String inputLeft) {
		this.inputLeft = inputLeft;
	}
	public String getUseExp() {
		return useExp;
	}
	public void setUseExp(String useExp) {
		this.useExp = useExp;
	}
	public ArrayList<AnalyzeProduce> getAnalyzeProduces() {
		return analyzeProduces;
	}
	public void setAnalyzeProduces(ArrayList<AnalyzeProduce> analyzeProduces) {
		this.analyzeProduces = analyzeProduces;
	}
	public Grammar getLL_1() {
		return LL_1;
	}
	public void setLL_1(Grammar lL_1) {
		LL_1 = lL_1;
	}
}

class AnalyzeProduce {
	private int index;
	private String analyzeStackitem;
	private String inputLeft;
	private String useExp;
	
	public AnalyzeProduce(int index, String analyzeStackitem, String inputLeft) {
		super();
		this.index = index;
		this.analyzeStackitem = analyzeStackitem;
		this.inputLeft = inputLeft;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getAnalyzeStackitem() {
		return analyzeStackitem;
	}
	public void setAnalyzeStackitem(String analyzeStackitem) {
		this.analyzeStackitem = analyzeStackitem;
	}
	public String getInputLeft() {
		return inputLeft;
	}
	public void setInputLeft(String inputLeft) {
		this.inputLeft = inputLeft;
	}
	public String getUseExp() {
		return useExp;
	}
	public void setUseExp(String useExp) {
		this.useExp = useExp;
	}
	
}
