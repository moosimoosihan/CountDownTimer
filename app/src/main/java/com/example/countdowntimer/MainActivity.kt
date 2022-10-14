package com.example.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.countdowntimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var countDownTimer: CountDownTimer

    var timerRunning = false //타이머 실행 상태
    var firstState = false //타이머 실행 처음인지 아닌지

    var time = 0L //타이머 시간
    var tempTime = 0L //타이머 임시 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //시작 버튼
        binding.startBtn.setOnClickListener {

            viewMode("start")
            startStop()
        }

        //정지 버튼
        binding.stopBtn.setOnClickListener {
            startStop()
        }

        //취소 버튼
        binding.cancelBtn.setOnClickListener {
            viewMode("cancel")
            stopTimer()
        }
    }//onCreate
    private fun viewMode(mode: String){
        //상태: 처음
        firstState = true

        if(mode == "start"){ //타이머 모드
            binding.settingLayout.visibility = View.GONE //사라짐
            binding.timerLayout.visibility = View.VISIBLE//보여짐
        }
        else{ //설정 모드
            binding.settingLayout.visibility = View.VISIBLE //사라짐
            binding.timerLayout.visibility = View.GONE//보여짐
        }
    }
    /**
     * 타이머 실행 상태에 따른 시작&정지
     */
    private fun startStop(){
        if(timerRunning){ // 실행중이면 정지
            stopTimer()
        }else{ //정지면 실행
            startTimer()
        }
    }
    /**
     * 타이머 실행
     */
    private fun startTimer(){

        //처음 실행이면 입력값을 타이머 설정값으로 사용
        if(firstState){
            val sHour = binding.hourEdit.text.toString()
            val sMin = binding.minEdit.text.toString()
            val sSec = binding.secEdit.text.toString()

            time = (sHour.toLong() * 3600000) + (sMin.toLong() * 60000) +
                    (sSec.toLong() * 1000) + 1000
        }else{ //정지 후 이어서 시작이면 기존값 사용
            time = tempTime
        }

        //타이머 실행
        countDownTimer = object: CountDownTimer(time, 1000){
            override fun onTick(millisUnitFinished: Long) {
                tempTime = millisUnitFinished
                //타이머 업데이트
                updateTime()
            }
            override fun onFinish() {}
        }.start()

        //정지 버튼 텍스트
        binding.stopBtn.text = "일시 정지"
        //타이머 상태: 실행
        timerRunning = true
        //처음 아님
        firstState = false
    }

    /**
     * 타이머 정지
     */
    private fun stopTimer(){
        //타이머 취소
        countDownTimer.cancel()

        //타이머 상태: 정지
        timerRunning = false

        //정지버튼 텍스트
        binding.stopBtn.text = "계속"
    }

    /**
     * 타이머 업데이트
     */
    private fun updateTime(){
        val hour = tempTime / 3600000
        val min = tempTime % 3600000 / 60000
        val sec = tempTime % 3600000 % 60000 / 1000

        //시간 추가
        var timeLeftText = "$hour :"

        //분이 10보다 작으면 0 붙이기
        if(min < 10) timeLeftText += "0"

        //분 추가
        timeLeftText += "$min :"

        //초가 10보다 작으면 0 붙이기
        if(sec < 10) timeLeftText += "0"

        //초 추가
        timeLeftText += sec

        //타이머 텍스트 보여주기
        binding.timerText.text = timeLeftText
    }
}