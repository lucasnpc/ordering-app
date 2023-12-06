package com.example.orderingapp.commons.pdf

import android.app.Activity
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.orderingapp.R
import com.example.orderingapp.commons.pdf.PdfUtil.createPDFDocument
import com.example.orderingapp.commons.pdf.PdfUtil.writeDocument
import com.example.orderingapp.main.domain.model.Order
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.io.File
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class PdfUtilTest {
    private lateinit var activity: Activity
    private lateinit var order: Order
    private val mockTitlePaint = mockk<Paint>(relaxed = true)
    private val mockContentPaint = mockk<Paint>(relaxed = true)
    private val mockPdfDocument = mockk<PdfDocument>(relaxed = true)
    private val mockPageInfoBuilder = mockk<PdfDocument.PageInfo>(relaxed = true)

    @Before
    fun setUp() {
        activity = mockk()
        order = mockk()
        mockkStatic(Typeface::class)
        mockkStatic(PdfDocument::class)
        every { Typeface.create(any<Typeface>(), any()) } returns mockk()
        mockkConstructor(PdfDocument.PageInfo.Builder::class)
        every { anyConstructed<PdfDocument.PageInfo.Builder>().create() } returns mockk()
    }

    @After
    fun teardown() {
        unmockkStatic(Typeface::class)
    }

    @Test
    fun createPDFDocument() {
        every { order.date } returns ""
        every { order.hour } returns ""
        every { order.items } returns mapOf()
        every { order.paymentWay } returns ""
        every { activity.getString(R.string.payment_voucher) } returns ""
        every { activity.getString(R.string.order_label) } returns ""
        every { activity.getString(R.string.document_item_info) } returns ""
        every { activity.getString(R.string.document_payment_info, order.paymentWay) } returns ""
        every { activity.getString(R.string.document_total_info, "R\$ 0,00") } returns ""
        val document = activity.createPDFDocument(
            order,
            mockTitlePaint,
            mockContentPaint,
            mockPdfDocument,
            mockPageInfoBuilder
        )

        assertThat(document).isNotNull()
    }

    @Test
    fun writeDocument() = runTest {
        mockkStatic(Environment::class)
        every { Environment.getExternalStorageDirectory() } returns File("/mock/path")
        every { order.id } returns ""
        every { activity.getString(R.string.documents_path, "") } returns ""
        activity.writeDocument(mockPdfDocument, order)
    }
}