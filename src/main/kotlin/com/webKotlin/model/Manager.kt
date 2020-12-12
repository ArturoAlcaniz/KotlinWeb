package com.webKotlin.model

import com.webKotlin.dao.UserDAO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.servlet.http.HttpSession

class Manager private constructor() {
    private val connectedUsersByUserName: ConcurrentHashMap<String, User?> = ConcurrentHashMap()
    private val connectedUsersByHttpSession: ConcurrentHashMap<String, User?> = ConcurrentHashMap()
    private val log = Logger.getLogger(Manager::class.java.name)
    private val chatGlobal = chat()

    private object ManagerHolder {
        var singleton = Manager()
    }

    /**
     * @param {String} email >> El email del usuario a validar
     * @return {Boolean} si el email es valido
     */

    private fun isEmailValid(email: String): Boolean {
        val pattern: Pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        val match: Matcher = pattern.matcher(email)
        return match.matches()
    }

    /**
     * @param {String} pass >> Las pass del usuario a validar
     * @return {Boolean} si la pass es valida
     */

    private fun isPassValid(pass: String): Boolean {
        val regex: Pattern = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")
        if (regex.matcher(pass).find()) {
           return false
        }
        return true
    }

    /**
     * @param {String} email >> El email del usuario a logear
     * @param {String} pass >> La pass del usuario a logear
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    private fun validarLogin(email: String, pass: String): ResponseEntity<String> {

        if (!isEmailValid(email))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("emailNotValid")

        if (!isPassValid(pass))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("passNotValid")

        if(UserDAO.select(email, pass) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("incorrectCredentials")

        return ResponseEntity.status(HttpStatus.OK).body("loginOK")
    }

    /**
     * @param {String} email >> El email del usuario a registrar
     * @param {String} pass >> La pass del usuario a registrar
     * @param {String} pass2 >> La confirmacion de pass a registrar
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    private fun validarRegistro(email: String, pass: String, pass2: String): ResponseEntity<String> {
        if(pass != pass2)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("passNotSame")

        if(pass.length<6)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("passLow")

        if(!isEmailValid(email))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("emailNotValid")

        if(!isPassValid(pass))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("passNotValid")

        if(UserDAO.getExist(email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("emailExists")
        }
        return ResponseEntity.status(HttpStatus.OK).body("registerOK")
    }

    /**
     * @param {String?} emailSession >> El email del usuario logeado
     * @param {String} email >> El email nuevo a validar
     * @param {String} nick >> El nick nuevo a validar
     * @param {String} pwd1 >> La nueva password a validar
     * @param {String} pwd2 >> La confirmacion de la nueva password a validar
     * @param {String} pass >> La password actual a validar
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    private fun validarCambioDatos(emailSession: String?, email: String, nick: String, pwd1: String, pwd2: String, pass: String): ResponseEntity<String> {
        if (!isEmailValid(email))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("emailNotValid")

        if (pwd1 != pwd2)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("passNotSame")

        if (!isPassValid(pwd1) && pwd1.isNotEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("newPassNotValid")

        if (pwd1.isNotEmpty() && pwd1.length < 6)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("newPassLow")

        if(email.length < 7)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nickNotValid")

        if(!emailSession.equals(email) && UserDAO.getExist(email))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("emailExists")


        if(pwd1.isEmpty()){

            if(!UserDAO.cambioDatosNoPass(emailSession, email, nick, pass))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("actualPassWrong")

        }else{

            if(!UserDAO.cambioDatos(emailSession, email, nick, pwd1, pass))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("actualPassWrong")

        }

        return ResponseEntity.status(HttpStatus.OK).body("changeDataOK")

    }

    /**
     * @param {HttpSession} session >> La sesion del actual usuario
     * @param {JSONObject} credenciales >> Los datos que el usuario envia en formulario de cambio
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    fun cambioDatos(session: HttpSession, credenciales: JSONObject): ResponseEntity<String> {
        val email: String = credenciales.getString("email")
        val nick: String = credenciales.getString("nick")
        val pwd1: String = credenciales.getString("newPass")
        val pwd2: String = credenciales.getString("newPass2")
        val pass: String = credenciales.getString("actualPass")
        val user: User? = findUserByHttpSessionId(session.id)
        var response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail")

        if(user == null)
            return response

        response = validarCambioDatos(user.email, email, nick, pwd1, pwd2, pass)

        if(response.statusCode.isError)
            return response

        if (pwd1.isEmpty()) {
            if (UserDAO.cambioDatosNoPass(user.email, email, nick, pass)){
                connectedUsersByUserName.remove(user.email)
                user.email = email
                user.nick = nick
                connectedUsersByUserName[email] = user
                connectedUsersByHttpSession[session.id] = user
                session.setAttribute("user", user)
            }

        } else{

            if (UserDAO.cambioDatos(user.email, email, nick, pwd1, pass)){
                connectedUsersByUserName.remove(user.email)
                user.email = email
                user.nick = nick
                connectedUsersByUserName[email] = user
                connectedUsersByHttpSession[session.id] = user
                session.setAttribute("user", user)
            }
        }

        return response
    }

    /**
     * @param {User} user >> El usuario a deslogear
     */

    private fun logout(user: User) {
        connectedUsersByUserName.remove(user.nick)
        connectedUsersByHttpSession.remove(user.httpSession?.id)
        chatGlobal.eliminarUsuario(user)
    }

    /**
     * @param {String?} httpSessionId >> La session del usuario que buscamos
     * @return {User?} El usuario encontrado
     */

    fun findUserByHttpSessionId(httpSessionId: String?): User? {
        return connectedUsersByHttpSession[httpSessionId]
    }

    /*
     * @param {String?} username >> El nick del usuario que buscamos
     * @return {User?} El usuario encontrado
     */

    private fun findUserByUsername(username: String?): User? {
        return connectedUsersByUserName[username]
    }

    /**
     * @param {HttpSession} session >> La session del usuario que queremos obtener
     * @return {ObjectNode?} Los datos a devolver del usuario a la interfaz
     */

    fun getUser(session: HttpSession): ObjectNode? {
        var map: ObjectNode?
        map = null
        val user: User? = findUserByHttpSessionId(session.id)
        if(user != null){
            map = user.toMapper()
        }
        return map
    }

    /**
     * @param {HttpSession} session >> La session del usuario que solicita ver el ranking
     * @return {ObjectNode?} Los datos del ranking a devolver
     */

    fun getRanking(session: HttpSession): ObjectNode? {

        if(findUserByHttpSessionId(session.id) == null)
            return null

        val listaUsuarios: ArrayList<User?>? = UserDAO.getRanking()
        val mapper = ObjectMapper()
        val obj: ObjectNode? = mapper.createObjectNode()
        if (listaUsuarios != null) {
            for (user in listaUsuarios) {
                run {
                    if (user != null && obj != null) {
                        user.httpSession = findUserByUsername(user.email)?.httpSession
                        val userMap: ObjectNode? = user.toMapper()
                        obj.set<ObjectNode>(userMap?.get("email").toString(), userMap)

                    }
                }
            }

        }

        return obj
    }

    /**
     * @param {HttpSession} session >> La session del usuario que se quiere saber si esta logeado
     * @return {Boolean} si el usuario esta logeado
     */

    fun getLogeado(session: HttpSession): Boolean {
        val user = connectedUsersByHttpSession[session.id]
        if(user != null){
            session.setAttribute("user", user)
            return true
        }
        return false
    }

    /**
     * @param {HttpSession} session >> La session del usuario que se va a registrar
     * @param {JSONObject} credenciales >> Las credenciales del usuario a registrar
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    fun register(session: HttpSession, credenciales: JSONObject): ResponseEntity<String> {
        val email: String = credenciales.getString("email")
        val pass: String = credenciales.getString("pwd1")
        val pass2: String = credenciales.getString("pwd2")
        var response : ResponseEntity<String> = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail")
        if(findUserByHttpSessionId(session.id) != null)
            return response
        try{
            response = validarRegistro(email, pass, pass2)
            if(!response.statusCode.isError){
                UserDAO.insert(email, pass)
                val user: User? = UserDAO.select(email, pass)
                if (user != null) {
                    user.httpSession = session
                }
                connectedUsersByUserName[email] = user
                connectedUsersByHttpSession[session.id] = user
                session.setAttribute("user", user)
                session.setAttribute("chatGlobal", chatGlobal)
            }
        }catch (e: Exception){
            log.info("Fallo durante el registro de "+email+" excepcion: "+e.message)
        }
        return response
    }

    /**
     * @param {HttpSession} session >> La session del usuario a logear
     * @param {JSONObject} credenciales >> Las credenciales del usuario a logear
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    fun login(session: HttpSession, credenciales: JSONObject): ResponseEntity<String> {
        val email: String = credenciales.getString("email")
        val pass: String = credenciales.getString("pass")
        var response : ResponseEntity<String> = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail")

        try{
            response = validarLogin(email, pass)
            if(!response.statusCode.isError){

                val userPrev : User? = findUserByUsername(email)
                if(userPrev != null)
                    logout(userPrev)

                val user : User? = UserDAO.select(email, pass)
                user?.httpSession = session
                connectedUsersByUserName[email] = user
                connectedUsersByHttpSession[session.id] = user
                session.setAttribute("user", user)
                session.setAttribute("chatGlobal", chatGlobal)
            }
            session.maxInactiveInterval = 900

        }catch (e: java.lang.Exception){
            log.info("Fallo durante el logeo de "+email+" excepcion: "+e.message)
        }
        return response
    }

    /**
     * @param {HttpSession} session >> La session del usuario a deslogear
     */

    fun logout(session: HttpSession){
        if (getLogeado(session)){
            val user = findUserByHttpSessionId(session.id)
            if (user != null) {
                session.invalidate()
                logout(user)
            }
            log.info("La session "+session.id+" se ha deslogeado")
        }
    }

    /**
     * @param {HttpSession} session >> La session del usuario que se unira al chat
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    fun joinToChat(session: HttpSession): ResponseEntity<String> {
        var response = ResponseEntity.status(HttpStatus.OK).body(Respuesta.JOINEDCHAT_OK.tipo)
        try {
            val user: User? = findUserByHttpSessionId(session.id)

            if (user != null) {
                log.info("Usuario "+user.nick+" se une al chatGlobal")
                chatGlobal.insertarUsuario(user)
                chatGlobal.notifyStart(user)
            }

        }catch (ex: Exception){
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.FAIL.tipo)
        }

        return response
    }

    /**
     * @param {HttpSession} session >> La session del usuario que envia un mensaje
     * @param {JSONObject} jso >> Los datos del mensaje que ha enviado
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    fun enviarMensaje(session: HttpSession, jso: JSONObject): ResponseEntity<String> {
        var response : ResponseEntity<String> = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.FAIL.tipo)
        val mensaje: String = jso.getString("mensaje")
        val user: User = findUserByHttpSessionId(session.id) ?: return response

        response = validarMensajeChat(user, mensaje)

        if(response.statusCode.isError)
            return response

        if(mensaje.take(1) != "/"){
            chatGlobal.insertarMensaje(user, mensaje)
            chatGlobal.notifyMensaje(Mensaje(Date(System.currentTimeMillis()), user.nick.toString(), mensaje))
        }

        if(mensaje == "/clear"){
            log.info("Chat limpiado")
            chatGlobal.eliminarTodosMensajes()
            chatGlobal.notifyComando(Mensaje(Date(System.currentTimeMillis()), user.nick.toString(), mensaje))
        }

        return response
    }

    /**
     * @param {User} user >> El user del mensaje a validar
     * @param {String} mensaje >> El mensaje a validar
     * @return {ResponseEntity<String>} La respuesta HTTP a devolver
     */

    private fun validarMensajeChat(user: User, mensaje: String): ResponseEntity<String> {
        if ((Date().time - (chatGlobal.getTimeOfUser(user)?.time ?: 0)) < 10000)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.SPAMM.tipo)
        if (containSpecialChar(mensaje))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.SPECIALCHAR.tipo)

        return ResponseEntity.status(HttpStatus.OK).body(Respuesta.SENTCHAT_OK.tipo)
    }

    private fun containSpecialChar(texto: String): Boolean{
        val pattern = Pattern.compile("[a-zA-Z0-9]*")
        val matcher = pattern.matcher(texto)
        return !matcher.matches()
    }

    companion object {
        fun get(): Manager {
            return ManagerHolder.singleton
        }
    }

}