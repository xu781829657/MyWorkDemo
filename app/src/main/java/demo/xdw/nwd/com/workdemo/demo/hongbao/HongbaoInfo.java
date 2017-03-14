package demo.xdw.nwd.com.workdemo.demo.hongbao;

import android.view.accessibility.AccessibilityNodeInfo;


import java.util.Calendar;

public class HongbaoInfo {

    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;

    public String sender;
    public String money;
    public String time;

    public void getInfo(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo hongbaoNode = node.getParent();
        sender = hongbaoNode.getChild(0).getText().toString();
        money = hongbaoNode.getChild(2).getText().toString();
        time = getStringTime();
    }

    private String getStringTime() {
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        sec = c.get(Calendar.SECOND);
        return month + "月" + day + "日  " + hour + ":" + min + ":" + sec;
    }


    public String toString() {
        return "HongbaoInfo [sender=" + sender + ", money=" + money + ", time=" + time + "]";
    }

    public float getMoney() {
        return Float.parseFloat(money);
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }
}
