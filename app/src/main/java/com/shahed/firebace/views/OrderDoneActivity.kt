package com.shahed.firebace.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.shahed.firebace.QR.QRCodeGen
import com.shahed.firebace.R
import com.shahed.firebace.network.model.Event
import com.shahed.firebace.network.model.Order

class OrderDoneActivity : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var qrs: ImageView
    var order: Order?? = null
    val qr = QRCodeGen()
    private var permissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_done)
        order = intent.getSerializableExtra("Order") as? Order
        title = findViewById(R.id.title)
        qrs = findViewById(R.id.qr)
        date = findViewById(R.id.date)
        title.text = order?.eventName.toString()
        date.text = order?.eventDate.toString()
        context = this
        object {
            val id = order?.id
            val userID = order?.userID
            val eventName = order?.eventName
            val date = order?.date
            val barcode = order?.barcode
            val eventDate = order?.eventDate
            val eventID = order?.eventID

        }

        try {
            generateQRCode()
        } catch (e: Exception) {
        }

    }

    @SuppressLint("SetTextI18n")
    private fun generateQRCode() {

        val bitmap = qr.encodeAsBitmap(
            "ID: ${order?.id}\nNAME: ${order?.eventName}\nDATE: ${order?.date}",
            400,
            400,
            context
        )
        qrs.setImageBitmap(bitmap)
        /*
            The code below will display the encoded credentials to a textView but in your production project do not show them.
         */
        //  encode_text.text = "Encoded credentials\n\nID: ${ID}\nNAME: ${NAME}\nCLOUD: ${CLOUD}\nLIBRARY: $LIBRARY\nDEVELOPER: $DEVELOPER"
    }


}