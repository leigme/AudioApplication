package me.leig.audiolibrary

import android.media.MediaPlayer
import android.util.Log

/**
 * 播放录音
 *
 * @author leig
 * @version 20180301
 *
 */

class SoundPlayer {

    private val tag = SoundPlayer::class.java.name

    var mPlayer: MediaPlayer? = null

    var isPlaying: Boolean = false

    /**
     * 开始播放操作
     *
     */
    fun startPlaying(fileName: String): Boolean {
        // 设置播放状态
        isPlaying = true
        // 创建播放器
        mPlayer = MediaPlayer()

        try {
            // 设定播放源
            mPlayer!!.setDataSource(fileName)
            // 准备
            mPlayer!!.prepare()
            // 开始播放
            mPlayer!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(tag, "prepare() failed")
        }
        return isPlaying
    }

    fun stopPlaying(): Boolean {

        if (isPlaying) {
            // 停止播放
            mPlayer!!.release()
            mPlayer = null
        }

        return isPlaying
    }

}