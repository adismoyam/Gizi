package com.tiuho22bangkit.gizi.helper

import android.content.Context
import android.net.Uri
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class StuntWastClassifierHelper(
    private val modelName: String = "model_1.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var tfliteInterpreter: Interpreter? = null

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {
        try {
            // Membaca model dari assets
            val assetFileDescriptor = context.assets.openFd(modelName)
            val inputStream = assetFileDescriptor.createInputStream()
            val modelByteBuffer = ByteBuffer.allocateDirect(inputStream.available())
            modelByteBuffer.order(ByteOrder.nativeOrder())
            inputStream.channel.read(modelByteBuffer)
            modelByteBuffer.rewind()  // Memposisikan buffer ke awal

            // Membuat interpreter dengan ByteBuffer
            val options = Interpreter.Options()
            options.setNumThreads(4)  // Set jumlah thread untuk inference
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
        Log.d("NumericalClassifierHelper", "Input shape required: ${tfliteInterpreter?.getInputTensor(0)?.shape()?.contentToString()}")
        // memeriksa tipe data input yang dibutuhkan model
        Log.d("NumericalClassifierHelper","Data type required: ${tfliteInterpreter?.getInputTensor(0)?.dataType().toString()}")

        // memeriksa isi input
        Log.d("NumericalClassifierHelper", "Input data: ${inputData.joinToString(", ")}")
        // memeriksa bentuk input
        Log.d("NumericalClassifierHelper","Input shape: ${tfliteInterpreter?.getInputTensor(0)?.shape().contentToString()}")

        // memeriksa jumlah output dari model
        Log.d("NumericalClassifierHelper", "Model has ${tfliteInterpreter?.outputTensorCount} outputs.")
        // memeriksa bentuk output dari model
        Log.d("NumericalClassifierHelper","Output 1 shape: ${tfliteInterpreter?.getOutputTensor(0)?.shape().contentToString()}")
        Log.d("NumericalClassifierHelper","Output 2 shape: ${tfliteInterpreter?.getOutputTensor(1)?.shape().contentToString()}")


        val input = arrayOf(inputData)

        // mencocokkan array ouput dengan bentuk [1, 4]
        val output1 = Array(1) { FloatArray(4) }
        val output2 = Array(1) { FloatArray(4) }

        try {
            // Siapkan map untuk output
            val outputs = mutableMapOf<Int, Any>()
            outputs[0] = output1 // Output tensor pertama
            outputs[1] = output2 // Output tensor kedua

            // Jalankan inferensi
            tfliteInterpreter?.runForMultipleInputsOutputs(arrayOf(input), outputs)

            // hasil inferensi
            Log.d(
                "NumericalClassifierHelper",
                "Output1: ${output1[0].joinToString(", ")}, Output2: ${output2[0].joinToString(", ")}"
            )

            // Kirim hasil ke listener
            classifierListener?.onResults(arrayOf(output1[0], output2[0]))
        } catch (e: Exception) {
            classifierListener?.onError("Error during inference: ${e.message}")
        }
    }


    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: Array<FloatArray>)
    }
}