package com.example.PracticaKotlin.http
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@CrossOrigin
@RestController
class Controlador_Error : ErrorController {
    @RequestMapping(value = [PATH])
    fun saveLeadQuery(): ModelAndView {
        return ModelAndView("redirect:/")
    }

    override fun getErrorPath(): String {
        return PATH
    }

    companion object {
        private const val PATH = "/error"
    }
}