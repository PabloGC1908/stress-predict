package com.pgc.stresspredict.data.model.response

data class PrediccionEstresResponse(
    val nivelEstres: String,  // "Low", "Moderate", "High"
    val mensaje: String
)