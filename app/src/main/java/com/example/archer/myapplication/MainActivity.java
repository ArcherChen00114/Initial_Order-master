package com.example.archer.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;

import DataBean.AddBean;
import adapter.AddRecyclerAdapter;
import adapter.ViewHolder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,RecyclerViewAdaptor.MyTouchListener,RecyclerViewAdaptor.change,EditDialog.inputListener,HpChangeDialog.AddorMinus {

    private RecyclerView recyclerView;
    private Context context;
    private DrawerLayout mDrawer;
    private ListView listView;
    private Color color;
    // Item List
    private List<person> mlist;
    // ImageList
    private int[] images = {R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
    // Custom Recycler View Adaptor
    private RecyclerViewAdaptor madapter;
    private adapter.SimpleAdapter<AddBean> adapter;
    private SqliteDBHelper sqliteDBHelper;
    private int currentVersion;
    private int imgArray[] = {R.mipmap.ic_launcher};
    private byte picArray[][];
    private Boolean plus;
    private int plusnum;
    private String[] ListViewItem={"数据清空","批量修改"};
    private ItemTouchHelper.Callback Callback;
    private NavigationView navigationView;
    private ProgressBar bar;

    private int position;
    public void viewInit(){
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawer=(DrawerLayout) findViewById(R.id.MDrawer);
        //add all parts need to added into view

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //在这里处理item的点击事件 int id = item.getItemId();
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                switch (id){
                    case R.id.action_clear:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("是否清空数据");
                        builder.setMessage("是否清空数据");
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sqliteDBHelper.cleanAllData();
                                notifyData();
                            }
                        }).setNegativeButton("no", null);
                        builder.show();
                        break;
                    case R.id.action_muti_add:
                        Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.MDrawer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    public void dataInit(){
        sqliteDBHelper= new SqliteDBHelper(context,Constants.DB_NAME,null, com.example.archer.myapplication.Constants.DB_VERSION);
        currentVersion= com.example.archer.myapplication.Constants.DB_VERSION;
        mlist=new ArrayList<>();
        madapter=new RecyclerViewAdaptor(context,mlist, this,this);
        recyclerView.setAdapter(madapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        notifyData();
        ItemTouchHelper.Callback callback=new ItemTouchHelperCallBack(madapter);
        ItemTouchHelper touchHelper=new ItemTouchHelper(callback);
        this.Callback=callback;
        touchHelper.attachToRecyclerView(recyclerView);
    }
    public void eventInit(){
        //add all click listener at here;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;

        viewInit();
        dataInit();
        eventInit();
        notifyData();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(view);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pop_up, menu);
        return true;
    }*/
   /* private void loadImg(){
        picArray=new byte[imgArray.length][];
        for (int i=0; i<imgArray.length;i++){
            picArray[i]=picTobyte(imgArray[i]);
        }
    }*/

    /*private byte[] picTobyte(int resourceID)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = context.getResources().openRawResource(resourceID);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        //压缩图片，100代表不压缩（0～100）
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }*/
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("是否清空数据");
                builder.setMessage("是否清空数据");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqliteDBHelper.cleanAllData();
                        notifyData();
                    }
                }).setNegativeButton("no", null);
                builder.show();
                break;
            case R.id.action_muti_add:
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);

                finish();
                break;

        }
        if (id == R.id.action_clear) {

        }

        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public void onClick(View v) {



        }

    private void addDataReturnID(person model){

        model=sqliteDBHelper.addPersonDataReturnID(model);
        if(model!=null){
            ToastUtils.show(context,"Data Added");
            mlist.add(0,model);
            sqliteDBHelper.queryAllPersonDataOrderBy();
            madapter.notifyDataSetChanged();
        }else {
            ToastUtils.show(context,"Data Add Failed");
        }
        notifyData();
    }
    private void notifyData(){
        mlist.clear();
        mlist.addAll(sqliteDBHelper.queryAllPersonData());
        madapter.notifyDataSetChanged();
    }

    private void UpdateDataReturnID(person model){


        if(model!=null){
            sqliteDBHelper.UpdatePersonData(model);
            ToastUtils.show(context,"Data Updated"+model.getId());
            notifyData();
        }else {
            ToastUtils.show(context,"Data Update failed");
        }
    }



    @Override
    public void clickListener(View v, ProgressBar bar) {//在接受adpator传出的view之后做出响应时间、
        //重点在于正确传出view并且避免让Adaptor中接触sqlite中的数据，避免无法提取的数据或者无法传出的数据造成错误
        //TODO Why i cant use switch?
        if (v.getTag(R.id.tag_second)=="delete") {
            sqliteDBHelper.deletePersonData(mlist.get((Integer) v.getTag(R.id.tag_first)));
            sqliteDBHelper.queryAllPersonDataAfterDelete();
            //                mySqliteHelper.deletePersonDataSql(new PersonModel());
            notifyData();

            ToastUtils.show(context, "删除第" + v.getTag(R.id.tag_first) + "条数据成功");
        }
        if (v.getTag(R.id.tag_second)=="Add"){
            showPlus(v,bar);
            this.position=((Integer)v.getTag(R.id.tag_first));
            plus=true;
        }
        if (v.getTag(R.id.tag_second)=="Minus"){
            showPlus(v,bar);
            this.position=((Integer)v.getTag(R.id.tag_first));
            plus=false;
        }
    }

    public PopupWindow showHintSurePopupWindow(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
        RecyclerView showArea = view.<RecyclerView>findViewById(R.id.pop_up_recycler);
        TextView cancel=view.<TextView>findViewById(R.id.cancel_button);
        TextView sure=view.<TextView>findViewById(R.id.sure_button);
        TextView add=view.<TextView>findViewById(R.id.add_button);
        final List<AddBean> mList = new ArrayList<AddBean>();
        mList.add(new AddBean());
        mList.add(new AddBean());
        mList.add(new AddBean());
        adapter = new adapter.SimpleAdapter<AddBean>(this, mList, R.layout.edit_dialog) {

            @Override
            protected void onBindViewHolder(ViewHolder holder, AddBean data) {

                holder.<EditText>getView(R.id.Input_name).setText(data.getName());
                holder.<EditText>getView(R.id.Input_HP).setText(data.getHP());
                holder.<EditText>getView(R.id.Input_Inti).setText(data.getInit());

            }
        };
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        // 必须要有这句否则弹出popupWindow后监听不到Back键
        // 使其聚集,布局里面的控件可以消费点击事件
        popupWindow.setFocusable(true);
        //阴影覆盖到状态栏
        popupWindow.setClippingEnabled(false);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.update();
        showArea.removeAllViews();
        showArea.setLayoutManager(new LinearLayoutManager(this));
        View itemView = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null);
        showArea.setAdapter(adapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        return popupWindow;
    }

    public void showEditDialog(View view)
    {
        showHintSurePopupWindow(this);
    }
    public void showPlus(View view,ProgressBar bar)
    {
        this.bar=bar;
        HpChangeDialog Hpchange=new HpChangeDialog(bar);
        Hpchange.show(getFragmentManager(),"Enter");

    }
    public void ControlProgressColor(int progress,ProgressBar HPBar){
        if (progress>=50){
            int RedColor=((100-progress)*5);
            ClipDrawable ColorDrawAble=new ClipDrawable(new ColorDrawable(color.argb(255,RedColor,255,0)), Gravity.LEFT,ClipDrawable.HORIZONTAL);
            ColorDrawAble.setLevel(progress*100);
            HPBar.setForeground(ColorDrawAble);
        } else{
            Integer GreenColor=(progress*5);
            ClipDrawable ColorDrawAble=new ClipDrawable(new ColorDrawable(color.argb(255,255,GreenColor,0)), Gravity.LEFT,ClipDrawable.HORIZONTAL);
            ColorDrawAble.setLevel(progress*100);
            HPBar.setForeground(ColorDrawAble);
        }
    }
    public int Hpdepends(person model,int change){
        int result=0;
        if (change>0){
            if((model.getHp()+change)>model.getMaxHP()){
                result=model.getMaxHP();
                model.setHP(model.getMaxHP());
            }else {result=model.getHp()+change;
                model.setHP(model.getHp()+change);
            }
        }else if(change<0){
            result=model.getHp()+change;
            model.setHP(model.getHp()+change);
        }
        return result*100/model.getMaxHP();
    }
    public void ProgrssBarControl(final int start,final int end,ProgressBar Hpbar){
        final ProgressBar finalbar=Hpbar;
        ValueAnimator animator = ValueAnimator.ofInt(start,end);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int mprogress = (int) animation.getAnimatedValue();
                Log.d("tag", "onAnimationUpdate: "+mprogress);
                ControlProgressColor(mprogress,finalbar);
                finalbar.invalidate();

                if (mprogress == end) {
                    madapter.notifyDataSetChanged();
                }
            }

        });
        animator.start();
        if (start>end){
            madapter.notifyDataSetChanged();
        }
    }
    public void AddorMinus(int change,ProgressBar Hpbar){
        if(plus){this.plusnum=change;
        }
        else{this.plusnum=-change;}//TODO we have plusnum to get change(+/-), so we need to use that to get the change of progress

        person model=mlist.get(position);
        //TODO Could I:change model.hp and show it before i showed the change happened on SQLite. can use NotifyDataSetChange, but not that NotifyData()
        //TODO: cause notifyData read all Data from SQL, so before it happen we can use model to show animation
            final int progress = (model.getHp() * 100 / model.getMaxHP());
            final int finalProgress=Hpdepends(model,plusnum);

            ProgrssBarControl(progress,finalProgress,Hpbar);

        sqliteDBHelper.changeHp(model,plusnum);

        //TODO DataSetChanged before animation finished, so that bar make animation cant be seen;
    }

    public void InputComplete(String name, int HP, int Inti) {
        person model = new person();
        model.setName(name);
        model.setMaxHP(HP);
        model.setHP(model.getMaxHP());
        model.setInti(Inti);
        addDataReturnID(model);
    }

    @Override
    public void ChangeNum(int fromposition, int toposition) {


            Collections.swap(mlist, fromposition, toposition);
            sqliteDBHelper.Switch(mlist.get(fromposition),mlist.get(toposition));

        //TODO: Should not notify dataset changed here, should find a way to swap those tags
    }
    public void ChangeNum(int positon){
        sqliteDBHelper.deletePersonData(mlist.get(position));
        sqliteDBHelper.queryAllPersonDataAfterDelete();
        notifyData();
        Log.e("tag","NumRemove Worked");

        ToastUtils.show(context, "删除第" + position + "条数据成功");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("是否清空数据");
                builder.setMessage("是否清空数据");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqliteDBHelper.cleanAllData();
                        notifyData();
                    }
                }).setNegativeButton("no", null);
                builder.show();
                break;
            case R.id.action_muti_add:
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);

                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.MDrawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
