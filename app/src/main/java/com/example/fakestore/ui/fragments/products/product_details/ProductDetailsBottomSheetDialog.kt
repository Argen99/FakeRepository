package com.example.fakestore.ui.fragments.products.product_details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.model.Product
import com.example.fakestore.R
import com.example.fakestore.core.extensions.loadImageWithGlide
import com.example.fakestore.core.extensions.setDrawableEnd
import com.example.fakestore.databinding.BottomSheetProductDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetProductDetailsBinding
    private val viewModel by viewModel<ProductDetailsViewModel>()
    private val navArgs by navArgs<ProductDetailsBottomSheetDialogArgs>()
    private var currentProduct: Product? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProductById(navArgs.productId).collect { product ->
                currentProduct = product
                setData(product)
            }
        }

        binding.btnAddToCart.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                currentProduct?.let { product ->
                    if (viewModel.itemIsAddedToCart(product.id)) {
                        findNavController().navigate(ProductDetailsBottomSheetDialogDirections.actionProductDetailsBottomSheetDialogToCartFragment())
                    } else {
                        viewModel.insertCartItem(product)
                        binding.btnAddToCart.apply {
                            text = "Go to cart"
                            setDrawableEnd(requireContext(), R.drawable.ic_cart_go)
                        }
                    }
                }
            }
        }
    }

    private fun setData(product: Product): Unit = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.itemIsAddedToCart(product.id)) {
                btnAddToCart.text = "Go to cart"
                btnAddToCart.setDrawableEnd(requireContext(), R.drawable.ic_cart_go)
            }
        }
        ivProductImage.loadImageWithGlide(product.image)
        tvTitle.text = product.title
        tvCategory.text = product.category
        ratingBar.rating = product.rating.rate.toFloat()
        tvPrice.text = product.price.toString()
        tvDescription.text = product.description
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}