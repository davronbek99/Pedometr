package uz.pdp.pedometr

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import uz.pdp.pedometr.listener.StepListener
import uz.pdp.pedometr.utils.StepDetector

class MainActivity : AppCompatActivity(), SensorEventListener, StepListener {
    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private val Text_Num_STEPS = ""
    private var numSteps: Int = 0
    private var count = 2
    private var calories = 0.0f
    private var caloryOfPerson = 0.03485f

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccelerometr(
                event.timestamp,
                event.values[0],
                event.values[1],
                event.values[2]
            )
        }
    }


    override fun step(timeNs: Long) {
        numSteps++
        tv_staps.text = Text_Num_STEPS.plus(numSteps)
        line_count_tv.text = numSteps.div(1400.0f).toString()
        line_count_calory_tv.text = Calory(numSteps).toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)


        play_pause.setOnClickListener {
            if (count == 1) {
                count = 2
                chronometer.stop()
                sensorManager!!.unregisterListener(this)
                play_pause.setBackgroundResource(R.drawable.resume_back)
                tv_play_paause.text = "Продолжит"
                Toast.makeText(this, "Пауза", Toast.LENGTH_SHORT).show()
                iv_play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                linear_pause.visibility = View.VISIBLE
            } else if (count == 2) {
                count = 1
                sensorManager!!.registerListener(
                    this,
                    sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST

                )
                chronometer.start()
                play_pause.setBackgroundResource(R.drawable.pause_resume_back)
                iv_play_pause.setImageResource(R.drawable.ic_baseline_pause_24)
                tv_play_paause.text = "Пауза"
                Toast.makeText(this, "Продолжит", Toast.LENGTH_SHORT).show()
                linear_pause.visibility = View.GONE
            }
        }

        play_pause.setOnLongClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            line_count_calory_tv.text = 0.0f.toString()
            numSteps = 0
            Calory(0)
            line_count_tv.text = 0.0f.toString()
            tv_staps.text = 0.toString()
            true
        }

    }

    private fun Calory(steps: Int): Float {
        for (i in 0 until steps) {
            calories += caloryOfPerson
        }
        return calories
    }
}