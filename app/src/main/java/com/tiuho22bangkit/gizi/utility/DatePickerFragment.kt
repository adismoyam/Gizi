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

        // Pastikan listener mengarah ke fragment yang benar (IsiDataAnakFragment)
        if (parentFragment is DialogDateListener) {
            mListener = parentFragment as DialogDateListener
        } else {
            throw RuntimeException("$context must implement DialogDateListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null // Pastikan mListener dilepas saat fragment terlepas
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)

        // Membuat DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), this, year, month, date
        )

        // Mengatur tanggal maksimum (hari ini)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        // Panggil listener langsung tanpa mengirimkan tag
        mListener?.onDialogDateSet(year, month, dayOfMonth)
    }

    interface DialogDateListener {
        // Kirimkan data langsung tanpa menggunakan tag
        fun onDialogDateSet(year: Int, month: Int, dayOfMonth: Int)
    }
}
