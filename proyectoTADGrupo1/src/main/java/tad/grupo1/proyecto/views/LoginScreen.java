package tad.grupo1.proyecto.views;

import java.io.Serializable;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import static tad.grupo1.proyecto.views.MainUI.gc;
import static tad.grupo1.proyecto.views.MainUI.uc;

/**
 * UI content when the user is not logged in yet.
 */
public class LoginScreen extends CssLayout {
    
    private TextField username;
    private PasswordField password;
    private Button login;
    private Button register;
    private LoginListener loginListener;

    public LoginScreen(LoginListener loginListener) {
        this.loginListener = loginListener;
        buildUI();
        username.focus();
    }

    //Crea ventana de login
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
        centeringLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

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

        // Formulario de login
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

        // Ventana de registro
        final Window window = new Window();
        window.setWidth(400, Unit.PIXELS);
        window.setHeight(400, Unit.PIXELS);
        window.setDraggable(false);
        window.setClosable(true);
        window.setModal(true);
        window.setResizable(false);
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);

        Label labelRegistro = new Label("Registro");
        labelRegistro.setStyleName("h1");
        labelRegistro.setWidth(80, Unit.PERCENTAGE);

        TextField nick = new TextField("Username");
        nick.setIcon(FontAwesome.USER);
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

        content.addComponents(labelRegistro, nick, email, password, signUp);

        content.setComponentAlignment(labelRegistro, Alignment.TOP_CENTER);
        content.setComponentAlignment(nick, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(email, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(signUp, Alignment.MIDDLE_CENTER);

        window.setContent(content);

        // Logarse
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

        // Mostrar venta de registro
        register.addClickListener((event) -> {
            this.getUI().getUI().addWindow(window);
        });

        // Registrarse
        signUp.addClickListener((event) -> {
            String new_nick = nick.getValue();
            String new_email = email.getValue();
            String new_password = password.getValue();

            
            // Comprobar campos no vacios
            if (new_nick != null && new_email != null && new_password != null && 
                    new_nick.length() > 0 && new_email.length() > 0 && new_password.length() > 0) {
                // Comprobar username ya existente
                if (uc.comprobarUsername(new_nick)) {
                    showNotification(new Notification("Nombre de usuario ya existe",
                            "Por favor introduzca otro nombre de usuario.",
                            Notification.Type.WARNING_MESSAGE));
                } else {
                    uc.registrarUsuario(new_nick, new_email, new_password);
                    showNotification(new Notification("AVISO",
                            "Usuario creado correctamente, ahora puede iniciar sesión",
                            Notification.Type.HUMANIZED_MESSAGE));
                    this.getUI().getUI().removeWindow(window);
                }
            }else{
                showNotification(new Notification("Campos incompletos",
                            "Por favor introduzca todos los campos.",
                            Notification.Type.WARNING_MESSAGE));
            }

        });

        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        return loginForm;
    }

    private CssLayout buildLoginInformation() {
        CssLayout loginInformation = new CssLayout();
        loginInformation.setStyleName("login-information");
        Image logo = new Image("", new FileResource(
                new File(gc.getLogo())));
        
        //logo.setWidth("30%");

        logo.setWidth("90%");
        
        Label loginInfoText = new Label(
                "<h1>Bienvenido</h1>"
                + "Para acceder a la plataforma, por favor inicie sesi&oacute;n. Si no tiene un usuario, por favor reg&iacute;strese.",
                ContentMode.HTML);
        loginInfoText.setSizeFull();
        loginInformation.addComponents(logo,loginInfoText);
        return loginInformation;
    }

    private void login() {

        if (uc.comprobarLogin(username.getValue(), password.getValue())) {
            //Is user admin
            if(uc.isUserAdmin(username.getValue()))
            {
                MainUI.session.setAttribute("type","admin");
            }
            MainUI.session.setAttribute("user", username.getValue());
            loginListener.loginSuccessful();
        } else {
            showNotification(new Notification("Login failed",
                    "Por favor, compruebe su nombre de usuario y contraseña e intente de nuevo.",
                    Notification.Type.WARNING_MESSAGE));
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
