package edu.fullerton.bookishh.api

import com.auth0.android.jwt.JWT

data class User(val idToken: String? = null) {
    var id = ""
    var name = ""
    var email = ""

    init {
        try {
            // Attempt to decode the ID token.
            val jwt = JWT(idToken ?: "")

            // The ID token is a valid JWT,
            // so extract information about the user from it.
            id = jwt.subject ?: ""
            name = jwt.getClaim("name").asString() ?: ""
            email = jwt.getClaim("email").asString() ?: ""

        } catch (e: com.auth0.android.jwt.DecodeException) {
            // The ID token is NOT a valid JWT,
            // so leave the user properties as empty strings.
        }
    }
}