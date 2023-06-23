package com.example.hireme

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.hireme.activities.ProfileActivity
import com.example.hireme.databinding.ActivityReadFeedbackBinding
import com.google.firebase.database.*

class ReadFeedback : AppCompatActivity() {

    private lateinit var binding: ActivityReadFeedbackBinding
    private lateinit var feedbacks: TextView
    private var recordId: String? = null // initialize to null
    private var ratingValue: Float = 0f // initialize to 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        feedbacks = binding.lastfeedback
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Reviews").limitToLast(1)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lastchild = dataSnapshot.children.lastOrNull() // get the last child node or null if empty
                recordId = lastchild?.key // store the record ID as a class-level variable
                val fb = lastchild?.child("tvContent")?.value?.toString()
                ratingValue = lastchild?.child("rating")?.getValue(Float::class.java) ?: 0f
                ratingBar.rating = ratingValue
                feedbacks.text = fb ?: "No feedback available"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        val deleteButton: Button = findViewById(R.id.Delete)
        deleteButton.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Reviews")
            val recordReference = databaseReference.child(recordId ?: "")

            Log.d("DeleteFeedback", "Deleting record with ID: $recordId")

            // Remove the record from Firebase
            recordReference.removeValue()

            // Show a toast message indicating that the record was deleted
            Toast.makeText(
                this@ReadFeedback,
                "Record Deleted successfully",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this@ReadFeedback, ProfileActivity::class.java)
            startActivity(intent)
        }

        val editButton: Button = findViewById(R.id.Edit)
        editButton.setOnClickListener {
            val intent = Intent(this@ReadFeedback, EditFeedback::class.java)
            intent.putExtra("recordId", recordId)
            startActivity(intent)
        }
    }
}
