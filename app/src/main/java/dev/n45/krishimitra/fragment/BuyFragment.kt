package dev.n45.krishimitra.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import dev.n45.krishimitra.CartActivity
import dev.n45.krishimitra.adapter.ProductAdapter
import dev.n45.krishimitra.R
import dev.n45.krishimitra.api.Client
import dev.n45.krishimitra.api.data.Product
import kotlinx.coroutines.launch

private lateinit var productAdapter: ProductAdapter
private lateinit var recyclerView: RecyclerView
private lateinit var cartPriceText: TextView
private var cartPrice: Double = 0.0
private var cartItems: MutableList<Product> = mutableListOf()

class BuyFragment : Fragment(), ProductAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cartPriceText = view.findViewById(R.id.cartPrice)

        val cartButton = view.findViewById<TextView>(R.id.viewCart)
        val clearCartButton = view.findViewById<TextView>(R.id.clearCart)

        cartButton.setOnClickListener {
            openCartActivity()
        }

        clearCartButton.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder.setTitle("Cart")
                .setMessage("Are you sure you want to clear the cart?")
                .setPositiveButton("Yes") { _, _ ->
                    cartPrice = 0.0
                    cartPriceText.text = "Empty"
                    cartItems.clear()
                    Log.d("BuyFragment", "Cart cleared")
                    true
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.show()
        }

        lifecycleScope.launch {
            fetchProducts()
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = requireActivity().getSharedPreferences("CartData", AppCompatActivity.MODE_PRIVATE)
        val isCartCleared = sharedPreferences.getBoolean("is_cart_cleared", false)

        if (isCartCleared) {
            cartPrice = 0.0
            cartPriceText.text = "Empty"
            cartItems.clear()

            // Reset the flag
            sharedPreferences.edit().putBoolean("is_cart_cleared", false).apply()

            Log.d("BuyFragment", "Cart cleared from CartActivity")
        }
    }

    private suspend fun fetchProducts() {
        try {
            val sharedPreferences = requireActivity().getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
            val token = sharedPreferences.getString("access_token", null)
            val products = Client.apiService.getProducts("Bearer $token")
            productAdapter = ProductAdapter(products, this@BuyFragment)
            recyclerView.adapter = productAdapter
        } catch (e: Exception) {
            Log.e("BuyFragment", "Error fetching products: ${e.message}")
        }
    }

    override fun onItemClick(product: Product) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Cart")
            .setMessage("Add this item to cart?")
            .setPositiveButton("Yes") { _, _ ->
                addToCart(product)
                true
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

    fun addToCart(product: Product) {
        cartPrice += product.price.toDouble()
        cartPriceText.text = ("₹$cartPrice").toString()
        cartItems.add(product)
        Log.d("BuyFragment", "Product added to cart: ${product.title}")
        Log.d("BuyFragment", "Cart items: $cartItems")
    }

    fun openCartActivity() {
        val intent = Intent(requireContext(), CartActivity::class.java)
        val gson = Gson()
        val cartItemsJson = gson.toJson(cartItems) // Serialize cart items
        intent.putExtra("cart_items", cartItemsJson)
        startActivity(intent)
    }
}