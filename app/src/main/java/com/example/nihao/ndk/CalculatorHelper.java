package com.example.nihao.ndk;
import java.util.Collections;
import java.util.Stack;

/*算数表达式求值，直接调用CalculatorHelper的类方法conversion()，传入算数表达式，将返回一个浮点值结果，如果计算过程错误，将返回一个NaN*/
public class CalculatorHelper {
    private Stack<String> postfixStack = new Stack<String>();
    private Stack<Character> opStack = new Stack<Character>();
    private int[] operatPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 };
    public native double add(double num1,double num2);
    public native double sub(double num1,double num2);
    public native double mul(double num1,double num2);
    public native double div(double num1,double num2);

    static {
        System.loadLibrary("ArithHelper");
    }
    public static double conversion(String expression) {
        double result = 0;
        CalculatorHelper cal = new CalculatorHelper();
        try {
            expression = transform(expression);
            result = cal.calculate(expression);
        } catch (Exception e) {
            return 0.0 / 0.0;
        }
        return result;
    }

    /*将表达式中负数的符号更改*/
    private static String transform(String expression) {
        char[] arr = expression.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }
        if(arr[0]=='~'||arr[1]=='('){
            arr[0]='-';
            return "0"+new String(arr);
        }else{
            return new String(arr);
        }
    }

    /*按照给定的表达式计算*/
    public double calculate(String expression) {
        Stack<String> resultStack = new Stack<String>();
        prepare(expression);
        Collections.reverse(postfixStack);
        String firstValue, secondValue, currentValue;
        while (!postfixStack.isEmpty()) {
            currentValue = postfixStack.pop();
            if (!isOperator(currentValue.charAt(0))) {
                currentValue = currentValue.replace("~", "-");
                resultStack.push(currentValue);
            } else {
                secondValue = resultStack.pop();
                firstValue = resultStack.pop();
                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");

                String tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
                resultStack.push(tempResult);
            }
        }
        return Double.valueOf(resultStack.pop());
    }

    /*数据准备阶段将表达式转换成为后缀式栈*/
    private void prepare(String expression) {
        opStack.push(',');
        char[] arr = expression.toCharArray();
        int currentIndex = 0;
        int count = 0;
        char currentOp, peekOp;
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if (isOperator(currentOp)) {
                if (count > 0) {
                    postfixStack.push(new String(arr, currentIndex, count));
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {
                    while (opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (opStack.peek() != ',') {
            postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
        }
    }

    /*判断是否为算术符号*/
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    /*利用ASCII码-40做下标去算术符号优先级*/
    public boolean compare(char cur, char peek) {
        boolean result = false;
        if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
            result = true;
        }
        return result;
    }

    /*按照给定的算术运算符做计算*/
    private String calculate(String firstValue, String secondValue, char currentOp) {
        String result = "";
        switch (currentOp) {
            case '+':
                result = String.valueOf(add(Double.valueOf(firstValue), Double.valueOf(secondValue)));
                break;
            case '-':
                result = String.valueOf(sub(Double.valueOf(firstValue), Double.valueOf(secondValue)));
                break;
            case '*':
                result = String.valueOf(mul(Double.valueOf(firstValue), Double.valueOf(secondValue)));
                break;
            case '/':
                result = String.valueOf(div(Double.valueOf(firstValue), Double.valueOf(secondValue)));
                break;
        }
        return result;
    }
}
