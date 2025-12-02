package com.example.levelupgamer.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.levelupgamer.data.model.OrderWithItems
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PdfGenerator(private val context: Context) {

    fun generateOrderPdf(order: OrderWithItems): File? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size approx
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            isFakeBoldText = true
        }
        val bodyPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
        }
        val boldBodyPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            isFakeBoldText = true
        }

        // Header
        canvas.drawText("Boleta Electrónica - Level Up Gamer", 50f, 50f, titlePaint)
        
        // Order Info
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(order.order.date))
        
        var yPos = 100f
        canvas.drawText("Orden #: ${order.order.orderId}", 50f, yPos, boldBodyPaint)
        yPos += 20f
        canvas.drawText("Fecha: $dateStr", 50f, yPos, bodyPaint)
        yPos += 30f
        
        // Customer Info
        canvas.drawText("Cliente: ${order.order.userName} ${order.order.userLastName}", 50f, yPos, bodyPaint)
        yPos += 20f
        canvas.drawText("RUT: ${order.order.userRut}", 50f, yPos, bodyPaint)
        yPos += 20f
        canvas.drawText("Email: ${order.order.userEmail}", 50f, yPos, bodyPaint)
        yPos += 40f
        
        // Items Header
        canvas.drawText("Detalle:", 50f, yPos, titlePaint)
        yPos += 30f
        
        // Items
        order.items.forEach { item ->
            val itemText = "${item.quantity} x ${item.productName}"
            val priceText = (item.productPrice * item.quantity).toPrice()
            
            canvas.drawText(itemText, 50f, yPos, bodyPaint)
            canvas.drawText(priceText, 400f, yPos, bodyPaint)
            yPos += 25f
        }
        
        yPos += 20f
        canvas.drawLine(50f, yPos, 500f, yPos, bodyPaint)
        yPos += 30f
        
        // Total
        canvas.drawText("TOTAL", 50f, yPos, titlePaint)
        canvas.drawText(order.order.totalAmount.toPrice(), 400f, yPos, titlePaint)

        document.finishPage(page)

        // Save file
        val fileName = "boleta_orden_${order.order.orderId}.pdf"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            document.writeTo(FileOutputStream(file))
            document.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            document.close()
            return null
        }
    }
    
    private fun Int.toPrice(): String {
        val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CL"))
        return format.format(this)
    }
}