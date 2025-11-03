package com.example.levelupgamer.ui.screens.cart

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamer.ui.screens.cart.components.CartItemCard
import com.example.levelupgamer.ui.screens.products.components.toPrice
import kotlinx.coroutines.launch
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onCheckoutSuccess: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val total by viewModel.cartTotal.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar("Error: ${uiState.error}")
        }
        if (uiState.saleSuccess) {
            Toast.makeText(context, "¡Venta registrada con éxito!", Toast.LENGTH_LONG).show()
            onCheckoutSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartBottomBar(
                    total = total,
                    isLoading = uiState.isLoading,
                    onCheckout = {
                        viewModel.checkout("cliente@duoc.cl")
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (cartItems.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carro está vacío.", fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cartItems, key = { it.codigo }) { item ->

                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.StartToEnd || dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.removeFromCart(item.codigo)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Producto eliminado")
                                    }
                                    return@rememberSwipeToDismissBoxState true
                                }
                                false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = true,
                            enableDismissFromEndToStart = true,
                            backgroundContent = {
                                val color by animateColorAsState(
                                    targetValue = when (dismissState.targetValue) {
                                        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.errorContainer
                                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                        SwipeToDismissBoxValue.Settled -> Color.Transparent
                                    }, label = "background color"
                                )
                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f, label = "scale"
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            }
                        ) {
                            CartItemCard(
                                item = item,
                                onUpdateQuantity = { newQuantity ->
                                    viewModel.updateQuantity(item.codigo, newQuantity)
                                }
                            )
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun CartBottomBar(total: Int, isLoading: Boolean, onCheckout: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Text(
                    total.toPrice(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Enviar Venta (Checkout)", fontSize = 16.sp)
                }
            }
        }
    }
}