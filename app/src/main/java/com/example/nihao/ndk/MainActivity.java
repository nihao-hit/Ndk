package com.example.nihao.ndk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    // Used to load the 'native-lib' library on application startup.

    GridView gridview;
    private SimpleAdapter adapter;
    List<Map<String,Object>> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        gridview = (GridView)findViewById(R.id.gridview);
        initGridView();
        // Example of a call to a native method
    }
    private void initData(){
        int[] imgList = {R.drawable.calculator,R.drawable.im,R.drawable.map};
        String[] textList = {"计算器","聊天室","地图"};
        dataList = new ArrayList<Map<String,Object>>();
        for (int i=0;i<3;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",imgList[i]);
            map.put("text",textList[i]);
            dataList.add(map);
        }
    }
    private void initGridView(){
        String[] from = {"image","text"};
        int[] to = {R.id.image,R.id.text};
        adapter = new SimpleAdapter(this,dataList,R.layout.layout_item,from,to);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3){
                switch(position){
                    case 0:
                        startActivity(new Intent(MainActivity.this,CalculatorActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,ImActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,MapActivity.class));
                        break;
                }
            }
        });
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
