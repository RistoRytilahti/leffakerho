package com.example.application.views.accessdenied;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Pääsy evätty | Leffakerho")
@Route(value = "access-denied", layout = MainLayout.class)
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {
        setSpacing(false);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        H1 header = new H1("Pääsy evätty!");
        header.getStyle().set("color", "var(--lumo-error-text-color)");

        Paragraph text = new Paragraph("Sinulla ei ole tarvittavia oikeuksia tälle sivulle.");
        text.getStyle().set("text-align", "center");

        add(header, text);
    }
}