package com.example.levelupgamer.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupgamer.data.AuthManager
import com.example.levelupgamer.data.local.AppDatabase
import com.example.levelupgamer.data.remote.RetrofitClient
import com.example.levelupgamer.data.repository.AuthRepository
import com.example.levelupgamer.data.repository.BlogRepository
import com.example.levelupgamer.data.repository.CartRepository
import com.example.levelupgamer.data.repository.OrderRepository
import com.example.levelupgamer.data.repository.ProductRepository
import com.example.levelupgamer.ui.components.MainScaffold
import com.example.levelupgamer.ui.screens.blog.BlogListScreen
import com.example.levelupgamer.ui.screens.blog.BlogListViewModel
import com.example.levelupgamer.ui.screens.cart.CartScreen
import com.example.levelupgamer.ui.screens.cart.CartViewModel
import com.example.levelupgamer.ui.screens.checkout.CheckoutScreen
import com.example.levelupgamer.ui.screens.checkout.CheckoutViewModel
import com.example.levelupgamer.ui.screens.checkout.OrderSuccessScreen
import com.example.levelupgamer.ui.screens.checkout.OrderSuccessViewModel
import com.example.levelupgamer.ui.screens.home.HomeScreen
import com.example.levelupgamer.ui.screens.home.HomeViewModel
import com.example.levelupgamer.ui.screens.login.LoginScreen
import com.example.levelupgamer.ui.screens.login.LoginViewModel
import com.example.levelupgamer.ui.screens.products.ProductListScreen
import com.example.levelupgamer.ui.screens.products.ProductListViewModel
import com.example.levelupgamer.ui.screens.profile.ProfileScreen
import com.example.levelupgamer.ui.screens.register.RegisterScreen
import com.example.levelupgamer.ui.screens.register.RegisterViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val gson = remember { Gson() }
    val authManager = remember { AuthManager(context, gson) }
    var currentUser by remember { mutableStateOf(authManager.loadUser()) }

    val db = remember { AppDatabase.getDatabase(context) }
    val cartDao = remember { db.cartDao() }
    val orderDao = remember { db.orderDao() }
    val apiService = remember { RetrofitClient.instance }
    val authRepository = remember { AuthRepository() }
    val productRepository = remember { ProductRepository(apiService) }
    val cartRepository = remember { CartRepository(cartDao) }
    val orderRepository = remember { OrderRepository(orderDao, apiService) }
    val blogRepository = remember { BlogRepository() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    MainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        snackbarHostState = snackbarHostState,
        currentUser = currentUser
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier
        ) {
            composable(Screen.Login.route) {
                val vm: LoginViewModel = viewModel { LoginViewModel(authRepository) }
                val uiState by vm.uiState.collectAsState()

                LaunchedEffect(uiState.loginSuccessUser) {
                    uiState.loginSuccessUser?.let { user ->
                        authManager.saveUser(user)
                        currentUser = user

                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }

                LoginScreen(
                    viewModel = vm,
                    onLoginSuccess = { },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                val vm: RegisterViewModel = viewModel { RegisterViewModel(authRepository) }
                val uiState by vm.uiState.collectAsState()

                LaunchedEffect(uiState.registerSuccessUser) {
                    if (uiState.registerSuccessUser != null) {
                        scope.launch {
                            Toast.makeText(context, "¡Registro exitoso! Inicia sesión.", Toast.LENGTH_LONG).show()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                    }
                }

                RegisterScreen(
                    viewModel = vm,
                    onRegisterSuccess = { },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Profile.route) {
                if (currentUser != null) {
                    ProfileScreen(
                        user = currentUser!!,
                        onNavigate = { route ->
                            navController.navigate(route)
                        },
                        onLogout = {
                            authManager.clearUser()
                            currentUser = null
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            }

            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel { HomeViewModel(productRepository, blogRepository) }
                val cartVm: CartViewModel = viewModel { CartViewModel(cartRepository, apiService) }
                HomeScreen(
                    viewModel = vm,
                    onProductClick = { navController.navigate(Screen.ProductList.route) },
                    onAddToCart = { product ->
                        cartVm.addToCart(product)
                        scope.launch { snackbarHostState.showSnackbar("Producto añadido al carro") }
                    },
                    onNavigateToBlog = { navController.navigate(Screen.Blog.route) },
                    currentUser = currentUser
                )
            }

            composable(Screen.Blog.route) {
                val vm: BlogListViewModel = viewModel { BlogListViewModel(blogRepository) }
                BlogListScreen(viewModel = vm, onBlogClick = { /* ... */ })
            }

            composable(Screen.ProductList.route) {
                val vm: ProductListViewModel = viewModel { ProductListViewModel(productRepository, cartRepository) }
                ProductListScreen(
                    viewModel = vm,
                    onProductAdded = {
                        scope.launch { snackbarHostState.showSnackbar("Producto añadido al carro") }
                    }
                )
            }

            composable(Screen.Cart.route) {
                val vm: CartViewModel = viewModel { CartViewModel(cartRepository, apiService) }
                CartScreen(
                    viewModel = vm,
                    currentUser = currentUser,
                    onNavigateToCheckout = {
                        navController.navigate(Screen.Checkout.route)
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Checkout.route) {
                 CheckoutScreen(
                     currentUser = currentUser,
                     cartRepository = cartRepository,
                     orderRepository = orderRepository,
                     onNavigateBack = { navController.popBackStack() },
                     onOrderPlacedSuccessfully = { orderId ->
                         navController.navigate(Screen.OrderSuccess.createRoute(orderId)) {
                             popUpTo(Screen.Cart.route) { inclusive = true }
                         }
                     }
                 )
            }
            
            composable(
                route = Screen.OrderSuccess.route,
                arguments = listOf(navArgument("orderId") { type = NavType.LongType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
                val vm: OrderSuccessViewModel = viewModel { 
                    OrderSuccessViewModel(orderId, orderRepository, context) 
                }
                val uiState by vm.uiState.collectAsState()

                OrderSuccessScreen(
                    uiState = uiState,
                    currentUser = currentUser,
                    onContinueShopping = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onViewOrders = {
                        navController.navigate(Screen.Orders.route)
                    }
                )
            }

            composable(Screen.Orders.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Pedidos (en construcción)")
                }
            }
            composable(Screen.Addresses.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Direcciones (en construcción)")
                }
            }
            composable(Screen.Settings.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Configuración (en construcción)")
                }
            }
        }
    }
}