package com.example.application.views.mainview;

import com.example.application.data.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Etusivu | Leffakerho")
@Route(value = "main", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class MainView extends VerticalLayout {

    public MainView(AuthenticatedUser authenticatedUser) {
        setSpacing(true);
        setPadding(true);

        User user = authenticatedUser.get().orElse(null);
        String username = user != null ? user.getName() : "Käyttäjä";

        add(
                new H1("Tervetuloa, " + username + "!"),
                new Paragraph("Voit selata elokuvia, lukea ja kirjoittaa arvosteluja sekä lisätä suosikkeja omaan listaan.")
        );
    }
}
