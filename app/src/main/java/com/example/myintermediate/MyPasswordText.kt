package com.example.myintermediate

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class MyPasswordText @JvmOverloads constructor(
    context: Context ,attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Input Your Password"

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START


    }

    override fun onTextChanged(
        text: CharSequence? ,
        start: Int ,
        lengthBefore: Int ,
        lengthAfter: Int
    ) {
        if (text.toString().length < 8) {
            setError("Password must be at least 8 characters long", null)
        } else {
            error = null
            setTextColor(ContextCompat.getColor(context, R.color.green))
        }
    }
}