package grammatical_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class TextUtil {
	/**
     * (3)B->aA,=Follow(B)
     *
     * @param nvSet				//非终结符集合
     * @param itemCharStr		//当前位置的非终结符所对应的右侧表达式
     * @param a					//当前分析的非终结符
     * @param expressionMap		//整理好的表达式集合
     */
    public static boolean containsbA(TreeSet<Character> nvSet, String itemCharStr, Character a,
                                     HashMap<Character, ArrayList<String>> expressionMap) {
        String aStr = a.toString();
        String lastStr = itemCharStr.substring(itemCharStr.length() - 1);
        
        /*
         * 用来判断当前传入的非终结符
         * 和对应的右侧表达式中的非终结符是否相等
         * 返回其对应的布尔值
         */
        
        return lastStr.equals(aStr);
    }

    /**
     * 形如 aBb,b->空
     * 也就是进一步判断是否需要将Follow(b)添加到B中
     *
     * @param nvSet
     * @param itemCharStr
     * @param a
     * @param expressionMap
     */
    public static boolean containsbAbIsNull(TreeSet<Character> nvSet, String itemCharStr, Character a,
                                            HashMap<Character, ArrayList<String>> expressionMap) {
        String aStr = a.toString();
        if (containsAB(nvSet, itemCharStr, a)) {
        	//获取到当前非终结符的后一位符号
            Character alastChar = getAlastChar(itemCharStr, a);
            System.out.println("\n------表达式集合打印:\n--" + expressionMap.toString() + "--\n------打印结束;");
            //将后一位的符号的表达式集合获取到
            ArrayList<String> arrayList = expressionMap.get(alastChar);
            //if判断是否能推出空集，也就是表达式集合中是否存在ε
            if (arrayList.contains("ε")) {
                System.out.println(alastChar + "添加ε到Follow(" + aStr + ")中");
                //存在就返回true
                return true;
            }
        }
        //不存在就返回false
        return false;

    }

    /**
     *是否包含这种的字符串
     * (2)Ab,=First(b)-ε,直接添加终结符
     *	与下面的方法不同的是传入的参数不同
     *	这个方法传入的是终结符集合，返回的也是终结符
     * @param ntSet
     * @param itemCharStr
     * @param a
     * @return boolean
     */
    public static boolean containsAb(TreeSet<Character> ntSet, String itemCharStr, Character a) {
        String aStr = a.toString();
        if (itemCharStr.contains(aStr)){
            int aIndex = itemCharStr.indexOf(aStr);
            String findStr;
            try {
                findStr = itemCharStr.substring(aIndex + 1, aIndex + 2);
            } catch (Exception e) {
                return false;
            }
            return ntSet.contains(findStr.charAt(0));
        } else {
            return false;
        }
    }

    /**
     * 是否包含这种的字符串
     * (2).2Ab,=First(b)-ε
     * 	该方法传入的是非终结符集合，返回的也是非终结符
     * @param nvSet
     * @param itemCharStr
     * @param a
     * @return boolean
     */
    public static boolean containsAB(TreeSet<Character> nvSet, String itemCharStr, Character a) {
        String aStr = a.toString();
        if (itemCharStr.contains(aStr)) {
            int aIndex = itemCharStr.indexOf(aStr);
            String findStr;
            try {
                findStr = itemCharStr.substring(aIndex + 1, aIndex + 2);
            } catch (Exception e) {
                return false;
            }
            return nvSet.contains(findStr.charAt(0));
        } else {
            return false;
        }
    }

    /**
     * 获取 A 后的字符
     *
     * @param itemCharStr
     * @param a
     */
    public static Character getAlastChar(String itemCharStr, Character a) {
        String aStr = a.toString();
        if (itemCharStr.contains(aStr)) {
            int aIndex = itemCharStr.indexOf(aStr);
            String findStr = "";
            try {
                findStr = itemCharStr.substring(aIndex + 1, aIndex + 2);
            } catch (Exception e) {
                return null;
            }
            return findStr.charAt(0);
        }
        return null;
    }

    /**
     * 是否为 ε 开始的
     *
     * @param selectExp
     */
    public static boolean isEmptyStart(String selectExp) {
        char charAt = selectExp.charAt(0);
        return charAt == 'ε';
    }

    /**
     * 是否是终结符开始的
     *
     * @param ntSet
     * @param selectExp
     */
    public static boolean isNtStart(TreeSet<Character> ntSet, String selectExp) {
        char charAt = selectExp.charAt(0);
        return ntSet.contains(charAt);
    }

    /**
     * 是否是非终结符开始的
     *
     * @param nvSet
     * @param selectExp
     * @return
     */
    public static boolean isNvStart(TreeSet<Character> nvSet, String selectExp) {
        char charAt = selectExp.charAt(0);
        return nvSet.contains(charAt);
    }

    /**
     * 查找产生式
     *
     * @param selectMap
     * @param peek      当前 Nv
     * @param charAt    当前字符
     * @return
     */
    public static String findUseExp(TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap, Character peek,
                                    char charAt) {
        try {
        	//获取select集合中当前分析的非终结符的集合
            HashMap<String, TreeSet<Character>> hashMap = selectMap.get(peek);
            //相当于获取当前非终结符对应的表达式
            Set<String> keySet = hashMap.keySet();
            for (String useExp : keySet) {
                TreeSet<Character> treeSet = hashMap.get(useExp);
                //查看当前的终结符是否在对应表达式的Select集合中
                if (treeSet.contains(charAt)) {
                    return useExp;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
