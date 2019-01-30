package cn.pooki.wechat_redenvelope_assistant.wechat

import android.view.accessibility.AccessibilityNodeInfo
import kotlin.concurrent.thread

object RedEnvelopeUIIdentifier {
    private lateinit var redEnvIds: Map<RedEnvelopeIds.RedEnvelopeKey, String>
    fun setWechatIds(ver: String) {
        when(ver) {
            "7.0.0" -> redEnvIds = RedEnvelopeIds.Ids[RedEnvelopeIds.WeChatVersion.v700]!!
        }
    }

    enum class UI {
        UNKNOWN,            //未知页面
        LAUNCHERUI,         //微信启动主页面
        CHATROOM,           //聊天页面
        TOOPENUI,           //点击红包后的页面
        MISS,               //红包被抢完
        SUCCESS             //成功抢到
    }

    private fun isLauncher(root: AccessibilityNodeInfo): Boolean {
        var ret = false
        val nodeMenu = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.layoutMenu])
        if (nodeMenu != null && nodeMenu.isNotEmpty()) {
            ret = true
        }
        return ret
    }

    private fun isChatRoom(root: AccessibilityNodeInfo): Boolean {
        var ret = false
        val nodeEdit = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.chatEdit])
        val nodeRedEnv = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.redEnvelope])
        if (nodeEdit != null && nodeEdit.isNotEmpty() && nodeRedEnv != null && nodeRedEnv.isNotEmpty()) {
            ret = true
        }
        return ret
    }

    private fun isOpenUI(root: AccessibilityNodeInfo): Boolean {
        var ret = false
        val nodeOpen = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.open])
        if (nodeOpen != null && nodeOpen.isNotEmpty()) {
            ret = true
        }
        return ret
    }

    private fun isMiss(root: AccessibilityNodeInfo): Boolean {
        var ret = false
        val nodeMiss = root.findAccessibilityNodeInfosByText(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.miss])
        if (nodeMiss != null && nodeMiss.isNotEmpty()) {
            ret = true
        }
        return ret
    }

    private fun isSuccess(root: AccessibilityNodeInfo): Boolean {
        var ret = false
        val nodeSuc = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.success])
        val nodeBack = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.chatRoomBack])
        if (nodeSuc != null && nodeSuc.isNotEmpty() && nodeBack != null && nodeBack.isNotEmpty()) {
            ret = true
        }
        return ret
    }

    fun identifyUI(root: AccessibilityNodeInfo?): UI {
        if (root == null) return UI.UNKNOWN
        if (isLauncher(root))  return UI.LAUNCHERUI
        if (isChatRoom(root)) return UI.CHATROOM
        if (isOpenUI(root)) return UI.TOOPENUI
        if (isMiss(root)) return UI.MISS
        if (isSuccess(root)) return UI.SUCCESS
        return UI.UNKNOWN
    }

    private fun performClickNode(nodeInfo: AccessibilityNodeInfo) {
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    fun clickRedEnvelope(root: AccessibilityNodeInfo) {
        //ToDo：check is it already over
        //考虑到红包的时效，只抢最后一个

        val node = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.redEnvelope])
        if (node != null && node.isNotEmpty()) {
//            val textCnt = node[0].getChild(0).getChild(0).getChild(1).childCount
            val cnt = node.size
            if (node[node.size-1] != null)
            performClickNode(node[node.size-1])
        }
    }

    fun clickOpen(root: AccessibilityNodeInfo) {
        val node = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.open])
        if (node != null && node.isNotEmpty()) {
            performClickNode(node[0])
        }
    }

    fun clickClose(root: AccessibilityNodeInfo) {
        val nodeMiss = root.findAccessibilityNodeInfosByText(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.miss])
        val nodeClose = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.close])
        if (nodeMiss != null && nodeMiss.isNotEmpty() && nodeClose != null && nodeClose.isNotEmpty()) {
            performClickNode(nodeClose[0])
        }
    }

    fun clickRedEnvelopeBack(root: AccessibilityNodeInfo) {
        val nodeBack = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.packageBack])
        if (nodeBack != null && nodeBack.isNotEmpty()) {
            performClickNode(nodeBack[0])
        }
    }

    fun clickChatRoomBack(root: AccessibilityNodeInfo) {
        val node = root.findAccessibilityNodeInfosByViewId(redEnvIds[RedEnvelopeIds.RedEnvelopeKey.chatRoomBack])
        if (node != null && node.isNotEmpty()) {
            performClickNode(node[0])
        }
    }

}