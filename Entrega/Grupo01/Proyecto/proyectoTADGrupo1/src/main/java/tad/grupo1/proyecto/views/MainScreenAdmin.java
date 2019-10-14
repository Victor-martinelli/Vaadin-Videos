package tad.grupo1.proyecto.views;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import static tad.grupo1.proyecto.views.MainUI.session;
import tad.grupo1.proyecto.views.panels.CommentAdminPanel;
import tad.grupo1.proyecto.views.panels.UserAdminPanel;
import tad.grupo1.proyecto.views.panels.VideoAdminPanel;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreenAdmin extends HorizontalLayout {
    
    HorizontalLayout page;
    VerticalLayout videoContainer;
    VerticalLayout content;

    public MainScreenAdmin() {
        content = new VerticalLayout();
        
        
       
        content.setSizeFull();
        
        Button closeSession = new Button("Cerrar Sesión");
        
         closeSession.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        
                        Notification.show("Exito",
                        "Se ha cerrado la sesion correctamente, ya puede cerrar esta ventana.",
                        Notification.Type.HUMANIZED_MESSAGE);
                        session.invalidate();
                    }
                });
         
      content.addComponents(new UserAdminPanel(),new VideoAdminPanel(),new CommentAdminPanel(),closeSession);
        
        addComponents(content);
    }
    

    
}
