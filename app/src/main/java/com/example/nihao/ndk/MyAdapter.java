package com.example.nihao.ndk;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<String> msgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftmsg;
        TextView rightmsg;
        public ViewHolder(View v){
            super(v);
            leftLayout = (LinearLayout)v.findViewById(R.id.leftLayout);
            rightLayout = (LinearLayout)v.findViewById(R.id.rightLayout);
            leftmsg = (TextView)v.findViewById(R.id.leftmsg);
            rightmsg = (TextView)v.findViewById(R.id.rightmsg);
        }
    }
    public MyAdapter(List<String> outMsgList){
        msgList = outMsgList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        String msg = msgList.get(position);
        try{
            String[] msgL = msg.split("-");
            String ip = msgL[0];
            String date = msgL[1];
            String relMsg = msgL[2];
            String packMsg = ip+" "+date+"\n"+relMsg;
            if (ip.equals(ImActivity.LOCALIP)){
                Log.d("eee","1");
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.leftmsg.setText(packMsg);
            }
            else{
                Log.d("eee","2");
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.rightmsg.setText(packMsg);
            }
        }catch(Exception e){
            Log.d("eee","3");
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftmsg.setText(msg);
        }
    }
    @Override
    public int getItemCount(){
        return msgList.size();
    }
}
