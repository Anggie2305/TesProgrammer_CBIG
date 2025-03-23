package com.example.tes_cbig.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tes_cbig.R
import com.example.tes_cbig.database.Item
import com.example.tes_cbig.login.LoginActivity
import com.example.tes_cbig.login.SharedPrefManager

class MainActivity : AppCompatActivity() {
    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Periksa token saat aplikasi dibuka
        val token = SharedPrefManager.getInstance(this).getToken()
        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Token ada, tampilkan MainActivity
            setContentView(R.layout.activity_main)
            Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ItemAdapter(
            onEditClick = { item ->
                showEditItemDialog(item)
            },
            onDeleteClick = { item ->
                showDeleteConfirmationDialog(item)
            }
        )
        recyclerView.adapter = adapter

        itemViewModel.allItems.observe(this, Observer { items ->
            // Update data di Adapter
            adapter.submitList(items)
        })

        itemViewModel.isLoading
        itemViewModel.refreshItems(token ?: "")

        val btnAddItem = findViewById<Button>(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun showAddItemDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextStock = dialogView.findViewById<EditText>(R.id.editTextStock)
        val editTextUnit = dialogView.findViewById<EditText>(R.id.editTextUnit)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Data Baru")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val name = editTextName.text.toString()
                val stock = editTextStock.text.toString().toIntOrNull() ?: 0
                val unit = editTextUnit.text.toString()

                if (name.isNotEmpty() && unit.isNotEmpty()) {
                    val item = Item(item_name = name, stock = stock, unit = unit)
                    itemViewModel.insert(item)
                } else {
                    Toast.makeText(this, "Nama dan Satuan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()
        dialog.show()
    }

    private fun showEditItemDialog(item: Item) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextStock = dialogView.findViewById<EditText>(R.id.editTextStock)
        val editTextUnit = dialogView.findViewById<EditText>(R.id.editTextUnit)

        editTextName.setText(item.item_name)
        editTextStock.setText(item.stock.toString())
        editTextUnit.setText(item.unit)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Data")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val name = editTextName.text.toString()
                val stock = editTextStock.text.toString().toIntOrNull() ?: 0
                val unit = editTextUnit.text.toString()

                if (name.isNotEmpty() && unit.isNotEmpty()) {
                    val updatedItem = item.copy(item_name = name, stock = stock, unit = unit)
                    itemViewModel.update(updatedItem)
                } else {
                    Toast.makeText(this, "Nama dan Satuan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(item: Item) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Data")
            .setMessage("Apakah Anda yakin ingin menghapus item ini?")
            .setPositiveButton("Hapus") { _, _ ->
                itemViewModel.delete(item)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun refreshData() {
        val token = SharedPrefManager.getInstance(this).getToken()
        if (token != null) {
            itemViewModel.refreshItems(token)
        } else {
            Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        // Hapus token dari SharedPreferences
        SharedPrefManager.getInstance(this).clearToken()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                refreshData()
                true
            }
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}