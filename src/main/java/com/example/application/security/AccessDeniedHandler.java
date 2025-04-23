package com.example.application.security;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedHandler implements BeforeEnterObserver {
    private final AccessAnnotationChecker accessChecker;

    @Autowired
    public AccessDeniedHandler(AccessAnnotationChecker accessChecker) {
        this.accessChecker = accessChecker;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!accessChecker.hasAccess(event.getNavigationTarget())) {
            event.rerouteTo("access-denied");
        }
    }
}


