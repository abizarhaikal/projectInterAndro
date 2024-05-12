package com.example.myintermediate.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myintermediate.R
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.ActivityUploadBinding
import com.example.myintermediate.utils.reduceFileImage
import com.example.myintermediate.utils.uriToFile
import com.example.myintermediate.view.CameraActivity.Companion.CAMERAX_RESULT
import com.example.myintermediate.view.CameraActivity.Companion.EXTRA_CAMERAX_IMAGE
import com.example.myintermediate.viewModel.UploadViewModel

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding
    private var currentImageUri : Uri? = null
    private val uploadViewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncer =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {isGranted : Boolean ->
            if (isGranted) {
                showToast("Pemission Granted")
            } else {
                showToast("Permission Request denied")
            }
        }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermissionLauncer.launch(REQUIRED_PERMISSION)
        }

        binding.btnGallery.setOnClickListener { startGalerry() }
        binding.btnUpload.setOnClickListener { uploadImage() }
        binding.btnCamera.setOnClickListener { startCameraX() }
    }

    private fun startCameraX() {
        val intent = Intent(this@UploadActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "show image: ${imageFile.path}")
            val description = binding.edtDescription.text.toString()

            uploadViewModel.uploadImage(imageFile, description).observe(this) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showToast("Loading")
                        }
                        is Result.Success -> {
                            showToast(result.data.message)
                        }
                        is Result.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startGalerry(){
        launcherGalery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGalery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }else {
            Log.d("Photo Picker", "No media selecter")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image Uri", "showImage: $it")
            binding.ivUpload.setImageURI(it)
        }

    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}