package grammatical_analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Grammar implements Serializable {
	private static final long serialVersionUID = 1L;

    public Grammar() {
        super();
        gsArray = new ArrayList<String>();
        nvSet = new TreeSet<Character>();
        ntSet = new TreeSet<Character>();
        firstMap = new HashMap<Character, TreeSet<Character>>();
        followMap = new HashMap<Character, TreeSet<Character>>();
        selectMap = new TreeMap<Character, HashMap<String, TreeSet<Character>>>();
    }

    private String[][] analyzeTable;

    /**
     * Select集合
     */
    private TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap;

    /**
     * LL（1）文法产生集合
     */
    private ArrayList<String> gsArray;

    /**
     * 表达式集合
     */
    private HashMap<Character, ArrayList<String>> expressionMap;

    /**
     * 开始符
     */
    private Character s;

    /**
     * Vn非终结符集合
     */
    private TreeSet<Character> nvSet;

    /**
     * Vt终结符集合
     */
    private TreeSet<Character> ntSet;

    /**
     * First集合
     */
    private HashMap<Character, TreeSet<Character>> firstMap;

    /**
     * Follow集合
     */
    private HashMap<Character, TreeSet<Character>> followMap;

    public String[][] getAnalyzeTable() {
        return analyzeTable;
    }

    public void setAnalyzeTable(String[][] analyzeTable) {
        this.analyzeTable = analyzeTable;
    }

    public TreeMap<Character, HashMap<String, TreeSet<Character>>> getSelectMap() {
        return selectMap;
    }

    public void setSelectMap(TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap) {
        this.selectMap = selectMap;
    }

    public HashMap<Character, TreeSet<Character>> getFirstMap() {
        return firstMap;
    }

    public void setFirstMap(HashMap<Character, TreeSet<Character>> firstMap) {
        this.firstMap = firstMap;
    }

    public HashMap<Character, TreeSet<Character>> getFollowMap() {
        return followMap;
    }

    public void setFollowMap(HashMap<Character, TreeSet<Character>> followMap) {
        this.followMap = followMap;
    }

    public HashMap<Character, ArrayList<String>> getExpressionMap() {
        return expressionMap;
    }

    public void setExpressionMap(HashMap<Character, ArrayList<String>> expressionMap) {
        this.expressionMap = expressionMap;
    }

    public ArrayList<String> getGsArray() {
        return gsArray;
    }

    public void setGsArray(ArrayList<String> gsArray) {
        this.gsArray = gsArray;
    }

    public Character getS() {
        return s;
    }

    public void setS(Character s) {
        this.s = s;
    }

    public TreeSet<Character> getNvSet() {
        return nvSet;
    }

    public void setNvSet(TreeSet<Character> nvSet) {
        this.nvSet = nvSet;
    }

    public TreeSet<Character> getNtSet() {
        return ntSet;
    }

    public void setNtSet(TreeSet<Character> ntSet) {
        this.ntSet = ntSet;
    }

    /**
     * 获取非终结符集与终结符集
     */
    public void getNvNt() {
        for (String gsItem : gsArray) {
            String[] nvNtItem = gsItem.split("->");
            String charItemStr = nvNtItem[0];
            char charItem = charItemStr.charAt(0);
            // nv在左边
            nvSet.add(charItem);
        }
        for (String gsItem : gsArray) {
            String[] nvNtItem = gsItem.split("->");
            // nt在右边
            String nvItemStr = nvNtItem[1];
            // 遍历每一个字
            for (int i = 0; i < nvItemStr.length(); i++) {
                char charItem = nvItemStr.charAt(i);
                if (!nvSet.contains(charItem)) {
                    ntSet.add(charItem);
                }
            }
        }
    }

    /**
     * 初始化表达式集合
     */
    public void initExpressionMaps() {
        expressionMap = new HashMap<Character, ArrayList<String>>();
        for (String gsItem : gsArray) {
            String[] nvNtItem = gsItem.split("->");
            String charItemStr = nvNtItem[0];
            String charItemRightStr = nvNtItem[1];
            char charItem = charItemStr.charAt(0);
            if (!expressionMap.containsKey(charItem)) {
                ArrayList<String> expArr = new ArrayList<String>();
                expArr.add(charItemRightStr);
                expressionMap.put(charItem, expArr);
            } else {
                ArrayList<String> expArr = expressionMap.get(charItem);
                expArr.add(charItemRightStr);
                expressionMap.put(charItem, expArr);
            }
        }
    }

    /**
     * 获取 First 集
     */
    public void getFirst() {
        // 遍历所有Nv,求出它们的First集合
        Iterator<Character> iterator = nvSet.iterator();
        while (iterator.hasNext()) {
            Character charItem = iterator.next();
            ArrayList<String> arrayList = expressionMap.get(charItem);
            for (String itemStr : arrayList) {
                boolean shouldBreak = false;
                // Y1Y2Y3...Yk
                for (int i = 0; i < itemStr.length(); i++) {
                    char itemitemChar = itemStr.charAt(i);
                    TreeSet<Character> itemSet = firstMap.get(charItem);
                    if (null == itemSet) {
                        itemSet = new TreeSet<Character>();
                    }
                    shouldBreak = calcFirst(itemSet, charItem, itemitemChar);
                    if (shouldBreak) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 计算 First 函数
     *
     * @param itemSet
     * @param charItem
     * @param itemitemChar
     * @return boolean
     */
    private boolean calcFirst(TreeSet<Character> itemSet, Character charItem, char itemitemChar) {

        // 将它的每一位和Nt判断下
        // 是终结符或空串,就停止，并将它加到FirstMap中
        if (itemitemChar == 'ε' || ntSet.contains(itemitemChar)) {
            itemSet.add(itemitemChar);
            firstMap.put(charItem, itemSet);
            // break;
            return true;
        } else if (nvSet.contains(itemitemChar)) {// 这一位是一个非终结符
            ArrayList<String> arrayList = expressionMap.get(itemitemChar);
            for (int i = 0; i < arrayList.size(); i++) {
                String string = arrayList.get(i);
                char tempChar = string.charAt(0);
                calcFirst(itemSet, charItem, tempChar);
            }
        }
        return true;
    }

    /**
     * 获取 Follow 集合
     */
    public void getFollow() {
    	//for循环初始化各个非终结符的follow集合
        for (Character tempKey : nvSet) {
            TreeSet<Character> tempSet = new TreeSet<Character>();
            followMap.put(tempKey, tempSet);
        }
        //遍历所有非终结符,求出它们的First集合
        //使用的方法是迭代器的反向返回
        Iterator<Character> iterator = nvSet.descendingIterator();

        //循环遍历迭代器
        while (iterator.hasNext()) {
        	//获取当前非终结符
            Character charItem = iterator.next();
            System.out.println("当前判断的非终结符:" + charItem);
            //获取表达式集合中的非终结符集合
            Set<Character> keySet = expressionMap.keySet();
            
            //遍历上述的keySet,也就是非终结符集合
            for (Character keyCharItem : keySet) {
            	//返回当前遍历到的非终结符的右侧表达式集合
                ArrayList<String> charItemArray = expressionMap.get(keyCharItem);
                
                for (String itemCharStr : charItemArray) {
                	//遍历该非终结符的右侧表达式的集合
                    System.out.println("\t"+keyCharItem + "->" + itemCharStr);
                    TreeSet<Character> itemSet = followMap.get(charItem);
                    calcFollow(charItem, charItem, keyCharItem, itemCharStr, itemSet);
                }
            }
        }
    }

    /**
     * 计算 Follow 集
     *
     * @param putCharItem 正在查询item
     * @param charItem    待找item
     * @param keyCharItem 节点名
     * @param itemCharStr 符号集
     * @param itemSet     结果集合
     */
    private void calcFollow(Character putCharItem, Character charItem, Character keyCharItem, String itemCharStr,
                            TreeSet<Character> itemSet) {

    	/*
    	 * 这是具体的计算Follow集合的方法
    	 * 通过调用TextUtil类中的四个不同情况的比较方法
    	 * 返回相应符合要求的布尔值
    	 * 然后进入其中的添加操作
    	 */
    	
        // (1):A是S(开始符号),加入#
        if (charItem.equals(s)) {
            itemSet.add('#');
            System.out.println("\t"+"-发现开始符:" + charItem + ",添加{#}到其Follow集合中。");
            followMap.put(putCharItem, itemSet);

        }
        // (2):Ab,直接添加终结符b
        if (TextUtil.containsAb(ntSet, itemCharStr, charItem)) {
            Character alastChar = TextUtil.getAlastChar(itemCharStr, charItem);
            System.out.println("\t"+"表达式:"+itemCharStr+"--发现当前非终结符:" + charItem + "后直接连接了终结符:" + alastChar);
            System.out.println("\t"+"--所以直接添加该终结符到该非终结符的Follow集合中");
            itemSet.add(alastChar);
            followMap.put(putCharItem, itemSet);
            // return;
        }
        // (2):AB,Follw(A) += First(B),添加first集合
        //	如果B能在n步内推出空值，那么就加入Follow(B)
        if (TextUtil.containsAB(nvSet, itemCharStr, charItem)) {
            Character alastChar = TextUtil.getAlastChar(itemCharStr, charItem);
            //获取当前表达式中该非终结符后的终结符字符串
            System.out.println("\t"+"表达式:"+itemCharStr+"--发现当前非终结符:"+ charItem + "连接了非终结符"+alastChar);
            System.out.println("\t"+"--所以添加First(" + alastChar + ")到该非终结符的Follow集合中");
            TreeSet<Character> treeSet = firstMap.get(alastChar);
            
            //这一步是用来将加入的ε转换成#
            itemSet.addAll(treeSet);
            if (treeSet.contains('ε')) {
                itemSet.add('#');
            }
            itemSet.remove('ε');
            
            //将得出的follow集合加入到对应的非终结符follow集合中
            followMap.put(putCharItem, itemSet);

            //下式是用来判断后面的符号能否推导出ε
            if (TextUtil.containsbAbIsNull(nvSet, itemCharStr, charItem, expressionMap)) {
                //获取后一个字符
            	char tempChar = TextUtil.getAlastChar(itemCharStr, charItem);
                //判断当前的标识符与遍历到的非终结符是否相同
                if (!keyCharItem.equals(charItem)) {
                    System.out.println("\t\t"+"-发现上述非终结符:"+ charItem + "的后一位:" + tempChar + "能推导出空");
                    System.out.println("\t\t"+"--所以要将Follow(" + keyCharItem + ")添加到Follow(" + itemCharStr + ")中");
                    //这一步就等价于getFollow集合中的计算方法
                    //也就是等同为了要添加遍历到的非终结符的follow集合到标识符的Follow集合中
                    Set<Character> keySet = expressionMap.keySet();
                    for (Character keyCharItems : keySet) {
                        ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
                        for (String itemCharStrs : charItemArray) {
                            calcFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
                        }
                    }
                }
            }
        }
        // (3)B->aA,=Follow(B),添加followB
        if (TextUtil.containsbA(nvSet, itemCharStr, charItem, expressionMap)) {
            /*
             * 由上面的if语句中的判断方法我们得知
             * 其返回的结果式比较
             */
        	if (!keyCharItem.equals(charItem)) {
                System.out.println("\t" + "表达式:" + itemCharStr + "-发现当前非终结符:" + charItem + "后面直接为空");
                System.out.println("\t" + "--所以将Follow(" + keyCharItem + ")添加到Follow(" + charItem + ")中");
                Set<Character> keySet = expressionMap.keySet();
                for (Character keyCharItems : keySet) {
                    ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
                    for (String itemCharStrs : charItemArray) {
                        calcFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
                    }
                }
            }
        }
    }

    /**
     * 获取 Select 集合
     */
    public void getSelect() {
        // 遍历每一个表达式
        // HashMap<Character, HashMap<String, TreeSet<Character>>>
        Set<Character> keySet = expressionMap.keySet();
        for (Character selectKey : keySet) {
            ArrayList<String> arrayList = expressionMap.get(selectKey);
            // 每一个表达式
            HashMap<String, TreeSet<Character>> selectItemMap = new HashMap<String, TreeSet<Character>>();
            for (String selectExp : arrayList) {
                /**
                 * 存放select结果的集合
                 */
                TreeSet<Character> selectSet = new TreeSet<Character>();
                // set里存放的数据分3种情况,由selectExp决定
                /*
                 * 第一种情况是当前非终结符直接推出ε
                 * A->ε
                 * 所以直接添加Follow(A)到Select(A)中
                 */
                if (TextUtil.isEmptyStart(selectExp)) {
                    selectSet = followMap.get(selectKey);
                    //去除其中的ε
                    selectSet.remove('ε');
                    selectItemMap.put(selectExp, selectSet);
                }
                // 2.Nt开始,=Nt
                /*
                 * 第二种情况是右侧第一位是终结符
                 * 那么就直接添加该非终结符到其中就可以了
                 */
                if (TextUtil.isNtStart(ntSet, selectExp)) {
                    selectSet.add(selectExp.charAt(0));
                    selectSet.remove('ε');
                    selectItemMap.put(selectExp, selectSet);
                }
                // 3.Nv开始，=first(Nv)
                /*
                 * 第三种情况是判断右侧表达式是否是非终结符开始的
                 * 如果是那就要添加右侧非终结符的First集合到Select(A)中
                 */
                if (TextUtil.isNvStart(nvSet, selectExp)) {
                    selectSet = firstMap.get(selectKey);
                    selectSet.remove('ε');
                    selectItemMap.put(selectExp, selectSet);
                }
                selectMap.put(selectKey, selectItemMap);
            }
        }
    }

    /**
     * 生成预测分析表
     */
    public void genAnalyzeTable() throws Exception {
        Object[] ntArray = ntSet.toArray();
        Object[] nvArray = nvSet.toArray();
        // 预测分析表初始化
        analyzeTable = new String[nvArray.length + 1][ntArray.length + 1];

        System.out.println("====================\n预测分析表\n====================");

        String timeString = (long)System.currentTimeMillis() + "";
        
        File outputFile = new File("F:\\Java_Programming\\Analysis_Lexical\\src\\" + timeString + "analysisTable.txt");
        try (Writer writer = new FileWriter(outputFile)) {
            writer.write("====================\n预测分析表\n====================\n");
            // 输出一个占位符
            System.out.print("表" + "\t");
            writer.write("表" + "\t");
            analyzeTable[0][0] = "Nv/Nt";

            // 初始化首行
            for (int i = 0; i < ntArray.length; i++) {
                if (ntArray[i].equals('ε')) {
                    ntArray[i] = '#';
                }
                writer.write(ntArray[i] + "\t\t");
                System.out.print(ntArray[i] + "\t\t");

                analyzeTable[0][i + 1] = ntArray[i] + "";
            }

            System.out.println("");
            writer.write("\n");
            for (int i = 0; i < nvArray.length; i++) {
                // 首列初始化
                writer.write(nvArray[i] + "\t");
                System.out.print(nvArray[i] + "\t");

                analyzeTable[i + 1][0] = nvArray[i] + "";
                for (int j = 0; j < ntArray.length; j++) {
                    String findUseExp = TextUtil.findUseExp(selectMap, Character.valueOf((Character) nvArray[i]),
                            Character.valueOf((Character) ntArray[j]));
                    if (null == findUseExp) {
                        writer.write("空\t\t");
                        System.out.print("空\t\t");

                        analyzeTable[i + 1][j + 1] = "";
                    } else {
                        writer.write(nvArray[i] + "->" + findUseExp + "\t\t");
                        System.out.print(nvArray[i] + "->" + findUseExp + "\t\t");

                        analyzeTable[i + 1][j + 1] = nvArray[i] + "->" + findUseExp;
                    }
                }
                writer.write("\n");
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
