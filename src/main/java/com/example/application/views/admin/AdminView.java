package com.example.application.views.admin;

import com.example.application.data.*;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@PageTitle("Ylläpito | Leffakerho")
@Route(value = "admin", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminView extends VerticalLayout {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    private final Grid<User> userGrid = new Grid<>(User.class);
    private final TextField usernameField = new TextField("Käyttäjänimi");
    private final TextField nameField = new TextField("Nimi");
    private final EmailField emailField = new EmailField("Sähköposti");
    private final PasswordField passwordField = new PasswordField("Salasana");
    private final ComboBox<Role> roleSelect = new ComboBox<>("Rooli");
    private final Button addUserButton = new Button("Lisää käyttäjä");

    @Autowired
    public AdminView(UserRepository userRepository, PasswordEncoder passwordEncoder, ReviewRepository reviewRepository, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reviewRepository = reviewRepository;
        this.userService = userService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H1("Admin hallintapaneeli"));


        add(new Paragraph("- Vain Admin pääsee tänne."));
        add(new Paragraph("- Vain Admin voi lisätä ja poistaa käyttäjiä. Muokkaus tapahtuu käyttäjän omalla profiilisivulla."));
        add(new Paragraph("- Vain Admin voi poistaa elokuvia, tapahtuu Elokuvat sivulla."));
        add(new Paragraph("- Vain Admin voi muokata ja poistaa arvosteluita Arvostelut sivulta."));


        configureGrid();
        configureForm();

        add(new HorizontalLayout(usernameField, nameField, emailField, passwordField, roleSelect, addUserButton));
        add(userGrid);

        updateGrid();
    }

    private void configureForm() {
        roleSelect.setItems(Role.USER, Role.ADMIN);
        roleSelect.setValue(Role.USER); // Oletusrooli

        addUserButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String name = nameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            Role role = roleSelect.getValue();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                Notification.show("Täytä kaikki kentät.");
                return;
            }

            if (userRepository.findByUsername(username).isPresent()) {
                Notification.show("Käyttäjänimi on jo käytössä.");
                return;
            }

            if (userRepository.existsByEmail(email)) {
                Notification.show("Sähköpostiosoite on jo käytössä.");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setHashedPassword(passwordEncoder.encode(password));
            user.setRoles(Collections.singleton(role));

            userRepository.save(user);
            Notification.show("Käyttäjä lisätty.");
            usernameField.clear();
            nameField.clear();
            emailField.clear();
            passwordField.clear();
            roleSelect.setValue(Role.USER);
            updateGrid();
        });
    }

    private void configureGrid() {
        userGrid.removeAllColumns();
        userGrid.addColumn(User::getUsername).setHeader("Käyttäjänimi");
        userGrid.addColumn(User::getEmail).setHeader("Sähköposti");
        userGrid.addColumn(user -> user.getRoles().toString()).setHeader("Rooli(t)");

        userGrid.addComponentColumn(user -> {
            Button deleteButton = new Button("Poista", e -> {
                if (user.getUsername().equals("admin")) {
                    Notification.show("Admin-käyttäjää ei voi poistaa.");
                    return;
                }

                // Poista käyttäjä käyttäen deleteCompletely-metodia
                userService.deleteCompletely(user.getId());
                Notification.show("Käyttäjä poistettu.");
                updateGrid();
            });
            return deleteButton;
        }).setHeader("Toiminnot");

        userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        userGrid.setSizeFull();
    }

    private void updateGrid() {
        List<User> users = userRepository.findAll();
        userGrid.setItems(users);
    }
}
