package com.example.application.views.mainview;


import com.example.application.data.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.util.I18nUtil; // toteutettu lokalisointi (FI/EN) MainView-sivulle.
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.html.Image;

import java.util.Locale;


@PageTitle("Etusivu | Leffakerho")
@Route(value = "", layout = MainLayout.class)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@CssImport("./themes/leffakerho/views/main-view.css")
public class MainView extends VerticalLayout {

    public MainView(AuthenticatedUser authenticatedUser) {
        // Alustus
        setClassName("main-view");
        setSpacing(true);
        setPadding(true);
        addClassNames("lumo-padding-l", "lumo-margin-m");

        // Hae käyttäjä
        User user = authenticatedUser.get().orElse(null);
        String username = user != null ? user.getName() : "Käyttäjä";

        // Luo komponentit
        H1 header = new H1();
        Paragraph description = new Paragraph();
        Image logo = new Image("/images/logo.png", "Leffakerho logo");
        logo.setWidth("650px");
        logo.setHeight("650px");
        logo.addClassNames("logo", "lumo-border-radius");

        // Päivitä tekstit alussa
        updateTexts(header, description, username);

        // Lisää kuuntelija kielen muutokselle
        UI.getCurrent().setLocale(VaadinSession.getCurrent().getLocale());
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> {
            updateTexts(header, description, username);
        });

        add(header, logo, description);
    }

    private void updateTexts(H1 header, Paragraph description, String username) {
        header.setText(I18nUtil.get("mainview.welcome", username));
        description.setText(I18nUtil.get("mainview.description"));
    }
}
