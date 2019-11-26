package com.example.archer.myapplication;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by archer on 2018/5/23.
 */

public class MyTextWatcher implements TextWatcher {
    private EditText view;
    private int position;
    private List<person> list;
    public MyTextWatcher(EditText v,int position,List<person> list){
        this.list=list;
        this.view=v;
        this.position=position;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s=="-"){

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        person model=list.get(position);
        //TODO this list need to be sycned with other list
        switch (view.getId()){
            case R.id.Input_HP:
                try{
                    Log.e("TAG","Adding Data");
                    model.setMaxHP(Integer.parseInt(s.toString()));
                    model.setHP(Integer.parseInt(s.toString()));
                    list.set(position, model);
                }catch (Exception e){

                }
                /*if (TextUtils.isEmpty(s)){
                    s.insert(0,"0",0,1);
                    model.setHP(0);
                }else {
                }*/
                break;
            case R.id.Input_Inti:
                try{
                    Log.e("TAG","Adding Data");
                    model.setInti(Integer.parseInt(s.toString()));
                    list.set(position, model);
                }catch(Exception e){

                }
                /*if (TextUtils.isEmpty(s)) {
                    s.insert(0, "0", 0, 1);
                }else if(TextUtils.equals(s,"-")){
                    s.replace(0,1,"0");
                    model.setInti(0);
                }else {
                    Log.e("TAG","Adding Data");
                        model.setInti(Integer.parseInt(s.toString()));
                        list.set(position, model);

                }*/
                break;
            case R.id.Input_name:
                try{
                    Log.e("TAG","Adding Data");
                    model.setName(s.toString());
                    list.set(position, model);
                }catch(Exception e){

                }
                /*if (TextUtils.isEmpty(s)){
                    s.insert(0,"0",0,1);
                    model.setName("");
                } else{
                    Log.e("TAG","Adding Data");
                    model.setName(s.toString());
                    list.set(position, model);
                }*/
                break;
        }
    }
}
