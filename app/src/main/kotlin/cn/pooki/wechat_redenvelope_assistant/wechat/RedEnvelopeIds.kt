package cn.pooki.wechat_redenvelope_assistant.wechat

object RedEnvelopeIds {
    enum class WeChatVersion {
        v700
    }

    enum class RedEnvelopeKey {
        layoutMenu,
        redEnvelope,
        chatEdit,
        alreadyOver,
        miss,
        open,
        close,
        success,
        packageBack,
        chatRoomBack
    }

    private val keyV700 = mapOf(
        RedEnvelopeKey.layoutMenu to "com.tencent.mm:id/bn",
        RedEnvelopeKey.redEnvelope to "com.tencent.mm:id/ao4",
        RedEnvelopeKey.chatEdit to "com.tencent.mm:id/alm",
        RedEnvelopeKey.miss to "手慢了，红包派完了",
        RedEnvelopeKey.alreadyOver to "已被领完",                //被领完的红包
        RedEnvelopeKey.close to "com.tencent.mm:id/cs9",        //没抢到，关闭红包
        RedEnvelopeKey.open to "com.tencent.mm:id/cv0",
        RedEnvelopeKey.success to "已存入零钱，可直接消费",
        RedEnvelopeKey.packageBack to "com.tencent.mm:id/k4",
        RedEnvelopeKey.chatRoomBack to "com.tencent.mm:id/ju"
    )

    val Ids = mapOf(
        WeChatVersion.v700 to keyV700
    )
}