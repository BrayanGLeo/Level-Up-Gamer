package com.example.levelupgamer.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.ui.screens.products.components.ProductCard

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductAdded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
            label = { Text("Buscar producto...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProducts, key = { it.codigo }) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = {
                            viewModel.addToCart(it)
                            onProductAdded()
                        }
                    )
                }
            }
        }
    }
}