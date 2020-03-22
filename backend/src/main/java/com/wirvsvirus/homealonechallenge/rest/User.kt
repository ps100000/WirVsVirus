package com.wirvsvirus.homealonechallenge.rest

import com.wirvsvirus.homealonechallenge.db.SpringJdbc
import com.wirvsvirus.homealonechallenge.restAccess.sha256Regex
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.ResultSet


data class UserDbEntry(
    val username: String,
    val mail: String,
    val pwd: String,
    val userId: Int
)

data class UserInfo(
    val username: String,
    val mail: String,
    val userId: Int
)

data class UserPost(
    val username: String,
    val mail: String,
    val pwd: String
)

data class UserPut(
    val username: String?,
    val mail: String?,
    val pwd: String?,
    val userId: Int
)

val usernameRegex = Regex("^[a-zA-Z0-9.\\-]+$")
/*@PathParam  @RequestBody @RequestParam*/
/*
@GetMapping("/method7/{id}")
fun getUser(@PathVariable("id") id: String): String {
    return id
}*/
@RestController() class UserRest{
    private fun doesUserExists(username: String, mail: String = ""): Boolean{
        val rs: ResultSet = SpringJdbc.executeQuery("SELECT COUNT (*) FROM users WHERE username=? OR mail=?;") {
            it.setString(1,username)
            it.setString(2, mail)
        }
        rs.next()
        return rs.getInt("count") != 0
    }

    @GetMapping("/user")
    fun getUser(@RequestParam("id") userId: Int): ResponseEntity<UserInfo> {
        val rs: ResultSet = SpringJdbc.executeQuery("SELECT username, mail FROM users WHERE user_id=?") {
            it.setInt(1, userId)
        }
        if (!rs.next()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(UserInfo(rs.getString("username"), rs.getString("mail"), userId), HttpStatus.OK)
    }

    @PostMapping("/user")
    fun addUser(@RequestBody userData: UserPost): ResponseEntity<UserInfo> {
        if (!userData.username.matches(usernameRegex)) {
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        }
        if (!userData.mail.matches(Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"))) {
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        }
        if (!userData.pwd.matches(sha256Regex)) {
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        }
        if(doesUserExists(userData.username, userData.mail)){
            return ResponseEntity(HttpStatus.CONFLICT)
        }
        SpringJdbc.executeUpdate("INSERT INTO users (username, mail, pwd) VALUES (?, ?, ?)") {
            it.setString(1, userData.username)
            it.setString(2, userData.mail)
            it.setString(3, userData.pwd)
        }
        val rs: ResultSet = SpringJdbc.executeQuery("SELECT username, mail, user_id FROM users WHERE username=?") {
            it.setString(1, userData.username)
        }
        if (!rs.next()) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(UserInfo(rs.getString("username"), rs.getString("mail"), rs.getInt("user_id")), HttpStatus.CREATED)
    }

    @PutMapping("/user")
    fun updateUser(): String {
        return ""
    }
}