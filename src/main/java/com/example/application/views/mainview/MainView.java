package com.example.application.views.mainview;


import com.example.application.data.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.html.Image;

@PageTitle("Etusivu | Leffakerho")
@Route(value = "", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@CssImport("./themes/leffakerho/views/main-view.css")
public class MainView extends VerticalLayout {

    public MainView(AuthenticatedUser authenticatedUser) {
        setClassName("main-view");
        setSpacing(true);
        setPadding(true);


        // Käytetään Lumo Utility -luokkia
        addClassNames("lumo-padding-l", "lumo-margin-m");

        User user = authenticatedUser.get().orElse(null);
        String username = user != null ? user.getName() : "Käyttäjä";

        H1 header = new H1("Tervetuloa, " + username + "!");
        header.addClassNames("main-header", "lumo-text-contrast");

        Image logo = new Image("/images/logo.png", "Leffakerho logo");
        logo.setWidth("650px");
        logo.setHeight("650px");
        logo.addClassNames("logo", "lumo-border-radius");

        Paragraph description = new Paragraph("Voit selata elokuvia, lukea ja kirjoittaa arvosteluja sekä lisätä suosikkeja omaan listaan.");
        description.addClassNames("lumo-font-size-m");

        add(header, logo, description);
    }
}
