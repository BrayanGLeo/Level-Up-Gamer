package com.example.levelupgamer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.data.remote.RetrofitClient
import com.example.levelupgamer.data.repository.BlogRepository
import com.example.levelupgamer.data.repository.ProductRepository
import com.example.levelupgamer.ui.screens.blog.BlogListScreen
import com.example.levelupgamer.ui.screens.blog.BlogListViewModel
import com.example.levelupgamer.ui.screens.home.HomeScreen
import com.example.levelupgamer.ui.screens.products.ProductDetailScreen
import com.example.levelupgamer.ui.screens.products.ProductListScreen
import com.example.levelupgamer.ui.screens.products.ProductListViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val productDao = remember { db.productDao() }
    val apiService = remember { RetrofitClient.instance }

    val productRepository = remember { ProductRepository(apiService, productDao) }
    val blogRepository = remember { BlogRepository() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home_screen",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home_screen") {
                HomeScreen(navController = navController)
            }

            composable("blog_screen") {
                val viewModel: BlogListViewModel = viewModel {
                    BlogListViewModel(blogRepository)
                }
                BlogListScreen(
                    viewModel = viewModel,
                    onBlogClick = { blog ->
                    }
                )
            }

            composable(
                route = "product_list_screen/{source}",
                arguments = listOf(navArgument("source") { type = NavType.StringType })
            ) { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: "API"

                val viewModel: ProductListViewModel = viewModel {
                    ProductListViewModel(productRepository)
                }

                ProductListScreen(
                    navController = navController,
                    viewModel = viewModel,
                    sourceType = source
                )
            }

            composable(
                route = "product_detail_screen/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""

                val viewModel: ProductListViewModel = viewModel {
                    ProductListViewModel(productRepository)
                }

                ProductDetailScreen(
                    navController = navController,
                    productId = productId,
                    viewModel = viewModel
                )
            }
        }
    }
}