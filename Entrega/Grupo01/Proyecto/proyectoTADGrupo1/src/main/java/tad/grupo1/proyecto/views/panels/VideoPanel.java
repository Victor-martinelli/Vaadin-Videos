/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.shared.ui.PreloadMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.Iterator;
import org.vaadin.gwtav.GwtVideo;
import tad.grupo1.proyecto.objects.User;
import tad.grupo1.proyecto.objects.UserComment;
import tad.grupo1.proyecto.objects.UserVideo;
import tad.grupo1.proyecto.views.MainScreen;
import static tad.grupo1.proyecto.views.MainUI.session;
import static tad.grupo1.proyecto.views.MainUI.uc;
import static tad.grupo1.proyecto.views.MainUI.vc;

/**
 *
 * @author Portatil
 */
public class VideoPanel extends CssLayout implements View {

    boolean clickLikeButton = false;
    boolean clickDislikeButton = false;
    boolean suscribeButton = false;
    Label uploaderUsernameLabel;
    Label suscribeStatus;
    UserVideo video;
    Label likesLabel;
    Label dislikesLabel;
    private MainScreen layout;
    
    public VideoPanel(MainScreen layout, String username, String videoTitle) {
        
        this.layout = layout;
        
        VerticalLayout content = new VerticalLayout();
        VerticalLayout layoutForVideoInfoPanel = new VerticalLayout();
        VerticalLayout usernameAndDate = new VerticalLayout();
        VerticalLayout commentSection = new VerticalLayout();
        HorizontalLayout uploaderInfo = new HorizontalLayout();
        HorizontalLayout videoInfo = new HorizontalLayout();
        HorizontalLayout interactionsInfo = new HorizontalLayout();
        Panel commentPanel = new Panel();
        Panel uploaderInfoPanel = new Panel();
        Panel videoInfoPanel = new Panel();
        
        User uploader = new User(username,uc.getSuscriptores(username));
        video = vc.playVideo(username, videoTitle);

        Label videoTitleLabel = new Label("<h1>" + videoTitle + "</h1>", ContentMode.HTML);
        Label commentsTitle = new Label("<h2>Comentarios</h2>", ContentMode.HTML);
        Label videoDateLabel = new Label("<p>Publicado el: " + video.getDate() + "</p>", ContentMode.HTML);
        Label viewsLabel = new Label("<h2>" + video.getViews() + " visitas</h2>", ContentMode.HTML);
        uploaderUsernameLabel= new Label("<h3>" + uploader.getUsername() + " - suscriptores: "+uploader.getSuscriptores()+"</h3>", ContentMode.HTML);
        likesLabel = new Label("<p>" + video.getLikesCount() + "</p>", ContentMode.HTML);
        dislikesLabel = new Label("<p>" + video.getDislikesCount() + "</p>", ContentMode.HTML);

        Button subscribeButton = createSuscribeButton(uploader);
        Button likesButton = createInteractionButton(1, session.getAttribute("user").toString(), likesLabel);
        Button dislikesButton = createInteractionButton(0, session.getAttribute("user").toString(), dislikesLabel);
        Button sendCommentButton = new Button("Publicar Comentario");
        TextArea comment = new TextArea("Tu comentario");

        content.setSizeFull();
        commentSection.setSizeFull();
        videoInfo.setSizeFull();
        videoInfo.setMargin(true);
        uploaderInfo.setMargin(true);
        uploaderInfo.setSpacing(true);
        content.setMargin(false);
        content.setSpacing(false);

        videoTitleLabel.setWidth(null);
        likesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        dislikesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        subscribeButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        comment.setWordWrap(true);
        comment.setSizeFull();

        GwtVideo sample = new GwtVideo();
        sample.setPreload(PreloadMode.NONE);

        final Resource mp4Resource = new FileResource(
                new File(video.getVideoPath()));

        Image profile = new Image("", new FileResource(
                new File(uc.getProfilePicture(username))));
        profile.addClickListener( e -> this.layout.createCanalView(username));
        
        profile.addStyleName("my-img-button");
        
        sendCommentButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        vc.publishComment(video.getTitle(),session.getAttribute("user").toString(),comment.getValue());
                        new Notification("AVISO", "Comentario Publicado", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        video.addComment(session.getAttribute("user").toString(),comment.getValue());
                        comment.clear();
                        commentSection.removeAllComponents();
                        commentSection.addComponents(commentsTitle,comment,sendCommentButton,getComments());
                    }
                });
        
        
        
        

        profile.setWidth("5em");

        sample.setSource(mp4Resource);
        sample.setResponsive(true);
        sample.setHtmlContentAllowed(true);
        sample.setShowControls(true);
        //sample.setWidth("100em");
        //sample.setWidth("100%");
        sample.setWidth("1000px");
        sample.setAltText("Can't play media");

        /*
        sample.ssetPoster(new FileResource(
                new File(vc.getVideoThumbnail(username, videoTitle)))); */
        commentSection.addComponents(commentsTitle,comment,sendCommentButton,getComments());
        commentPanel.setContent(commentSection);
        usernameAndDate.addComponents(uploaderUsernameLabel, videoDateLabel);
        interactionsInfo.addComponents(likesButton, likesLabel, dislikesButton, dislikesLabel);
        videoInfo.addComponents(viewsLabel, interactionsInfo);
        uploaderInfo.addComponents(profile, usernameAndDate, subscribeButton,suscribeStatus);
        
        
        layoutForVideoInfoPanel.addComponents(videoInfo,uploaderInfo);
        videoInfoPanel.setContent(layoutForVideoInfoPanel);
        
        
        //VerticalLayout solamente es para los espacios
        content.addComponents(videoTitleLabel, sample, new VerticalLayout(),videoInfoPanel, uploaderInfoPanel,new VerticalLayout(),commentPanel);

        videoInfo.setComponentAlignment(interactionsInfo, Alignment.MIDDLE_RIGHT);
        uploaderInfo.setComponentAlignment(subscribeButton, Alignment.MIDDLE_RIGHT);
        uploaderInfo.setComponentAlignment(suscribeStatus, Alignment.MIDDLE_RIGHT);

        
        
        addComponent(content);

    }

    private Button createSuscribeButton(User uploader)
    {
        String currentUser = session.getAttribute("user").toString();
        
        Button interactionButton = new Button("Suscríbete");
        
        //El usuario esta susrito al que ha subido el video
        if(uc.isUserSuscribed(currentUser, uploader.getUsername()))
        {
            suscribeButton= true;
            suscribeStatus = new Label("<h3>Suscrito</h3>",ContentMode.HTML);
        } //El usuario no esta susrito al que ha subido el video
        else
        {
            suscribeStatus = new Label("<h3>Suscr&iacute;bete</h3>",ContentMode.HTML);
        }
        
        interactionButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        
                        //Si estamos suscritos
                        if(suscribeButton)
                        {
                            uc.removeSuscripcion(currentUser, uploader.getUsername());
                            uploader.removeSuscriptores();
                            suscribeButton=false;
                            suscribeStatus.setValue("<h3>Suscr&iacute;bete</h3>");
                            new Notification("AVISO", "Ya no estás suscrito", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        }
                        else //Si no estabamos suscritos
                        {
                             uc.addSuscripcion(currentUser, uploader.getUsername());
                            uploader.addSuscriptores();
                            suscribeButton=true;
                            suscribeStatus.setValue("<h3>Suscrito</h3>");
                            new Notification("AVISO", "Te has suscrito", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        }
                        
                        uploaderUsernameLabel.setValue("<h3>" + uploader.getUsername() + " - suscriptores: "+uploader.getSuscriptores()+"</h3>");
                    }
                });
        
        return interactionButton;
    }
    
    
    private Button createInteractionButton(int buttonType, String username, Label count) {
        /*
        0 --> Like
        1 --> Dislike
         */

        Button interactionButton = null;

        if (buttonType == 1) {
            interactionButton = new Button(FontAwesome.THUMBS_UP);
            if (video.hasUserLikedVideo(username)) {
                clickLikeButton = true;
            }
                interactionButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {

                        //Video ya tenia like
                        if (clickLikeButton) {
                            clickLikeButton = false;
                            vc.unlikeVideo(video.getTitle(), username);
                            video.removeLike(username);
                            new Notification("AVISO", "Ya no te gusta el vídeo", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        } else {
                            //Comprobamos si esta en dislike
                            if (video.hasUserDisLikedVideo(username)) {
                                video.removeDislike(username);
                                clickDislikeButton = false;
                                dislikesLabel.setValue(Integer.toString(video.getDislikesCount()));
                                vc.undislikeVideo(video.getTitle(), username);
                            }
                            clickLikeButton = true;
                            video.addLike(username);
                            vc.likeVideo(video.getTitle(), username);
                            new Notification("AVISO", "Te gusta el vídeo", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        }
                        count.setValue(Integer.toString(video.getLikesCount()));
                    }
                });
        } else {
            interactionButton = new Button(FontAwesome.THUMBS_DOWN);
            if (video.hasUserDisLikedVideo(username)) {
                clickDislikeButton = true;
            }
                interactionButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {

                        //Video ya tenia like
                        if (clickDislikeButton) {
                            clickDislikeButton = false;
                            vc.undislikeVideo(video.getTitle(), username);
                            video.removeDislike(username);
                            new Notification("AVISO", "Quitado dislike", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        } else {
                            //Comprobamos si esta en like
                            if (video.hasUserLikedVideo(username)) {
                                video.removeLike(username);
                                clickLikeButton = false;
                                likesLabel.setValue(Integer.toString(video.getLikesCount()));
                                vc.unlikeVideo(video.getTitle(), username);
                            }
                            clickDislikeButton = true;
                            video.addDisLike(username);
                            vc.dislikeVideo(video.getTitle(), username);
                            new Notification("AVISO", "No te gusta el vídeo", Notification.TYPE_TRAY_NOTIFICATION).show(Page.getCurrent());
                        }
                        count.setValue(Integer.toString(video.getDislikesCount()));
                    }
                });

        }

        return interactionButton;
    }

    
    private VerticalLayout getComments()
    {
        VerticalLayout comments = new VerticalLayout();
                
        Iterator it = video.getComments().iterator();
        
        while(it.hasNext())
        {
            VerticalLayout aux = new VerticalLayout();
            UserComment comment = (UserComment)it.next();
            
            aux.addComponents(new Label("<b>"+comment.getUsername()+"</b>",ContentMode.HTML),new Label(comment.getDate(),ContentMode.HTML),new Label("<i>"+comment.getComment()+"</i>",ContentMode.HTML));
            comments.addComponent(aux);
        }
                
        return comments;
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
