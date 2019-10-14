/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.controllers;

import tad.grupo1.proyecto.model.DAO;

/**
 *
 * @author Portatil
 */
public class GeneralController {
    
    static DAO dao = new DAO();
    
    public String getLogo()
    {
        return dao.getLogoPath();
    }
    
}
