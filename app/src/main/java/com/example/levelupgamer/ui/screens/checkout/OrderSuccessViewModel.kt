package com.example.levelupgamer.ui.screens.checkout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.OrderWithItems
import com.example.levelupgamer.data.repository.OrderRepository
import com.example.levelupgamer.utils.PdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class OrderSuccessUiState(
    val isLoading: Boolean = true,
    val order: OrderWithItems? = null,
    val pdfFile: File? = null,
    val error: String? = null
)

class OrderSuccessViewModel(
    private val orderId: Long,
    private val orderRepository: OrderRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderSuccessUiState())
    val uiState: StateFlow<OrderSuccessUiState> = _uiState.asStateFlow()

    init {
        loadOrderAndGeneratePdf()
    }

    private fun loadOrderAndGeneratePdf() {
        viewModelScope.launch {
            orderRepository.getOrderById(orderId).collect { order ->
                if (order != null) {
                    // Ejecutamos la generacion de PDF en un hilo IO para no bloquear UI
                    val generator = PdfGenerator(context)
                    val pdf = generator.generateOrderPdf(order)
                    _uiState.value = OrderSuccessUiState(
                        isLoading = false,
                        order = order,
                        pdfFile = pdf
                    )
                } else {
                     _uiState.value = OrderSuccessUiState(isLoading = false, error = "Orden no encontrada")
                }
            }
        }
    }
}