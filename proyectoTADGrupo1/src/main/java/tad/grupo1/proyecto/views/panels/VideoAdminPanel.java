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
import tad.grupo1.proyecto.objects.UserVideo;
import static tad.grupo1.proyecto.views.MainUI.vc;

/**
 *
 * @author Portatil
 */
public class VideoAdminPanel extends CssLayout implements View {

    private VerticalLayout content;
    private String titulo;
    private String username;

    public VideoAdminPanel() {

        videoManagement();
        
    }
    
    public void videoManagement()
    {
        HorizontalLayout confirmation = new HorizontalLayout();
        content = new VerticalLayout();
        Grid<UserVideo> grid = new Grid<>();

        List<UserVideo> users = vc.getAllVideos();

        grid.setItems(users);
        grid.addColumn(UserVideo::getTitle).setCaption("Titulo");
        grid.addColumn(UserVideo::getUsername).setCaption("Usuario");
        grid.addColumn(UserVideo::getDate).setCaption("Subida");
        grid.addColumn(UserVideo::getLikesCount).setCaption("Likes");
        grid.addColumn(UserVideo::getDislikesCount).setCaption("Dislikes");

        grid.addSelectionListener(event -> {
            Set<UserVideo> selected = event.getAllSelectedItems();
            
            UserVideo aux = (UserVideo) selected.toArray()[0];
            
            titulo = aux.getTitle();
            username = aux.getUsername();
            
            confirmation.removeAllComponents();
            
            Button confirmationButton = new Button("Borrar Video");
            
            confirmation.addComponents(new Label("<h2><b>¿Esta seguro de que quiere borrar el v&iacute;deo "+titulo+"? - Pulse el bot&oacute;n para confirmar</b></h2>",ContentMode.HTML),confirmationButton);
            
            confirmationButton.addStyleName(ValoTheme.BUTTON_DANGER);
            confirmationButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        vc.deleteVideo(username, titulo);
                        users.remove(new UserVideo(titulo));
                        grid.getDataProvider().refreshAll();
                        grid.setHeightByRows(users.size());
                        Notification.show("Éxito",
                        "Se ha borrado el vídeo correctamente",
                        Notification.Type.HUMANIZED_MESSAGE);
                        confirmation.removeAllComponents();
                    }
                });
            
        });

        grid.setHeightByRows(users.size());
        
        content.addComponents(new Label("<h1>Gestion de v&iacute;deos - Pulse un v&iacute;deo para borrarlo</h1>",ContentMode.HTML),new Label("<h2>Puedes pulsar las columnas para ordenar seg&uacute;n ese par&aacute;metro</h2>",ContentMode.HTML),grid, confirmation);

        content.setSizeFull();
        
        addComponent(content);
    }
    
    

}
