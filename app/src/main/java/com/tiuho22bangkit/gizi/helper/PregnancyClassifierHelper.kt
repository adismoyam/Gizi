package com.tiuho22bangkit.gizi.helper

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PregnancyClassifierHelper(
    private val modelName: String = "model_2.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var tfliteInterpreter: Interpreter? = null

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {
        try {
            val assetFileDescriptor = context.assets.openFd(modelName)
            val inputStream = assetFileDescriptor.createInputStream()
            val modelByteBuffer = ByteBuffer.allocateDirect(inputStream.available())
            modelByteBuffer.order(ByteOrder.nativeOrder())
            inputStream.channel.read(modelByteBuffer)
            modelByteBuffer.rewind()

            val options = Interpreter.Options()
            options.setNumThreads(4)
            tfliteInterpreter = Interpreter(modelByteBuffer, options)

            Log.d("NumericalClassifierHelper", "Model loaded successfully")
        } catch (e: IOException) {
            Log.e("NumericalClassifierHelper", "Failed to load model: ${e.message}")
            classifierListener?.onError("Failed to load model: ${e.message}")
        }
    }

    fun classifyInput(inputData: FloatArray) {
        if (tfliteInterpreter == null) {
            classifierListener?.onError("Interpreter is not initialized")
            return
        }

        // memeriksa bentuk input yang dibutuhkan model
        Log.d("NumericalClassifierHelper","Input shape required: ${tfliteInterpreter?.getInputTensor(0)?.shape()?.contentToString()}")
        // memeriksa tipe data input yang dibutuhkan model
        Log.d("NumericalClassifierHelper","Data type required: ${tfliteInterpreter?.getInputTensor(0)?.dataType().toString()}")

        // memeriksa isi input
        Log.d("NumericalClassifierHelper", "Input data: ${inputData.joinToString(", ")}")
        // memeriksa bentuk input
        Log.d("NumericalClassifierHelper","Input shape: ${tfliteInterpreter?.getInputTensor(0)?.shape().contentToString()}")

        // memeriksa jumlah output dari model
        Log.d("NumericalClassifierHelper","Model has ${tfliteInterpreter?.outputTensorCount} outputs.")

        // memeriksa bentuk output dari model
        Log.d("NumericalClassifierHelper","Output 1 shape: ${tfliteInterpreter?.getOutputTensor(0)?.shape().contentToString()}")

        val input = arrayOf(inputData)
        val output = Array(1) { FloatArray(3) }

        try {
            tfliteInterpreter?.run(input, output)
            Log.d(
                "NumericalClassifierHelper",
                "Inference result: Output: ${output[0].joinToString(", ")})}"
            )
            classifierListener?.onResults(arrayOf(output[0])) // Return both outputs
        } catch (e: Exception) {
            classifierListener?.onError("Error during inference: ${e.message}")
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: Array<FloatArray>)
    }
}