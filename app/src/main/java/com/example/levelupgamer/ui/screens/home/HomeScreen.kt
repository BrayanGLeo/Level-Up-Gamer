package com.example.levelupgamer.ui.screens.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelupgamer.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp),
            onClick = {
                if (isInternetAvailable(context)) {
                    navController.navigate("product_list_screen/API")
                } else {
                    Toast.makeText(context, "Sin conexión a Internet", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Cargar desde Rest API")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp),
            onClick = {
                navController.navigate("product_list_screen/LOCAL")
            }
        ) {
            Text("Cargar desde Base de Datos Local")
        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}