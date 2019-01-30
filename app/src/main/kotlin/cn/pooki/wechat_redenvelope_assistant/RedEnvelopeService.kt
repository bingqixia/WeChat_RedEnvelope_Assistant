package cn.pooki.wechat_redenvelope_assistant

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import cn.pooki.wechat_redenvelope_assistant.wechat.RedEnvelopeUIIdentifier
import kotlin.concurrent.thread

class RedEnvelopeService: AccessibilityService() {
    companion object {

        private val TAG = "RedEnvelopeService"

        private var logEnabled = false

        // 只进入一次控制
        private var currentPage = -1

        // 工作流控制变量
        private var isBackToMain = false    // 是否要回到主界面
        private var isSetMemoTag = false    // 是否已设置标签和备注（当isTagInput和isMemoInput均为true时，此选项为true）

        var running = false
        var working = false
        private var actionInterval = 500L

        private var heartBeat = false
        private var survive = 0
        private var isDone = false

        fun initData(interval: Long) {
            if (logEnabled) {
                Log.e(TAG, "initData")
            }
            currentPage = -1
            isBackToMain = false
            actionInterval = interval
            survive = 0
        }
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent) {
        when (p0.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                if (p0.packageName.equals("com.tencent.mm") && p0.text.contains("微信红包")) {
                    Log.e(TAG, "pooki ---> red package")
                }
            }
        }
    }

    private fun work() = thread {
        while (running) {
            Thread.sleep(actionInterval)

            val ui = RedEnvelopeUIIdentifier.identifyUI(rootInActiveWindow)

            if (logEnabled) {
                Log.e(TAG, "ui => $ui")
                Log.e(TAG, "currentPage ==> $currentPage")
            }

            when (ui) {
                RedEnvelopeUIIdentifier.UI.LAUNCHERUI -> {
                    if (isDone) {
                        sendProcess(2)
                        running = false
                        working = false
                        backHome()
                    }
                    currentPage = 0
                    isBackToMain = false
                    survive = 0
                }

                RedEnvelopeUIIdentifier.UI.CHATROOM -> {
                    if (currentPage != 1) {
                        currentPage = 1
                        survive = 0
                        if (isBackToMain) {

                        } else {

                        }
                    } else {
                        sendSurvive()
                    }
                }

                RedEnvelopeUIIdentifier.UI.TOOPENUI -> {
                    if (currentPage != 2) {
                        currentPage = 2
                        survive = 0
                        if (isBackToMain) {

                        } else {

                        }
                    } else {
                        sendSurvive()
                    }
                }

                RedEnvelopeUIIdentifier.UI.MISS -> {
                    if (currentPage != 3) {
                        currentPage = 3
                        survive = 0
                        if (isBackToMain) {

                        } else {

                        }
                    } else {
                        sendSurvive()
                    }
                }

                RedEnvelopeUIIdentifier.UI.SUCCESS -> {
                    if (currentPage != 4) {
                        currentPage = 4
                        survive = 0
                        heartBeat = false
                        isBackToMain = true
                        sendProcess(2)
                        running = false
                        working = false
                    } else {
                        sendSurvive(2)
                    }
                }

                RedEnvelopeUIIdentifier.UI.UNKNOWN -> {
                    sendSurvive()
                }
            }
        }
    }

    fun backHome() {
        Thread.sleep(actionInterval)
        val home = Intent(Intent.ACTION_MAIN)

        home.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        home.addCategory(Intent.CATEGORY_HOME)

        startActivity(home)
    }

    private fun sendProcess(state: Int, heart: Int = 0) {
        val inProcess = Intent("action.hujiang.process")
        inProcess.putExtra("state", state)
        inProcess.putExtra("heart", heart)
        sendBroadcast(inProcess)
    }

    private fun sendSurvive(times: Int = 1) {
        survive++
        if (logEnabled) {
            Log.e(TAG, "heartbeat => $survive")
        }
        if (survive > 5) {
            sendProcess(3, survive)
        }
        if (survive > 10) {
            survive = 0
//            Utils.clickBack(times)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logEnabled = true
        if (logEnabled) {
            Log.e(TAG, "Started")
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onInterrupt() {
    }
}