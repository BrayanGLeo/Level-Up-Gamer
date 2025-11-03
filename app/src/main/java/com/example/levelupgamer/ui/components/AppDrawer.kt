package com.example.levelupgamer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.navigation.Screen

data class DrawerItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

private val drawerItems = listOf(
    DrawerItem(Screen.ProductList.route, "Inicio", Icons.Default.Home),
    DrawerItem(Screen.Cart.route, "Carro", Icons.Default.ShoppingCart),
    DrawerItem(Screen.Profile.route, "Perfil", Icons.Default.AccountCircle),
    DrawerItem(Screen.Orders.route, "Mis Pedidos", Icons.AutoMirrored.Filled.List)
)

@Composable
fun AppDrawer(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Level-Up Gamer", style = MaterialTheme.typography.titleLarge)
        }
        Divider()
        Spacer(Modifier.height(12.dp))
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    onNavigate(item.route)
                    closeDrawer()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}