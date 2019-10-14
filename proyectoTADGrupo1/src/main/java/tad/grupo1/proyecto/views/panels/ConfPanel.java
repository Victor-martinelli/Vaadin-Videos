package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import tad.grupo1.proyecto.objects.FileUploader;
import static tad.grupo1.proyecto.views.MainUI.uc;

public class ConfPanel extends CssLayout implements View {
    
    public ConfPanel(String username) {
        VerticalLayout view = new VerticalLayout();
        view.setSizeFull();
        view.setMargin(true);
        view.setSpacing(true);
        Label nombreCanal = new Label("<h2><b>" + username + "</b></h2>", ContentMode.HTML);
        Label divider1 = new Label("<hr />", ContentMode.HTML);
        divider1.setWidth(100f, Sizeable.Unit.PERCENTAGE);
        Label contrasenya = new Label("<b>Cambiar contraseña</b>", ContentMode.HTML);
        PasswordField password = new PasswordField("Contraseña");
        PasswordField newpassword = new PasswordField("Nueva contraseña");
        PasswordField newpassword2 = new PasswordField("Repita la nueva contraseña");
        Button changePassword = new Button("Cambiar");
        Label divider2 = new Label("<hr />", ContentMode.HTML);
        divider2.setWidth(100f, Sizeable.Unit.PERCENTAGE);
        changePassword.addClickListener(e -> changePassword(username, password.getValue(), newpassword.getValue(), newpassword2.getValue()));
        Label fotoPerfil = new Label("<b>Cambiar foto de perfil</b>", ContentMode.HTML);
        Upload uploadFoto = subirFotoPerfil(username);
        uploadFoto.setImmediateMode(false);
        uploadFoto.setIcon(FontAwesome.IMAGE);
        view.addComponents(nombreCanal, divider1,contrasenya, password, newpassword, newpassword2, changePassword, divider2, fotoPerfil, uploadFoto);
        view.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        view.setComponentAlignment(newpassword, Alignment.MIDDLE_CENTER);
        view.setComponentAlignment(newpassword2, Alignment.MIDDLE_CENTER);
        addComponent(view);
    }

    private void changePassword(String username, String old, String new1, String new2) {
        if(old.equals(uc.getPassword(username, "password")) && new1.equals(new2)){
            uc.changePassword(username, new1);
            Notification.show("Contraseña cambiada correctamente.", Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Contraseña incorrecta o no coinciden las nuevas.", Notification.Type.ERROR_MESSAGE);
        }
    }
    
    private Upload subirFotoPerfil(String username) {
        FileUploader receiver = new FileUploader(this,username,2);
        
        Upload upload = receiver.createThumbUploadForm();
        
        upload.addSucceededListener(receiver);
        
        return upload;
    }
}