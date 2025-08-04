package com.example.todolist.presentation.qrcode // Thay bằng package của bạn

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.databinding.FragmentQrScannerBinding
import com.google.mlkit.vision.common.InputImage
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrScannerFragment : Fragment() {

    private var _binding: FragmentQrScannerBinding? = null
    private val binding get() = _binding!!

    // Khởi tạo ViewModel bằng KTX delegate
    private val viewModel: QrScannerViewModel by viewModels()

    private lateinit var cameraExecutor: ExecutorService

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    val image = InputImage.fromFilePath(requireContext(), it)
                    viewModel.processStaticImage(image)
                } catch (e: IOException) {
                    Log.e("QrScannerFragment", "Failed to read image", e)
                    Toast.makeText(requireContext(), "Không thể đọc file ảnh.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        checkCameraPermission()
        observeViewModel()

        binding.galleryButton.setOnClickListener {
            // Mở trình chọn ảnh
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            // ImageAnalysis giờ chỉ đơn giản là gọi hàm trong ViewModel
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        viewModel.processImageProxy(imageProxy)
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e("QrScannerFragment", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun observeViewModel() {
        viewModel.scannedResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                // Khi có kết quả, hiển thị lên UI
                binding.resultText.text = it
                Log.d("QrScannerFragment", "Result observed: $it")

                // Sau khi hiển thị, bạn có thể reset để cho phép quét lại nếu cần
                // Hoặc điều hướng sang màn hình khác
                // viewModel.resetResult()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}