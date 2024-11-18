package com.tiuho22bangkit.gizi.helper

import android.content.Context
import android.net.Uri

class StuntWastClassifierHelper(
//    private var threshold: Float = 0.3f,
//    private var maxResults: Int = 3,
    private val modelName: String = "model_1.tflite",
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
