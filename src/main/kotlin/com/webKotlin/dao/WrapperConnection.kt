package com.webKotlin.dao

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.logging.Logger

class WrapperConnection(pool2: Pool) : AutoCloseable {
    private var connection: Connection? = null
    private val log = Logger.getLogger(WrapperConnection::class.java.name)
    private var pool: Pool? = null

    override fun close() {
        this.pool?.liberame(this)
    }

    fun prepareStatement(sql: String?): PreparedStatement? {
        try {
            return connection!!.prepareStatement(sql)
        } catch (e: SQLException) {
            log.info("La conexión con la BBDD ha fallado")
        }
        return null
    }

    init {
        this.pool = pool2
        try {
            val url = "jdbc:mysql://localhost:3306/Practica-Kotlin?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
            connection = DriverManager.getConnection(url, "root", "root")
        } catch (e: Exception) {
            log.info("La conexión con la BBDD ha fallado")
            log.info(e.message)
        }
    }
}