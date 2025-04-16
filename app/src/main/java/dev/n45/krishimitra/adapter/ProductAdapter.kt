package dev.n45.krishimitra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.n45.krishimitra.R
import dev.n45.krishimitra.api.data.Product

class ProductAdapter(private val products: List<Product>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val productTitleText: TextView = itemView.findViewById(R.id.productTitle)
        private val productPriceText: TextView = itemView.findViewById(R.id.productPrice)
        private val productDescriptionText: TextView = itemView.findViewById(R.id.ProductDescription)
        private val addBtn = itemView.findViewById<TextView>(R.id.addButton)

        init {
            addBtn.setOnClickListener(this)
        }

        fun bind(product: Product) {
            productTitleText.text = product.title
            productPriceText.text = "₹${product.price} per kg"
            productDescriptionText.text = product.description
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(products[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = products[position]
        holder.bind(currentProduct)
    }

    override fun getItemCount() = products.size
}