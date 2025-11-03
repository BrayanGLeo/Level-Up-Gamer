package com.example.levelupgamer.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.levelupgamer.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    currentRoute: String,
    snackbarHostState: SnackbarHostState,
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val noScaffoldRoutes = listOf(
        Screen.Login.route,
        Screen.Register.route
    )

    if (currentRoute in noScaffoldRoutes) {
        content(Modifier)
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    closeDrawer = { scope.launch { drawerState.close() } }
                )
            }
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Text("LEVEL-UP GAMER", fontWeight = FontWeight.Bold) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.primary
                        ),
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, "Menú")
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                                Icon(Icons.Default.ShoppingCart, "Carro")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                content(Modifier.padding(paddingValues))
            }
        }
    }
}