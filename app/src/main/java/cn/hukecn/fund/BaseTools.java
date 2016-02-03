package cn.hukecn.fund;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kelson on 2015/12/5.
 */
public class BaseTools {
    public static FundBean praseJsonToFundBean(String content)
    {
        FundBean bean;
        int start = -1,end = -1;
        start = content.indexOf('{');
        end = content.indexOf('}');
        String jsonStr = content.substring(start,end+1);
        try {
            JSONObject json = new JSONObject(jsonStr);
            bean = new FundBean();
            bean.id = json.getString("fundcode");
            bean.name = json.getString("name");
            bean.pecent_value = json.getString("gszzl")+"%";
            bean.fundpz = json.getString("gsz");
            bean.gztime = json.getString("gztime");

        } catch (JSONException e) {
            return null;
        }
        return bean;
    }
}
