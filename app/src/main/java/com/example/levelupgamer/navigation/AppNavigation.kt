package com.example.levelupgamer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.example.levelupgamer.data.repository.CartRepository
import com.example.levelupgamer.data.repository.ProductRepository
import com.example.levelupgamer.ui.components.MainScaffold
import com.example.levelupgamer.ui.screens.cart.CartScreen
import com.example.levelupgamer.ui.screens.cart.CartViewModel
import com.example.levelupgamer.ui.screens.login.LoginScreen
import com.example.levelupgamer.ui.screens.login.LoginViewModel
import com.example.levelupgamer.ui.screens.products.ProductListScreen
import com.example.levelupgamer.ui.screens.products.ProductListViewModel
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Login.route

    MainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        snackbarHostState = snackbarHostState
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = modifier
        ) {
            composable(Screen.Login.route) {
                val vm: LoginViewModel = viewModel { LoginViewModel(authRepository) }
                LoginScreen(
                    viewModel = vm,
                    onLoginSuccess = {
                        navController.navigate(Screen.ProductList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
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

            composable(Screen.Home.route) {
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
                        navController.navigate(Screen.ProductList.route) {
                            popUpTo(Screen.ProductList.route) { inclusive = true }
                        }
                    }
                )
            }

            // TODO: Crear las pantallas de Perfil y Mis Pedidos
            composable(Screen.Profile.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Perfil (en construcción)")
                }
            }
            composable(Screen.Orders.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Pedidos (en construcción)")
                }
            }
        }
    }
}