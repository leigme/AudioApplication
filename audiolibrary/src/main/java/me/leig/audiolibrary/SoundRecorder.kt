package me.leig.audiolibrary

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues.TAG
import android.os.Handler


/**
 * 录音类
 *
 * @author leig
 * @version 20180301
 *
 */

class SoundRecorder constructor(private val micVoiceStatus: MicVoiceStatus, private val rootPath: String = "") {

    private val tag = SoundRecorder::class.java.name

    var mRecorder: MediaRecorder? = null

    var isRecorder: Boolean = false

    var fileName: String  = ""

    private val voiceBase = 1
    // 间隔取样时间
    private val voiceSpace: Long = 100

    private val mHandler = Handler()
    private val mUpdateMicStatusTimer = Runnable { updateMicVoiceStatus() }

    /**
     * 录音操作
     *
     */
    fun startRecording(): Boolean {
        // 设置录音状态
        isRecorder = true
        // 创建媒体对象
        mRecorder = MediaRecorder()
        // 设定采集来源
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        // 设定输出格式
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        // 设定文件路径
        mRecorder!!.setOutputFile(newFileName(rootPath))
        // 设定编码
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

        try {
            // 准备
            mRecorder!!.prepare()
            // 开始录音
            mRecorder!!.start()
            updateMicVoiceStatus()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(tag, "prepare() failed")
            throw e
        }
        return isRecorder
    }

    fun stopRecording(): String {
        if (isRecorder) {
            // 停止录音
            mRecorder!!.stop()
            // 清除
            mRecorder!!.release()
            // 置空
            mRecorder = null
            // 设定录音状态
            isRecorder = false
        }
        return fileName
    }

    /**
     * 创建录音文件
     *
     */
    private fun newFileName(rootPath: String = ""): String {

        fileName = rootPath

        if ("" == fileName) {

            fileName = Environment.getExternalStorageDirectory().absolutePath

        }

        val s = SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(Date())

        fileName += "/rcd_$s.mp4"

        return fileName
    }

    /**
     * 更新录音分贝
     *
     */
    private fun updateMicVoiceStatus() {

        if (null != mRecorder && isRecorder) {
            // 更新状态
            val ratio = mRecorder!!.maxAmplitude.toDouble() / voiceBase
            var db = 0.0// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio)
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, voiceSpace)
            micVoiceStatus.micVoiceListener(db)
        }
    }
}