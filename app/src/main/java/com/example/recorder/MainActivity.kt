package com.example.recorder

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button


class MainActivity : AppCompatActivity() {
    private val resetButton: Button by lazy{
        findViewById(R.id.resetButton)
    }
    private val recordButton: RecordButton by lazy{
        findViewById(R.id.recordButton)
    }
    private val requestPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private val recordingFilePath: String by lazy{
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }  // audio 저장할 경로

    private var state = State.BEFORE_RECODING
    set(value) {
        field = value
        resetButton.isEnabled = value == (State.AFTER_RECODING) || (value == State.ON_PLAYING)
        recordButton.updateIconWithState(value)
    }//state에따라 icon변경
    private var recoder: MediaRecorder?= null
    private var player: MediaPlayer?= null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity","requestPermission")
        requestAudioPermission()  // 권한요청실행

        initView()
        bindView()
        initVariables()
    }

    // 요청한 권한 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted = requestCode ==  REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if(!audioRecordPermissionGranted){
            Log.d("MainActivity","appFinish")
            finish() //앱종료
        }
    }


    private fun initView(){
        recordButton.updateIconWithState(state)
    }

    private fun bindView(){
        resetButton.setOnClickListener {
            stopPlay()
            state = State.BEFORE_RECODING
            Log.d("bindView","초기화")
        }

        recordButton.setOnClickListener{
            when(state) {
                State.BEFORE_RECODING -> {
                    startRecord()
                }
                State.ON_RECODING -> {
                    stopRecording()
                }
                State.AFTER_RECODING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlay()
                }
            }
        }
    }

    private fun initVariables(){
        state = State.BEFORE_RECODING
    }

    private fun requestAudioPermission(){
        requestPermissions(requestPermissions, REQUEST_RECORD_AUDIO_PERMISSION) // 권한, 승인코드 요청
    }

   /* https://developer.android.com/reference/android/media/MediaRecorder */
    private fun startRecord(){
        recoder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
       recoder?.start()
       state = State.ON_RECODING
       Log.d("recoder","녹음시작")
    }

    /* https://developer.android.com/reference/android/media/MediaPlayer */
    private fun stopRecording(){
        recoder?.run {
            stop()
            release()
            Log.d("stopRecoding","녹음종료")
        }
        recoder = null
        state = State.AFTER_RECODING

    }

    private fun startPlaying(){
        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }
        player?.start()  //재생
        state = State.ON_PLAYING
    }

    private fun stopPlay(){
        player?.release()
        player = null
        state = State.AFTER_RECODING

    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201  // 승인 코드
    }

}


