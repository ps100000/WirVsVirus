package com.wirvsvirus.homealonechallenge


fun ByteArray.toHexString() : String {
    return this.joinToString("") {
        String.format("%02x", it)
    }
}