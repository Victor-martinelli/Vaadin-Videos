package com.mycompany.proyectotad.samples.authentication;

import java.io.Serializable;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * UI content when the user is not logged in yet.
 */
public class LoginScreen extends CssLayout {

    private TextField username;
    private PasswordField password;
    private Button login;
    private Button register;
    private LoginListener loginListener;
    private AccessControl accessControl;

    public LoginScreen(AccessControl accessControl, LoginListener loginListener) {
        this.loginListener = loginListener;
        this.accessControl = accessControl;
        buildUI();
        username.focus();
    }

    private void buildUI() {
        addStyleName("login-screen");

        
        // login form, centered in the available part of the screen
        Component loginForm = buildLoginForm();

        // layout to center login form when there is sufficient screen space
        // - see the theme for how this is made responsive for various screen
        // sizes
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setMargin(false);
        centeringLayout.setSpacing(false);
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(loginForm);
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);

        // information text about logging in
        CssLayout loginInformation = buildLoginInformation();

        addComponent(centeringLayout);
        addComponent(loginInformation);
    }

    private Component buildLoginForm() {
        FormLayout loginForm = new FormLayout();

        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        loginForm.setMargin(false);

        loginForm.addComponent(username = new TextField("Usuario"));
        username.setWidth(15, Unit.EM);
        loginForm.addComponent(password = new PasswordField("Contraseña"));
        password.setWidth(15, Unit.EM);

        HorizontalLayout buttonsArea = new HorizontalLayout();
        buttonsArea.setSpacing(true);
        loginForm.addComponent(buttonsArea);

        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");
        loginForm.addComponent(buttons);

        buttonsArea.addComponent(login = new Button("Login"));
        login.setDisableOnClick(true);
        buttonsArea.addComponent(register = new Button("Registro"));
        buttonsArea.addComponent(buttons);

        // Venta de registro
        final Window window = new Window();
        window.setWidth(300, Unit.PIXELS);
        window.setHeight(400, Unit.PIXELS);
        window.setDraggable(false);
        window.setClosable(true);
        window.setModal(true);
        window.setResizable(false);
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        TextField name = new TextField("Name");
        name.setIcon(FontAwesome.USER);
        name.setWidth(80, Unit.PERCENTAGE);
        TextField nick = new TextField("Nick");
        nick.setIcon(FontAwesome.GAMEPAD);
        nick.setWidth(80, Unit.PERCENTAGE);
        TextField email = new TextField("Email");
        email.setIcon(FontAwesome.MAIL_FORWARD);
        email.setWidth(80, Unit.PERCENTAGE);
        PasswordField password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.setWidth(80, Unit.PERCENTAGE);
        Button signUp = new Button("Sign Up");
        signUp.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        signUp.setWidth(80, Unit.PERCENTAGE);
        content.addComponents(name, nick, email, password, signUp);
        content.setComponentAlignment(name, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(nick, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(email, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(signUp, Alignment.MIDDLE_CENTER);

        window.setContent(content);

        login.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    login();
                } finally {
                    login.setEnabled(true);
                }
            }
        });

        register.addClickListener((event) -> {
            this.getUI().getUI().addWindow(window);
        });

        signUp.addClickListener((event) -> {
            String new_name = name.getValue();
            String new_nick = nick.getValue();
            String new_email = email.getValue();
            String new_password = password.getValue();

            // TODO: Añadir nuevo usuario
            this.getUI().getUI().removeWindow(window);
        });

        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        return loginForm;
    }

    private CssLayout buildLoginInformation() {
        CssLayout loginInformation = new CssLayout();
        loginInformation.setStyleName("login-information");
        Label loginInfoText = new Label(
                "<h1>Login Information</h1>"
                        + "Log in as &quot;admin&quot; to have full access. Log in with any other username to have read-only access. For all users, any password is fine",
                ContentMode.HTML);
        loginInfoText.setSizeFull();
        loginInformation.addComponent(loginInfoText);
        return loginInformation;
    }

    private void login() {
        if (accessControl.signIn(username.getValue(), password.getValue())) {
            loginListener.loginSuccessful();
        } else {
            showNotification(new Notification("Login failed",
                    "Please check your username and password and try again.",
                    Notification.Type.HUMANIZED_MESSAGE));
            username.focus();
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }
}