package com.webKotlin.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class chat {
    private var Mensajes: MutableList<Mensaje> = mutableListOf()
    private var usuarios: MutableList<User> = mutableListOf()
    private val lastTimeMessage: ConcurrentHashMap<User, Date> = ConcurrentHashMap()


    fun obtenerMensajes(): ObjectNode {
        val map = ObjectMapper()
        val obj: ObjectNode = map.createObjectNode()
        val array: ArrayNode = map.createArrayNode()
        val iterator = this.Mensajes.iterator()

        iterator.forEach {
            array.add(it.toMapper())
        }

        obj.put("type", "startChat")
        obj.set<ObjectNode>("mensajes", array)

        return obj
    }

    fun insertarMensaje(usuario: User, texto: String){
        val date = Date()
        val mensaje = usuario.nick?.let { Mensaje(date, it, texto) }
        if(usuarios.contains(usuario)){
            if (mensaje != null) {
                Mensajes.add(mensaje)
            }
            lastTimeMessage[usuario] = date
        }
    }

    fun eliminarTodosMensajes(){
        Mensajes.clear()
    }

    fun insertarUsuario(user: User){
        if(!usuarios.contains(user))
            usuarios.add(user)
    }

    fun eliminarUsuario(user: User){
        if (usuarios.contains(user))
            usuarios.remove(user)
    }

    fun notifyMensaje(Mensaje: Mensaje) {
        for (user: User in this.usuarios){
            var obj: ObjectNode = ObjectMapper().createObjectNode()
            obj.put("type", "MensajeChat")
            obj.set<ObjectNode>("mensaje", Mensaje.toMapper())
            user.send(obj)
        }

    }

    fun notifyComando(Mensaje: Mensaje) {
        for (user: User in this.usuarios){
            var obj: ObjectNode = ObjectMapper().createObjectNode()
            obj.put("type", "ComandoChat")
            obj.put("comando", Mensaje.texto)
            user.send(obj)
        }

    }

    fun getTimeOfUser(user: User): Date? {
        return lastTimeMessage[user]
    }

    fun notifyStart(user: User?) {
        if(Mensajes.size >= 1)
            user?.send(obtenerMensajes())
    }

}