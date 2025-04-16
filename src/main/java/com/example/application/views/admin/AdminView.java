package com.example.application.views.admin;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@PageTitle("Ylläpito | Leffakerho")
@Route(value = "admin", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN"})
public class AdminView extends VerticalLayout implements BeforeEnterObserver {

    public AdminView() {
        // Tämä on vain esimerkki sisältöä admin-sivulla
        Text text = new Text("Tervetuloa Admin-sivulle! Vain admin voi nähdä tämän.");
        add(text);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Tarkistetaan käyttäjän rooli käyttäen Spring Security
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser == null || !currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            // Jos käyttäjä ei ole admin, ohjataan "access-denied" sivulle
            event.forwardTo("/access-denied");
        }
    }
}
