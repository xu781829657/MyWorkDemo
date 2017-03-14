package demo.xdw.nwd.com.workdemo.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import demo.xdw.nwd.com.workdemo.util.LogUtils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by xudengwang on 2017/1/22.
 * 抢红包service监听
 */

public class RobHongBaoService extends AccessibilityService {
    private final static String KEY_TEXT_HONGBAO = "[微信红包]";
    private boolean mLuckyMoneyPicked, mLuckyMoneyReceived, mNeedUnpack, mNeedBack;
    private static final String GET_RED_PACKET = "领取红包";
    private static final String CHECK_RED_PACKET = "查看红包";
    private static final String RED_PACKET_PICKED = "手慢了，红包派完了";
    private static final String RED_PACKET_PICKED2 = "手气";
    private static final String RED_PACKET_PICKED_DETAIL = "红包详情";
    private static final String RED_PACKET_SAVE = "已存入零钱";
    private static final String RED_PACKET_NOTIFICATION = "[微信红包]";

    private String lastContentDescription = "";

    private AccessibilityNodeInfo rootNodeInfo;

    //有关AccessibilityEvent事件的回调函数.系统通过sendAccessibiliyEvent()不断的发送AccessibilityEvent到此处
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                LogUtils.d("TYPE_NOTIFICATION_STATE_CHANGED");
                //handleNotification(event);
                watchNotifications(event);
                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                LogUtils.d("TYPE_WINDOW_STATE_CHANGED");
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                LogUtils.d("TYPE_WINDOW_CONTENT_CHANGED");
                if (watchList(event)) {
                    LogUtils.d("watchList true");
                    return;
                }
                rootNodeInfo = event.getSource();
                if (rootNodeInfo == null) {
                    return;
                }
                checkNodeInfo();
//                String className = event.getClassName().toString();
//                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
//                    getPacket();
//                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
//                    openPacket();
//                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
//                    close();
//                }

                break;
        }
    }

    @Override
    public void onInterrupt() {
        LogUtils.d(getClass(), "onInterrupt");
    }

    //系统成功绑定该服务时被触发,也就是当你在设置中开启相应的服务,系统成功的绑定了该服务时会触发,通常我们可以在这里做一些初始化操作
    @Override
    protected void onServiceConnected() {
        LogUtils.d(getClass(), "onServiceConnected");
        super.onServiceConnected();
    }


    /**
     * 处理通知栏信息
     * <p>
     * 如果是微信红包的提示信息,则模拟点击
     *
     * @param event
     */
    private void handleNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                //如果微信红包的提示信息,则模拟点击进入相应的聊天窗口
                if (content.contains(KEY_TEXT_HONGBAO)) {
                    if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                        Notification notification = (Notification) event.getParcelableData();
                        PendingIntent pendingIntent = notification.contentIntent;
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 关闭红包详情界面,实现自动返回聊天窗口
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void close() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了关闭按钮的id
            List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByViewId("@id/ez");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : infos) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 模拟点击,拆开红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("@id/b9m");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 模拟点击,打开抢红包界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        AccessibilityNodeInfo node = recycle(rootNode);

        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        AccessibilityNodeInfo parent = node.getParent();
        while (parent != null) {
            if (parent.isClickable()) {
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            parent = parent.getParent();
        }

    }

    /**
     * 递归查找当前聊天窗口中的红包信息
     * <p>
     * 聊天窗口中的红包都存在"领取红包"一词,因此可根据该词查找红包
     *
     * @param node
     */
    public AccessibilityNodeInfo recycle(AccessibilityNodeInfo node) {
        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if ("领取红包".equals(node.getText().toString())) {
                    return node;
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycle(node.getChild(i));
                }
            }
        }
        return node;
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

    private boolean watchList(AccessibilityEvent event) {
        // Not a message
        if (event.getSource() == null)
            return false;

        List<AccessibilityNodeInfo> nodes = event.getSource().findAccessibilityNodeInfosByText(RED_PACKET_NOTIFICATION);
        LogUtils.d("watchList nodes.isEmpty():" + nodes.isEmpty());
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


    private void checkNodeInfo() {

        LogUtils.d("checkNodeInfo rootNodeInfo:" + rootNodeInfo);
        if (rootNodeInfo == null) return;
         /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        List<AccessibilityNodeInfo> nodes1 = findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                GET_RED_PACKET, CHECK_RED_PACKET});
        LogUtils.d("checkNodeInfo nodes1.isEmpty():" + nodes1.isEmpty());

        if (!nodes1.isEmpty()) {
            AccessibilityNodeInfo targetNode = nodes1.get(nodes1.size() - 1);
//            L.d("targetNode: " + targetNode.toString());
            if ("android.widget.LinearLayout".equals(targetNode.getParent().getClassName()))//避免被文字干扰导致外挂失效
            {
                targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                LogUtils.d("this is text");
            }
            return;
        }

        List<AccessibilityNodeInfo> nodes2 = this.findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                "拆红包"});
        LogUtils.d("checkNodeInfo nodes2.isEmpty():" + nodes2.isEmpty());
        if (!nodes2.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodes2) {
                if (nodeInfo.getClassName().equals("android.widget.Button"))
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else {
             /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
            AccessibilityNodeInfo node2 = (rootNodeInfo.getChildCount() > 3) ? rootNodeInfo.getChild(3) : null;
            if (node2 != null && node2.getClassName().equals("android.widget.Button")) {
                node2.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            } else {
            }
        }
         /* 戳开红包，红包已被抢完，遍历节点匹配“已存入零钱”和“手慢了” */
        if (mLuckyMoneyPicked) {
            List<AccessibilityNodeInfo> nodes3 = this.findAccessibilityNodeInfosByTexts(rootNodeInfo, new String[]{
                    RED_PACKET_PICKED, RED_PACKET_SAVE, RED_PACKET_PICKED2, RED_PACKET_PICKED_DETAIL});
            LogUtils.d("checkNodeInfo nodes3.isEmpty():" + nodes3.isEmpty());
            if (!nodes3.isEmpty()) {
                LogUtils.d("rootNodeInfo.getChildCount(): " + rootNodeInfo.getChild(0).getChildCount());
                if (rootNodeInfo.getChildCount() > 1) {
                    LogUtils.d("RED_PACKET_PICKED!");
                } else {
                    LogUtils.d("nodes3.get(0).toString(): " + nodes3.get(0).getText().toString());
                    if (!nodes3.get(0).getText().toString().equals(RED_PACKET_PICKED_DETAIL)) {
                        AccessibilityNodeInfo targetNode = nodes3.get(nodes3.size() - 1);

                        AccessibilityNodeInfo hongbaoNode = targetNode.getParent();
                        String sender = hongbaoNode.getChild(0).getText().toString();
                        String money = hongbaoNode.getChild(2).getText().toString();

                        LogUtils.d("save sender:" + sender + ",money:" + money + getStringTime());

                    } else {
                        LogUtils.d("this packet is myself!");
                    }

                }
                mNeedBack = true;
                mLuckyMoneyPicked = false;
            }
        }
    }


    private String getStringTime() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        return month + "月" + day + "日  " + hour + ":" + min + ":" + sec;
    }


}
