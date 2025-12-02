package com.example.levelupgamer.ui.screens.checkout

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamer.data.model.User
import com.example.levelupgamer.data.repository.CartRepository
import com.example.levelupgamer.data.repository.OrderRepository
import com.example.levelupgamer.ui.screens.products.components.toPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    currentUser: User?,
    cartRepository: CartRepository,
    orderRepository: OrderRepository,
    onNavigateBack: () -> Unit,
    onOrderPlacedSuccessfully: (Long) -> Unit
) {
    val viewModel: CheckoutViewModel = viewModel { 
        CheckoutViewModel(currentUser, cartRepository, orderRepository) 
    }
    val uiState by viewModel.uiState.collectAsState()
    
    val cartItems by viewModel.cartItems.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()

    LaunchedEffect(uiState.orderPlacedId) {
        uiState.orderPlacedId?.let { orderId ->
            onOrderPlacedSuccessfully(orderId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout - Paso ${uiState.currentStep}/3",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.currentStep > 1) {
                            viewModel.onEvent(CheckoutEvent.PreviousStep)
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedContent(
                targetState = uiState.currentStep,
                label = "Checkout Steps"
            ) { step ->
                when (step) {
                    1 -> Step1UserInfo(uiState, viewModel::onEvent)
                    2 -> Step2DeliveryInfo(uiState, viewModel::onEvent, viewModel.regions)
                    3 -> Step3Summary(
                        uiState = uiState, 
                        onEvent = viewModel::onEvent,
                        cartItems = cartItems,
                        total = cartTotal
                    )
                }
            }
        }
    }
}

@Composable
fun Step1UserInfo(uiState: CheckoutUiState, onEvent: (CheckoutEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Información de Contacto",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = uiState.name,
            onValueChange = { onEvent(CheckoutEvent.NameChanged(it)) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.lastName,
            onValueChange = { onEvent(CheckoutEvent.LastNameChanged(it)) },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.rut,
            onValueChange = { onEvent(CheckoutEvent.RutChanged(it)) },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(CheckoutEvent.EmailChanged(it)) },
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = { onEvent(CheckoutEvent.PhoneChanged(it)) },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { onEvent(CheckoutEvent.NextStep) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Siguiente: Envío")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2DeliveryInfo(
    uiState: CheckoutUiState,
    onEvent: (CheckoutEvent) -> Unit,
    regions: List<String>
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Método de Entrega",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // Selector de Método de Entrega
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = uiState.deliveryMethod == DeliveryMethod.STORE_PICKUP,
                        onClick = { onEvent(CheckoutEvent.DeliveryMethodChanged(DeliveryMethod.STORE_PICKUP)) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = uiState.deliveryMethod == DeliveryMethod.STORE_PICKUP,
                    onClick = null 
                )
                Text(
                    text = "Retiro en Tienda",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = uiState.deliveryMethod == DeliveryMethod.HOME_DELIVERY,
                        onClick = { onEvent(CheckoutEvent.DeliveryMethodChanged(DeliveryMethod.HOME_DELIVERY)) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = uiState.deliveryMethod == DeliveryMethod.HOME_DELIVERY,
                    onClick = null
                )
                Text(
                    text = "Despacho a Domicilio",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        HorizontalDivider()

        if (uiState.deliveryMethod == DeliveryMethod.STORE_PICKUP) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Dirección de la Tienda:", fontWeight = FontWeight.Bold)
                    Text("Av. Siempre Viva 123, Santiago.")
                    Text("Horario: Lun-Vie 09:00 - 18:00")
                }
            }
        } else {
            // Formulario de Despacho
            Text("Dirección de Envío", style = MaterialTheme.typography.titleMedium)

            // Dropdown Región
            var regionExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = regionExpanded,
                onExpandedChange = { regionExpanded = !regionExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    readOnly = true,
                    value = uiState.region,
                    onValueChange = {},
                    label = { Text("Región") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    regions.forEach { region ->
                        DropdownMenuItem(
                            text = { Text(region) },
                            onClick = {
                                onEvent(CheckoutEvent.RegionChanged(region))
                                regionExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            // Dropdown Comuna
            var communeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = communeExpanded,
                onExpandedChange = { communeExpanded = !communeExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    readOnly = true,
                    value = uiState.commune,
                    onValueChange = {},
                    label = { Text("Comuna") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) },
                    enabled = uiState.region.isNotEmpty(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = communeExpanded,
                    onDismissRequest = { communeExpanded = false }
                ) {
                    uiState.availableCommunes.forEach { commune ->
                        DropdownMenuItem(
                            text = { Text(commune) },
                            onClick = {
                                onEvent(CheckoutEvent.CommuneChanged(commune))
                                communeExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.street,
                onValueChange = { onEvent(CheckoutEvent.StreetChanged(it)) },
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.houseNumber,
                    onValueChange = { onEvent(CheckoutEvent.HouseNumberChanged(it)) },
                    label = { Text("Número") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.apartment,
                    onValueChange = { onEvent(CheckoutEvent.ApartmentChanged(it)) },
                    label = { Text("Depto (Opcional)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }

        Button(
            onClick = { onEvent(CheckoutEvent.NextStep) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Siguiente: Pago")
        }
    }
}

@Composable
fun Step3Summary(
    uiState: CheckoutUiState,
    onEvent: (CheckoutEvent) -> Unit,
    cartItems: List<com.example.levelupgamer.data.model.CartItem>,
    total: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Resumen y Pago",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // Resumen del pedido REAL
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Resumen de compra", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                
                cartItems.forEach { item ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.quantity}x ${item.nombre}", style = MaterialTheme.typography.bodyMedium)
                        Text((item.precio * item.quantity).toPrice(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total a pagar:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(total.toPrice(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) 
                }
            }
        }

        Text("Selecciona tu medio de pago:", style = MaterialTheme.typography.titleMedium)

        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = uiState.paymentMethod == PaymentMethod.WEBPAY,
                        onClick = { onEvent(CheckoutEvent.PaymentMethodChanged(PaymentMethod.WEBPAY)) },
                        role = Role.RadioButton
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = uiState.paymentMethod == PaymentMethod.WEBPAY, onClick = null)
                Text("WebPay Plus (Débito/Crédito)", Modifier.padding(start = 8.dp))
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = uiState.paymentMethod == PaymentMethod.TRANSFER,
                        onClick = { onEvent(CheckoutEvent.PaymentMethodChanged(PaymentMethod.TRANSFER)) },
                        role = Role.RadioButton
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = uiState.paymentMethod == PaymentMethod.TRANSFER, onClick = null)
                Text("Transferencia Bancaria", Modifier.padding(start = 8.dp))
            }
        }

        Button(
            onClick = { onEvent(CheckoutEvent.PlaceOrder) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Pagar y Finalizar Compra")
            }
        }
    }
}