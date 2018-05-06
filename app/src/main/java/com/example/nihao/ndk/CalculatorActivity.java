package com.example.nihao.ndk;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.util.Half.NaN;

public class CalculatorActivity extends AppCompatActivity {
    private TextView text;
    private Button backspace;
    private Button cls;
    private Button divide;
    private Button multiply;
    private Button minus;
    private Button add;
    private Button jiu;
    private Button ba;
    private Button qi;
    private Button liu;
    private Button wu;
    private Button shi;
    private Button san;
    private Button er;
    private Button yi;
    private Button ling;
    private Button dot;
    private Button abs;
    private Button equal;
    private static String expression = "";
    private static char opFlag = ' ';
    private static Double result = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calculator);
        initView();
        setOnClickListener();
    }
    private void initView(){
        text = (TextView)findViewById(R.id.text);
        backspace= (Button)findViewById(R.id.backspace);
        cls = (Button)findViewById(R.id.cls);
        divide= (Button)findViewById(R.id.divide);
        multiply = (Button)findViewById(R.id.multiply);
        minus = (Button)findViewById(R.id.minus);
        add = (Button)findViewById(R.id.add);
        jiu = (Button)findViewById(R.id.jiu);
        ba = (Button)findViewById(R.id.ba);
        qi = (Button)findViewById(R.id.qi);
        liu = (Button)findViewById(R.id.liu);
        wu = (Button)findViewById(R.id.wu);
        shi = (Button)findViewById(R.id.shi);
        san= (Button)findViewById(R.id.san);
        er = (Button)findViewById(R.id.er);
        yi = (Button)findViewById(R.id.yi);
        ling = (Button)findViewById(R.id.ling);
        dot= (Button)findViewById(R.id.dot);
        abs = (Button)findViewById(R.id.abs);
        equal= (Button)findViewById(R.id.equal);
    }
    private void setOnClickListener(){
        OnClick onClick = new OnClick();
        backspace.setOnClickListener(onClick);
        cls.setOnClickListener(onClick);
        divide.setOnClickListener(onClick);
        multiply.setOnClickListener(onClick);
        minus.setOnClickListener(onClick);
        add.setOnClickListener(onClick);
        jiu.setOnClickListener(onClick);
        ba.setOnClickListener(onClick);
        qi.setOnClickListener(onClick);
        liu.setOnClickListener(onClick);
        wu.setOnClickListener(onClick);
        shi.setOnClickListener(onClick);
        san.setOnClickListener(onClick);
        er.setOnClickListener(onClick);
        yi.setOnClickListener(onClick);
        ling.setOnClickListener(onClick);
        dot.setOnClickListener(onClick);
        abs.setOnClickListener(onClick);
        equal.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(expression.length() != 0){
                opFlag = expression.charAt(expression.length()-1);
            }
            switch(v.getId()){
                case R.id.backspace:
                    if(expression.length() != 0)
                    {
                        String tmp = "";
                        for(int i=0;i<expression.length()-1;i++){
                            tmp += expression.charAt(i);
                        }
                        expression = tmp;
                    }
                    else opFlag = ' ';
                    text.setText(expression);
                    break;
                case R.id.cls:
                    expression = "";
                    opFlag = ' ';
                    text.setText("");
                    break;
                case R.id.divide:
                    if(isOperator(opFlag)){
                        break;
                    }
                    expression += "/";
                    text.setText(expression);Log.d("eee",opFlag+"");
                    break;
                case R.id.multiply:
                    if(isOperator(opFlag)){
                        break;
                    }
                    expression += "*";
                    text.setText(expression);Log.d("eee",opFlag+"");
                    break;
                case R.id.minus:
                    expression += "-";
                    text.setText(expression);Log.d("eee",opFlag+"");
                    break;
                case R.id.add:
                    if(isOperator(opFlag)){
                        break;
                    }
                    expression += "+";
                    text.setText(expression);Log.d("eee",opFlag+"");
                    break;
                case R.id.jiu:
                    expression += "9";
                    text.setText(expression);
                    break;
                case R.id.ba:
                    expression += "8";
                    text.setText(expression);
                    break;
                case R.id.qi:
                    expression += "7";
                    text.setText(expression);
                    break;
                case R.id.liu:
                    expression += "6";
                    text.setText(expression);
                    break;
                case R.id.wu:
                    expression += "5";
                    text.setText(expression);
                    break;
                case R.id.shi:
                    expression += "4";
                    text.setText(expression);
                    break;
                case R.id.san:
                    expression += "3";
                    text.setText(expression);
                    break;
                case R.id.er:
                    expression += "2";
                    text.setText(expression);
                    break;
                case R.id.yi:
                    expression += "1";
                    text.setText(expression);
                    break;
                case R.id.ling:
                    expression += "0";
                    text.setText(expression);
                    break;
                case R.id.dot:
                    if(isOperator(opFlag)){
                        break;
                    }
                    expression += ".";
                    text.setText(expression);
                    break;
                case R.id.abs:
                    break;
                case R.id.equal:
                    result = CalculatorHelper.conversion(expression);
                    Log.d("ddd","ddd");
                    if(!Double.isNaN(result))
                    {
                        text.setText(expression+"="+result);
                        expression = result.toString();
                    }
                    else
                        error();
                    break;
            }
        }
    }
    private void error(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("输入错误");
        dialog.setMessage("请按回退键或清屏键重新输入");
        dialog.setCancelable(true);
        dialog.show();
    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '.';
    }
}
