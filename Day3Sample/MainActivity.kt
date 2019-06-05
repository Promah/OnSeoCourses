package com.example.applicationday3pet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val data = Pet.PetTypes.values().asList()
    private lateinit var comboBox : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonMakePet.setOnClickListener {


            val initializerPet3 = NamedPet().apply {
                age = editTextPetAge.text.toString().toIntOrNull()
                type = spinnerPetTypes.selectedItem as Pet.PetTypes
                name = editTextPetName.text.toString().let {
                    if (it.isEmpty()) null else it
                }
                isBaby = editTextPetAge.text.toString().toIntOrNull()?.let {
                    it < 2
                }
            }
            Toast.makeText(
                this,
                "initializerPet3.Age=${initializerPet3.age}; initializerPet3.type=${initializerPet3.type} initializerPet3.name=${initializerPet3.name}; initializerPet3.isbaby=${initializerPet3.isBaby};",
                Toast.LENGTH_SHORT)
                .show()
//
        }
        val comboBoxAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, data)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        comboBox.run {
            spinnerPetTypes.apply {
                adapter = comboBoxAdapter
                prompt = "Title"
            }
        }
        comboBox = spinnerPetTypes
            .apply {
                adapter = comboBoxAdapter
                prompt = "Title"
            }

    }

    class NamedPet : Pet(){
        var name : String? = null
        var isBaby : Boolean? = null
    }



}
