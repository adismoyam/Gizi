package com.tiuho22bangkit.gizi.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.DataType

class PregnancyClassifierHelper(
//    private var threshold: Float = 0.3f,
//    private var maxResults: Int = 3,
    private val modelName: String = "model_2.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?,
) {
//    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
//            results: List<Classifications>?
        )
    }

    companion object {
        private const val TAG = "PregnancyClassifierHelper"
    }
}
