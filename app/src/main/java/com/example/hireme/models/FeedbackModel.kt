package com.example.hireme.models

data class FeedbackModel(
    var rvId: String? = null,
    var tvContent: String? = null,
    var rating: Float = 0f
)

data class RatingModel(
    var value: Float = 0f,
    var max: Int = 5
)
