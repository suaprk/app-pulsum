package com.example.pulsum

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var currentPhotoPath: String
    private lateinit var galleryContainer: LinearLayout

    // 이미지 추가 결과 처리
    private val imageAddLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 갤러리 새로고침
            loadGalleryImages()
        }
    }

    // 갤러리에서 이미지 선택 (권한 플래그 추가)
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri = data?.data
            imageUri?.let { uri ->
                // URI 권한 유지 시도
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.w("ImagePicker", "Cannot take persistable permission: ${e.message}")
                }

                // 선택된 이미지로 ImageAddActivity 시작
                val intent = Intent(activity, ImageAddActivity::class.java)
                intent.putExtra("image_uri", uri.toString())
                imageAddLauncher.launch(intent)
            }
        }
    }

    // 카메라로 사진 촬영
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 촬영된 사진으로 ImageAddActivity 시작
            val photoUri = Uri.fromFile(File(currentPhotoPath))
            val intent = Intent(activity, ImageAddActivity::class.java)
            intent.putExtra("image_uri", photoUri.toString())
            imageAddLauncher.launch(intent)
        }
    }

    // 카메라 권한 요청
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Gallery Container 참조
        galleryContainer = view.findViewById(R.id.gallery_container)

        // New Plant 버튼 클릭 이벤트
        val btnNewPlant = view.findViewById<Button>(R.id.btn_new_plant)
        btnNewPlant.setOnClickListener {
            Toast.makeText(context, "New Plant 버튼이 클릭되었습니다!", Toast.LENGTH_SHORT).show()
        }

        // Post Picture 버튼 클릭 이벤트
        val btnPostPicture = view.findViewById<Button>(R.id.btn_post_picture)
        btnPostPicture.setOnClickListener {
            showImagePickerDialog()
        }

        // 검색 아이콘 클릭 이벤트
        val ivSearch = view.findViewById<ImageView>(R.id.iv_search)
        ivSearch?.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Iris 카드 클릭 이벤트
        val cardIris = view.findViewById<androidx.cardview.widget.CardView>(R.id.card_iris)
        cardIris?.setOnClickListener {
            val intent = Intent(activity, PlantDetailActivity::class.java)
            intent.putExtra("plant_name", "Iris")
            intent.putExtra("plant_scientific_name", "Iris")
            intent.putExtra("plant_image", R.drawable.iris)
            intent.putExtra("plant_date", "2025.9.16")
            intent.putExtra("plant_status", "Healthy")
            intent.putExtra("plant_last_care", "2 Days")
            intent.putExtra("plant_next_task", "Water")
            startActivity(intent)
        }

        // Succulent 카드 클릭 이벤트
        val cardSucculent = view.findViewById<androidx.cardview.widget.CardView>(R.id.card_succulent)
        cardSucculent?.setOnClickListener {
            val intent = Intent(activity, PlantDetailActivity::class.java)
            intent.putExtra("plant_name", "Succulent")
            intent.putExtra("plant_scientific_name", "Succulent")
            intent.putExtra("plant_image", R.drawable.succulent)
            intent.putExtra("plant_date", "2025.10.27")
            intent.putExtra("plant_status", "Healthy")
            intent.putExtra("plant_last_care", "7 Days")
            intent.putExtra("plant_next_task", "Fertilize")
            startActivity(intent)
        }

        // Rosemary 카드 클릭 이벤트
        val cardRosemary = view.findViewById<androidx.cardview.widget.CardView>(R.id.card_rosemary)
        cardRosemary?.setOnClickListener {
            val intent = Intent(activity, PlantDetailActivity::class.java)
            intent.putExtra("plant_name", "Rosemary")
            intent.putExtra("plant_scientific_name", "Rosemary")
            intent.putExtra("plant_image", R.drawable.rosemary)
            intent.putExtra("plant_date", "2025.11.12")
            intent.putExtra("plant_status", "Good")
            intent.putExtra("plant_last_care", "3 Days")
            intent.putExtra("plant_next_task", "Prune")
            startActivity(intent)
        }

        // Monstera 카드 클릭 이벤트
        val cardMonstera = view.findViewById<androidx.cardview.widget.CardView>(R.id.card_monstera)
        cardMonstera?.setOnClickListener {
            val intent = Intent(activity, PlantDetailActivity::class.java)
            intent.putExtra("plant_name", "Monstera")
            intent.putExtra("plant_image", R.drawable.monstera)
            intent.putExtra("plant_date", "2025.11.8")
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // 화면이 다시 보일 때마다 갤러리 새로고침
        loadGalleryImages()
    }

    private fun loadGalleryImages() {
        // 기존 동적 이미지들 제거 (기본 이미지들은 유지)
        removeDynamicImages()

        // SharedPreferences에서 저장된 이미지들 로드
        val sharedPref = requireContext().getSharedPreferences("gallery_images", Context.MODE_PRIVATE)
        val imageUris = sharedPref.getStringSet("image_uris", setOf()) ?: setOf()

        // 이미지들을 앞에서부터 추가
        imageUris.forEachIndexed { index, uriString ->
            addImageToGalleryView(uriString, index)
        }
    }

    private fun removeDynamicImages() {
        // "dynamic_image" 태그가 있는 뷰들만 제거
        val childrenToRemove = mutableListOf<View>()
        for (i in 0 until galleryContainer.childCount) {
            val child = galleryContainer.getChildAt(i)
            if (child.tag == "dynamic_image") {
                childrenToRemove.add(child)
            }
        }
        childrenToRemove.forEach { galleryContainer.removeView(it) }
    }

    private fun addImageToGalleryView(imageUriString: String, position: Int) {
        try {
            val imageUri = Uri.parse(imageUriString)

            // CardView 생성
            val cardView = androidx.cardview.widget.CardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.gallery_image_size), // 100dp
                    resources.getDimensionPixelSize(R.dimen.gallery_image_size)
                ).apply {
                    if (position < 3) marginEnd = resources.getDimensionPixelSize(R.dimen.gallery_margin) // 12dp
                }
                radius = resources.getDimensionPixelSize(R.dimen.gallery_corner_radius).toFloat() // 8dp
                cardElevation = resources.getDimensionPixelSize(R.dimen.gallery_elevation).toFloat() // 2dp
                tag = "dynamic_image" // 동적으로 추가된 이미지임을 표시
            }

            // ImageView 생성
            val imageView = ImageView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            // 안전한 이미지 로드
            loadImageSafely(imageView, imageUri)

            cardView.addView(imageView)

            // 맨 앞에 추가 (기존 이미지들 앞에)
            galleryContainer.addView(cardView, 0)

        } catch (e: Exception) {
            Log.e("Gallery", "Error adding image to gallery: ${e.message}")
        }
    }

    private fun loadImageSafely(imageView: ImageView, uri: Uri) {
        try {
            // ContentResolver를 통해 안전하게 접근
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                // 비트맵이 null인 경우 기본 이미지 설정
                imageView.setImageResource(R.drawable.ic_image_placeholder)
            }

        } catch (e: SecurityException) {
            Log.e("Gallery", "Permission denied: ${e.message}")
            // 권한 오류 시 기본 이미지 설정
            imageView.setImageResource(R.drawable.ic_image_placeholder)

        } catch (e: Exception) {
            Log.e("Gallery", "Error loading image: ${e.message}")
            // 기타 오류 시 기본 이미지 설정
            imageView.setImageResource(R.drawable.ic_image_placeholder)
        }
    }

    private fun showImagePickerDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image_picker, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<View>(R.id.btn_choose_gallery).setOnClickListener {
            dialog.dismiss()
            openGallery()
        }

        dialogView.findViewById<View>(R.id.btn_take_photo).setOnClickListener {
            dialog.dismiss()
            checkCameraPermissionAndOpen()
        }

        dialogView.findViewById<View>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            // URI 권한 플래그 추가
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        galleryLauncher.launch(intent)
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile = createImageFile()
            photoFile?.let {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.pulsum.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraLauncher.launch(intent)
            }
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = requireActivity().getExternalFilesDir("Pictures")
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                currentPhotoPath = absolutePath
            }
        } catch (e: Exception) {
            null
        }
    }
}