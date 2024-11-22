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

    init {
        setupClassifier()
    }

    private fun setupClassifier() {
    }

    fun classify() {
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
