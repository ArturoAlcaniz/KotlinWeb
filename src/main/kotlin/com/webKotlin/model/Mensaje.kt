package com.webKotlin.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.text.SimpleDateFormat
import java.util.*


class Mensaje(var fecha: Date, var usuario: String, var texto: String) {

    fun toMapper(): ObjectNode? {
        val map = ObjectMapper()
        val obj: ObjectNode? = map.createObjectNode()

        if (obj != null) {
            obj.put("usuario", usuario)
            obj.put("fecha", SimpleDateFormat("h:mm a").format(fecha))
            obj.put("mensaje", texto)
        }
        return obj
    }

}