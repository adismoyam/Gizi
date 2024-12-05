package com.tiuho22bangkit.gizi.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Tidak ada aksi yang diperlukan sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                // Tidak ada aksi yang diperlukan setelah teks berubah
            }
        })
    }

    private fun validateEmail(email: String) {
        if (email.isEmpty()) {
            setError("Email tidak boleh kosong", null)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError("Format email tidak valid", null)
        } else {
            error = null
        }
    }
}
