/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.objects;

/**
 *
 * @author Portatil
 */
//Esto solamente lo utiliza para guardar archivos
import com.vaadin.server.Page;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import org.apache.commons.io.output.NullOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import static tad.grupo1.proyecto.views.MainUI.session;
import static tad.grupo1.proyecto.views.MainUI.uc;
import static tad.grupo1.proyecto.views.MainUI.vc;
import tad.grupo1.proyecto.views.panels.SubirVideoPanel;

public class FileUploader implements Upload.Receiver, SucceededListener {

    private String username;
    /*
            0 --> Comprobar thumnail
            1 --> Comprobar video
            2 --> Comprobar foto de perfil
     */
    private int typeCheck;
    private CssLayout layout;
    private String title;

    public FileUploader(CssLayout layout, String username, int typeCheck) {
        this.username = username;
        this.typeCheck = typeCheck;
        this.layout = layout;
    }

    public FileUploader(CssLayout layout, String username, int typeCheck, String title) {
        this.username = username;
        this.typeCheck = typeCheck;
        this.layout = layout;
        this.title = title;
    }

    public OutputStream receiveUpload(String filename,
            String mimeType) {

        // Create upload stream
        FileOutputStream fos = null; // Stream to write to

        if (typeCheck == 1) {
            //We only allow videos
            if (mimeType.equals("video/mp4")) {
                
                //We check if there is a video with that name already uploaded
                
                String videoName =filename.substring(0, filename.indexOf(".")); 
                
                if(vc.getVideosBusqueda(videoName).isEmpty()) //There is no video with that name
                {
                     session.setAttribute("currentVideo", videoName);
                     fos = vc.uploadVideo(username, filename);
                }
                else
                {
                    new Notification("ERROR",
                        "Ya existe un video con ese nombre en la plataforma, por favor cambie el nombre e intentelo de nuevo.",
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                     return new NullOutputStream();
                }
               
            } else {
                new Notification("ERROR",
                        "Solamente se admiten videos en formato .mp4",
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                return new NullOutputStream();
            }

        } //Create Thumbnail Upload Stream
        else if (typeCheck == 0) {
            if (mimeType.equals("image/png"))//We only allow pngs
            {
                session.setAttribute("currentThumb", filename);
                fos = vc.uploadThumnbail(username, title, filename);
            } else {
                new Notification("ERROR",
                        "Solamente se admiten imagenes en formato .png",
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                return new NullOutputStream();
            }
        }
        else if(typeCheck==2) //Foto de perfil
        {
            if (mimeType.equals("image/png"))//We only allow pngs
            {
                session.setAttribute("currentThumb", filename);
                fos = uc.changeProfilePicture(username, filename);
            } else {
                new Notification("ERROR",
                        "Solamente se admiten imagenes en formato .png",
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                return new NullOutputStream();
            }
        }

        return fos; // Return the output stream to write to
    }

    //What happends when upload is successul
    public void uploadSucceeded(SucceededEvent event) {
        //Estabamos comprobando un video
        if (typeCheck == 1) {
            //If upload didnt fail
            if (session.getAttribute("currentVideo") != null) {
                Notification.show("Exito",
                        "El video se ha subido correctamente",
                        Notification.Type.HUMANIZED_MESSAGE);

                SubirVideoPanel aux = (SubirVideoPanel) layout;

                aux.createSecondUploader();
            }

        } else if (typeCheck == 0) {
            //If Upload didnt fail
            if (session.getAttribute("currentThumb") != null) {
                Notification.show("Exito",
                        "Se ha subido la miniatura correctamente, ya puede ver el video accediendo a su canal",
                        Notification.Type.HUMANIZED_MESSAGE);
                vc.moveThumnbail(username, title, session.getAttribute("currentThumb").toString());

                session.setAttribute("currentThumb", null);
            }
        }
        else if(typeCheck == 2)
        {
            Notification.show("Exito",
                        "Se ha cambiado su foto de perfil correctamente, por favor refresque la página para ver los cambios",
                        Notification.Type.HUMANIZED_MESSAGE);
            uc.moveProfilePicture(username,session.getAttribute("currentThumb").toString());
        }
    }

    public Upload createThumbUploadForm() {

        return new Upload("Sube la imagen aquí", this);
    }

    public Upload createVideoUploadForm() {

        return new Upload("Sube el video aquí", this);
    }

    public Upload createProfileUploadForm() {

        return new Upload("Sube el video aquí tu foto de perfil", this);
    }
    
};
