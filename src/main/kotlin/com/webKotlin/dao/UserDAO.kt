package com.webKotlin.dao

import com.webKotlin.dao.Broker.Companion.get
import com.webKotlin.model.User
import java.sql.SQLException
import java.util.logging.Logger

object UserDAO {
    private val log = Logger.getLogger(UserDAO::class.java.name)

    fun select(email: String?, pwd: String?): User? {
        get().bd.use { bd ->
            val sql = ("SELECT u.idUsers, u.email, u.nick, u.efectivo, u.experiencia, u.poder, u.muteado "
                    + "FROM usuarios u "
                    + "WHERE u.email = ? AND u.pass = AES_ENCRYPT(?, 'software')")
            try {
                bd.prepareStatement(sql).use { ps ->
                    ps!!.setString(1, email)
                    ps.setString(2, pwd)
                    ps.executeQuery().use { rs ->
                        return if (rs.next()) {
                            val user = User()
                            user.idUsers = rs.getString(1)
                            user.email = rs.getString(2)
                            user.nick = rs.getString(3)
                            user.efectivo = rs.getString(4)
                            user.experiencia = rs.getString(5)
                            user.poder = rs.getString(6)
                            user.muteado = rs.getString(7)
                            user
                        } else {
                            throw SQLException()
                        }
                    }
                }
            } catch (e: Exception) {
                log.info("\nError al identificar usuario")
                return null
            }
        }
    }

    fun getRanking(): ArrayList<User?>? {
        val lista: ArrayList<User?>? = ArrayList()
        get().bd.use { bd ->
            val sql = ("SELECT u.email, u.efectivo "
                    + "FROM usuarios u "
                    + "ORDER by efectivo DESC")
            try {
                bd.prepareStatement(sql).use { ps ->
                    ps!!.executeQuery().use { rs ->
                        while (rs.next()) {
                            val user = User()
                            user.email = rs.getString(1)
                            user.efectivo = rs.getString(2)
                            lista?.add(user)
                        }
                    }
                }
                return lista

            } catch (e: Exception){
                log.info("\nError al obtener los usuarios")
                return null
            }
        }
    }

    fun getExist(email: String?): Boolean {
        var em: String? = null
        try {
            get().bd.use { bd ->
                val sql = "SELECT email " + "FROM usuarios u " + "WHERE email = ?"

                bd.prepareStatement(sql).use { ps ->
                    ps!!.setString(1, email)
                    ps.executeQuery().use { rs ->
                        if (rs.next()) {
                            val reqEmail: String = rs.getString(1)
                            em = reqEmail
                        }
                    }
                }
            }
        } catch (e1: Exception) {
            log.info("\nError al obtener email")
            return false
        }
        return em != null
    }


    fun insert(email: String?, pwd: String?) {
        try {
            get().bd.use { bd ->
                val sql = "insert into usuarios (email, nick, pass) values (?, ?, AES_ENCRYPT(?, 'software'))"
                bd.prepareStatement(sql).use { ps ->
                    ps!!.setString(1, email)
                    ps.setString(2, email)
                    ps.setString(3, pwd)
                    ps.executeUpdate()
                }
            }
        } catch (e: Exception) {
            log.info("Error al insertar usuario "+e.message)
        }
    }

    fun cambioDatosNoPass(email: String?, email1: String, nick: String, pass: String): Boolean {
        get().bd.use { bd ->
            val sql = ("UPDATE usuarios "
                    + "SET email = ?, nick = ? "
                    + "WHERE email = ? AND pass = AES_ENCRYPT(?, 'software')")

            try {
                bd.prepareStatement(sql).use { ps ->
                    ps?.setString(1, email1)
                    ps?.setString(2, nick)
                    ps?.setString(3, email)
                    ps?.setString(4, pass)
                    if(ps?.executeUpdate() == 1)
                        return true
                }
            }catch (e1: Exception) {
                log.info("\nError al cambiar datos")
                return false
            }
        }
        return false
    }

    fun cambioDatos(email: String?, email1: String, nick: String, pwd1: String, pass: String): Boolean {
        get().bd.use { bd ->
            val sql = ("UPDATE usuarios "
                    + "SET email = ?, nick = ?, pass = AES_ENCRYPT(?, 'software') "
                    + "WHERE email = ? AND pass = AES_ENCRYPT(?, 'software')")

            try {
                bd.prepareStatement(sql).use { ps ->
                    ps?.setString(1, email1)
                    ps?.setString(2, nick)
                    ps?.setString(3, pwd1)
                    ps?.setString(4, email)
                    ps?.setString(5, pass)
                    if(ps?.executeUpdate() == 1)
                        return true
                }
            }catch (e1: Exception) {
                log.info("\nError al cambiar datos")
                return false
            }
        }
        return false
    }

}