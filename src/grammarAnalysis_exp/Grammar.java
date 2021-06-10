package grammarAnalysis_exp;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author 22948
 *
 */
/**
 * @author 22948
 *
 */
public class Grammar {
	
	public static final String EPSILON = "ε";
	
	//预测分析二维数组
	private String[][] analyzeTable;
	//LL(1)文法集合
	private ArrayList<String> ll;
	//开始符号
	private String start;
	//非终结符集合
	private HashSet<String> vn;
	//终结符集合
	private HashSet<String> vt;
	//表达式集合
	private HashMap<String, ArrayList<String>> exp;
	//First集合
	private HashMap<String, ArrayList<String>> first;
	//Follow集合
	private HashMap<String, ArrayList<String>> follow;
	//select集合
	private HashMap<HashMap<String, String>,ArrayList<String>> select;
	//
	private HashMap<String, Boolean> isfinishFirst;
	private HashMap<String, Boolean> isfinishFollow;
	
	//用来记录当前是否是LL(1)文法
	public boolean isLL;
	
	private Grammar() {
		// TODO Auto-generated constructor stub
		super();
		ll = new ArrayList<String>();
		vn = new HashSet<String>();
		vt = new HashSet<String>();
		first = new HashMap<String, ArrayList<String>>();
		follow = new HashMap<String, ArrayList<String>>();
		select = new HashMap<HashMap<String, String>,ArrayList<String>>();
		isfinishFirst = new HashMap<String, Boolean>();
		isfinishFollow = new HashMap<String, Boolean>();
	}

	public Grammar(ArrayList<String> ll, String start) {
		this();
		this.ll = ll;
		this.start = start;
	}
	
	public void analysis() {
		getVnVt();
		initExp();
		getFirst();
		getFollow();
		getSelect();
		
		/*
		 * select集合已经计算完毕
		 * 接下来填充进分析二维数组即可
		 */
		if (isLL()) {
			System.out.println("输入的确实为LL(1)文法");
			isLL = true;
			writerIntoTable();
		} else {
			System.out.println("输入的不是LL(1)文法，请重新输入");
			isLL = false;
		}
		
		
	}
	
	private boolean isLL() {
		// TODO Auto-generated method stub
		/*
		 * 1）文法不含左递归 像这个样子A->Ab是不允许的
		 * 2）对于文法中每一个非终结符A的各个产生式的候选首符集两两不相交
		 * .即对于A->α1|α2|…|αn要求FIRST（αi）∩FIRST（αj）=Ø （i≠j）
		 * 3）对于文法中的每个非终结符A，若它存在某个候选首符集包含ε，则FIRST（A）∩FOLLOW（A）=Ø
		 * .总结下来就是求first，follow和select，同一个非终结符的select有交集就不是LL(1)了
		 */
		int index = vn.size();
		for (String Vn : vn) {
			List<String> temList = new ArrayList<String>();
			for (String expForm : exp.get(Vn)) {
				HashMap<String, String> temMap = new HashMap<String, String>();
				temMap.put(Vn, expForm);
				temList.addAll(select.get(temMap));
			}
			HashSet<String> hashSet = new HashSet<String>(temList);
			if (temList.size() != hashSet.size()) {
				index--;
			} 
		}
		
		if (index == vn.size()) {
			return true;
		} else {
			return false;
		}
	}

	private void writerIntoTable() {
		Object[] vtArray = vt.toArray();
		Object[] vnArray = vn.toArray();
		
		analyzeTable = new String[vnArray.length + 1][vtArray.length + 1];
		
		java.util.Date date = new java.util.Date();
		String fileName = dateForFilename(date);
		
		File outputFile = new File("F:\\Java_Programming\\Analysis_Lexical\\" + fileName + ".txt");
		
		try {
			Writer writer = new FileWriter(outputFile);
			writer.write("\n");
			writer.write("====================预测分析表====================");
			writer.write("\n");
			writer.write("Table\t");
			System.out.println("\n====================预测分析表====================\n");
			System.out.print("Table\t");
			analyzeTable[0][0] = "Vn/Vt";
			
			/*
			 * 初始化首行
			 */
			for (int i = 0; i < vtArray.length; i++) {
				if (vtArray[i].equals(EPSILON)) {
					vtArray[i] = "#";
				}
				writer.write(vtArray[i] + "\t\t");
				analyzeTable[0][i + 1] = vtArray[i] + "";
				System.out.print(vtArray[i] + "\t\t");
			}
			
			writer.write("\n");
			System.out.println();
			/*
			 * 在初始化完毕行之后
			 * 开始逐列计算
			 * LL(1)文法判断可以加在添加分析表中
			 * 但是这样会不会导致在打印的时候停止，然后蹦出来一个报错
			 */
			for (int i = 0; i < vnArray.length; i++) {
				writer.write(vnArray[i] + "\t");
				System.out.print(vnArray[i] + "\t");
				
				analyzeTable[i + 1][0] = vnArray[i] + "";
				for (int j = 0; j < vtArray.length; j++) {
					String findSelectExp = TestUtil.findUseExp(select, exp, vnArray[i] + "", vtArray[j] + "");
					
					if (findSelectExp.equals("")) {
						writer.write("null\t\t");
						System.out.print("null\t\t");
						analyzeTable[i+1][j+1] = "";
					} else {
						writer.write(vnArray[i] + "->" + findSelectExp + "\t\t");
						System.out.print(vnArray[i] + "->" + findSelectExp + "\t\t");
						analyzeTable[i+1][j+1] = vnArray[i] + "->" + findSelectExp;
					}
				}
				writer.write("\n");
				System.out.println();
			}
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		
	}
	
	private String dateForFilename(java.util.Date date) {
		// TODO Auto-generated method stub
		return date.toString().replaceAll(" ", "_").replaceAll(":", "_");
	}

	private void getVnVt() {
		for (String llItem : ll) {
			String[] vnvtItem = llItem.split("->");
			String item = vnvtItem[0];
			
			//先行添加非终结符集合,便于之后的终结符判断
			vn.add(item);
		}
		for (String llItem : ll) {
			String[] vnvtItem = llItem.split("->");
			String item = vnvtItem[1];
			
			//只要不是非终结符，那么剩下的就都是终结符了
			for (int i = 0; i < item.length(); i++) {
				String vtTemp = item.charAt(i) + "";
				
				if (!vn.contains(vtTemp)) {
					vt.add(vtTemp);
				}
			}
		}
		
		System.out.println("非终结符集合:" + vn.toString());
		System.out.println("终结符集合:" + vt.toString());
	}
	
	private void initExp() {
		exp = new HashMap<String, ArrayList<String>>();
		
		for (String llItem : ll) {
			String[] Item = llItem.split("->");
			
			String left = Item[0];
			String right = Item[1];
			
			if (!exp.containsKey(left)) {
				ArrayList<String> expList = new ArrayList<String>();
				//没有就先初始化再添加
				expList.add(right);
				exp.put(left, expList);
			} else {
				//有了就不用初始化了，直接拿过来用
				ArrayList<String> expList = exp.get(left);
				expList.add(right);
				exp.put(left, expList);
			}
		}
		
		System.out.println("表达式集合:" + exp.toString());
	}
	
	private void getFirst() {
		
		for (String temp : vn) {
			ArrayList<String> tempList = new ArrayList<String>();
			first.put(temp, tempList);
			isfinishFirst.put(temp, false);
		}
		
		Iterator<String> iterator = vn.iterator();
		
		while (iterator.hasNext()) {
			String item = (String) iterator.next();
			//获取当前非终结符的表达式
			ArrayList<String> arrayList = exp.get(item);
			
			for (String itemString : arrayList) {
				//遍历表达式，逐句获取
				
				/*
				 * 发现疑问，为什么这里要设置遍历右侧表达式的全部？
				 */
//				for (int i = 0; i < itemString.length(); i++) {
//					String itemAlone = itemString.charAt(i) + "";
//					
//					if (calcFirst(item,itemAlone,itemString)) 
//						break;
//				}
				
				String expFirst = itemString.charAt(0) + "";
				
				calcFirst(item, expFirst);
			}
			isfinishFirst.replace(item, true);
		}
		
		processingCollection();
		
		System.out.println("First集合:" + first.toString());
	}
	
	
	private void processingCollection() {
		// TODO Auto-generated method stub
		Iterator<String> iterator = vn.iterator();
		
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			
			LinkedHashSet<String> set = new LinkedHashSet<String>(first.get(string));
			ArrayList<String> deleteList = new ArrayList<String>(set);
			first.replace(string, deleteList);
			
			if (!exp.get(string).contains(TestUtil.EPSILON)) {
				if (first.get(string).contains(TestUtil.EPSILON)) {
					first.remove(string, EPSILON);
				}
			}
		}
	}

	private boolean calcFirst(String item, String itemAlone) {
		
		if (itemAlone.equals(EPSILON) || vt.contains(itemAlone)) {
			/*
			 * 找到匹配到空值和终结符的情况了
			 * 那么就直接添加到item的first集合中
			 */
			
			if (!first.get(item).contains(itemAlone)) {
				first.get(item).add(itemAlone);
			}
			
			return true;
			
		} else if (vn.contains(itemAlone)) {
			
			/*
			 * 说明item的这个表达式的第一位不是终结符或者空
			 * 那么就要接着替换，直到找到接下来的非终结符的表达式中的第一位终结符或者空
			 */
			ArrayList<String> nextExp = exp.get(itemAlone);
				
			for (String string : nextExp) {
				if (getFirstbool().get(itemAlone)) {
					ArrayList<String> tempList = new ArrayList<String>();
					tempList = first.get(itemAlone);
					first.get(item).addAll(tempList);
					break;
				}
				calcFirst(item, string.charAt(0) + "");
			}
			
//			if (!TestUtil.is_the_last_digit_a_empty(itemAlone, itemString)) {
//				if (TestUtil.is_the_last_digit_a_non_terminator(vn, itemAlone, itemString)) {
//					calcFirst(item, TestUtil.getLastDigit(itemAlone, itemString), itemString);
//				}
//			}
			
		}
		return true;
	}
	
	private void getFollow() {
		//循环初始化
		for (String temp : vn) {
			ArrayList<String> tempList = new ArrayList<String>();
			
			if (temp.equals(start)) {
				tempList.add("#");
			}
			
			follow.put(temp, tempList);
			isfinishFollow.put(temp, false);
		}
		
		Iterator<String> iterator = vn.iterator();
		
		while (iterator.hasNext()) {
			String item = (String) iterator.next();
			
			Set<String> ketSet = exp.keySet();
			
			for (String item_Exp : ketSet) {
				ArrayList<String> explist = exp.get(item_Exp);
				
				for (String expFrom : explist) {
					calcFollow(item,item,item_Exp,expFrom,ketSet);
				}
				
				while (follow.get(item).contains(EPSILON)) {
					follow.get(item).remove(EPSILON);
				}
				if (!follow.get(item).contains("#")) {
					follow.get(item).add("#");
				}
				
				isfinishFollow.replace(item, true);
			}
			isfinishFollow.replace(item, true);
			
		}
		
		Set<String> ketSet = exp.keySet();
		delete_duplicate_elements(follow,ketSet);
		
		System.out.println("Follow集合:" + follow.toString());
	}

	private void delete_duplicate_elements(HashMap<String, ArrayList<String>> inputMap, Set<String> ketSet) {
		// TODO Auto-generated method stub
		for (String t : ketSet) {
			if (!inputMap.isEmpty()) {
				LinkedHashSet<String> set = new LinkedHashSet<String>(inputMap.get(t));
				ArrayList<String> temp = new ArrayList<String>(set);
				follow.replace(t, temp);
			}
			
			if (inputMap.get(t).contains(EPSILON)) {
				inputMap.remove(t, EPSILON);
				if (!inputMap.get(t).contains("#")) {
					inputMap.get(t).add("#");
				}
			}
		}
	}

	/**
	 * @param item 当前判断的非终结符,在递归调用中重复传递，保持不变，主要是为了其中能够准确插入到该非终结符的Follow集合中
	 * @param putItem 判断的非终结符，在第一遍遍历的时候跟item一样 
	 * @param ketSet 表达式左侧集合
	 * @param item_Exp 当前的表达式的左侧
	 * @param expFrom 当前加入判断的右侧表达式
	 */
	private void calcFollow(String item, String putItem, String item_Exp, String expFrom, Set<String> ketSet) {
		// TODO Auto-generated method stub
		
		/*
		 * 当前非终结符是否是在右侧表达式的最后一位
		 * 也就是后面没有符号了
		 * 那么就要将表达式左侧的非终结符的Follow集合加入到该非终结符的Follow集合中
		 * 
		 * 发现A->aA这种的会出现无限递归
		 * 所以要避免这样的
		 * 
		 */
		if (TestUtil.is_the_last_digit_a_empty(putItem, expFrom)) {
			
			if (TestUtil.isleftequalItem(putItem, item_Exp)) {
				return;
			}
			
			for (String Vn : ketSet) {
				ArrayList<String> expList = exp.get(Vn);
				
				for (String _expFrom : expList) {
					calcFollow(item, item_Exp, item_Exp, _expFrom, ketSet);
				}
			}
			
			if (!follow.get(item_Exp).isEmpty()) {
				
				follow.put(item, follow.get(item_Exp));
				
			}
			
		}
		
		if (TestUtil.is_the_last_digit_a_terminator(vt, putItem, expFrom)) {
			//如果后一位是终结符，那么其follow集合就加入该终结符，然后进入下一步的判断
			
			String last = TestUtil.getLast(putItem, expFrom);
			ArrayList<String> lastFirst = TestUtil.getFirst(first, last);
			
			if (follow.get(item).equals(lastFirst)) {
				return;
			}
			
			if (!follow.get(item).isEmpty()) {
				follow.get(item).addAll(lastFirst);
			} else {
				follow.put(item, lastFirst);
			}
			
		}
		
		if (TestUtil.is_the_last_digit_a_non_terminator(vn, putItem, expFrom)) {
			//如果后一位是非终结符，那就要加入后一位非终结符的First集合并且去除EPSILON
			//而且如果后一位能直接推出EPSILON，那么才加入该非终结符的Follow集合
			//但是如果后一位没有计算怎么办？
			//所以需要当场计算得出
			
			String last = TestUtil.getLastDigit(putItem, expFrom);
			
			for (String t : first.get(last)) {
				if (!follow.get(item).contains(t)) {
					follow.get(item).add(t);
				}
			}
			
			if (exp.get(last).contains(EPSILON)) {
				for (String Vn : ketSet) {
					ArrayList<String> explist = exp.get(Vn);
					
					if (!getFollowbool().get(Vn)) {
						for (String _expFrom : explist) {
							calcFollow(last, last, Vn, _expFrom, ketSet);
						}
					}
				}
				
				/*
				 * 经过循环之后其非终结符的Follow集合就计算完毕了
				 * 那么直接拿过来用就可以了
				 */
				
				if (!follow.get(last).isEmpty()) {
					
					if (!follow.get(item).isEmpty()) {
						follow.get(item).addAll(follow.get(last));
					} else {
						follow.put(item, follow.get(last));
					}
					
				}
			}
		}
	}

	private void getSelect() {
		/*
		 * 记录思路，相当于每个表达式分析
		 * 分析表达式的first集合
		 * 我记得TestUtil中应该有写好的整个表达式的First集合的计算式
		 * 所以这里应该就是简单的调用就可以了
		 * 整理思路：
		 * 1.遍历所有的表达式
		 * 2.得出表达式右侧的first集合
		 * 3.根据情况加入follow集合
		 * 4.加入select集合中
		 * 5.去除相同元素
		 */
		
		for (String Vn : vn) {
			/*
			 * select集合的值为:表达式的整体，所以还是有问题
			 * 通过二重循环将select集合初始化完成
			 */
			ArrayList<String> explist = exp.get(Vn);
			
			for (String right : explist) {
				ArrayList<String> selectList = new ArrayList<String>();
				
				HashMap<String, String> selectLeft = new HashMap<String, String>();
				selectLeft.put(Vn, right);
				
				select.put(selectLeft, selectList);
			}
		}
		
		Iterator<String> iterator = exp.keySet().iterator();
		
		while (iterator.hasNext()) {
			String leftItem = (String) iterator.next();
			
			ArrayList<String> rightExp = exp.get(leftItem);
			
			for (String expFrom : rightExp) {
				
				calcSelect(leftItem,expFrom);
				
			}
		}
		
		delete_duplicate_elements(select, exp);
		
		System.out.println("Select集合:" + select.toString());
		
	}
	
	private void delete_duplicate_elements(
			HashMap<HashMap<String, String>, ArrayList<String>> inputMap, 
			HashMap<String, ArrayList<String>> _exp) {
		
		for (String left : _exp.keySet()) {
			for (String right : exp.get(left)) {
				
				HashMap<String, String> tempMap = new HashMap<String, String>();
				tempMap.put(left, right);
				
				LinkedHashSet<String> set = new LinkedHashSet<String>(inputMap.get(tempMap));
				ArrayList<String> temp = new ArrayList<String>(set);
				
				select.put(tempMap, temp);
			}
		}
	}

	/*
	 * @param leftItem 表达式左边的非终结符
	 * @param expFrom 表达式右边的语句s
	 */
	private void calcSelect(String leftItem, String expFrom) {
		// TODO Auto-generated method stub
		//先获取表达式整体的first集合
		ArrayList<String> firstExp = TestUtil.getFirst(first, expFrom);
		
		if (!firstExp.isEmpty()) {
			//先看右侧表达式能否推出空，也就是右侧表达式的first集合是否有#
			if (firstExp.contains(EPSILON) || firstExp.contains("#")) {
				ArrayList<String> leftFollow = follow.get(leftItem);
				
				HashMap<String, String> tempMap = new HashMap<String, String>();
				tempMap.put(leftItem, expFrom);
				
				if (!select.get(tempMap).isEmpty()) {
					select.get(tempMap).addAll(leftFollow);
				} else {
					select.put(tempMap, leftFollow);
				}
				
				for (String tempString : firstExp) {
					if (!tempString.equals(TestUtil.EPSILON)) {
						select.get(tempMap).add(tempString);
					}
				}
				
			} else if (!firstExp.contains(EPSILON) || !firstExp.contains("#")) {
				
				HashMap<String, String> tempMap = new HashMap<String, String>();
				tempMap.put(leftItem, expFrom);
				
				if (!select.get(tempMap).isEmpty()) {
					select.get(tempMap).addAll(firstExp);
				} else {
					select.put(tempMap, firstExp);
				}	
			}
		}
		
	}

	public String[][] getAnalyzeTable() {
		return analyzeTable;
	}

	public ArrayList<String> getLl() {
		return ll;
	}

	public void setLl(ArrayList<String> ll) {
		this.ll = ll;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public HashSet<String> getVn() {
		return vn;
	}

	public void setVn(HashSet<String> vn) {
		this.vn = vn;
	}

	public HashSet<String> getVt() {
		return vt;
	}

	public HashMap<String, ArrayList<String>> getExp() {
		return exp;
	}

	public static String getEpsilon() {
		return EPSILON;
	}
	
	public HashMap<String, ArrayList<String>> returnFirst() {
		return first;
	}
	
	public HashMap<String, ArrayList<String>> returnFollow() {
		return follow;
	}
	
	public HashMap<HashMap<String, String>, ArrayList<String>> returnSelect() {
		return select;
	}
	
	public boolean getIsLL() {
		return isLL;
	}
	
	public HashMap<String, Boolean> getFirstbool() {
		return isfinishFirst;
	}
	
	public HashMap<String, Boolean> getFollowbool() {
		return isfinishFollow;
	}
}
