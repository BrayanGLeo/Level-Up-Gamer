package com.example.levelupgamer.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.Blog
import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.repository.BlogRepository
import com.example.levelupgamer.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val featuredProducts: List<Product> = emptyList(),
    val featuredBlogs: List<Blog> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFeaturedData()
    }

    private fun loadFeaturedData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            try {
                val products = productRepository.getProducts().take(4)
                val blogs = blogRepository.getBlogPosts().take(2)

                _uiState.value = HomeUiState(
                    isLoading = false,
                    featuredProducts = products,
                    featuredBlogs = blogs
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(isLoading = false, error = e.message)
            }
        }
    }
}