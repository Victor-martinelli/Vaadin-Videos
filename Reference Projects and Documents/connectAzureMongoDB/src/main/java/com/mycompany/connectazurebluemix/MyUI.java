package com.mycompany.connectazurebluemix;



import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        MongoClientURI uri = new MongoClientURI(
                "mongodb://mongoUser:mongoPassword@proyectotad2019-shard-00-00-dpclw.azure.mongodb.net:27017,proyectotad2019-shard-00-01-dpclw.azure.mongodb.net:27017,proyectotad2019-shard-00-02-dpclw.azure.mongodb.net:27017/test?ssl=true&replicaSet=proyectoTAD2019-shard-0&authSource=admin&retryWrites=true");
        
        DBCursor cursor=null;
        
        MongoClient mongoClient = null;
        mongoClient = new MongoClient(uri);
        DB db = mongoClient.getDB("test");
        layout.addComponent(new Label("Connected"));
        DBCollection collection = db.getCollection("test");
        /*
        
        cursor = collection.find();
        int i = 1;
        while (cursor.hasNext()) {
            
            DBObject current = cursor.next();
            current.put("text","REEEEEEEEEEEEEE");
            //layout.addComponent(new Label("Documento Leido "+i+cursor.next()));
            i++;
        }
        
        cursor.close();

        */
        
        BasicDBObject newDocument = new BasicDBObject();
	newDocument.put("text", "REEEEEEEEEEEEEEEE");
        
        BasicDBObject searchQuery = new BasicDBObject().append("text", "this is a test string");

	collection.update(searchQuery, newDocument);
        

        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
        
        
        
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
