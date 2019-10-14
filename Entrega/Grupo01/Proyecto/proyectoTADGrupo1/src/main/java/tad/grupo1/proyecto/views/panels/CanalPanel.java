package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.util.List;
import tad.grupo1.proyecto.objects.User;
import tad.grupo1.proyecto.objects.UserVideo;
import tad.grupo1.proyecto.views.MainScreen;
import static tad.grupo1.proyecto.views.MainUI.sc;
import static tad.grupo1.proyecto.views.MainUI.session;
import static tad.grupo1.proyecto.views.MainUI.uc;

public class CanalPanel extends CssLayout implements View {

    String username = session.getAttribute("user").toString();
    boolean suscribeButton = false;
    Label suscribeStatus;
    
    private MainScreen layout;

    public CanalPanel(MainScreen layout, String userChannel) {
        this.layout = layout;
        VerticalLayout view = new VerticalLayout();
        view.setSizeFull();
        view.addComponent(buildCanalInfo(userChannel));
        Label divider = new Label("<hr />", ContentMode.HTML);
        divider.setWidth(100f, Sizeable.Unit.PERCENTAGE);
        view.addComponent(divider);
        view.addComponent(buildVideos(userChannel));
        addComponent(view);
    }

    private HorizontalLayout buildCanalInfo(String userChannel) {
        HorizontalLayout canalInfo = new HorizontalLayout();
        canalInfo.setSizeFull();
        User uploader = new User(userChannel, uc.getSuscriptores(userChannel));
        Image profile = new Image("", new FileResource(
                new File(uc.getProfilePicture(userChannel))));
        profile.setWidth("7em");
        VerticalLayout vl = new VerticalLayout();
        Label nombreCanal = new Label("<h2><b>" + userChannel + "</b></h2>", ContentMode.HTML);
        Label suscriptores = new Label("Suscriptores: "+uploader.getSuscriptores());
        vl.addComponents(nombreCanal, suscriptores);
        Button boton;
        if (username.equals(userChannel)) {
            boton = new Button("Configurar");
            boton.addClickListener( e -> layout.createConfView(userChannel));
            suscribeStatus = new Label("");
        } else {
            boton = createSuscribeButton(uploader);    
        }
        canalInfo.addComponents(profile, vl, boton,suscribeStatus);
        canalInfo.setComponentAlignment(boton, Alignment.MIDDLE_RIGHT);
        return canalInfo;
    }

    private VerticalLayout buildVideos(String userChannel) {
        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout videos = new HorizontalLayout();
        vl.addComponent(new Label("<h2><b>Videos</b></h2>", ContentMode.HTML));
        List<UserVideo> listVideo = (List<UserVideo>) sc.getVideoSuscrito(userChannel);
        if (listVideo.size() <= 0) {
            Label label = new Label("Este canal no tiene videos");
            videos.addComponent(label);
        } else {
            VerticalLayout canalLayout = new VerticalLayout();
            canalLayout.setSpacing(true);
            canalLayout.setMargin(true);
            videos.addComponent(canalLayout);

            for (UserVideo video : listVideo) {

                VerticalLayout vlayout = new VerticalLayout();

                Label tituloLabel = new Label("<h3>" + video.getTitle() + "</h3>", ContentMode.HTML);
                
                Image thumb = new Image("", new FileResource(new File(video.getThumbPath())));
                thumb.setWidth("15em");
                thumb.addClickListener( e -> layout.createVideoPanel(userChannel, video.getTitle()));
                thumb.addStyleName("my-img-button");

                vlayout.addComponents(thumb, tituloLabel);

                canalLayout.addComponent(vlayout);
            }
        }
        vl.addComponent(videos);
        return vl;
    }

    private Button createSuscribeButton(User uploader) {
        
        String currentUser = session.getAttribute("user").toString();

        Button interactionButton = new Button("Suscríbete");

        //El usuario esta susrito al que ha subido el video
        if (uc.isUserSuscribed(currentUser, uploader.getUsername())) {
            suscribeButton = true;
            suscribeStatus = new Label("<h3>Suscrito</h3>", ContentMode.HTML);
        } //El usuario no esta susrito al que ha subido el video
        else {
            suscribeStatus = new Label("<h3>Suscríbete</h3>", ContentMode.HTML);
        }

        interactionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                //Si estamos suscritos
                if (suscribeButton) {
                    uc.removeSuscripcion(currentUser, uploader.getUsername());
                    uploader.removeSuscriptores();
                    suscribeButton = false;
                    suscribeStatus.setValue("<h3>Suscríbete</h3>");
                    new Notification("AVISO", "Ya no estás suscrito", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                } else //Si no estabamos suscritos
                {
                    uc.addSuscripcion(currentUser, uploader.getUsername());
                    uploader.addSuscriptores();
                    suscribeButton = true;
                    suscribeStatus.setValue("<h3>Suscrito</h3>");
                    new Notification("AVISO", "Te has suscrito", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                }

            }
        });

        return interactionButton;
    }
}
