package com.example.dicerollingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import com.example.dicerollingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // Declare view binding
    private lateinit var binding: ActivityMainBinding

    // Declare list to hold spinner items
    private val sidesList = mutableListOf<String>()

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

        // Add button click listener to add user input to spinner
        binding.sendButton.setOnClickListener {
            val input = binding.inputEditText.text.toString()
            if (input.isNotEmpty()) {
                sidesList.add(input)
                binding.inputEditText.text.clear()
                updateSpinnerAdapter()
            }
        }

        // Initialize spinner adapter with default items
        updateSpinnerAdapter()

        // Add button click listener to roll the die and display result in TextView
        binding.roll1Button.setOnClickListener {
            val selectedItem = binding.sidesSpinner.selectedItem.toString()
            val maxVal = selectedItem.toInt()
            val randomVal = ((Math.random() * maxVal) + 1).toInt()
            binding.output1TextView.text = randomVal.toString()
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
