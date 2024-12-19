package com.example.sqllite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchData()

        val nom_edtxt = findViewById<EditText>(R.id.name)
        val prix_edtxt = findViewById<EditText>(R.id.price)
        val image_edtxt = findViewById<EditText>(R.id.image)
        val AddBtn = findViewById<Button>(R.id.AddBtn)
        val listview = findViewById<ListView>(R.id.lv)

        AddBtn.setOnClickListener{
            val nom = nom_edtxt.text.toString()
            val price = prix_edtxt.text.toString().toDoubleOrNull()
            val image = image_edtxt.text.toString()

            if (nom.isNotBlank() && price!= null && image.isNotBlank()){
                val smartphone = SmartPhone(nom = nom , prix = price , image = image)
                SmartPhoneDatabase.getDataBase(applicationContext).smartphoneDao().insertSmartphone(smartphone)

                Toast.makeText(this, "SmartPhone added successfully!", Toast.LENGTH_SHORT).show()
                fetchData()
            }else{
                Toast.makeText(this, "Please complete all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        listview.setOnItemClickListener { _, _, position, _ ->
            val smartphones = SmartPhoneDatabase.getDataBase(applicationContext).smartphoneDao().getSmartPhone()
            val selectedSmartphone = smartphones[position]

            val intent = Intent(this, Up_And_Dell::class.java).apply {
                putExtra("id", selectedSmartphone.id)
                putExtra("name", selectedSmartphone.nom)
                putExtra("price", selectedSmartphone.prix)
                putExtra("image", selectedSmartphone.image)
            }
            startActivity(intent)
        }
    }

    private fun fetchData(){
        val listViewSmartphones = findViewById<ListView>(R.id.lv)
        val smartphoneList = mutableListOf<String>()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, smartphoneList)
        listViewSmartphones.adapter = arrayAdapter

        val smartphones = SmartPhoneDatabase.getDataBase(applicationContext).smartphoneDao().getSmartPhone()
        smartphoneList.clear()

        for (phone in smartphones){
            smartphoneList.add("${phone.nom} - ${phone.prix} Dh")
        }

        arrayAdapter.notifyDataSetChanged()


//        Recherche

        val searchED = findViewById<EditText>(R.id.searchEd)
        searchED.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    val smartphones = SmartPhoneDatabase.getDataBase(applicationContext)
                        .smartphoneDao()
                        .searchSmartphonesByName(searchQuery)

                    val filteredResults = smartphones.map { "${it.nom} - ${it.prix} Dh" }


                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, filteredResults)
                    listViewSmartphones.adapter = adapter

                    if (filteredResults.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Aucun produit trouvé", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    fetchData()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    override fun onResume() {
        super.onResume()
        fetchData()
    }
}