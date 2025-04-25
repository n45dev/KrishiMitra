package dev.n45.krishimitra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.n45.krishimitra.api.data.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.n45.krishimitra.adapter.ProductCartAdapter

class CartActivity : AppCompatActivity(), ProductCartAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productCartAdapter: ProductCartAdapter
    private var cartItems: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val topBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)

        topBar.setNavigationOnClickListener {

        }

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val cartItemsJson = intent.getStringExtra("cart_items")
        if (cartItemsJson != null) {
            val type = object : TypeToken<MutableList<Product>>() {}.type
            cartItems = Gson().fromJson(cartItemsJson, type)
        }

        productCartAdapter = ProductCartAdapter(cartItems, this)
        recyclerView.adapter = productCartAdapter

        val placeOrderBtn = findViewById<Button>(R.id.placeOrderButton)

        placeOrderBtn.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Order")
                .setMessage("Are you sure you want to place the order?")
                .setPositiveButton("Yes") { _, _ ->
                    Log.d("CartActivity", "Order Placed")
                    val toast = Toast.makeText(this, "Order placed successfully.", Toast.LENGTH_SHORT)
                    toast.show()
                    clearCart()
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.show()
        }
    }

    override fun onItemClick(product: Product) {
        Log.d("CartActivity", "Clicked on product: ${product.name}")
    }

    private fun clearCart() {
        cartItems.clear()
        productCartAdapter.notifyDataSetChanged()

        val sharedPreferences = getSharedPreferences("CartData", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_cart_cleared", true).apply()

        Log.d("CartActivity", "Cart cleared")
    }
}