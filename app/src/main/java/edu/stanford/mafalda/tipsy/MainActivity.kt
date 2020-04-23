package edu.stanford.mafalda.tipsy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Listening to the changes on the SeekBar and translating into the percentage number
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tip_percentage.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tip_percentage.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Listening to the changes in the "Base" and computing them with the tip in SeekBar
        actual_amount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }
    private fun computeTipAndTotal() {
        // Get the value of the base and tip percent
        // The if function makes sure that if the value in the Base Box is empty, the app knows to return empty strings
        if (actual_amount.text.isEmpty()){
            result_tip.text = ""
            result_total.text = ""
            return
        }
        val baseAmount = actual_amount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // Reflect the values in the corresponding textView
        result_tip.text = "%.2f".format(tipAmount)
        result_total.text = "%.2f".format(totalAmount)

    }

    private fun updateTipDescription(tipPercent:Int){
        val tipDescription: String
        when(tipPercent){
            in 0..9 -> tipDescription = "Scumbag!"
            in 10..14 -> tipDescription = "Cheap!"
            in 15..19 -> tipDescription = "Decent"
            in 20..25 -> tipDescription = "You're on a date, aren't you?"
            else -> tipDescription = "Now you're just bragging"
        }
        tvTipDescription.text = tipDescription

    }
}
