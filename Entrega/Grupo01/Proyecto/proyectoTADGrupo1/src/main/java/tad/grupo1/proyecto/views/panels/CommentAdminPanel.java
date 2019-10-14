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
import java.util.Date;
import java.util.List;
import java.util.Set;
import tad.grupo1.proyecto.objects.UserComment;
import static tad.grupo1.proyecto.views.MainUI.vc;

/**
 *
 * @author Portatil
 */
public class CommentAdminPanel extends CssLayout implements View {

    private VerticalLayout content;
    private String username;
    private Date date;
    private String comentario;
    private String videoTitle;

    public CommentAdminPanel() {

        commentManagement();
        
    }
    
    
    public void commentManagement()
    {
        
        HorizontalLayout confirmation = new HorizontalLayout();
        content = new VerticalLayout();
        Grid<UserComment> grid = new Grid<>();

        List<UserComment> users = vc.getAllComments();

        grid.setItems(users);
        grid.addColumn(UserComment::getVideoTitle).setCaption("Vídeo");
        grid.addColumn(UserComment::getUsername).setCaption("Usuario");
        grid.addColumn(UserComment::getDate).setCaption("Fecha");
        grid.addColumn(UserComment::getComment).setCaption("Comentario");

        grid.addSelectionListener(event -> {
            Set<UserComment> selected = event.getAllSelectedItems();
            
            UserComment aux = (UserComment) selected.toArray()[0];
            
            username = aux.getUsername();
            date = aux.getRawDate();
            comentario = aux.getComment();
            videoTitle = aux.getVideoTitle();
            
            confirmation.removeAllComponents();
            
            Button confirmationButton = new Button("Borrar comentario");
            
            confirmation.addComponents(new Label("<h2><b>¿Esta seguro que quiere borrar el comentario del usuario "+username+" que dice <br>'"+comentario+"'? - Pulse el bot&oacute;n para confirmar</b></h2>",ContentMode.HTML),confirmationButton);
            
            confirmationButton.addStyleName(ValoTheme.BUTTON_DANGER);
            confirmationButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        vc.deleteComment(date,videoTitle);
                        users.remove(new UserComment(date));
                        grid.getDataProvider().refreshAll();
                        grid.setHeightByRows(users.size());
                        grid.setWidth("50%");
                        Notification.show("Exito",
                        "Se ha borrado el comentario correctamente",
                        Notification.Type.HUMANIZED_MESSAGE);
                        confirmation.removeAllComponents();
                    }
                });
            
        });

        grid.setHeightByRows(users.size());
        grid.setWidth("50%");
        
        content.addComponents(new Label("<h1>Gesti&oacute;n de comentarios - Pulse un comentario para borrarlo</h1>",ContentMode.HTML),new Label("<h2>Puedes pulsar las columnas para ordenar seg&uacute;n ese par&aacute;metro</h2>",ContentMode.HTML),grid, confirmation);

        //grid.setSizeFull();
        content.setSizeFull();
        addComponent(content);
        
        
    }

}
