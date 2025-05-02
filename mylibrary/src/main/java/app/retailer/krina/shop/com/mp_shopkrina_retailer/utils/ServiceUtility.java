package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ServiceUtility {

    public static Object chkNull(Object pData) {
        return (pData == null ? "" : pData);
    }

    public static Map<String, String> tokenizeToHashMap(String msg, String delimPairValue, String delimKeyPair) throws Exception {
        Map<String, String> keyPair = new HashMap<>();
        ArrayList<String> respList = new ArrayList<>();
        String part = "";
        StringTokenizer strTkn = new StringTokenizer(msg, delimPairValue, true);
        while (strTkn.hasMoreTokens()) {
            part = (String) strTkn.nextElement();
            if (part.equals(delimPairValue)) {
                part = null;
            } else {
                respList = tokenizeToArrayList(part, delimKeyPair);
                if (respList.size() == 2) keyPair.put(respList.get(0), respList.get(1));
                else if (respList.size() == 1) keyPair.put(respList.get(0), null);
            }
            if (part == null) continue;
            if (strTkn.hasMoreTokens()) strTkn.nextElement();
        }
        return keyPair.size() > 0 ? keyPair : null;
    }

    public static ArrayList<String> tokenizeToArrayList(String msg, String delim) {
        ArrayList<String> respList = new ArrayList<>();
        String varName = null;
        String varVal = null;
        int index = msg.indexOf(delim);
        varName = msg.substring(0, index);
        if ((index + 1) != msg.length())
            varVal = msg.substring(index + 1);
        respList.add(varName);
        respList.add(varVal);
        return respList.size() > 0 ? respList : null;
    }

    public static String addToPostParams(String paramKey, String paramValue) {
        if (paramValue != null)
            return paramKey.concat(Constant.PARAMETER_EQUALS).concat(paramValue)
                    .concat(Constant.PARAMETER_SEP);
        return "";
    }


}