package com.wirvsvirus.homealonechallenge

import org.springframework.web.bind.annotation.*

/*@PathParam  @RequestBody @RequestParam*/
@RestController() class UserRest{
    companion object{
        val users: HashSet<Pair<Int, String>> = emptySet<Pair<Int, String>>().toHashSet()
    }

    @GetMapping("/user")
    fun getUser(): String {
        return ""
    }

    @PostMapping("/user")
    fun addUser(@RequestBody username: String): String {
        return ""
    }

    @PutMapping("/user")
    fun updateUser(): String {
        return ""
    }
}