package com.example.hireme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class EditFeedback : AppCompatActivity() {

    private lateinit var feed: TextView
    private lateinit var ratingBar: RatingBar
    private var recordId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_feedback)

        // Initialize the TextViews and RatingBar
        feed = findViewById(R.id.efeed)
        ratingBar = findViewById(R.id.eratingBar)

        // Initialize the Buttons
        var submitButton = findViewById<Button>(R.id.ebutton)

        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance().reference

        // Query the database to retrieve the last row of data
        database.child("Reviews").orderByKey().limitToLast(1).get()
            .addOnSuccessListener { dataSnapshot ->
                // Update the values of that row with the new data
                // Get the last child node
                val lastChild = dataSnapshot.children.lastOrNull()
                // Store the record ID as a class-level variable
                recordId = lastChild?.key
                // Get the values of the child nodes and convert them to strings
                val fb = lastChild?.child("tvContent")?.value?.toString()
                val rb = lastChild?.child("rating")?.value?.toString()

                // Set the values of the TextViews and RatingBar
                feed.text = fb
                if (rb != null) {
                    ratingBar.rating = rb.toFloat()
                }

                // Set up the submit button onClick listener
                submitButton.setOnClickListener {
                    // Get the updated input values
                    val feedb = feed.text.toString()
                    val rating = ratingBar.rating.toString()

                    // Update the record with the new values
                    lastChild?.ref?.updateChildren(
                        mapOf(
                            "tvContent" to feedb,
                            "rating" to rating
                        )
                    )

                    // Show a toast message indicating that the record was updated
                    Toast.makeText(
                        this@EditFeedback,
                        "Record updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Finish the activity and return to the previous screen
                    finish()
                }

            }.addOnFailureListener { exception ->
                // Handle any errors that occur
            }
    }
}
