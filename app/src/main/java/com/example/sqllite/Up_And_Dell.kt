package com.example.sqllite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Up_And_Dell : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_up_and_dell)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editName = findViewById<EditText>(R.id.editName)
        val editPrice = findViewById<EditText>(R.id.editPrice)
        val editImage = findViewById<EditText>(R.id.editImage)
        val updateBtn = findViewById<Button>(R.id.updateBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteBtn)


        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")
        val price = intent.getDoubleExtra("price", 0.0)
        val image = intent.getStringExtra("image")

        editName.setText(name)
        editPrice.setText(price.toString())
        editImage.setText(image)

        val smartphoneDao = SmartPhoneDatabase.getDataBase(applicationContext).smartphoneDao()

        updateBtn.setOnClickListener {
            val updatedName = editName.text.toString()
            val updatedPrice = editPrice.text.toString().toDoubleOrNull()
            val updatedImage = editImage.text.toString()

            if (updatedName.isNotBlank() && updatedPrice != null && updatedImage.isNotBlank()) {
                val updatedSmartphone = SmartPhone(id = id, nom = updatedName, prix = updatedPrice, image = updatedImage)
                smartphoneDao.updateSmartphone(updatedSmartphone)
                Toast.makeText(this, "Smartphone updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
            smartphoneDao.deleteSmartphoneById(id)
            Toast.makeText(this, "Smartphone deleted successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}