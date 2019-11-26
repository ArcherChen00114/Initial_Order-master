package com.example.archer.myapplication;

/**
 * Created by archer on 2018/4/21.
 */

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.polyak.iconswitch.IconSwitch;

/**
 * Created by archer on 2018/2/10.
 */

public class EditDialog extends DialogFragment {
    private EditText Input_name;
    private EditText Input_HP;
    private EditText Input_Inti;
    private Switch iconSwitch;
    private inputListener Listener;
    public interface inputListener{
        void InputComplete(String name, int HP, int Inti);
    }
    public boolean check(String name, int HP, int Inti){
        if (name==null||name.length()==0){
            Toast.makeText(getContext(),"name不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (HP==0){
            Toast.makeText(getContext(),"HP不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Inti==0){
            Toast.makeText(getContext(),"Inti不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_dialog, null);
        Input_name = (EditText) view.findViewById(R.id.Input_name);
        Input_Inti = (EditText) view.findViewById(R.id.Input_Inti);
        Input_HP = (EditText) view.findViewById(R.id.Input_HP);
        iconSwitch=(Switch) view.findViewById(R.id.Icon);



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                if(check(Input_name.getText().toString()
                                        , Integer.parseInt(Input_HP.getText().toString().equals("")?"0":Input_HP.getText().toString())
                                        , Integer.parseInt(Input_Inti.getText().toString().equals("")?"0":Input_Inti.getText().toString()))==true) {
                                    inputListener Listener = (inputListener) getActivity();
                                    Listener.InputComplete(Input_name.getText().toString()
                                            , Integer.parseInt(Input_HP.getText().toString().equals("")?"0":Input_HP.getText().toString())
                                            , Integer.parseInt(Input_Inti.getText().toString().equals("")?"0":Input_Inti.getText().toString()));
                                }
                            }
                        }).setNegativeButton("Cancel", null);

        return builder.create();
    }
}
