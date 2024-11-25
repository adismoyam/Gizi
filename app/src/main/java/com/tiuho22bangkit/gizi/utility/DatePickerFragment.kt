package com.tiuho22bangkit.gizi.utility

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var mListener: DialogDateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Cek apakah parentFragment adalah listener
        mListener = when {
            parentFragment is DialogDateListener -> parentFragment as DialogDateListener
            context is DialogDateListener -> context
            else -> throw RuntimeException("$context must implement DialogDateListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null // Pastikan listener dilepas saat fragment dilepas
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), this, year, month, date
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        mListener?.onDialogDateSet(year, month, dayOfMonth)
    }

    interface DialogDateListener {
        fun onDialogDateSet(year: Int, month: Int, dayOfMonth: Int)
    }
}

