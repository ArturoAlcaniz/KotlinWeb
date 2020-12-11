package com.example.PracticaKotlin.http

import com.example.PracticaKotlin.model.Manager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpSession


@Controller
class ControladorViews {

    fun redirect(): String{
        return "redirect:/"
    }

    fun yaLogeado(session: HttpSession): String{
        if(Manager.get().getLogeado(session))
            return "redirect:/home"

        return "index"
    }

    fun noLogeado(session: HttpSession, urlSolicitada: String): String{
        if(!Manager.get().getLogeado(session))
            return redirect()

        return urlSolicitada
    }

    @RequestMapping("/")
    fun inicio(session: HttpSession): String {
        return yaLogeado(session)
    }

    @RequestMapping("/home")
    fun home(session: HttpSession): String {
        return noLogeado(session, "home")
    }

    @RequestMapping("/home/logout")
    fun logout(session: HttpSession): String {
        Manager.get().logout(session)
        return redirect()
    }

    @RequestMapping("/home/perfil")
    fun perfil(session: HttpSession): String {
        return noLogeado(session, "perfil")
    }

    @RequestMapping("/home/tienda")
    fun tienda(session: HttpSession): String {
        return noLogeado(session, "tienda")
    }

    @RequestMapping("/home/trabajo")
    fun trabajo(session: HttpSession): String {
        return noLogeado(session, "trabajo")
    }

    @RequestMapping("/home/ranking")
    fun ranking(session: HttpSession): String {
        return noLogeado(session, "ranking")
    }

    @RequestMapping("/home/chat")
    fun chat(session: HttpSession): String {
        return noLogeado(session, "chat")
    }

}