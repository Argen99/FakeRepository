package com.example.fakestore.ui.fragments.products

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fakestore.R
import com.example.fakestore.core.base.BaseFragment
import com.example.fakestore.core.extensions.showShortToast
import com.example.fakestore.databinding.DialogCategoriesBinding
import com.example.fakestore.databinding.FragmentProductsBinding
import com.example.fakestore.ui.MainViewModel
import com.example.fakestore.ui.adapters.ProductAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment :
    BaseFragment<FragmentProductsBinding, ProductsViewModel>(R.layout.fragment_products) {

    override val viewBinding by viewBinding(FragmentProductsBinding::bind)
    override val viewModel by viewModel<ProductsViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(::onItemClick)
    }

    private val categories = mutableListOf<String>()

    override fun initialize(): Unit = with(viewBinding) {
        rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    override fun setupListeners() {
        viewBinding.btnCategory.setOnClickListener {
            setupCategoryDialog()
        }
        viewBinding.btnCart.setOnClickListener {
            findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCartFragment())
        }
    }

    private fun setupCategoryDialog() {
        val binding = DialogCategoriesBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        categories.forEach {
            binding.rgCategories.addView(createRadioButton(it))
        }
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.tvDecline.setOnClickListener {
            dialog.hide()
        }

        binding.tvAccept.setOnClickListener {
            dialog.hide()
        }

        binding.rgCategories.setOnCheckedChangeListener { _, i ->
            binding.root.findViewById<RadioButton>(i)?.let {
                viewModel.filterBy(
                    category = if (it.text.toString()
                            .lowercase() == "all"
                    ) null else it.text.toString().lowercase()
                )
            }
        }
    }

    private fun createRadioButton(category: String): RadioButton {
        val params = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(resources.getDimensionPixelSize(R.dimen.radio_btn_horizontal_margin))
        }
        return RadioButton(requireContext()).apply {
            text = category
            setTextColor(ContextCompat.getColor(context, R.color.black))
            textSize = 16f
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            buttonTintMode = PorterDuff.Mode.MULTIPLY
            layoutParams = params
        }
    }

    override fun launchObservers() {
        viewModel.serverRequestState.spectateUiState(
            error = {
                showShortToast(it)
            },
            completed = {
                mainViewModel.requestCompleted()
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect { products ->
                productAdapter.submitList(products)
            }
        }

        viewModel.categoriesState.spectateUiState(
            success = {
                categories.clear()
                categories.add("all")
                categories.addAll(it)
            },
            error = {
                showShortToast(it)
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterByCategory.collect {
                viewBinding.tvCurrentCategory.text = if (it.isNullOrEmpty()) "all" else it
            }
        }
    }

    private fun onItemClick(id: Int) {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductsFragmentToProductDetailsBottomSheetDialog(id)
        )
    }
}