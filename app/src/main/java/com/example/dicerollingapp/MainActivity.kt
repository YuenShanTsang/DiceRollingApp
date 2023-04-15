package com.example.dicerollingapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.dicerollingapp.databinding.ActivityMainBinding
import android.content.SharedPreferences
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // Declare view binding
    private lateinit var binding: ActivityMainBinding

    // Declare share preferences
    private lateinit var sharedPrefs: SharedPreferences

    // Declare list to hold spinner items
    private val sidesList = mutableListOf<String>()

    // Declare preferences key
    private val preferences = "MySidesList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add default spinner items
        sidesList.add("4")
        sidesList.add("6")
        sidesList.add("8")
        sidesList.add("10")
        sidesList.add("12")
        sidesList.add("20")

        // Initialize the shared preferences object with the mode set to private
        sharedPrefs = getPreferences(Context.MODE_PRIVATE)

        // Send button click listener to add user input to spinner
        binding.sendButton.setOnClickListener {
            // Retrieve the text input from the inputEditText view and converts it to a string
            val input = binding.inputEditText.text.toString()
            if (input.isNotEmpty()) {
                // Convert the string input to an integer
                val intValue = input.toIntOrNull()
                // Add to the sidesList if it is a valid integer greater than 0
                if (intValue != null && intValue > 0) {
                    sidesList.add(input)
                    // Google Json
                    val gson = Gson()
                    // Save the updated list to shared preferences as a JSON string
                    val sideJson = gson.toJson(sidesList)
                    // Get an editor object for modifying the shared preferences
                    val editor = sharedPrefs.edit()
                    // Put the string into the shared preferences using the key
                    editor.putString(preferences, sideJson)
                    // Apply the changes made to the editor object to the shared preferences
                    editor.apply()
                    // Clear the inputEditText view
                    binding.inputEditText.text.clear()
                    // Update the spinner adapter
                    updateSpinnerAdapter()
                } else {
                    // If not, show a toast message to the user
                    Toast.makeText(
                        this,
                        "Please enter a valid number greater than zero.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Initialize spinner adapter with saved items
        val gson = Gson()
        val json = sharedPrefs.getString(preferences, "")
        // Convert the string to an array of Strings using the Gson object
        val savedSidesList = gson.fromJson(json, Array<String>::class.java)
        // Check if the saved list is not null
        if (savedSidesList != null) {
            // Clear the current sidesList
            sidesList.clear()
            // Add all the saved sides to the current sidesList
            sidesList.addAll(savedSidesList.toList())
        }

        // Update spinner adapter with current items in sidesList
        updateSpinnerAdapter()

        // Add button click listener to roll the die and display result in TextView
        binding.roll1Button.setOnClickListener {
            val selectedItem = binding.sidesSpinner.selectedItem.toString()
            val maxVal = selectedItem.toInt()
            val randomVal = if (binding.tenSidedDieCheckbox.isChecked) {
                ((Math.random() * maxVal) + 1).toInt() * 10
            } else {
                ((Math.random() * maxVal) + 1).toInt()
            }
            binding.output1TextView.text = randomVal.toString()
            binding.output2TextView.text = ""
        }

        binding.roll2Button.isEnabled = false
        binding.twoDiceSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.roll2Button.isEnabled = isChecked
        }

        // Add button click listener to roll two dice and display two results in TextView
        binding.roll2Button.setOnClickListener {
            if (binding.twoDiceSwitch.isChecked) {
                val selectedItem = binding.sidesSpinner.selectedItem.toString()
                val maxVal = selectedItem.toInt()
                val randomVal1 = if (binding.tenSidedDieCheckbox.isChecked) {
                    ((Math.random() * maxVal) + 1).toInt() * 10
                } else {
                    ((Math.random() * maxVal) + 1).toInt()
                }
                val randomVal2 = if (binding.tenSidedDieCheckbox.isChecked) {
                    ((Math.random() * maxVal) + 1).toInt() * 10
                } else {
                    ((Math.random() * maxVal) + 1).toInt()
                }
                binding.output1TextView.text = randomVal1.toString()
                binding.output2TextView.text = randomVal2.toString()
            }
        }
    }

    // Update spinner adapter with current items in sidesList
    private fun updateSpinnerAdapter() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sidesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sidesSpinner.adapter = adapter
    }

    // Handle spinner item selection
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position).toString()
        // Do something with the selected item
    }

    // Handle no item selected in spinner
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle nothing selected
    }

    // Add night switch in menu of the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.night_switch)
        val switchView = item.actionView as Switch

        // Set the switch state based on the current night mode
        switchView.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        // Set a listener for changes to the switch state
        switchView.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        return true
    }
}
