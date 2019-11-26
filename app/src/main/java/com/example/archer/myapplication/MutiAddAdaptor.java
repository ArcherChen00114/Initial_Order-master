package com.example.archer.myapplication;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archer on 2018/5/13.
 */

public class MutiAddAdaptor extends RecyclerView.Adapter<MutiAddAdaptor.ViewHolder> {
    private Context context;
    public List<person> list;
    public MutiAddAdaptor(Context context, List<person> list){
        this.context=context;
        this.list=list;
    }

    public void addPerson(){
        person model=new person();
        list.add(model);
        notifyDataSetChanged();
    }
    public List<person> getData(List<person> mlist){
         List<person> Addlist = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            person model=list.get(i);
            //TODO make model=Name.gettext,HP.getText,Inti.getText
            Addlist.add(model);
            //TODO BUG!
            Log.e("tag","getData"+list.size());
        }
        return Addlist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null);
        MutiAddAdaptor.ViewHolder viewHolder = new MutiAddAdaptor.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MutiAddAdaptor.ViewHolder holder, final int position) {

        final person model = list.get(position);
        holder.Name.setText(model.getName());
        holder.HP.setText(""+model.getMaxHP());
        holder.Inti.setText(""+model.getInti());
        holder.HP.addTextChangedListener(new MyTextWatcher(holder.HP,position,list));
        holder.Name.addTextChangedListener(new MyTextWatcher(holder.Name,position,list));
        holder.Inti.addTextChangedListener(new MyTextWatcher(holder.Inti,position,list));
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public EditText HP;
        public EditText Name;
        public EditText Inti;
        public ViewHolder(View itemView) {
            super(itemView);
            HP=itemView.findViewById(R.id.Input_HP);
            Name=itemView.findViewById(R.id.Input_name);
            Inti=itemView.findViewById(R.id.Input_Inti);
        }
    }

    @Override
    public int getItemCount() {
        return list !=null? list.size():0;
    }
}
