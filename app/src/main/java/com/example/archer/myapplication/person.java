package com.example.archer.myapplication;

/**
 * Created by archer on 2018/4/21.
 */

public class person {
    private long id;
    private String name;
    private int MaxHP;
    private int HP;
    private int Inti;

    public void setInti(int Inti){this.Inti=Inti;}
    public int getInti(){return Inti;}
    public void setId(long id){this.id=id;}

    public long getId(){return id;}

    public void setMaxHP(int MaxHP){this.MaxHP=MaxHP;}

    public int getMaxHP(){return MaxHP;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }


}
