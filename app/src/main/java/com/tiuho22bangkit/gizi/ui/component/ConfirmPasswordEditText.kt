package com.tiuho22bangkit.gizi.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.tiuho22bangkit.gizi.R

class ConfirmPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    private var passwordEditText: TextInputEditText? = null

    init {
        passwordEditText = findViewById(R.id.passwordEditText)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak ada aksi yang diperlukan sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password = passwordEditText?.text.toString()
                if (s.toString() != password) {
                    setError("Password tidak cocok", null)
                } else if (s.length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // Tidak ada aksi yang diperlukan setelah teks berubah
            }
        })
    }
}
