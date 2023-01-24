package com.hotmail.example.proyecto_mov
import android.app.Dialog

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment (val listener : (HH:Int,MM:Int) -> Unit) : DialogFragment(),
        TimePickerDialog.OnTimeSetListener{

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener(hourOfDay,minute)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c : Calendar = Calendar.getInstance()
        val hora:Int=c.get(Calendar.HOUR_OF_DAY)
        val minuto:Int=c.get(Calendar.MINUTE)

        val picker = TimePickerDialog(activity as Context,R.style.timePickerTheme,this,hora,minuto,true)
        return picker
    }
}
