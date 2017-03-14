package demo.xdw.nwd.com.workdemo.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import com.android.base.frame.Base;

import java.util.ArrayList;
import java.util.List;

import demo.xdw.nwd.com.workdemo.demo.hongbao.HongbaoInfo;
import demo.xdw.nwd.com.workdemo.demo.hongbao.HongbaoSignature;
import demo.xdw.nwd.com.workdemo.util.PreferencesUtils;

/**
 * 项目名称：Hongbaotest
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/1/27 17:47
 */
public class HongbaoService extends AccessibilityService {

    private AccessibilityNodeInfo rootNodeInfo;
    private AccessibilityNodeInfo mReceiveNode, mUnpackNode;
    private HongbaoSignature signature = new HongbaoSignature();
    private HongbaoInfo hongbaoInfo = new HongbaoInfo();
    private boolean mLuckyMoneyPicked, mLuckyMoneyReceived, mNeedUnpack, mNeedBack;
    private static final String GET_RED_PACKET = "领取红包";
    private static final String CHECK_RED_PACKET = "查看红包";
    private static final String RED_PACKET_PICKED = "手慢了，红包派完了";
    private static final String RED_PACKET_PICKED2 = "手气";
    private static final String RED_PACKET_PICKED_DETAIL = "红包详情";
    private static final String RED_PACKET_SAVE = "已存入零钱";
    private static final String RED_PACKET_NOTIFICATION = "[微信红包]";
    private boolean mMutex = false;
    private boolean isToGetMoney = false;
    private boolean isGetMoney = false;
    private float totalMoney;
    private int totalSuccessNum;
    private float gotMoney;
    private String lastContentDescription = "";

    /**
     * 通过这个函数可以接收系统发送来的AccessibilityEvent，
     * 接收来的AccessibilityEvent是经过过滤的，过滤是在配置工作时设置的
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        L.d("RECEIVE EVENT!");
         /* 检测通知消息 */
        if (!mMutex) {
            if (watchNotifications(event)) return;
            if (watchList(event)) return;
        }
        this.rootNodeInfo = event.getSource();
        if (rootNodeInfo == null) return;
        mReceiveNode = null;
        mUnpackNode = null;
        checkNodeInfo();

         /* 如果已经接收到红包并且还没有戳开 */
        if (mLuckyMoneyReceived && !mLuckyMoneyPicked && (mReceiveNode != null)) {
            mMutex = true;
            AccessibilityNodeInfo cellNode = mReceiveNode;
            cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            mLuckyMoneyReceived = false;
            mLuckyMoneyPicked = true;
            L.d("正在打开！");
        }

         /* 如果戳开但还未领取 */
        if (mNeedUnpack && (mUnpackNode != null)) {
            AccessibilityNodeInfo cellNode = mUnpackNode;
            cellNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            mNeedUnpack = false;
            L.d("正在领取！");
        }

        if (mNeedBack) {
            if (Build.VERSION.SDK_INT >= 16) {
                performGlobalAction(GLOBAL_ACTION_BACK);
            }

            mMutex = false;
            mNeedBack = false;
            L.d("正在返回！");
            //总次数和金额统计
            if (isGetMoney) {
                Base.showToast("抢到一个红包: " + gotMoney + "元!");
                totalMoney = totalMoney + gotMoney;
                totalSuccessNum++;
                L.d("totalMoney: " + totalMoney);
                L.d("totalSuccessNum: " + totalSuccessNum);
                saveToLog(hongbaoInfo);
                isGetMoney = false;
            }
        }
    }

    private void checkNodeInfo() {
        L.d("checkNodeInfo!");
        if (this.rootNodeInfo == null) return;
         /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        List<AccessibilityNodeInfo> nodes1 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                GET_RED_PACKET, CHECK_RED_PACKET});

        if (!nodes1.isEmpty()) {
            AccessibilityNodeInfo targetNode = nodes1.get(nodes1.size() - 1);
            L.d("!nodes1.isEmpty()");
            if ("android.widget.LinearLayout".equals(targetNode.getParent().getClassName()))//避免被文字干扰导致外挂失效
            {
                if (this.signature.generateSignature(targetNode)) {
                    mLuckyMoneyReceived = true;
                    mReceiveNode = targetNode;
                    L.d("signature:" + this.signature.toString());
                }
            } else {
                L.d("this is text");
            }
            return;
        }

        List<AccessibilityNodeInfo> nodes2 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                "拆红包"});
        if (!nodes2.isEmpty()) {
            L.d("node2 != null");
            for (AccessibilityNodeInfo nodeInfo : nodes2) {
                if (nodeInfo.getClassName().equals("android.widget.Button"))
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else {
             /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
            AccessibilityNodeInfo node2 = (this.rootNodeInfo.getChildCount() > 3) ? this.rootNodeInfo.getChild(3) : null;
            if (node2 != null && node2.getClassName().equals("android.widget.Button")) {
                L.d("!node2 != null");
                mUnpackNode = node2;
                mNeedUnpack = true;
                isToGetMoney = true;
                L.d("find red packet!");
                return;
            } else {
                L.d("this.rootNodeInfo.getChildCount(): " + this.rootNodeInfo.getChildCount());
            }
        }
         /* 戳开红包，红包已被抢完，遍历节点匹配“已存入零钱”和“手慢了” */
        if (mLuckyMoneyPicked) {
            List<AccessibilityNodeInfo> nodes3 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                    RED_PACKET_PICKED, RED_PACKET_SAVE, RED_PACKET_PICKED2, RED_PACKET_PICKED_DETAIL});
            if (!nodes3.isEmpty()) {
                L.d("!nodes3.isEmpty()");
//                for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
//                    L.d("rootNodeInfo.getChild(i).toString() :" + rootNodeInfo.getChild(i).toString());
//                }
                L.d("rootNodeInfo.getChildCount(): " + rootNodeInfo.getChild(0).getChildCount());
                if (rootNodeInfo.getChildCount() > 1) {
                    L.d("RED_PACKET_PICKED!");
                } else {
                    L.d("nodes3.get(0).toString(): " + nodes3.get(0).getText().toString());
                    if (!nodes3.get(0).getText().toString().equals(RED_PACKET_PICKED_DETAIL)) {
                        AccessibilityNodeInfo targetNode = nodes3.get(nodes3.size() - 1);
                        hongbaoInfo.getInfo(targetNode);
                        if (isToGetMoney) {
                            isGetMoney = true;
                            isToGetMoney = false;
                            gotMoney = hongbaoInfo.getMoney();
                            L.d("gotMoney: " + gotMoney);
                        }
                        L.d("RED_PACKET_SAVE!");
                        L.d("hongbaoInfo: " + hongbaoInfo.toString());
                    } else {
                        L.d("this packet is myself!");
                    }

                }
                mNeedBack = true;
                mLuckyMoneyPicked = false;
            }
        }
    }

    private void saveToLog(HongbaoInfo hongbaoInfo) {
        PreferencesUtils.saveRobHongbaoRecords(Base.getContext(), hongbaoInfo.toString());
    }

    private boolean watchList(AccessibilityEvent event) {
        // Not a message
        L.d("watchList");
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || event.getSource() == null)
            return false;

        List<AccessibilityNodeInfo> nodes = event.getSource().findAccessibilityNodeInfosByText(RED_PACKET_NOTIFICATION);
        L.d("watchList nodes.isEmpty():" + nodes.isEmpty());
        if (!nodes.isEmpty()) {
            AccessibilityNodeInfo nodeToClick = nodes.get(0);
            CharSequence contentDescription = nodeToClick.getContentDescription();
            if (contentDescription != null && !lastContentDescription.equals(contentDescription)) {
                nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                lastContentDescription = contentDescription.toString();
                return true;
            }
        }
        return false;
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        // Not a notification
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return false;

        // Not a hongbao
        String tip = event.getText().toString();
        if (!tip.contains(RED_PACKET_NOTIFICATION)) return true;

        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            try {
                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 批量化执行AccessibilityNodeInfo.findAccessibilityNodeInfosByText(text).
     * 由于这个操作影响性能,将所有需要匹配的文字一起处理,尽早返回
     *
     * @param nodeInfo 窗口根节点
     * @param texts    需要匹配的字符串们
     * @return 匹配到的节点数组
     */
    private List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {
        for (String text : texts) {
            if (text == null) continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) return nodes;
        }
        return new ArrayList<>();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        L.d("totalMoney: " + totalMoney);
        L.d("totalSuccessNum: " + totalSuccessNum);
    }

    /**
     * 这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次。
     */
    @Override
    public void onInterrupt() {
    }

}
