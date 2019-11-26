package com.example.archer.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by archer on 2018/5/2.
 */

public class HpChangeDialog extends DialogFragment {

    private TextView Input_change;
    private AddorMinus number;
    private ProgressBar bar;
    public HpChangeDialog(){

    }
    @SuppressLint("ValidFragment")
    public HpChangeDialog(ProgressBar bar){
        this.bar=bar;
    }
    public interface AddorMinus{
        void AddorMinus(int change,ProgressBar bar);
    }
    public boolean NoNull(int change){
        if (change==0){
            return false;
        }
        return true;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder Plus = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addminus_dialog, null);
        Input_change = (EditText) view.findViewById(R.id.Input_change);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        Plus.setView(view)
                // Add action buttons
                .setPositiveButton("Enter",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                if(NoNull(Integer.parseInt(Input_change.getText().toString().equals("")?"0":Input_change.getText().toString()))){
                                   AddorMinus number= (AddorMinus) getActivity();
                                   number.AddorMinus(Integer.parseInt(Input_change.getText().toString()),bar);
                                }
                            }
                        }).setNegativeButton("Cancel", null);

        return Plus.create();
    }
}
