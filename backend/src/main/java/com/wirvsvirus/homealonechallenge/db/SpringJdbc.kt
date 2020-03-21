package com.wirvsvirus.homealonechallenge.db

import ch.qos.logback.core.db.DriverManagerConnectionSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.sql.*

class SpringJdbc {
    companion object{
        private const val url = "jdbc:postgresql://172.24.32.1:5432/postgres"
        var connection: Connection
            private set
        init{
            Class.forName("org.postgresql.Driver")
            connection = DriverManager.getConnection(url, "postgres", "example")
        }

        fun executeUpdate(query: String, fill: (PreparedStatement) -> Unit = {}): Int{
            val pst: PreparedStatement = connection.prepareStatement(query)
            fill(pst)
            return pst.executeUpdate()
        }
        fun executeQuery(query: String, fill: (PreparedStatement) -> Unit = {}): ResultSet{
            val pst: PreparedStatement = connection.prepareStatement(query)
            fill(pst)
            return pst.executeQuery()
        }
    }
}


object JdbcPostgresqlDriverUrlExample {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val rs: ResultSet = SpringJdbc.executeQuery("SELECT VERSION()")
            if (rs.next()) {
                println(rs.getString(1));
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            System.exit(1)
        } catch (e: SQLException) {
            e.printStackTrace()
            System.exit(2)
        }
    }
}
