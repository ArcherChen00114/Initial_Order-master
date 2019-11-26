package DataBean;

public class AddBean {
    private String name;
    private Integer HP;
    private int Con;
    private int init;

    public AddBean() {
        this.name = "";
        this.HP = 0;
        this.Con = 0;
        this.init = 0;
    }

    public AddBean(String name, int HP, int Con, int init) {
        this.name = name;
        this.HP = HP;
        this.Con = Con;
        this.init = init;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHP() {
        return HP;
    }

    public void setHP(Integer HP) {
        this.HP = HP;
    }

    public int getCon() {
        return Con;
    }

    public void setCon(int con) {
        Con = con;
    }

    public int getInit() {
        return init;
    }

    public void setInit(int init) {
        this.init = init;
    }
}
