package com.wirvsvirus.homealonechallenge.rest

import org.springframework.web.bind.annotation.*
import java.awt.SystemColor.text
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.websocket.server.PathParam


data class UserCreate(
    val username: String,
    val pwd: String
)

/*@PathParam  @RequestBody @RequestParam*/
/*
@GetMapping("/method7/{id}")
fun getUser(@PathVariable("id") id: String): String {
    return id
}*/
@RestController() class UserRest{

    private val digest = MessageDigest.getInstance("SHA-256")
    companion object{
        val users: HashSet<Pair<Int, String>> = emptySet<Pair<Int, String>>().toHashSet()
    }

    @GetMapping("/user")
    fun getUser(@RequestParam("id") id: String): String {
        return id
    }

    @PostMapping("/user")
    fun addUser(@RequestBody userData: UserCreate): UserCreate {
        //val hash = digest.digest(userData.username.toByteArray(StandardCharsets.UTF_8))
        return userData
    }

    @PutMapping("/user")
    fun updateUser(): String {
        return ""
    }
}