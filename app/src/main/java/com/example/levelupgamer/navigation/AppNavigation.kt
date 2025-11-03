package com.example.levelupgamer.navigation

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.data.remote.RetrofitClient
import com.example.levelupgamer.data.repository.AuthRepository
import com.example.levelupgamer.data.repository.BlogRepository
import com.example.levelupgamer.data.repository.CartRepository
import com.example.levelupgamer.data.repository.ProductRepository
import com.example.levelupgamer.ui.components.MainScaffold
import com.example.levelupgamer.ui.screens.blog.BlogListScreen
import com.example.levelupgamer.ui.screens.blog.BlogListViewModel
import com.example.levelupgamer.ui.screens.cart.CartScreen
import com.example.levelupgamer.ui.screens.cart.CartViewModel
import com.example.levelupgamer.ui.screens.home.HomeScreen
import com.example.levelupgamer.ui.screens.home.HomeViewModel
import com.example.levelupgamer.ui.screens.login.LoginScreen
import com.example.levelupgamer.ui.screens.login.LoginViewModel
import com.example.levelupgamer.ui.screens.products.ProductListScreen
import com.example.levelupgamer.ui.screens.products.ProductListViewModel
import com.example.levelupgamer.ui.screens.register.RegisterScreen
import com.example.levelupgamer.ui.screens.register.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val cartDao = remember { db.cartDao() }
    val apiService = remember { RetrofitClient.instance }

    val authRepository = remember { AuthRepository() }
    val productRepository = remember { ProductRepository(apiService) }
    val cartRepository = remember { CartRepository(cartDao) }
    val blogRepository = remember { BlogRepository() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    MainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        snackbarHostState = snackbarHostState
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier
        ) {
            composable(Screen.Login.route) {
                val vm: LoginViewModel = viewModel { LoginViewModel(authRepository) }
                LoginScreen(
                    viewModel = vm,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                val vm: RegisterViewModel = viewModel { RegisterViewModel(authRepository) }
                RegisterScreen(
                    viewModel = vm,
                    onRegisterSuccess = {
                        scope.launch {
                            Toast.makeText(context, "¡Registro exitoso! Inicia sesión.", Toast.LENGTH_LONG).show()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel {
                    HomeViewModel(productRepository, blogRepository)
                }
                val cartVm: CartViewModel = viewModel {
                    CartViewModel(cartRepository, apiService)
                }
                HomeScreen(
                    viewModel = vm,
                    onProductClick = { product ->
                        navController.navigate(Screen.ProductList.route)
                    },
                    onAddToCart = { product ->
                        cartVm.addToCart(product)
                        scope.launch {
                            snackbarHostState.showSnackbar("Producto añadido al carro")
                        }
                    },
                    onNavigateToBlog = {
                        navController.navigate(Screen.Blog.route)
                    }
                )
            }

            composable(Screen.Blog.route) {
                val vm: BlogListViewModel = viewModel {
                    BlogListViewModel(blogRepository)
                }
                BlogListScreen(
                    viewModel = vm,
                    onBlogClick = { blogId ->
                    }
                )
            }

            composable(Screen.ProductList.route) {
                val vm: ProductListViewModel = viewModel {
                    ProductListViewModel(productRepository, cartRepository)
                }
                ProductListScreen(
                    viewModel = vm,
                    onProductAdded = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Producto añadido al carro")
                        }
                    }
                )
            }

            composable(Screen.Cart.route) {
                val vm: CartViewModel = viewModel {
                    CartViewModel(cartRepository, apiService)
                }
                CartScreen(
                    viewModel = vm,
                    onCheckoutSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}