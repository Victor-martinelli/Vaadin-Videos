/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import tad.grupo1.proyecto.objects.UserVideo;
import tad.grupo1.proyecto.views.MainScreen;
import static tad.grupo1.proyecto.views.MainUI.vc;

/**
 *
 * @author victormartinelli
 */
public class ResultadoBusquedaVideosPanel extends CssLayout implements View {

    private VerticalLayout content;
    private MainScreen layout;

    public ResultadoBusquedaVideosPanel(MainScreen layout, String word) {
        this.layout = layout;

        content = new VerticalLayout();

        Label result = new Label("<h2>Resultado de buscar v&iacute;deos que contengan la palabra: '" + word + "'</h2>", ContentMode.HTML);

        VerticalLayout resultVideos = getMatchingVideos(word);

        content.addComponents(result, resultVideos);

        addComponent(content);

    }

    public VerticalLayout getMatchingVideos(String word) {

        List<UserVideo> videoResults = vc.getVideosBusqueda(word);

        VerticalLayout result = new VerticalLayout();

        Iterator it = videoResults.iterator();

        while (it.hasNext()) {
            HorizontalLayout aux = new HorizontalLayout();
            VerticalLayout videoInfo = new VerticalLayout();
            UserVideo auxVideo = (UserVideo) it.next();
            Image img = new Image("",new FileResource(
                new File(auxVideo.getThumbPath())));
            Label title = new Label("<b>" + auxVideo.getTitle() + "</b>", ContentMode.HTML);
            Label username = new Label("<b>usuario: " + auxVideo.getUsername() + "</b>", ContentMode.HTML);
            Label date = new Label("Publicado el: " + auxVideo.getDate());
            Label views = new Label(auxVideo.getViews() + " visitas");

            img.setWidth("15em");

            img.addClickListener(e -> layout.createVideoPanel(auxVideo.getUsername(), auxVideo.getTitle()));

            img.addStyleName("my-img-button");

            videoInfo.addComponents(title, date, username,views);

            aux.addComponents(img, videoInfo);

            result.addComponent(aux);

        }

        return result;
    }

}
