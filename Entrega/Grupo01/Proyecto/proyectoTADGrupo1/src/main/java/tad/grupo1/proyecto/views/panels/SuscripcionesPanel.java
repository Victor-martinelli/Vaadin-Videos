/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.util.List;
import tad.grupo1.proyecto.objects.UserVideo;
import tad.grupo1.proyecto.views.MainScreen;
import static tad.grupo1.proyecto.views.MainUI.sc;

/**
 *
 * @author Lydia
 */
public class SuscripcionesPanel extends CssLayout implements View {

    MainScreen ms;
    
    public SuscripcionesPanel(MainScreen ms,String username) {
        
        this.ms=ms;
        VerticalLayout suscripcionesLayout = new VerticalLayout();
        suscripcionesLayout.setSpacing(true);
        suscripcionesLayout.setMargin(true);

        List<String> listSuscripciones = (List<String>) sc.getSuscripciones(username);

        Label suscripcionesLabel = new Label("<h1><b>Suscripciones</b></h1>", ContentMode.HTML);
        suscripcionesLayout.addComponent(suscripcionesLabel);
        suscripcionesLayout.setComponentAlignment(suscripcionesLabel, Alignment.MIDDLE_CENTER);
        
        if (listSuscripciones.size() < 0) {
            Label label = new Label("No se ha suscrito a ningun canal todavía.");
            suscripcionesLayout.addComponent(label);
        } else {
            for (String nombre : listSuscripciones) {
                List<UserVideo> listVideo = (List<UserVideo>) sc.getVideoSuscrito(nombre);

                
                Label nombreCanalLabel = new Label("<h2><b>" + nombre + "</b></h2>", ContentMode.HTML);
                suscripcionesLayout.addComponents(nombreCanalLabel);

                if (listVideo.size() < 0) {
                    Label label = new Label("Este canal no tiene videos");
                    suscripcionesLayout.addComponent(label);
                } else {
                    Panel panel = new Panel();
                    VerticalLayout result = new VerticalLayout();
                    
                    
                    panel.setContent(result);
                    suscripcionesLayout.addComponent(panel);
                    for (UserVideo video : listVideo) {
                        
                        HorizontalLayout aux = new HorizontalLayout();
                        VerticalLayout videoInfo = new VerticalLayout();

                        Label title = new Label("<b>" + video.getTitle() + "</b>", ContentMode.HTML);
                        Label date = new Label("Publicado el: " + video.getDate());
                        Label views = new Label(video.getViews() + " visitas");

                        Image img = new Image("", new FileResource(
                                new File(video.getThumbPath())));
                        
                        img.setWidth("15em");

                        img.addClickListener(e -> ms.createVideoPanel(video.getUsername(), video.getTitle()));

                        img.addStyleName("my-img-button");

                        videoInfo.addComponents(title, date,views);

                        aux.addComponents(img, videoInfo);

                        result.addComponent(aux);
                        
                        aux.setSpacing(true);
                        aux.setMargin(true);
                        
                    }
                }

            }
        }

        suscripcionesLayout.setSizeFull();
        
        addComponent(suscripcionesLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
