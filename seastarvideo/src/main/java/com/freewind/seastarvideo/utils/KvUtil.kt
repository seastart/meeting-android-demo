/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */
package com.freewind.seastarvideo.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * @author wiatt
 * @description: 基于 MMKV 的 key-value 存储工具， 用于替代 SharePreference
 */
object  KvUtil{

    var mv: MMKV? = null
    init {
        mv=MMKV.defaultMMKV()
    }
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    fun encode(key: String?, `object`: Any) {
        when (`object`) {
            is String -> {
                mv?.encode(key, `object`)
            }
            is Int -> {
                mv?.encode(key, `object`)
            }
            is Boolean -> {
                mv?.encode(key, `object`)
            }
            is Float -> {
                mv?.encode(key, `object`)
            }
            is Long -> {
                mv?.encode(key, `object`)
            }
            is Double -> {
                mv?.encode(key, `object`)
            }
            is ByteArray -> {
                mv?.encode(key, `object`)
            }
            else -> {
                mv?.encode(key, `object`.toString())
            }
        }
    }

    fun encodeSet(key: String?, sets: Set<String?>?) {
        mv?.encode(key, sets)
    }

    fun encodeParcelable(key: String?, obj: Parcelable?) {
        mv?.encode(key, obj)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    fun decodeInt(key: String?): Int? {
        return mv?.decodeInt(key, 0)
    }

    fun decodeDouble(key: String?): Double {
        return mv?.decodeDouble(key, 0.00)!!
    }

    fun decodeLong(key: String?): Long {
        return mv?.decodeLong(key, 0L)!!
    }

    fun decodeBoolean(key: String?): Boolean {
        return mv?.decodeBool(key, false) == true
    }

    fun decodeBooleanTure(key: String?, defaultValue: Boolean): Boolean {
        return mv?.decodeBool(key, defaultValue) == true
    }

    fun decodeFloat(key: String?): Float {
        return mv?.decodeFloat(key, 0f)!!
    }

    fun decodeBytes(key: String?): ByteArray {
        return mv?.decodeBytes(key)!!
    }

    fun decodeString(key: String?): String {
        return mv?.decodeString(key, "").toString()
    }

    fun decodeStringDef(key: String?, defaultValue: String): String {
        return mv?.decodeString(key, defaultValue).toString()
    }


    fun decodeStringSet(key: String?): Set<String> {
        return mv?.decodeStringSet(key, emptySet()) as Set<String>
    }

    fun <T : Parcelable?> decodeParcelable(key: String?, tClass: Class<T>?): T? {
        return mv?.decodeParcelable(key, tClass)
    }

    /**
     * 移除某个key对
     *
     * @param key
     */
    fun removeKey(key: String?) {
        mv?.removeValueForKey(key)
    }

    /**
     * 清除所有key
     */
    fun clearAll() {
        mv?.clearAll()
    }

    // 用户信息：uid
    const val USER_INFO_UID = "userInfoUid"
    // 用户信息：电话号码
    const val USER_INFO_MOBILE = "userInfoMobile"
    // 用户信息：密码
    const val USER_INFO_PWD = "userInfoPwd"
    // 用户信息：昵称
    const val USER_INFO_NICK_NAME = "userInfoNickName"
    // 用户信息：头像
    const val USER_INFO_AVATAR = "userInfoAvatar"
    // 用户信息：应用层 token
    val JWT_TOKEN = "jwt_token"

    // 房间视频状态
    val MEETING_ROOM_VIDEO_STATUS = "meetingRoomVideoStatus"
    // 房间音频状态
    val MEETING_ROOM_AUDIO_STATUS = "meetingRoomAudioStatus"
    // 是否允许成员自行解除视频状态
    val MEETING_ROOM_ENABLE_SELF_OPEN_VIDEO = "meetingRoomEnableSelfOpenVideo"
    // 是否允许成员自行解除音频状态
    val MEETING_ROOM_ENABLE_SELF_OPEN_AUDIO = "meetingRoomEnableSelfOpenAudio"
}