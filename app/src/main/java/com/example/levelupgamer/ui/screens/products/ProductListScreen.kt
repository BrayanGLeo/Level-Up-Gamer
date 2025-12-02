package com.example.levelupgamer.ui.screens.products

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.levelupgamer.ui.screens.products.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel,
    sourceType: String // "API" o "LOCAL"
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(sourceType) {
        if (sourceType == "API") {
            viewModel.loadFromApi()
        } else {
            viewModel.loadFromDb()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (sourceType == "API") "Productos (API)" else "Productos (Local)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (sourceType == "API" && state.products.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.saveToLocalDb()
                        Toast.makeText(context, "Guardado en Base de Datos Local", Toast.LENGTH_SHORT).show()
                    },
                    icon = { Icon(Icons.Default.Save, contentDescription = null) },
                    text = { Text("Almacenar") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.products.isEmpty()) {
                Text("No se encontraron productos.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(state.products) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = {
                                navController.navigate("product_detail_screen/${product.codigo}")
                            }
                        )
                    }
                }
            }
        }
    }
}