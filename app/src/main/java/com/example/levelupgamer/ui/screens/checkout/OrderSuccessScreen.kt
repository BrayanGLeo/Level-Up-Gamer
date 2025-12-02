package com.example.levelupgamer.ui.screens.checkout

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.levelupgamer.data.model.User

@Composable
fun OrderSuccessScreen(
    uiState: OrderSuccessUiState,
    currentUser: User?,
    onContinueShopping: () -> Unit,
    onViewOrders: () -> Unit
) {
    val context = LocalContext.current

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Procesando tu boleta...")
            } else if (uiState.error != null) {
                Text(
                    text = "¡Compra realizada!",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Pero hubo un error generando la boleta: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    text = "¡Felicidades!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tu compra ha sido realizada con éxito.\nHemos generado tu boleta electrónica.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        uiState.pdfFile?.let { file ->
                            try {
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    file
                                )
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    enabled = uiState.pdfFile != null
                ) {
                    Text("Ver Boleta (PDF)")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(onClick = onContinueShopping, modifier = Modifier.fillMaxWidth()) {
                Text("Seguir Comprando")
            }
            
            if (currentUser != null) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onViewOrders) {
                    Text("Ver mis Órdenes")
                }
            }
        }
    }
}