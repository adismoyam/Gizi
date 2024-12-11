package com.tiuho22bangkit.gizi.ui.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.tiuho22bangkit.gizi.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var isPasswordVisible = false
    private var minPasswordLength = 8

    init {
        setupDrawable()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak ada aksi yang diperlukan sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                     setError("Password tidak boleh kurang dari 8 karakter", null)
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // Tidak ada aksi yang diperlukan setelah teks berubah
            }
        })
    }

    private fun setupDrawable() {
        // Set drawableEnd (icon mata tertutup) secara default
        setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawableVisibilityOff(), null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = compoundDrawables[2] // Drawable end (kanan)
            if (drawableEnd != null && event.rawX >= (right - drawableEnd.bounds.width())) {
                togglePasswordVisibility()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) {
            HideReturnsTransformationMethod.getInstance() // Tampilkan password
        } else {
            PasswordTransformationMethod.getInstance() // Sembunyikan password
        }
        // Update ikon sesuai dengan status
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            if (isPasswordVisible) getDrawableVisibilityOn() else getDrawableVisibilityOff(),
            null
        )
        // Pindahkan cursor ke akhir teks
        setSelection(text?.length ?: 0)
    }

    private fun validatePassword(password: CharSequence?) {
        val parent = parent.parent
        if (parent is TextInputLayout) {
            if (password.isNullOrEmpty() || password.length < minPasswordLength) {
                parent.error = "Password tidak boleh kurang dari $minPasswordLength karakter"
            } else {
                parent.error = null
            }
        }
    }

    private fun getDrawableVisibilityOn(): Drawable? {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_eye)
        drawable?.setTint(ContextCompat.getColor(context, R.color.pink)) // Ganti warna
        return drawable
    }

    private fun getDrawableVisibilityOff(): Drawable? {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_eye)
        drawable?.setTint(ContextCompat.getColor(context, R.color.black)) // Ganti warna
        return drawable
    }
}

