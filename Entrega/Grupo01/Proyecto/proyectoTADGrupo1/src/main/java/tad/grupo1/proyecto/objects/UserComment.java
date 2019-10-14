/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Portatil
 */
public class UserComment {
    
    private Date date;
    private String comment;
    private String videoTitle;
    private String username;

    public UserComment(Date date, String comment, String username) {
        this.date = date;
        this.comment = comment;
        this.username = username;
    }

    public UserComment(Date date, String comment, String username, String videoTitle) {
        this.date = date;
        this.comment = comment;
        this.videoTitle = videoTitle;
        this.username = username;
    }
    
    

    public UserComment(Date date) {
        this.date = date;
    }
    
    
    
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 

       return formatter.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
    
    public Date getRawDate()
    {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.date);
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
        final UserComment other = (UserComment) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
