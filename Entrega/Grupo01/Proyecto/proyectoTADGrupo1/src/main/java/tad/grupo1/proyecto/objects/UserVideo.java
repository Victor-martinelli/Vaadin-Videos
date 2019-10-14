package tad.grupo1.proyecto.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Portatil
 */
public class UserVideo {
    
    private String title;
    private String username;
    private Date date;
    private String videoPath;
    private String thumbPath;
    private int views;
    private List likes;
    private List dislikes;
    private List<UserComment> comments;

    public UserVideo(String title, Date date, String videoPath, int views, List likes, List dislikes, List<UserComment> comments) {
        this.title = title;
        this.date = date;
        this.videoPath = videoPath;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
    }

    public UserVideo(String title, String username, Date date, int views, List likes, List dislikes,List<UserComment> comments) {
        this.title = title;
        this.username = username;
        this.date = date;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
    }
    
    

    public UserVideo(String username,String title, Date date, int views,String thumbPath) {
        this.username = username;
        this.title = title;
        this.date = date;
        this.views = views;
        this.thumbPath = thumbPath;
    }

    public UserVideo(String title) {
        this.title = title;
    }
    
    
    

    public List getLikes() {
        return likes;
    }
    
    public int getLikesCount()
    {
        return getLikes().size();
    }

    public void setLikes(List likes) {
        this.likes = likes;
    }

    public List getDislikes() {
        return dislikes;
    }
    
    public int getDislikesCount()
    {
        return getDislikes().size();
    }

    public void setDislikes(List dislikes) {
        this.dislikes = dislikes;
    }

    public void addLike(String username)
    {
        likes.add(username);
    }
    
    public void removeLike(String username)
    {
        likes.remove(username);
    }
    
    public void addDisLike(String username)
    {
        dislikes.add(username);
    }
    
    public void removeDislike(String username)
    {
        dislikes.remove(username);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void addComment(String username, String comment)
    {
        comments.add(new UserComment(new Date(),comment,username));
    }

    public String getDate() {
        
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 

       return formatter.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    


    public List<UserComment> getComments() {
        
        //Sorted by date
        
        List<UserComment> reverse = new ArrayList<UserComment>(comments);
        
        Collections.reverse(reverse);
        
        return reverse;
    }

    public void setComments(List<UserComment> comments) {
        this.comments = comments;
    }
    
    public boolean hasUserLikedVideo(String username)
    {
        return likes.contains(username);
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.title);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserVideo other = (UserVideo) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
    }
    
    
    
    
    public boolean hasUserDisLikedVideo(String username)
    {
        return dislikes.contains(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserVideo{" + "title=" + title + ", username=" + username + ", date=" + date + ", videoPath=" + videoPath + ", thumbPath=" + thumbPath + ", views=" + views + ", likes=" + likes + ", dislikes=" + dislikes + ", comments=" + comments + '}';
    }
    
    
    
}
