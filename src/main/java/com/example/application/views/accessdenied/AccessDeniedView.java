package com.example.application.views.accessdenied;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.component.dependency.CssImport;

@AnonymousAllowed
@PageTitle("Pääsy evätty | Leffakerho")
@Route(value = "access-denied", layout = MainLayout.class)
@CssImport("./themes/leffakerho/views/access-denied-view.css")
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {
        // Poista setSpacing(false) ja lisää marginaali
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(true);
        setMargin(true);

        // Lisää kuva
        Image accessDeniedImage = new Image("images/accessdenied.png", "Pääsy evätty");
        accessDeniedImage.addClassName("access-denied-image");

        // Lisää otsikko
        H1 header = new H1("Pääsy evätty!");
        header.addClassName("error-header");

        // Lisää teksti
        Paragraph text = new Paragraph("Sinulla ei ole tarvittavia oikeuksia tälle sivulle.");
        text.addClassName("center-text");

        // Lisää komponentit layoutiin
        add(header, accessDeniedImage, text);
    }
}
