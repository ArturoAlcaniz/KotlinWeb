package com.example.PracticaKotlin.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.json.JSONException
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.io.Serializable
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpSession

class User : Serializable {
    var idUsers: String? = null
    var nick: String? = null
    var email: String? = null
    var efectivo: String? = null
    var experiencia: String? = null
    var muteado: String? = null
    var poder: String? = null

    private var map: ObjectMapper = ObjectMapper()

    @Transient
    var session: WebSocketSession? = null

    @Transient
    var httpSession: HttpSession? = null

    @Throws(JSONException::class)
    fun toMapper(): ObjectNode? {
        val obj: ObjectNode? = map.createObjectNode()
        if(obj != null){
            obj.put("id", idUsers)
            obj.put("email", email)
            obj.put("nick", nick)
            obj.put("efectivo", efectivo)
            obj.put("experiencia", experiencia)
            if(muteado == null)
                obj.put("muteado", 0)
            else
                obj.put("muteado", muteado)
            obj.put("poder", poder)
            if(httpSession == null){
                obj.put("LastAccesed", "Offline")
            }else{
                try {
                    val dif = System.currentTimeMillis() - httpSession?.lastAccessedTime!!
                    val last: String = "Hace "+TimeUnit.MILLISECONDS.toMinutes(dif)+" minutos"
                    obj.put("LastAccesed", last)
                }catch (ex: Exception){
                    obj.put("LastAccesed", "Offline")
                }

            }
        }
        return obj
    }

    @Throws(IOException::class)
    fun send(map: ObjectNode) {
        session!!.sendMessage(TextMessage(map.toString()))
    }

}