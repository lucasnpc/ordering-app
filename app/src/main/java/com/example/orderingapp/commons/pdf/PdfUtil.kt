package com.example.orderingapp.commons.pdf

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ComponentActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.orderingapp.BuildConfig
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.brazilianCurrencyFormat
import com.example.orderingapp.main.domain.model.Order
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.VisibleForTesting

object PdfUtil {

    private const val pageHeight = 1120
    private const val pageWidth = 792
    private const val txtSize = 16F
    private const val appType = "application/pdf"

    fun ComponentActivity.generatePDF(
        order: Order
    ) {
        lifecycleScope.launch {
            val doc = createPDFDocument(order)
            writeDocument(doc, order)
        }
    }

    fun Activity.openPDF(order: Order) {
        try {
            val file = File(
                Environment.getExternalStorageDirectory().path,
                getString(
                    R.string.documents_path,
                    order.id
                )
            )
            if (!file.exists()) {
                throw FileNotFoundException()
            }

            val uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = appType
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(shareIntent)
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show()
        }
    }


    @VisibleForTesting
    fun Activity.createPDFDocument(
        order: Order,
        titlePaint: Paint = Paint(),
        contentPaint: Paint = Paint(),
        doc: PdfDocument = PdfDocument(),
        pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1)
            .create()
    ): PdfDocument {
        val pageCenter = (pageWidth / 2).toFloat()
        val title = titlePaint.apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = txtSize
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
        }
        val content = contentPaint.apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = txtSize
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
        }
        val myPage: PdfDocument.Page = doc.startPage(pageInfo)
        val canvas = myPage.canvas

        canvas.apply {
            var height = 80F
            drawText(getString(R.string.payment_voucher), pageCenter, height, title)
            height += 40
            drawText("${order.date}, ${order.hour}", pageCenter, height, content)
            height += 40
            drawText(getString(R.string.order_label), pageCenter, height, title)
            order.items.values.forEach { item ->
                height += 20f
                this.drawText(
                    getString(
                        R.string.document_item_info,
                        item.description,
                        item.finalQuantity.toString(),
                        item.currentValue.brazilianCurrencyFormat()
                    ),
                    pageCenter,
                    height,
                    content
                )
            }
            height += 40
            drawText(
                getString(
                    R.string.document_total_info,
                    order.items.values.sumOf { it.finalQuantity * it.currentValue }
                        .brazilianCurrencyFormat()
                ),
                pageCenter,
                height,
                title
            )
            height += 20
            drawText(
                getString(R.string.document_payment_info, order.paymentWay.uppercase()),
                pageCenter,
                height,
                title
            )
        }

        doc.finishPage(myPage)

        return doc
    }

    @VisibleForTesting
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
}