package com.example.hireme.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.example.hireme.R
import com.example.hireme.ReadFeedback
import com.example.hireme.models.FeedbackModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddReview : AppCompatActivity() {

    private lateinit var etRvContent : EditText
    private lateinit var btnSubmit : Button
    private lateinit var ratingBar : RatingBar

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        etRvContent = findViewById(R.id.edtText_1)
        btnSubmit = findViewById(R.id.button_2)

        ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Handle the rating change here
            Log.d("MyApp", "New rating: $rating")
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Reviews")

        btnSubmit.setOnClickListener{
            saveCustomerReviews()
        }

    }

    private fun saveCustomerReviews(){

        val tvContent = etRvContent.text.toString()
        val rating = ratingBar.rating

        if (tvContent.isEmpty()){
            etRvContent.error = "Please enter content"
            return
        }

        val rvId = dbRef.push().key!!

        val feedback = FeedbackModel(rvId, tvContent, rating)

        dbRef.child(rvId).setValue(feedback).addOnCompleteListener{
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
            // create Intent for the target activity
            val intent = Intent(this, ReadFeedback::class.java)
// start the target activity
            startActivity(intent)

        }.addOnFailureListener{err ->
            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

        }

    }
}
