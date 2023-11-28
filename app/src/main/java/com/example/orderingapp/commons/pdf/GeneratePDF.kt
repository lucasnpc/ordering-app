package com.example.orderingapp.commons.pdf

import android.app.Activity
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.Order
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Activity.createPDFDocument(order: Order): PdfDocument {
    val pageHeight = 1120
    val pagewidth = 792
    val doc = PdfDocument()
    val title = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 16F
        color = android.graphics.Color.BLACK
        textAlign = Paint.Align.CENTER
    }
    val text = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textSize = 16F
        color = android.graphics.Color.BLACK
        textAlign = Paint.Align.CENTER
    }
    val mypageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
    val myPage: PdfDocument.Page = doc.startPage(mypageInfo)
    val canvas = myPage.canvas

    canvas.apply {
        drawText(getString(R.string.payment_voucher), 396F, 80F, title)
        drawText("${order.date} ${order.hour}", 396F, 120F, text)
        drawText("PEDIDO: ", 396F, 160F, title)
        var height = 160F
        order.items.forEach { item ->
            height += 20f
            this.drawText(
                "Item: ${item.description} Qtd. ${item.finalQuantity}",
                396F,
                height,
                text
            )
        }
        height += 40
        drawText(
            "VALOR: ${
                order.items.sumOf { it.finalQuantity * it.currentValue }.currencyFormat()
            }", 396F, height, title
        )
        drawText("PAGAMENTO: ${order.paymentWay}", 396F, height + 20, title)
    }

    doc.finishPage(myPage)

    return doc
}

suspend fun Activity.writeDocument(pdfDocument: PdfDocument, order: Order) {
    try {
        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(
                FileOutputStream(
                    File(
                        Environment.getExternalStorageDirectory().path,
                        getString(
                            R.string.documents_path,
                            order.id
                        )
                    )
                )
            )
        }
        Toast.makeText(
            this,
            getString(R.string.voucher_saved),
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    pdfDocument.close()
}