package grammarAnalysis_exp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TestUtil {
	/*
	 * 这个方法是用来判断的
	 * 判断当前非终结符在当前的右侧表达式中的后一位
	 * 是否是终结符
	 */
	
	public static final String EPSILON = "ε";
	
	public static boolean is_the_last_digit_a_terminator(
			HashSet<String> vt,
			String putItem, String itemToExp) {
		
		if (is_the_last_digit_a_empty(putItem, itemToExp)) {
			return false;
		}
		
		if (itemToExp.contains(putItem)) {
			int index = itemToExp.indexOf(putItem);
		 	String lastDigit = itemToExp.substring(index+1, index+2);
		 	return vt.contains(lastDigit);
		} else {
			return false;
		}
	}
	
	/*
	 * 该方法如同上述的方法
	 * 是用来判断后一位是不是非终结符
	 */
	public static boolean is_the_last_digit_a_non_terminator(
			HashSet<String> vn,
			String putItem, String itemToExp) {
		
		if (is_the_last_digit_a_empty(putItem, itemToExp)) {
			return false;
		}
		
		if (itemToExp.contains(putItem)) {
			int index = itemToExp.indexOf(putItem);
		 	String lastDigit = itemToExp.substring(index+1, index+2);
		 	return vn.contains(lastDigit);
		} else {
			return false;
		}
	}
	
	/*
	 * 该方式是用来判断
	 * 当前非终结符是否是在右侧表达式的最后一位
	 * 也就是后面没有符号了
	 */
	public static boolean is_the_last_digit_a_empty(
			String putItem, String itemToExp) {
		if (itemToExp.contains(putItem)) {
			
			int index = itemToExp.indexOf(putItem);
			return ( index == itemToExp.length() - 1);
			
		} else {
			return false;
		}
	}
	
	public static boolean isleftequalItem(
			String item, String item_Exp) {
		return item.equals(item_Exp);
	}
	
	//当调用这个方法就默认后面是有字符的
	//没有字符的情况不调用这个方法
	public static String getLastDigit(
			String putItem,String itemToExp) {
		if (itemToExp.contains(putItem)) {
			int index = itemToExp.indexOf(putItem);
		 	return itemToExp.substring(index+1, index+2);
		} else {
			return null;
		}
	}
	
	public static String getLast(
			String putItem, String itemToExp) {
		if (itemToExp.contains(putItem)) {
			int index = itemToExp.indexOf(putItem);
		 	return itemToExp.substring(index+1);
		} else {
			return null;
		}
	}
	
	public static ArrayList<String> getFirst(
			HashMap<String, ArrayList<String>> first,
			String itemExp) {
		
		String firstItem = itemExp.substring(0,1);
		
		if (firstItem == null) {
			return null;
		}
		
		if (firstItem.equals(EPSILON)) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("#");
			return temp;
		}
		
		if (first.containsKey(firstItem)) {
			ArrayList<String> temp = first.get(firstItem);
			
//			if (temp.contains(EPSILON)) {
//				temp.add("#");
//				temp.remove(EPSILON);
//			}
			
			return temp;
		} else if (!first.containsKey(firstItem)) {
			
			ArrayList<String> temp =  new ArrayList<String>();
			temp.add(firstItem);
			
			return temp;
		} else {
			return null;
		}
		
	}
	
	public static String findUseExp(
			HashMap<HashMap<String, String>, ArrayList<String>> selectMap,
			HashMap<String, ArrayList<String>> expMap,
			String VnArr, String VtArr) {
		
		try {
			ArrayList<String> keyToExps = expMap.get(VnArr);
			for (String keyToExp : keyToExps) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put(VnArr, keyToExp);
				
				if (selectMap.get(temp).contains(VtArr)) {
					return keyToExp;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
		return "";
	}
}
