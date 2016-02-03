package cn.hukecn.fund;

/**
 * Created by Kelson on 2015/12/1.
 */
public class DataTemp {
    private static DataTemp ourInstance = null;
    private String info = "";

    public static DataTemp getInstance() {
        if(ourInstance == null)
            ourInstance = new DataTemp();
        return ourInstance;
    }

    private DataTemp() {
    }

    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo(){
        return info;
    }
}
