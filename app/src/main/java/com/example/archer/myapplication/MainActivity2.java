package com.example.archer.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archer on 2018/5/19.
 */

public class MainActivity2 extends AppCompatActivity {
    private MutiAddAdaptor MutiAdaptor;
    private RecyclerView recyclerView;
    private SqliteDBHelper sqliteDBHelper;
    private List<person> mlist;
    private Context context;
    private Button Finish;
    private FloatingActionButton fab;
    public void viewInit(){
        recyclerView=(RecyclerView) findViewById(R.id.MutiView);
    }
    public void dataInit(){
        sqliteDBHelper= new SqliteDBHelper(context,Constants.DB_NAME,null, com.example.archer.myapplication.Constants.DB_VERSION);
        mlist=new ArrayList<>();
        MutiAdaptor=new MutiAddAdaptor(context,mlist);
        recyclerView.setAdapter(MutiAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        notifyData();
    }
    public void eventInit(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setEnterTransition(explode);
        setContentView(R.layout.activity_main2);
        context=this;
        viewInit();
        dataInit();
        eventInit();
        Button Finish=(Button) findViewById(R.id.finish);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,MainActivity.class);

                Log.e("TAG","Start Get DATA");
                mlist=MutiAdaptor.getData(mlist);
                sqliteDBHelper.addPersonDataWithList(mlist);
                startActivity(intent);
                sqliteDBHelper.queryAllPersonDataOrderBy();
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MutiAdaptor.addPerson();
            }
        });
    }
    private void notifyData(){
        mlist.clear();
        mlist.addAll(sqliteDBHelper.queryAllPersonData());
        MutiAdaptor.notifyDataSetChanged();
        //TODO DO I NEED THIS?
    }
}
