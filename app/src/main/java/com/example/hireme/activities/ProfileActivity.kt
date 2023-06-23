package com.example.hireme.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.hireme.R
import com.example.hireme.ReadFeedback
import com.example.hireme.databinding.ActivityProfileBinding
import com.example.hireme.models.FeedbackModel
import com.google.firebase.database.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnRateNow : Button
    private lateinit var btnSeeAll : Button
    private lateinit var feedback: TextView

    private var recordId: String? = null

    private lateinit var rvList : ArrayList<FeedbackModel>
    private lateinit var firebase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        feedback = findViewById(R.id.feedback)

        btnRateNow = findViewById(R.id.button_1)
        btnSeeAll = findViewById(R.id.button_see)

        btnRateNow.setOnClickListener {
            val intent = Intent(this, AddReview::class.java)
            startActivity(intent)
        }
        btnSeeAll.setOnClickListener{
            val intent = Intent(this, ReadFeedback::class.java)
            startActivity(intent)
        }

        firebase = FirebaseDatabase.getInstance().getReference("Reviews")

        rvList = arrayListOf()

        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val lastChild = dataSnapshot.children.last() // get the last child node
                    recordId = lastChild.key // store the record ID as a class-level variable
                    val feedbackText = lastChild.child("tvContent").value?.toString()

                    feedback.text = feedbackText
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
