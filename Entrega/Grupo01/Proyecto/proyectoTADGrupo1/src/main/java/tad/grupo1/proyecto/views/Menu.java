package tad.grupo1.proyecto.views;


import com.vaadin.server.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import static tad.grupo1.proyecto.views.MainUI.gc;

/**
 * Responsive navigation menu presenting a list of available views to the user.
 */
public class Menu extends CssLayout {
    private VerticalLayout sidebar = new VerticalLayout();

    public Menu() {
        Image logo = new Image("", new FileResource(
                new File(gc.getLogo())));
        
        //logo.setWidth("30%");

        logo.setWidth("90%");
        
        sidebar.setWidth("5%");
        
        sidebar.setMargin(false);
        sidebar.setSpacing(false);
        sidebar.setSizeFull();
        
        
        sidebar.addComponents(logo);
        
        sidebar.setComponentAlignment(logo,Alignment.MIDDLE_CENTER);
        
        
        
        addComponent(sidebar);
        
    }

    public VerticalLayout getSidebar() {
        return sidebar;
    }
    

    
}
