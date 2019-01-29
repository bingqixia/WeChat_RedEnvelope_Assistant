package cn.pooki.wechat_redenvelope_assistant

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.KeyEvent
import com.rarnu.kt.android.resStr
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var wechatVersion: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_service.setOnClickListener { goAccess() }

    }

    override fun onResume() {
        super.onResume()
        updateAccessibilityServiceStatus()
    }

    private fun goAccess() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun updateAccessibilityServiceStatus() {
        val e = isAccessibilitySettingsOn()
        tv_service.text = resStr(if (e) R.string.service_run else R.string.service_stop)
        tv_service.setTextColor(if (e) resources.getColor(R.color.ddark_pink)  else Color.RED)
    }

    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val service = "$packageName/${RedEnvelopeService::class.java.canonicalName}"
        try {
            accessibilityEnabled = Settings.Secure.getInt(applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Exception) {

        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isWechatSupported(): Boolean {
        var ret = false
        if (wechatVersion == "7.0.0") ret = true
        return ret
    }

    private fun getWechatVersion() {
        try {
            val info = packageManager.getPackageInfo("com.tencent.mm", 0)
            wechatVersion = info.versionName
            tv_version.text = resStr(R.string.wechat_version, wechatVersion)
            tv_version.setTextColor(if (isWechatSupported()) resources.getColor(R.color.ddark_pink) else Color.RED)
        } catch (e: Throwable) {
            KeyEvent.KEYCODE_APP_SWITCH
        }
    }
}
