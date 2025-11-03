package com.example.levelupgamer.ui.screens.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupgamer.data.model.CartItem
import com.example.levelupgamer.ui.screens.products.components.toPrice

@Composable
fun CartItemCard(
    item: CartItem,
    onUpdateQuantity: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imagen),
            contentDescription = item.nombre,
            modifier = Modifier
                .size(80.dp)
                .weight(1f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 16.dp)
        ) {
            Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                item.precio.toPrice(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(2f)
        ) {
            IconButton(onClick = { onUpdateQuantity(item.quantity - 1) }) {
                Icon(Icons.Default.Remove, contentDescription = "Restar")
            }
            Text(
                "${item.quantity}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = { onUpdateQuantity(item.quantity + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Sumar")
            }
        }
    }
}