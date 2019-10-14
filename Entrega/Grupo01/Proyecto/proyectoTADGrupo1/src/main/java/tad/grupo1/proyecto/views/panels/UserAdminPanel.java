/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.Set;
import tad.grupo1.proyecto.objects.User;
import static tad.grupo1.proyecto.views.MainUI.uc;

/**
 *
 * @author Portatil
 */
public class UserAdminPanel extends CssLayout implements View {

    private VerticalLayout content;
    private String username;

    public UserAdminPanel() {

        userManagement();
        
    }
    
    
    public void userManagement()
    {
        
        HorizontalLayout confirmation = new HorizontalLayout();
        content = new VerticalLayout();
        Grid<User> grid = new Grid<>();

        List<User> users = uc.getAllUsers();

        grid.setItems(users);
        grid.addColumn(User::getUsername).setCaption("Usuario");
        grid.addColumn(User::getSuscriptores).setCaption("Suscriptores");

        grid.addSelectionListener(event -> {
            Set<User> selected = event.getAllSelectedItems();
            
            User aux = (User) selected.toArray()[0];
            
            username = aux.getUsername();
            
            confirmation.removeAllComponents();
            
            Button confirmationButton = new Button("Borrar usuario");
            
            confirmation.addComponents(new Label("<h2><b>¿Esta seguro que quiere borrar al usuario "+username+"? - Pulse el bot&oacute;n para confirmar</b></h2>",ContentMode.HTML),confirmationButton);
            
            confirmationButton.addStyleName(ValoTheme.BUTTON_DANGER);
            confirmationButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        uc.deleteUser(username);
                        users.remove(new User(username,0));
                        grid.getDataProvider().refreshAll();
                        grid.setHeightByRows(users.size());
                        Notification.show("Exito",
                        "Se ha borrado al usuario correctamente",
                        Notification.Type.HUMANIZED_MESSAGE);
                        confirmation.removeAllComponents();
                    }
                });
            
        });

        grid.setHeightByRows(users.size());
        
        content.addComponents(new Label("<h1>Gesti&oacute;n de usuarios - Pulse un usuario para borrarlo</h1>",ContentMode.HTML),new Label("<h2>Puedes pulsar las columnas para ordenar seg&uacute;n ese par&aacute;metro</h2>",ContentMode.HTML),grid, confirmation);

        content.setSizeFull();
        
        addComponent(content);
        
        
    }

}
