package com.example.levelupgamer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.R
import com.example.levelupgamer.navigation.Screen

private val drawerItems = listOf(
    Screen.Home,
    Screen.ProductList,
    Screen.Blog,
    Screen.Cart,
    Screen.Login
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
            val (titleRes, icon) = getScreenMetadata(item)
            val title = stringResource(id = titleRes)

            NavigationDrawerItem(
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(title) },
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

@Composable
private fun getScreenMetadata(screen: Screen): Pair<Int, ImageVector> {
    return when (screen) {
        Screen.Home -> Pair(R.string.screen_home, Icons.Default.Home)
        Screen.ProductList -> Pair(R.string.screen_products, Icons.Default.List)
        Screen.Blog -> Pair(R.string.screen_blog, Icons.Default.Article) // <-- AÑADIDO
        Screen.Cart -> Pair(R.string.screen_cart, Icons.Default.ShoppingCart)
        Screen.Login -> Pair(R.string.screen_login, Icons.Default.AccountCircle)
        Screen.Register -> Pair(R.string.screen_login, Icons.Default.AccountCircle)
    }
}