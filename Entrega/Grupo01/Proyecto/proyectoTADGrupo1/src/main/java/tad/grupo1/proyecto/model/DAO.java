/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.FileInputStream;
import com.vaadin.server.VaadinService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import tad.grupo1.proyecto.objects.User;
import tad.grupo1.proyecto.objects.UserComment;
import tad.grupo1.proyecto.objects.UserVideo;

public class DAO {

    String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

    MongoClientURI uri = new MongoClientURI(
            "mongodb://mongoUser:mongoPassword@proyectotad2019-shard-00-00-dpclw.azure.mongodb.net:27017,proyectotad2019-shard-00-01-dpclw.azure.mongodb.net:27017,proyectotad2019-shard-00-02-dpclw.azure.mongodb.net:27017/test?ssl=true&replicaSet=proyectoTAD2019-shard-0&authSource=admin&retryWrites=true");

    DBCollection collection = new MongoClient(uri).getDB("database").getCollection("proyectoTAD");

    public FileOutputStream uploadVideo(String username, String filename) {

        FileOutputStream fos = null;

        String videoTitle = filename.substring(0, filename.indexOf("."));

        try {
            String folderPath = basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + videoTitle;
            //Create video folder
            boolean success = (new File(folderPath)).mkdirs();
            if (success) {
                String videoPath = folderPath + File.separator + filename;
                // Open the file for writing.
                File file = new File(videoPath);
                fos = new FileOutputStream(file);
                addVideo(videoTitle, username);

                //Add Default thumb
                copyFile(basepath + File.separator + "thumb.png", folderPath + File.separator + "thumb.png");
            } else {

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fos;
    }

    public FileOutputStream uploadThumbnail(String username, String title, String filename) {

        FileOutputStream fos = null;

        try {
            String folderPath = basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + title;
            //Create video folder
            String thumbPath = folderPath + File.separator + filename;
            // Open the file for writing.
            File file = new File(thumbPath);

            fos = new FileOutputStream(file);

            // file.delete();//Deletes original upload
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fos;
    }

    public void moveThumbnail(String username, String title, String filename) {
        //Primero borramos la miniatura por defecto
        new File(basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + title + File.separator + "thumb.png").delete();

        //Le cambiamos el nombre a la miniatura subida
        new File(basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + title + File.separator + filename).renameTo(new File(basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + title + File.separator + "thumb.png"));
    }

    public void moveProfilePicture(String username, String filename) {
        //Primero borramos la miniatura por defecto
        new File(basepath + File.separator + "users" + File.separator + username + File.separator + "profile.png").delete();

        //Le cambiamos el nombre a la miniatura subida
        new File(basepath + File.separator + "users" + File.separator + username + File.separator + filename).renameTo(new File(basepath + File.separator + "users" + File.separator + username + File.separator + "profile.png"));
    }

    public List<UserVideo> getVideosThatContainInTitle(String word) {
        List<UserVideo> list = new ArrayList<UserVideo>();

        //Encontrar los titulos de video que contains la palabra introducida
        BasicDBObject q = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject("videos.$", 1);
        q.put("videos.title", java.util.regex.Pattern.compile(word));
        //User with the videos that we are looking for
        DBCursor cursor = collection.find(q, fields);

        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            //Cogemos todos sus videos
            BasicDBList currentVideos = (BasicDBList) current.get("videos");

            //Iteramos sobre ellos
            Iterator it = currentVideos.iterator();
            while (it.hasNext()) {

                DBObject currentVideo = (DBObject) it.next();

                String title = currentVideo.get("title").toString();
                String username = getUserWhoUploadedVideo(title);

                list.add(new UserVideo(username, title, (Date) currentVideo.get("date"), (int) currentVideo.get("views"), getVideoThumbnailPath(username, title)));

            }

        }

        return list;
    }

    public void changeUserPassword(String username, String password) {
        DBCollection collection = this.collection;

        Boolean result = false;

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("password", password));

        BasicDBObject searchQuery = new BasicDBObject().append("username", username);

        collection.update(searchQuery, newDocument);
    }

    public UserVideo getVideo(String username, String title) {

        return new UserVideo(title, (Date) getVideoInfo(title, "date"), getVideoPath(username, title), (int) getVideoInfo(title, "views"), (List) getVideoInfo(title, "likes"), (List) getVideoInfo(title, "dislikes"), getVideoComments(title));
    }

    public String getVideoPath(String username, String title) {
        return basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + title + File.separator + title + ".mp4";
    }

    public String getVideoThumbnailPath(String username, String title) {
        return basepath + File.separator + "users" + File.separator + username + File.separator + "videos" + File.separator + title + File.separator + "thumb.png";
    }

    public String getUserProfilePath(String username) {
        return basepath + File.separator + "users" + File.separator + username + File.separator + "profile.png";
    }

    public String getLogoPath() {
        return basepath + File.separator + "logo.png";
    }

    private List<UserComment> getVideoComments(String title) {

        List<UserComment> list = new ArrayList();
        //Cogemos a los comentarios
        BasicDBList comments = (BasicDBList) getVideoInfo(title, "comments");

        //Iteramos sobre ellos
        Iterator it = comments.iterator();
        while (it.hasNext()) {
            DBObject currentComentario = (DBObject) it.next();

            list.add(new UserComment((Date) currentComentario.get("date"), currentComentario.get("comment").toString(), currentComentario.get("username").toString(), title));
        }

        return list;
    }

    public int getUserSuscriptores(String username) {
        return (int) getUserInfo(username, "suscriptores");
    }

    public boolean isUserSuscribed(String user, String uploader) {
        List<String> suscripciones = (List<String>) getUserInfo(user, "suscripciones");

        return suscripciones.contains(uploader);
    }

    public void removeSuscripcion(String user, String uploader) {

        //Quitamos al usuario de su lista de suscripciones
        BasicDBObject newDocument
                = new BasicDBObject().append("$pull",
                        new BasicDBObject().append("suscripciones", uploader));

        collection.update(new BasicDBObject().append("username", user), newDocument);

        //Decrementamos el número de suscriptores
        BasicDBObject newDocument2
                = new BasicDBObject().append("$inc",
                        new BasicDBObject().append("suscriptores", -1));

        collection.update(new BasicDBObject().append("username", uploader), newDocument2);

    }

    public void addSuscripcion(String user, String uploader) {

        //Agregamos al usuario de su lista de suscripciones
        BasicDBObject newDocument
                = new BasicDBObject().append("$push",
                        new BasicDBObject().append("suscripciones", uploader));

        collection.update(new BasicDBObject().append("username", user), newDocument);

        //Incrementamos el número de suscriptores
        BasicDBObject newDocument2
                = new BasicDBObject().append("$inc",
                        new BasicDBObject().append("suscriptores", 1));

        collection.update(new BasicDBObject().append("username", uploader), newDocument2);

    }

    public Object getUserInfo(String username, String searchedParameter) {

        Object result = new Object();

        BasicDBObject whereQuery = new BasicDBObject();
        //Donde el titulo sea el buscado
        whereQuery.put("username", username);

        //Resultado de la buscada
        DBCursor cursor = collection.find(whereQuery);

        //Solamente queda coger el resultado
        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            result = current.get(searchedParameter);

        }

        cursor.close();
        return result;
    }

    public FileOutputStream changeProfilePicture(String username, String filename) {

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(basepath + File.separator + "users" + File.separator + username + File.separator + filename));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fos;
    }

    //Método que busca lo que le pidamos
    private Object getVideoInfo(String title, String searchedParameter) {

        Object result = new Object();

        //Donde el titulo sea el buscado
        BasicDBObject whereQuery = new BasicDBObject("videos.title", title);
        BasicDBObject fields = new BasicDBObject("videos.$", 1);

        //Resultado de la buscada
        DBCursor cursor = collection.find(whereQuery, fields);

        //Solamente queda coger el resultado
        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            //Cogemos todos sus videos
            BasicDBList currentVideos = (BasicDBList) current.get("videos");

            //Iteramos sobre ellos
            Iterator it = currentVideos.iterator();
            while (it.hasNext()) {
                DBObject currentVideo = (DBObject) it.next();
                result = currentVideo.get(searchedParameter);
            }

        }

        cursor.close();
        

        return result;
    }

    public String getUserWhoUploadedVideo(String title) {

        String result = "";

        BasicDBObject whereQuery = new BasicDBObject();
        //Donde el titulo sea el buscado
        whereQuery.put("videos.title", title);

        //Resultado de la buscada
        DBCursor cursor = collection.find(whereQuery);

        //Solamente queda coger el resultado
        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            result = (String) current.get("username");

        }

        cursor.close();
        

        return result;
    }

    public void incrementVideoViews(String title) {
        BasicDBObject newDocument
                = new BasicDBObject().append("$inc",
                        new BasicDBObject().append("videos.$.views", 1));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        ;
    }

    public void unlikeVideo(String title, String username) {

        BasicDBObject newDocument
                = new BasicDBObject().append("$pull",
                        new BasicDBObject().append("videos.$.likes", username));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        
    }

    public void likeVideo(String title, String username) {

        BasicDBObject newDocument
                = new BasicDBObject().append("$push",
                        new BasicDBObject().append("videos.$.likes", username));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        
    }

    public void undislikeVideo(String title, String username) {

        BasicDBObject newDocument
                = new BasicDBObject().append("$pull",
                        new BasicDBObject().append("videos.$.dislikes", username));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        
    }

    public void dislikeVideo(String title, String username) {

        BasicDBObject newDocument
                = new BasicDBObject().append("$push",
                        new BasicDBObject().append("videos.$.dislikes", username));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        
    }

    public void deleteUser(String username) {
        //Remove user from suscripciones

        DBCollection allUsers = this.collection;

        DBCursor cursor = allUsers.find();

        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            List<String> suscripciones = (List<String>) current.get("suscripciones");

            //If user to be deleted is in another users suscripciones
            if (suscripciones.contains(username)) {
                this.removeSuscripcion(current.get("username").toString(), username);
            }
        }
        

        //Remove from database
        BasicDBObject document = new BasicDBObject();
        document.put("username", username);
        this.collection.remove(document);
        ;

        //Remove folders
        try {

            FileUtils.deleteDirectory(new File(basepath + File.separator + "users" + File.separator + username));
        } catch (IOException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void publishComment(String title, String username, String comment) {

        // Crear documento usurio
        BasicDBObject usuario = new BasicDBObject();

        usuario.append("date", new Date());
        usuario.append("username", username);
        usuario.append("comment", comment);

        BasicDBObject newDocument
                = new BasicDBObject().append("$push",
                        new BasicDBObject().append("videos.$.comments", usuario));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        
    }

    private void addVideo(String title, String username) {
        // Crear documento video
        BasicDBObject video = new BasicDBObject();

        video.append("title", title);
        video.append("date", new Date());
        video.append("likes", new ArrayList());
        video.append("dislikes", new ArrayList());
        video.append("views", 0);
        video.append("comments", new ArrayList());

        BasicDBObject newDocument
                = new BasicDBObject().append("$push",
                        new BasicDBObject().append("videos", video));

        collection.update(new BasicDBObject().append("username", username), newDocument);
        
    }

    /**
     * Método para insertar un nuevo usuario
     *
     * @param username
     * @param email
     * @param password
     */
    public void insertUsuario(String username, String email, String password) {
        DBCollection collection = this.collection;

        // Crear documento usurio
        BasicDBObject usuario = new BasicDBObject();

        usuario.append("username", username);
        usuario.append("password", password);
        usuario.append("type", "user");
        usuario.append("email", email);
        usuario.append("suscriptores", 0);

        BasicDBList listSuscripciones = new BasicDBList();
        usuario.append("suscripciones", listSuscripciones);

        BasicDBList listVideos = new BasicDBList();
        usuario.append("videos", listVideos);

        //Introducir usuario en la base de datos
        collection.insert(usuario);
        

        //Crear las carpetas necesarias para el usuario
        String folderPath = basepath + File.separator + "users" + File.separator + username + File.separator + "videos";
        //Create user folder
        new File(folderPath).mkdirs();

        //Add Default profile pciture
        copyFile(basepath + File.separator + "profile.png", basepath + File.separator + "users" + File.separator + username + File.separator + "profile.png");
    }

    /**
     * Método para logarse
     *
     * @param username
     * @param password
     * @return
     */
    public Boolean login(String username, String password) {
        DBCollection collection = this.collection;

        BasicDBObject query = new BasicDBObject("username", username).append("password", password);
        DBObject user = collection.findOne(query);
        
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método que comprueba si el nombre de usuario ya existe
     *
     * @param username
     * @return
     */
    public Boolean getUsername(String username) {
        DBCollection collection = this.collection;
        BasicDBObject query = new BasicDBObject("username", username);
        DBObject user = collection.findOne(query);
        

        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método para obtener las suscripciones de un usuario
     *
     * @param username
     * @return
     */
    public List<String> getSuscripciones(String username) {
        return (List<String>) this.getUserInfo(username, "suscripciones");
    }

    /**
     * Método para obtener los videos de los canales a los que un usuario esta
     * suscrito
     *
     * @param username
     * @return
     */
    public List<UserVideo> getSuscritoVideo(String username) {

        List<UserVideo> list = new ArrayList<UserVideo>();

        //Cogemos todos sus videos
        BasicDBList currentVideos = (BasicDBList) this.getUserInfo(username, "videos");

        //Iteramos sobre ellos
        Iterator it = currentVideos.iterator();
        while (it.hasNext()) {

            DBObject currentVideo = (DBObject) it.next();

            list.add(new UserVideo(username, currentVideo.get("title").toString(), (Date) currentVideo.get("date"), (int) currentVideo.get("views"), getVideoThumbnailPath(username, currentVideo.get("title").toString())));

        }

        return list;

    }

    public List<User> getAllUsers() {
        DBCollection allUsers = this.collection;

        List<User> result = new ArrayList<User>();

        DBCursor cursor = allUsers.find();

        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            result.add(new User(current.get("username").toString(), (int) current.get("suscriptores")));
        }
        
        return result;
    }

    public List<UserVideo> getAllVideos() {
        DBCollection allUsers = this.collection;

        List<UserVideo> result = new ArrayList<UserVideo>();

        DBCursor cursor = allUsers.find();

        //Recorremos todos los usuarios
        while (cursor.hasNext()) {
            DBObject current = cursor.next();

            //Cogemos todos sus videos
            BasicDBList currentVideos = (BasicDBList) current.get("videos");

            //Iteramos sobre ellos
            Iterator it = currentVideos.iterator();
            while (it.hasNext()) {

                DBObject currentVideo = (DBObject) it.next();

                result.add(new UserVideo(currentVideo.get("title").toString(), current.get("username").toString(), (Date) currentVideo.get("date"), (int) currentVideo.get("views"), (ArrayList) currentVideo.get("likes"), (ArrayList) currentVideo.get("dislikes"), getVideoComments(currentVideo.get("title").toString())));

            }
        }
        
        return result;
    }

    public void deleteVideo(String uploader, String title) {

        BasicDBObject match = new BasicDBObject("username", uploader);
        BasicDBObject update = new BasicDBObject("videos", new BasicDBObject("title",title));
        this.collection.update(match, new BasicDBObject("$pull", update));

        //Remove video folder
        //Remove folders
        try {

            FileUtils.deleteDirectory(new File(basepath + File.separator + "users" + File.separator + uploader + File.separator + "videos" + File.separator + title));
        } catch (IOException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isUserAdmin(String username) {
        return this.getUserInfo(username, "type").toString().equals("admin");
    }

    public void deleteComment(Date date, String title) {
        BasicDBObject newDocument
                = new BasicDBObject().append("$pull",
                        new BasicDBObject().append("videos.$.comments",
                                new BasicDBObject().append("date", date)));

        collection.update(new BasicDBObject().append("videos.title", title), newDocument);
        
    }

    public List<UserComment> getAllComments() {
        List<UserVideo> videos = this.getAllVideos();
        List<UserComment> result = new ArrayList<UserComment>();

        Iterator it = videos.iterator();
        //Recorremos todos los comentarios de los videos
        while (it.hasNext()) {
            UserVideo current = (UserVideo) it.next();

            result.addAll(current.getComments());

        }

        return result;
    }

    public void copyFile(String original, String destination) {

        InputStream inStream = null;
        OutputStream outStream = null;

        try {

            File afile = new File(original);
            File bfile = new File(destination);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes 
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
