package me.leig.audioapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import me.leig.audiolibrary.MicVoiceStatus
import me.leig.audiolibrary.SoundPlayer
import me.leig.audiolibrary.SoundRecorder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val tag = MainActivity::class.java.name

    private var soundRecorder: SoundRecorder? = null

    private var isRecorder = false

    private var soundPlayer: SoundPlayer? = null

    private var isPlaying = false

    private var fileName: String = ""

    private val mHandler: Handler = Handler(Handler.Callback {

        when (it.what) {
            1001 -> {
                val value = it.obj as Double
                progressBar.progress = value.toInt()
            }
        }

        return@Callback false
    })

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundRecorder = SoundRecorder(object : MicVoiceStatus {

            override fun micVoiceListener(db: Double) {
                Log.e(tag, "音量大小是: $db")
                mHandler.obtainMessage(1001, db).sendToTarget()
            }

        })

        soundPlayer = SoundPlayer()

        button.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            button -> {
                isRecorder = soundRecorder!!.startRecording()
            }
            button2 -> {
                fileName = soundRecorder!!.stopRecording()
                isRecorder = false
                mHandler.obtainMessage(1001, 0.0).sendToTarget()
                Log.e(tag, "文件保存的路径是: $fileName")
            }
            button3 -> {
                if ("" != fileName) {
                    isPlaying = soundPlayer!!.startPlaying(fileName)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != soundPlayer && isPlaying && !isRecorder) {
            soundPlayer!!.stopPlaying()
        }
    }

}
