package tad.grupo1.proyecto.views;


import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Responsive navigation menu presenting a list of available views to the user.
 */
public class TopMenu extends CssLayout {
    private HorizontalLayout searchBar = new HorizontalLayout();

    public TopMenu() {
        searchBar.setMargin(false);
        searchBar.setSpacing(false);
        addComponent(searchBar);
    }

    public HorizontalLayout getTopBar() {
        return searchBar;
    }
    

    
}
