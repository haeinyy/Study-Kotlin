package com.example.a07_stopwatch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), View.OnClickListener{

    var isRunning = false  // 실행여부 확인 변수
    var timer:Timer?= null
    var time = 0

    private lateinit var btn_start:Button
    private lateinit var btn_refresth:Button
    private lateinit var tv_millisecond:TextView
    private lateinit var tv_second:TextView
    private lateinit var tv_minute:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰 가져오기
        btn_start = findViewById(R.id.btn_start)
        btn_refresth = findViewById(R.id.btn_refresh)
        tv_millisecond = findViewById(R.id.tv_millisecond)
        tv_second = findViewById(R.id.tv_second)
        tv_minute = findViewById(R.id.tv_minute)
        // 버튼별 OnClickListener 등록
        btn_start.setOnClickListener(this)
        btn_refresth.setOnClickListener(this)
    }

    // 클릭 이벤트 처리
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start ->{
                if(isRunning) {
                    pause()
                } else {
                    start()
                }
            }
            R.id.btn_refresh -> {
                refresh()
            }
        }
    }

    private fun start() {
        btn_start.text = "일시정지"
        btn_start.setBackgroundColor(getColor(R.color.red))
        isRunning = true

        // 스톱워치를 시작하는 로직
        timer = timer(period = 10) {// 백그라운드 스레드에서 실행
            time++ // 1000밀리초 = 1초

            val milli_second = time%100
            val seond = (time%6000)/100
            val minute = time/6000

            runOnUiThread { // UI스레드 생성 - 안하면 백그라운드에서 UI 작업했기 때문에 ERROR 발생
                if (isRunning) {// UI 업데이트 조건 설정
                    // 밀리초
                    tv_millisecond.text =
                        if(milli_second<10) ".0${milli_second}" else ".${milli_second}" // 초

                    // 초
                    tv_second.text =
                        if(seond<10) ":0${seond}" else ".${seond}"

                    // 분
                    tv_minute.text ="${minute}"
                }
            }

        }

    }

    private fun pause() {
        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))

        isRunning = false
        timer?.cancel() // 백그라운드스레드 큐를 비워줌
    }

    private fun refresh() {
        timer?.cancel() // 백그라운드 타이머 멈추기

        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))
        isRunning = false

        time = 0
        tv_millisecond.text = ".00"
        tv_second.text = ".00"
        tv_minute.text = "00"
    }
}