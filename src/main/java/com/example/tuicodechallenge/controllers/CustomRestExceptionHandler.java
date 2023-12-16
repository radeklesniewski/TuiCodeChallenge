package com.example.tuicodechallenge.controllers;

import com.example.tuicodechallenge.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
public class CustomRestExceptionHandler extends DefaultHandlerExceptionResolver {

    public static final String MESSAGE_ATTRIBUTE_NAME = "message";
    public static final String STATUS_ATTRIBUTE_NAME = "status";

    @SneakyThrows
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception exception) {
        ModelAndView mav = new ModelAndView();

        logger.info("Handling exception: %s for request: %s".formatted(exception, request.getRequestURI()));

        mav.addObject(MESSAGE_ATTRIBUTE_NAME, exception.getMessage());

        if (exception instanceof HttpMediaTypeNotAcceptableException) {
            mav.addObject(STATUS_ATTRIBUTE_NAME, NOT_ACCEPTABLE.value());
            response.setStatus(NOT_ACCEPTABLE.value());
        } else if (exception instanceof NotFoundException) {
            mav.addObject(STATUS_ATTRIBUTE_NAME, NOT_FOUND.value());
            response.setStatus(NOT_FOUND.value());
        } else {
            mav.addObject(STATUS_ATTRIBUTE_NAME, INTERNAL_SERVER_ERROR.value());
            response.setStatus(INTERNAL_SERVER_ERROR.value());
        }

        mav.setView(new MappingJackson2JsonView());
        return mav;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}