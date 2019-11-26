package com.example.archer.myapplication;

/**
 * Created by archer on 2018/4/21.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by archer on 2018/1/6.
 */

public class SqliteDBHelper extends SQLiteOpenHelper{
    private final String TABLE_NAME_PERSON="person";
    private final String TABLE_NAME_TEMP_PERSON="person";
    //two catogory in SqliteDBHelper, one is  TABLE_NAME,the other is values
    private final String VALUE_ID="id";
    private final String VALUE_NAME="name";
    private final String VALUE_HP="MaxHp";
    private final String VALUE_CURRENTHP="Hp";
    private final String VALUE_Inti="Inti";
    private final String VALUE_Checked="LEFT";
    private final String CREATE_PERSON="create table if not exists " + TABLE_NAME_PERSON +"("+
            VALUE_ID + " integer primary key, " +//SQL add data to person DataBase,set primary key for id
            VALUE_NAME + " text, " +//and set data's Type
            VALUE_CURRENTHP+" integer, "+
            VALUE_HP + " integer, " +
            VALUE_Inti +" integer, "+
            VALUE_Checked +" text"+
            ")";//this String used for create person listItem
    //may not use too many times
    private final String CREATE_TEMP_PERSON="alter table"+TABLE_NAME_PERSON+"rename to"+TABLE_NAME_TEMP_PERSON;
    //used for change the name of person database
    private final String INSERT_DATA="insert into "+TABLE_NAME_PERSON+" select "+VALUE_ID+" , "+
            VALUE_ID+" , "+
            VALUE_NAME+" , "+
            VALUE_CURRENTHP+" , "+
            VALUE_HP+" , "+
            VALUE_Inti +" integer, "+
            VALUE_Checked +" text"+
            " from "+TABLE_NAME_TEMP_PERSON;
    private final String CREATE_NEW_TABLE  = "create table " + TABLE_NAME_PERSON + "(" +
            VALUE_ID + " integer primary key, " +//SQL add data to person DataBase,set primary key for id
            VALUE_NAME + " text, " +//and set data's Type
            VALUE_CURRENTHP+" integer, "+
            VALUE_HP + " integer, " +
            VALUE_Inti +" integer, "+
            VALUE_Checked +" text"+
            ")";
    private final String CREATE_TEMP_TABLE  = "alter table "+TABLE_NAME_PERSON+" rename to "+TABLE_NAME_TEMP_PERSON;

    private final String DROP_TEMP_TABLE="drop table "+TABLE_NAME_TEMP_PERSON;
    //ought to be id,name,hp,ac,inti and info
    public SqliteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        Log.e(TAG, "-------> MySqliteHelper");
    }
    //构造函数
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_PERSON);
        Log.e(TAG, "-------> onCreate");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {

        Log.e(TAG, "-------> onUpgrade"+"  oldVersion = "+oldVersion+"   newVersion = "+newVersion);

        if(oldVersion != newVersion)
        {
            switch (newVersion)
            {
                case 3:

                    //升级数据库，不改变表结构 注意空格
                    //添加列 addcol_goods2 , text 为字符串数据类型 ,person为表名
                    //alter table person add column addcol_goods2 text
                    //添加的列 添加优点 添加缺点
                    String addColGoods = "addcol_goods"+newVersion;
                    String addColBads = "addcol_bads"+newVersion;
                    //添加列的sql语句
                    String upgradeGoods = "alter table "+TABLE_NAME_PERSON + " add column "+ addColGoods+" text";
                    String upgradeBads = "alter table "+TABLE_NAME_PERSON+" add column "+addColBads+" text";
                    //执行sql语句  一次只能添加一个字段

                    db.execSQL(upgradeGoods);
                    db.execSQL(upgradeBads);

                    Log.e(TAG, ""+upgradeGoods );

                    break;

                case 2:

                    db.execSQL(CREATE_TEMP_TABLE);
                    db.execSQL(CREATE_NEW_TABLE);
                    db.execSQL(INSERT_DATA);
                    db.execSQL(DROP_TEMP_TABLE);



                    break;

            }
        }
    }
    public List<person> addPersonDataWithList(List<person> list){
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_PERSON, null, null, null, null, null, null, null);
        int i=0;
        for (person model:list){
            if(i<cursor.getCount()){
                UpdatePersonData(model);
                cursor.moveToNext();
                i++;
                Log.d(TAG, "addPersonDataWithList:cursor Moving "+cursor.getCount());
            }else{
                addPersonDataReturnID(model);
                Log.d(TAG, "addPersonDataWithList:addData ");
            }
            /*TODO firgure out if this data exist or not
            exist=>UpdatePersonData
            Not=>AddpersonDataReturn ID
            */
        }
        getWritableDatabase().close();
        cursor.close();
        return null;
    }
    public person addPersonDataReturnID(person model) {
        //把数据添加到ContentValues
        ContentValues values = new ContentValues();
        values.put(VALUE_NAME, model.getName());
        values.put(VALUE_CURRENTHP,model.getMaxHP());
        values.put(VALUE_HP, model.getMaxHP());
        values.put(VALUE_Inti,model.getInti());
        values.put(VALUE_Checked,1);//TODO need to add check's value
        //添加数据到数据库
        long index = getWritableDatabase().insert(TABLE_NAME_PERSON, null, values);

        //不等于-1表示添加成功(可以看insert源码)
        //    public long insert(String table, String nullColumnHack, ContentValues values) {
        //        try {
        //            return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
        //        } catch (SQLException e) {
        //            Log.e(TAG, "Error inserting " + values, e);
        //            return -1;
        //        }
        //    }
        if (index != -1) {
            model.setId(index);
            return model;
        } else {
            return null;
        }
    }
    public void deletePersonData(person model) {

        //delete that model
        //where后跟条件表达式 =,!=,>,<,>=,<=
        //多条件  and or

        //删除数据库里的model数据 因为_id具有唯一性。
        getWritableDatabase().delete(TABLE_NAME_PERSON, VALUE_ID + "=? ", new String[]{" " + model.getId()});

      /*//删除数据库里 _id = 1 的数据
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+"=?",new String[]{"1"});
      //删除 age >= 18 的数据
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_AGE+">=?",new String[]{"18"});
      //删除 id > 5 && age <= 18 的数据
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+">?"+" and "+VALUE_AGE +"<=?",new String[]{"5","18"});
      //删除 id > 5 || age <= 18 的数据
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+">?"+" or "+VALUE_AGE +"<=?",new String[]{"5","18"});
      //删除数据库里 _id != 1 的数据
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+"!=?",new String[]{"1"});
      //删除所有 _id >= 7 的男生
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ISBOY+"=?"+" and "+VALUE_ID+">=?",new String[]{"1","7"});
      //删除所有 _id >= 7 和 _id = 3 的数据
      getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+">=?"+" or "+VALUE_ID+"=?",new String[]{"7","3"});*/

    }
    public void UpdatePersonData(person model) {
        //把数据添加到ContentValues
        ContentValues values = new ContentValues();
        values.put(VALUE_NAME, model.getName());
        values.put(VALUE_Inti,model.getInti());
        values.put(VALUE_HP,model.getMaxHP());
        values.put(VALUE_CURRENTHP, model.getHp());

        //添加数据到数据库
        getWritableDatabase().update(TABLE_NAME_PERSON, values, VALUE_ID + "=?", new String[]{"" + model.getId()});
        getWritableDatabase().close();
        Log.e(TAG, "" + VALUE_ID + ">=? and " + VALUE_ID + "<=?");
    }
    public void changeHp(person model,int change) {
        Cursor get = getWritableDatabase().query(TABLE_NAME_PERSON, null, VALUE_ID + "=" + model.getId(), null, null, null, null);
        get.moveToFirst();
        long ID = get.getLong(get.getColumnIndex(VALUE_ID));
        String name = get.getString(get.getColumnIndex(VALUE_NAME));
        int Hp = get.getInt(get.getColumnIndex(VALUE_CURRENTHP));
        int MaxHp = get.getInt(get.getColumnIndex(VALUE_HP));
        int Inti = get.getInt(get.getColumnIndex(VALUE_Inti));
        int NewHp = Hp + change;
        if (NewHp > MaxHp) {
            NewHp = MaxHp;
        }
        ContentValues values = new ContentValues();
        values.put(VALUE_ID, ID);
        values.put(VALUE_NAME, name);
        values.put(VALUE_Inti, Inti);
        values.put(VALUE_CURRENTHP, NewHp);
        getWritableDatabase().update(TABLE_NAME_PERSON, values, VALUE_ID + "=?", new String[]{"" + ID});
        get.close();
        getWritableDatabase().close();
    }
    public void Switch(person FromModel,person ToModel){
        Cursor from=getWritableDatabase().query(TABLE_NAME_PERSON,null,VALUE_ID+"="+FromModel.getId(),null,null,null,null);
        Cursor to=getWritableDatabase().query(TABLE_NAME_PERSON,null,VALUE_ID+"="+ToModel.getId(),null,null,null,null);
        from.moveToFirst();
        to.moveToFirst();
        long FromID = from.getLong(from.getColumnIndex(VALUE_ID));
        long toID=to.getLong(to.getColumnIndex(VALUE_ID));
        String name = from.getString(from.getColumnIndex(VALUE_NAME));
        int Hp = from.getInt(from.getColumnIndex(VALUE_CURRENTHP));
        int MaxHp=from.getInt(from.getColumnIndex(VALUE_HP));
        int ToInti=(to.getInt(to.getColumnIndex(VALUE_Inti)));
        int FromInti=(from.getInt(from.getColumnIndex(VALUE_Inti)));

        ContentValues FromValues=new ContentValues();
        ContentValues ToValues=new ContentValues();
        FromValues.put(VALUE_ID, FromID);
        FromValues.put(VALUE_NAME, name);
        FromValues.put(VALUE_Inti,ToInti);//form获取to的先攻
        FromValues.put(VALUE_HP,MaxHp);
        FromValues.put(VALUE_CURRENTHP, Hp);

        ToValues.put(VALUE_ID, toID);
        ToValues.put(VALUE_Inti,FromInti);//to与from交换先攻
        getWritableDatabase().update(TABLE_NAME_PERSON, ToValues, VALUE_ID + "=?", new String[]{"" + toID});
        getWritableDatabase().update(TABLE_NAME_PERSON, FromValues, VALUE_ID + "=?", new String[]{"" + FromID});
        from.close();
        to.close();
        getWritableDatabase().close();
        getWritableDatabase().close();
    }
    public void cleanAllData() {
        SQLiteDatabase db = getWritableDatabase();
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_PERSON);
        this.getWritableDatabase().execSQL(CREATE_NEW_TABLE);
    }
    /*public person queryData(long id) {
         person model = new person();
         Cursor cursor = getWritableDatabase().query(TABLE_NAME_PERSON, null, null, null, null, null, null, null);
         if (cursor.getCount() > 0) {
             //移动到首位
             cursor.moveToFirst();
             for (long i = 0; i>=id; i++) {
                 cursor.moveToNext();
             }
             String name = cursor.getString(cursor.getColumnIndex(VALUE_NAME));
             int AC = cursor.getInt(cursor.getColumnIndex(VALUE_AC));
             int Hp = cursor.getInt(cursor.getColumnIndex(VALUE_HP));
             String info = cursor.getString(cursor.getColumnIndex(VALUE_Info));
             model.setId(id);
             model.setName(name);
             model.setAC(AC);
             model.setHP(Hp);
             model.setInfo(info);
         }
         return model;
     }
     */
    public List<person> queryAllPersonData() {
        //查询全部数据

        Cursor cursor = getWritableDatabase().query(TABLE_NAME_PERSON, null, null, null, null, null, null, null);
        List<person> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                long id = cursor.getLong(cursor.getColumnIndex(VALUE_ID));
                String name = cursor.getString(cursor.getColumnIndex(VALUE_NAME));
                int MaxHp = cursor.getInt(cursor.getColumnIndex(VALUE_HP));
                int Hp=cursor.getInt(cursor.getColumnIndex(VALUE_CURRENTHP));
                int Inti=cursor.getInt(cursor.getColumnIndex(VALUE_Inti));

                person model = new person();
                if (VALUE_NAME==null&&VALUE_Inti==null&&VALUE_HP==null){
                    i--;
                }
                ContentValues values=new ContentValues();
                values.put(VALUE_ID, i);
                values.put(VALUE_NAME, name);
                values.put(VALUE_Inti,Inti);
                values.put(VALUE_HP,MaxHp);
                values.put(VALUE_CURRENTHP, Hp);
                getWritableDatabase().update(TABLE_NAME_PERSON, values, VALUE_ID + "=?", new String[]{"" + id});
                model.setId(id);
                model.setName(name);
                model.setMaxHP(MaxHp);
                model.setHP(Hp);
                model.setInti(Inti);
                list.add(model);
                //移动到下一位
                cursor.moveToNext();
            }
        }

        cursor.close();
        getWritableDatabase().close();

        return list;
    }

    private boolean order_by;
    public List<person> queryAllPersonDataOrderBy() {

        order_by = !order_by;
        //查询全部数据
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_PERSON, null, null, null, null, null, order_by ? VALUE_Inti + " desc" : VALUE_NAME + " asc", null);
        List<person> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String name = cursor.getString(cursor.getColumnIndex(VALUE_NAME));
                int Inti=cursor.getInt(cursor.getColumnIndex(VALUE_Inti));
                int Hp = cursor.getInt(cursor.getColumnIndex(VALUE_CURRENTHP));
                int MaxHp=cursor.getInt(cursor.getColumnIndex(VALUE_HP));

                person model = new person();
                model.setId(i);
                model.setName(name);
                model.setMaxHP(MaxHp);
                model.setHP(Hp);
                model.setInti(Inti);
                UpdatePersonData(model);
                cursor.moveToNext();
            }
        }

        cursor.close();
        getWritableDatabase().close();

        return list;
    }
    public List<person> queryAllPersonDataAfterDelete() {

        order_by = !order_by;
        //查询全部数据
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_PERSON, null, null, null, null, null, order_by ? VALUE_ID + " desc" : VALUE_NAME + " asc", null);
        List<person> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            //移动到首位
            cursor.moveToFirst();
            for (int i = 1; i < cursor.getCount(); i++) {


                person model = new person();long id = cursor.getLong(cursor.getColumnIndex(VALUE_ID));
                String name = cursor.getString(cursor.getColumnIndex(VALUE_NAME));
                int Inti=cursor.getInt(cursor.getColumnIndex(VALUE_Inti));
                int MaxHp=cursor.getInt(cursor.getColumnIndex(VALUE_HP));
                int Hp = cursor.getInt(cursor.getColumnIndex(VALUE_CURRENTHP));

                if (id!=i){
                    model.setId(i);
                }
                model.setName(name);
                model.setMaxHP(MaxHp);
                model.setHP(Hp);
                model.setInti(Inti);

                list.add(model);
                //移动到下一位
                cursor.moveToNext();
            }
        }

        cursor.close();
        getWritableDatabase().close();

        return list;
    }

}