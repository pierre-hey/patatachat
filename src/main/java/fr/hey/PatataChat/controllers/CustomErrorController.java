package fr.hey.PatataChat.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/error")
    public ModelAndView handleConflict403(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        LOGGER.info("Erreur : " + status);

        ModelAndView modelAndView = new ModelAndView();

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                modelAndView.setViewName("error/error-404");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                modelAndView.setViewName("error/error-500");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                modelAndView.setViewName("error/error-403");
            }
        } else {
            modelAndView.setViewName("error/error");
        }

        // Ajouter des objets au modèle si nécessaire
        modelAndView.addObject("customAttribute", "valeur");

        return modelAndView;
    }
}