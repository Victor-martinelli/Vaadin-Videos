/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.controllers;

import java.util.List;
import static tad.grupo1.proyecto.controllers.GeneralController.dao;
import tad.grupo1.proyecto.objects.UserVideo;

/**
 *
 * @author Lydia
 */
public class SuscripcionesController {
    
    public List<String> getSuscripciones(String username){
        return dao.getSuscripciones(username);
    }
    
    public List<UserVideo> getVideoSuscrito(String username){
        return dao.getSuscritoVideo(username);
    }
    
}
