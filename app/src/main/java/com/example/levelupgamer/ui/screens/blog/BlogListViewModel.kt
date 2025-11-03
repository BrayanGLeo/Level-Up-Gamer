package com.example.levelupgamer.ui.screens.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.Blog
import com.example.levelupgamer.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BlogUiState(
    val blogs: List<Blog> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class BlogListViewModel(
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState: StateFlow<BlogUiState> = _uiState.asStateFlow()

    init {
        loadAllBlogPosts()
    }

    private fun loadAllBlogPosts() {
        viewModelScope.launch {
            _uiState.value = BlogUiState(isLoading = true)
            try {
                val blogs = blogRepository.getBlogPosts()
                _uiState.value = BlogUiState(isLoading = false, blogs = blogs)
            } catch (e: Exception) {
                _uiState.value = BlogUiState(isLoading = false, error = e.message)
            }
        }
    }
}