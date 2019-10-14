/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.views.panels;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import tad.grupo1.proyecto.objects.FileUploader;
import static tad.grupo1.proyecto.views.MainUI.session;

/**
 *
 * @author Portatil
 */
public class SubirVideoPanel extends CssLayout implements View{
    
    private VerticalLayout content;
    private String username;
    private String title;
    
    public SubirVideoPanel(String username)
    {
    
        Panel panel = new Panel();
        
        this.username=username;
        
        VerticalLayout labels = new VerticalLayout();
        
        Label title = new Label("<h2>Subida de Videos</h2>",ContentMode.HTML);
        Label warning = new Label("Ten en cuenta que el titulo del video sera el nombre del archivo sin la extensi&oacute;n y que solamente se aceptan v&iacute;deos en formato mp4",ContentMode.HTML);
        Upload upload = createVideoUploadForm(username);
        
        content = new VerticalLayout();
        
        labels.addComponents(title,warning);
        labels.setSizeFull();
        labels.setMargin(false);
        labels.setSpacing(false);
        content.setSizeFull();
        
        content.addComponents(labels,upload);
        
        labels.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        labels.setComponentAlignment(warning, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
        
        
        
        content.setComponentAlignment(labels, Alignment.MIDDLE_RIGHT);
        
        panel.setContent(content);
        
        panel.setSizeFull();
        
        addComponent(panel);
        
    }
    
    public void createSecondUploader()
    {
        Panel panel = new Panel();
        
        this.removeAllComponents();
        
        VerticalLayout labels = new VerticalLayout();
        
        String videoTitle = session.getAttribute("currentVideo").toString();
        
        session.setAttribute("curretnVideo", null);
        
        Label title = new Label("<h2>Subida de Miniatura</h2>",ContentMode.HTML);
        Label warning = new Label("Sube una miniatura para tu video, si no subes una se aplicara una por defecto",ContentMode.HTML);
        Upload upload = createThumbUploadForm(username,videoTitle);
        
        content.removeAllComponents();
        
        content.addComponents(labels,upload);
        labels.addComponents(title,warning);
        labels.setSizeFull();
        labels.setMargin(false);
        labels.setSpacing(false);
        content.setSizeFull();
        
        

        labels.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        labels.setComponentAlignment(warning, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(labels, Alignment.MIDDLE_RIGHT);
        
        panel.setContent(content);
        
        panel.setSizeFull();
        
        addComponent(panel);
    }
    
    public Upload createVideoUploadForm(String username)
    {
        
        FileUploader receiver = new FileUploader(this,username,1);
        
        Upload upload = receiver.createVideoUploadForm();
        
        upload.addSucceededListener(receiver);
        
        return upload;
    }
    
    public Upload createThumbUploadForm(String username,String title)
    {
    
        FileUploader receiver = new FileUploader(this,username,0,title);
        
        Upload upload = receiver.createThumbUploadForm();
        
        upload.addSucceededListener(receiver);
        
        return upload;
    }
    
}
