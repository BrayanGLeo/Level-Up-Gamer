package com.example.levelupgamer.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamer.R
import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.model.User
import com.example.levelupgamer.ui.screens.home.components.BlogSummaryCard
import com.example.levelupgamer.ui.screens.products.components.ProductCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onNavigateToBlog: () -> Unit,
    currentUser: User?
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = if (currentUser != null)
                    "👋 ¡Bienvenido, ${currentUser.name}!"
                else
                    "👋 ¡Bienvenido a LevelUp Gamer!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Logo
        item {
            Image(
                painter = painterResource(id = R.drawable.logo_levelup),
                contentDescription = "Logo Level Up Gamer",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "PRODUCTOS DESTACADOS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.error != null) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.featuredProducts) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = { onAddToCart(product) },
                                modifier = Modifier
                                    .width(220.dp)
                                    .clickable { onProductClick(product) }
                            )
                        }
                    }
                }
            }
        }

        // Últimas noticias
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToBlog() }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ÚLTIMAS NOTICIAS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Ver todas las noticias"
                )
            }
        }

        items(uiState.featuredBlogs) { blog ->
            BlogSummaryCard(
                blog = blog,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
