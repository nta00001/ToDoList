package com.example.todolist.presentation.qrcode

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class QrScannerViewModel : ViewModel() {

    // LiveData để giữ kết quả quét được
    private val _scannedResult = MutableLiveData<String?>()
    val scannedResult: LiveData<String?> get() = _scannedResult

    // Trạng thái cho biết quá trình quét có đang diễn ra hay không để tránh xử lý chồng chéo
    private val _isScanning = MutableLiveData(false)

    private val barcodeScanner: BarcodeScanner

    init {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun processImageProxy(imageProxy: ImageProxy) {
        if (_isScanning.value == true) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)


            _isScanning.postValue(true)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {

                        _scannedResult.postValue(barcodes.first().rawValue)
                    }
                }
                .addOnFailureListener {
                    Log.e("QrScannerViewModel", "Barcode scanning failed", it)
                }
                .addOnCompleteListener {

                    _isScanning.postValue(false)
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    fun processStaticImage(image: InputImage) {
        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    _scannedResult.postValue("Từ ảnh: " + barcodes.first().rawValue)
                } else {
                    _scannedResult.postValue("Không tìm thấy mã QR trong ảnh.")
                }
            }
            .addOnFailureListener {
                _scannedResult.postValue("Lỗi khi quét ảnh.")
                Log.e("QrScannerViewModel", "Static image scanning failed", it)
            }
    }


    fun resetResult() {
        _scannedResult.value = null
    }
}