package com.example.levelupgamer.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.User
import com.example.levelupgamer.data.repository.CartRepository
import com.example.levelupgamer.data.repository.OrderRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class DeliveryMethod {
    STORE_PICKUP,
    HOME_DELIVERY
}

enum class PaymentMethod {
    TRANSFER,
    WEBPAY
}

data class CheckoutUiState(
    val currentStep: Int = 1,
    // Step 1: User Info
    val name: String = "",
    val lastName: String = "",
    val rut: String = "",
    val email: String = "",
    val phone: String = "",
    // Step 2: Delivery
    val deliveryMethod: DeliveryMethod = DeliveryMethod.STORE_PICKUP, 
    val region: String = "",
    val commune: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val apartment: String = "", 
    val availableCommunes: List<String> = emptyList(),
    // Step 3: Payment
    val paymentMethod: PaymentMethod = PaymentMethod.WEBPAY,
    // General State
    val isLoading: Boolean = false,
    val orderPlacedId: Long? = null,
    val error: String? = null
)

class CheckoutViewModel(
    private val currentUser: User?,
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    // Exponemos los items del carrito y el total para la UI
    val cartItems = cartRepository.cartItems.stateIn(
        viewModelScope, 
        SharingStarted.WhileSubscribed(5000), 
        emptyList()
    )
    
    val cartTotal = cartRepository.cartTotal.stateIn(
        viewModelScope, 
        SharingStarted.WhileSubscribed(5000), 
        0
    )

    val regionsAndCommunes = mapOf(
        "Metropolitana" to listOf("Santiago", "Providencia", "Las Condes", "Maipú", "Puente Alto", "La Florida"),
        "Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Quilpué", "Villa Alemana"),
        "Biobío" to listOf("Concepción", "Talcahuano", "San Pedro de la Paz")
    )
    
    val regions = regionsAndCommunes.keys.toList()

    init {
        currentUser?.let { user ->
            _uiState.update { state ->
                state.copy(
                    name = user.name,
                    // lastName no existe en User, el usuario deberá completarlo
                    email = user.email
                )
            }
        }
    }

    fun onEvent(event: CheckoutEvent) {
        when (event) {
            // Step 1
            is CheckoutEvent.NameChanged -> _uiState.update { it.copy(name = event.name) }
            is CheckoutEvent.LastNameChanged -> _uiState.update { it.copy(lastName = event.lastName) }
            is CheckoutEvent.RutChanged -> _uiState.update { it.copy(rut = event.rut) }
            is CheckoutEvent.EmailChanged -> _uiState.update { it.copy(email = event.email) }
            is CheckoutEvent.PhoneChanged -> _uiState.update { it.copy(phone = event.phone) }
            
            // Step 2
            is CheckoutEvent.DeliveryMethodChanged -> _uiState.update { it.copy(deliveryMethod = event.method) }
            is CheckoutEvent.RegionChanged -> {
                val newCommunes = regionsAndCommunes[event.region] ?: emptyList()
                _uiState.update { 
                    it.copy(
                        region = event.region, 
                        availableCommunes = newCommunes,
                        commune = "" 
                    ) 
                }
            }
            is CheckoutEvent.CommuneChanged -> _uiState.update { it.copy(commune = event.commune) }
            is CheckoutEvent.StreetChanged -> _uiState.update { it.copy(street = event.street) }
            is CheckoutEvent.HouseNumberChanged -> _uiState.update { it.copy(houseNumber = event.number) }
            is CheckoutEvent.ApartmentChanged -> _uiState.update { it.copy(apartment = event.apartment) }
            
            // Step 3
            is CheckoutEvent.PaymentMethodChanged -> _uiState.update { it.copy(paymentMethod = event.method) }

            // Navigation
            is CheckoutEvent.NextStep -> {
                if (validateStep(_uiState.value.currentStep)) {
                     _uiState.update { it.copy(currentStep = it.currentStep + 1) }
                }
            }
            is CheckoutEvent.PreviousStep -> _uiState.update { it.copy(currentStep = it.currentStep - 1) }
            is CheckoutEvent.PlaceOrder -> placeOrder()
        }
    }

    private fun validateStep(step: Int): Boolean {
        val state = _uiState.value
        return when (step) {
            1 -> state.name.isNotBlank() && state.lastName.isNotBlank() && state.rut.isNotBlank() && state.email.isNotBlank() && state.phone.isNotBlank()
            2 -> {
                if (state.deliveryMethod == DeliveryMethod.HOME_DELIVERY) {
                    state.region.isNotBlank() && state.commune.isNotBlank() && state.street.isNotBlank() && state.houseNumber.isNotBlank()
                } else {
                    true 
                }
            }
            else -> true
        }
    }

    private fun placeOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val items = cartItems.value
                val total = cartTotal.value
                
                if (items.isEmpty()) {
                     _uiState.update { it.copy(isLoading = false, error = "El carro está vacío") }
                     return@launch
                }

                val result = orderRepository.placeOrder(
                    user = currentUser,
                    checkoutState = _uiState.value,
                    cartItems = items,
                    total = total
                )
                
                if (result.isSuccess) {
                    cartRepository.clearCart()
                    _uiState.update { it.copy(isLoading = false, orderPlacedId = result.getOrNull()) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error al procesar: ${result.exceptionOrNull()?.message}") }
                }
                
            } catch (e: Exception) {
                 _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

sealed interface CheckoutEvent {
    data class NameChanged(val name: String) : CheckoutEvent
    data class LastNameChanged(val lastName: String) : CheckoutEvent
    data class RutChanged(val rut: String) : CheckoutEvent
    data class EmailChanged(val email: String) : CheckoutEvent
    data class PhoneChanged(val phone: String) : CheckoutEvent

    data class DeliveryMethodChanged(val method: DeliveryMethod) : CheckoutEvent
    data class RegionChanged(val region: String) : CheckoutEvent
    data class CommuneChanged(val commune: String) : CheckoutEvent
    data class StreetChanged(val street: String) : CheckoutEvent
    data class HouseNumberChanged(val number: String) : CheckoutEvent
    data class ApartmentChanged(val apartment: String) : CheckoutEvent

    data class PaymentMethodChanged(val method: PaymentMethod) : CheckoutEvent

    object NextStep : CheckoutEvent
    object PreviousStep : CheckoutEvent
    object PlaceOrder : CheckoutEvent
}