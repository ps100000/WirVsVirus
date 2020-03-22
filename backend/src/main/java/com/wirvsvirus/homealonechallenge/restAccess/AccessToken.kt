package com.wirvsvirus.homealonechallenge.restAccess

import com.wirvsvirus.homealonechallenge.db.SpringJdbc
import com.wirvsvirus.homealonechallenge.rest.usernameRegex
import com.wirvsvirus.homealonechallenge.toHexString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.security.MessageDigest
import java.sql.ResultSet
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


data class AccessTokenAnswer(
    val userId: Int,
    val seed: String,
    val created: Timestamp
)

val sha256Regex = Regex("^[0-9a-f]{64}$")
const val tokenValidMs = 10 * 60 * 1000

@RestController() class AccessTokenRest {
    private val digest = MessageDigest.getInstance("SHA-256")
    @GetMapping("/token")
    fun newToken(@RequestParam("username") username: String ): ResponseEntity<AccessTokenAnswer> {
        if (!username.matches(usernameRegex)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val userResultSet: ResultSet = SpringJdbc.executeQuery("SELECT user_id, pwd FROM users WHERE username=?") {
            it.setString(1, username)
        }
        if (!userResultSet.next()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val userId = userResultSet.getInt("user_id")
        val existingTokenResultSet: ResultSet = SpringJdbc.executeQuery("SELECT seed, created FROM access_tokens WHERE user_id=?") {
            it.setInt(1, userId)
        }
        val userHasToken = existingTokenResultSet.next()
        if (!userHasToken || existingTokenResultSet.getTimestamp("created").time + tokenValidMs < System.currentTimeMillis()) {
            var seed = ""
            for (i in 0..15) {
                seed += ('a'..'z').random()
            }
            val creationTimestamp = Timestamp(System.currentTimeMillis())
            val tokenData = (userResultSet.getString("pwd") + seed)
            val token = digest.digest(tokenData.toByteArray()).toHexString()
            if(userHasToken){
                SpringJdbc.executeUpdate("UPDATE access_tokens SET token = ?, created = ?, seed = ? WHERE user_id = ?") {
                    it.setString(1, token)
                    it.setTimestamp(2, creationTimestamp)
                    it.setString(3, seed)
                    it.setInt(4, userId)
                }
            }else{
                SpringJdbc.executeUpdate("INSERT INTO access_tokens (user_id, token, created, seed) VALUES (?, ?, ?, ?)") {
                    it.setInt(1, userId)
                    it.setString(2, token)
                    it.setTimestamp(3, creationTimestamp)
                    it.setString(4, seed)
                }
            }
            val newTokenResultSet: ResultSet = SpringJdbc.executeQuery("SELECT seed, created FROM access_tokens WHERE user_id=?") {
                it.setInt(1, userId)
            }
            if (!newTokenResultSet.next()) {
                return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
            }
            return ResponseEntity(AccessTokenAnswer(userId, newTokenResultSet.getString("seed"), newTokenResultSet.getTimestamp("created")), HttpStatus.OK)
        }else{
            return ResponseEntity(AccessTokenAnswer(userId, existingTokenResultSet.getString("seed"),existingTokenResultSet.getTimestamp("created")), HttpStatus.OK)
        }

    }

}

@Component
class AccessTokenInterceptorConfigurer : WebMvcConfigurer {
    @Autowired
    var accessTokenServiceInterceptor: AccessTokenServiceInterceptor? = null
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(accessTokenServiceInterceptor as HandlerInterceptor)
    }
}

@Component
class AccessTokenServiceInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerType = (handler as HandlerMethod).beanType
        if (handlerType == AccessTokenRest::class.java ) {
            return true
        }

        val authUserIdHeader: String? = request.getHeader("AuthUserId")
        val authTokenHeader: String? = request.getHeader("AuthToken")
        if (authUserIdHeader == null || authTokenHeader == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }

        if (!authUserIdHeader.matches(Regex("^[0-9]+$")) || !authTokenHeader.matches(sha256Regex)) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }
        val userId = authUserIdHeader.toInt()
        val rs: ResultSet = SpringJdbc.executeQuery("SELECT token, created FROM access_tokens WHERE user_id=? AND token=?") {
            it.setInt(1, userId)
            it.setString(2, authTokenHeader)
        }

        if (!rs.next() || rs.getTimestamp("created").time + tokenValidMs < System.currentTimeMillis()) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }

        return true
    }
}