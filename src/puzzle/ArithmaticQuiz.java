package puzzle;

import java.util.ArrayList;
import java.util.Random;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class ArithmaticQuiz {

    public static String puzzle() {
        ArrayList<String> operator = new ArrayList<String>();
        operator.add("+");
        operator.add("-");
        operator.add("*");
        operator.add("/");

        String expression = "";
        int j = 0, k = 0;
        for (int i = 1; i <= 7; i++) {
            if (i % 2 == 0) {
                if (j == 0) {
                    expression = expression + operator.get(generateoperator());
                    j++;
                } else {
                    expression = expression + operator.get(generateoperator());
                    if (j == 1) {
                        expression = expression + "(";
                    }
                    j = 2;
                }
            } else {
                if (k == 0) {
                    expression = expression + "(" + generateNum();
                    k++;
                } else {
                    expression = expression + generateNum();
                    if (k == 1) {
                        expression = expression + ")";
                    }
                    if (k == 2) {
                        expression = expression + ")";
                    }
                    k = 3;
                }
            }
        }
        expression = expression + ")";
//        System.out.println(expression);
        return expression;
    }

    public static String puzzleAns(String expression) throws Exception {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        double result = Double.valueOf(engine.eval(expression) + "");
        return result + "";
    }

    public static int generateoperator() {
        Random rand = new Random();
        int randomNum = 1 + rand.nextInt((2 - 0) + 1);
        return randomNum;
    }

    public static int generateNum() {
        Random rand = new Random();
        int randomNum = 1 + rand.nextInt((9 - 0) + 1);
        return randomNum;
    }

    public static void main(String[] args) {
        String q = puzzle();
        try {
            System.out.println("Ans: " + puzzleAns(q));
            if (puzzleAns(q).equals("-Infinity")) {
                q = puzzle();
                System.out.println("Ans: " + puzzleAns(q));
            }
        } catch (Exception ex) {
        }
        double vals = 12.69524242;
        String ttao = String.format("%.01f", vals);
        String var = "12.7";
        if (var.equals(ttao)) {
            System.out.println("true");
        }
    }
}
