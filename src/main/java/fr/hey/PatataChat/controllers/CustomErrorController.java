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

import java.text.MessageFormat;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/error")
    public ModelAndView handleErrorHttp(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        Object uriQuery = request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
        String remoteUser = request.getRemoteUser();
        String sessionId = request.getRequestedSessionId();

        LOGGER.error(MessageFormat.format("User: {0} - SessionId: {1}  - Http Error: {2} - URI: {3} - Query: {4}",
                remoteUser,sessionId, status, uri, uriQuery));

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

        return modelAndView;
    }
}