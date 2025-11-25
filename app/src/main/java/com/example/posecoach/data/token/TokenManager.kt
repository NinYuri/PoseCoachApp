package com.example.posecoach.data.token

object TokenManager {
    var accessToken: String = ""
    var refreshToken: String = ""

    fun updateTokens(access: String, refresh: String) {
        accessToken = access
        refreshToken = refresh
    }

    fun clearTokens() {
        accessToken = ""
        refreshToken = ""
    }

    fun hasValidToken(): Boolean {
        return accessToken.isNotEmpty()
    }
}