package com.example.fakestore.ui.fragments.cart

import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestore.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.model.Product
import com.example.fakestore.core.base.BaseFragment
import com.example.fakestore.core.extensions.setDrawableEnd
import com.example.fakestore.core.extensions.showShortToast
import com.example.fakestore.databinding.FragmentCartBinding
import com.example.fakestore.ui.adapters.CartAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(R.layout.fragment_cart) {

    override val viewBinding by viewBinding(FragmentCartBinding::bind)
    override val viewModel by viewModel<CartViewModel>()
    private val cartAdapter: CartAdapter by lazy {
        CartAdapter(::onItemClick)
    }

    override fun initialize() {
        viewBinding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    override fun setupListeners() {
        viewBinding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        viewBinding.btnBuy.setOnClickListener {
            if (cartAdapter.currentList.isEmpty()) {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToProductsFragment())
            } else {
                showShortToast(cartAdapter.currentList.joinToString())
                viewModel.clearCart()
            }
        }
    }

    override fun launchObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartState.collect {
                setupButton(it)
                cartAdapter.submitList(it)
            }
        }
    }

    private fun setupButton(it: List<Product>) {
        if (it.isEmpty()) {
            viewBinding.btnBuy.apply {
                text = "Add something"
                setDrawableEnd(requireContext(), R.drawable.ic_basket_empty)
            }
        } else {
            viewBinding.btnBuy.apply {
                text = "buy"
                setDrawableEnd(requireContext(), R.drawable.ic_basket_fill)
            }
        }
    }

    private fun onItemClick(product: Product) {
        viewModel.deleteCartItem(product)
    }
}