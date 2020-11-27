package com.example.PracticaKotlin.http

import com.example.PracticaKotlin.model.Manager
import com.fasterxml.jackson.databind.node.ObjectNode
import org.json.JSONObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession


@RestController
class Controlador_Rutas {

    @PostMapping("/login")
    fun login(session: HttpSession, @RequestBody credenciales: Map<String?, Any?>?): ResponseEntity<String> {
        return Manager.get().login(session, JSONObject(credenciales))
    }

    @PostMapping("/register")
    fun register(session: HttpSession, @RequestBody credenciales: Map<String?, Any?>?): ResponseEntity<String> {
        return Manager.get().register(session, JSONObject(credenciales))
    }

    @PostMapping("/cambiarDatosPerfil")
    fun cambiarDatosPerfil(session: HttpSession, @RequestBody credenciales: Map<String?, Any?>?): ResponseEntity<String> {
        return Manager.get().cambioDatos(session, JSONObject(credenciales))
    }

    @GetMapping("/getUser")
    fun getUser(session: HttpSession): ObjectNode? {
        return Manager.get().getUser(session)
    }

    @GetMapping("/getLogeado")
    fun getLogeado(session: HttpSession): Boolean {
        return Manager.get().getLogeado(session)
    }

    @PostMapping("/getRanking")
    fun getRanking(session: HttpSession): ObjectNode? {
        return Manager.get().getRanking(session)
    }

    @PostMapping("/enviarMensaje")
    fun enviarMensaje(session: HttpSession, @RequestBody credenciales: Map<String?, Any?>?): ResponseEntity<String> {
        return Manager.get().enviarMensaje(session, JSONObject(credenciales))
    }

    @PostMapping("/joinToChat")
    fun joinToChat(session: HttpSession, @RequestBody credenciales: Map<String?, Any?>?): ResponseEntity<String> {
        return Manager.get().joinToChat(session)
    }

}