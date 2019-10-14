/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.objects;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Portatil
 */
public class User {
    private String username;
    private int suscriptores;
    private List<String> suscripciones;

    public User(String username, int suscriptores) {
        this.username = username;
        this.suscriptores = suscriptores;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getSuscriptores() {
        return suscriptores;
    }

    public void setSuscriptores(int suscriptores) {
        this.suscriptores = suscriptores;
    }

    public List<String> getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(List<String> suscripciones) {
        this.suscripciones = suscripciones;
    }
    
    public void addSuscriptores()
    {
        suscriptores++;
    }
    
    public void removeSuscriptores()
    {
        suscriptores--;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.username);
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
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }
    
    
    
}
