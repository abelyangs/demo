package com.abel.example;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/8.
 */
public class MapSimple {
    public static void main(String args[]) {
        String mapStr1 = "{\n" +
                "  'accountInfo':[\n" +
                "    {'accNum':'CNHSBC730025764050','currency':'CNY','balance':'1.00'},\n" +
                "\t{'accNum':'CNHSBC110013323427','currency':'AUD','balance':'1.00'},\n" +
                "\t{'accNum':'CNHSBC110013323406','currency':'USD','balance':'99.00'},\n" +
                "\t{'accNum':'CNHSBC110013323407','currency':'USD','balance':'22.00'},\n" +
                "    {'accNum':'CNHSBC110013323408','currency':'CNY','balance':'100.00'}\n" +
                "  ]\n" +
                "}";
        JSONObject json2 = JSONObject.fromObject(mapStr1);
        JSONArray accountInfo = json2.getJSONArray("accountInfo");// 找到accountInfo的json数组
        Map<String, Double> hm = new HashMap<String, Double>();

        for (int i = 0; i<accountInfo.size(); i++) {
            JSONObject info = accountInfo.getJSONObject(i);
            String currency = info.getString("currency");
            Double balance = info.getDouble("balance");
            System.out.println("this accNum value is: "+currency);

            if (hm.containsKey(currency)) {
                hm.put(currency, hm.get(currency)+balance);
            }else {
                hm.put(currency,balance);
            }
        }

        System.out.println("--the first method--");
        for (String key: hm.keySet()) {
            System.out.println("key= "+ key + " and value= " + hm.get(key));
        }

        System.out.println("--the second method--");
        Iterator<Map.Entry<String, Double>> it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Double> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
    }
}
