package com.wirvsvirus.homealonechallenge.db

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
            println(SpringJdbc.executeUpdate("insert into users (username, pwd, mail) values ('a', 'b', 'c')"))
            val rs: ResultSet = SpringJdbc.executeQuery("SELECT * FROM users")
            while (rs.next()) {
                println(rs.getString(1) + ' ' + rs.getString(2) + ' ' + rs.getString(3) + ' ' + rs.getString(4))
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
