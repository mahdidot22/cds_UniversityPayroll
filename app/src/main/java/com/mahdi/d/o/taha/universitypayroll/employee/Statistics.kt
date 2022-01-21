package com.mahdi.d.o.taha.universitypayroll.employee

import android.Manifest.permission.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityStatisticsBinding
import androidx.core.app.ActivityCompat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import java.io.File
import java.util.*
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


class Statistics : AppCompatActivity() {
    private val FILE_PATH = "storage/emulated/0/UniversityPayroll"
    private var _binding: ActivityStatisticsBinding? = null
    private val binding get() = _binding!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    lateinit var bitmap: Bitmap
    lateinit var scaledBitmap: Bitmap
    val fileName = "Payment_Form";
    val file_name_path = "/$FILE_PATH/$fileName.pdf";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStatisticsBinding.inflate(layoutInflater)
        _constants = Constants()
        setContentView(binding.root)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.main_icon);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, false);
        binding.apply {
            settings.setOnClickListener {
                startActivity(
                    Intent(
                        this@Statistics,
                        Settings::class.java
                    )
                )
            }
            btnPaymentForm.setOnClickListener {
//                val file = File(getExternalFilesDir(null)!!.absolutePath, FILE_PATH)
//                if (!file.exists()) {
//                    file.mkdir()
//                }
//                if (!constants.hasPermissions(this@Statistics, constants.PERMISSIONS)) {
//                    ActivityCompat.requestPermissions(
//                        this@Statistics,
//                        constants.PERMISSIONS,
//                        constants.PERMISSION_ALL
//                    );
//                } else {
//                    constants.generatePDF(
//                        300,
//                        470,
//                        1,
//                        scaledBitmap,
//                        this@Statistics,
//                        file_name_path
//                    )
//                }
                startActivity(Intent(this@Statistics,CreditCardForm::class.java))
            }
        }
    }
}