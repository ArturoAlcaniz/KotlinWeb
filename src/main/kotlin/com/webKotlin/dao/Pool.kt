package com.webKotlin.dao

import java.util.*
import java.util.logging.Logger

class Pool(numeroDeConexiones: Int) {
    private val libres: ArrayList<WrapperConnection>
    private val ocupadas: ArrayList<WrapperConnection>
    private val log = Logger.getLogger(Pool::class.java.name)

    val connection: WrapperConnection
        get() {
            val result = libres.removeAt(0)
            ocupadas.add(result)
            return result
        }

    fun liberame(wrapperConnection: WrapperConnection) {
        ocupadas.remove(wrapperConnection)
        libres.add(wrapperConnection)
    }

    init {
        libres = ArrayList(numeroDeConexiones)
        ocupadas = ArrayList(numeroDeConexiones)
        for (i in 0 until numeroDeConexiones) {
            libres.add(WrapperConnection(this))
            log.info("Pool De Conexiones - Conexion nº "+i+" creada")
        }
    }
}