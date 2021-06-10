package grammatical_analysis;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws Exception {

        // 第一步：获取 LL(1)文法
        ArrayList<String> gsArray = new ArrayList<String>();
        Grammar grammar = new Grammar();

        //初始化 LL(1), 设定该文法的产生式
        initGs(gsArray);

        grammar.setGsArray(gsArray);
        grammar.getNvNt();
        grammar.initExpressionMaps();
        grammar.getFirst();

        // 设置开始符
        grammar.setS('S');
        grammar.getFollow();
        grammar.getSelect();

        //打印预测分析表, 并保存文件
        grammar.genAnalyzeTable();

        // 创建一个分析器
//        Analyzer analyzer = new Analyzer();
//
//        // 设定开始符号
//        analyzer.setStartChar('S');
//        analyzer.setLl1Grammar(grammar);
//
//        // 待分析的字符串
//        analyzer.setStr("i+i*i#");
//
//        // 执行分析, 打印分析步骤, 保存文件
//        analyzer.analyze();
        
        Analyzer analyzer = new Analyzer();
        analyzer.setStartChar('S');
        analyzer.setLl1Grammar(grammar);
        analyzer.setStr("abcb#");
        analyzer.analyze();
        

    }

    /**
     * 初始化 LL(1)文法, 设定产生式
     *
     * @param gsArray
     */
    private static void initGs(ArrayList<String> gsArray) {
        //E' = M
        //T' = N
//        gsArray.add("E->TM");
//        gsArray.add("M->+TF");
//        gsArray.add("M->ε");
//        gsArray.add("T->FN");
//        gsArray.add("N->*FN");
//        gsArray.add("N->ε");
//        gsArray.add("F->(E)");
//        gsArray.add("F->i");
    	
//    	gsArray.add("S->MH");
//    	gsArray.add("S->a");
//    	gsArray.add("H->LSo");
//    	gsArray.add("H->ε");
//    	gsArray.add("K->dML");
//    	gsArray.add("K->ε");
//    	gsArray.add("L->eHf");
//    	gsArray.add("M->K");
//    	gsArray.add("M->bLM");
    	
    	gsArray.add("S->aSAb");
    	gsArray.add("S->Ab");
    	gsArray.add("A->cA");
    	gsArray.add("A->ε");
    }
}
