/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tad.grupo1.proyecto.controllers;

import java.io.FileOutputStream;
import java.util.List;
import static tad.grupo1.proyecto.controllers.GeneralController.dao;
import tad.grupo1.proyecto.objects.User;

/**
 *
 * @author Lydia
 */
public class UsuarioController {
    
    public void registrarUsuario(String username, String email, String password){
        dao.insertUsuario(username, email, password);
    }
    
    public String getPassword(String username,String parameter)
    {
        return (String) dao.getUserInfo(username, parameter);
    }
    
    public void changePassword(String username,String password)
    {
        dao.changeUserPassword(username, password);
    }
    
    public FileOutputStream changeProfilePicture(String username,String filename)
    {
        return dao.changeProfilePicture(username, filename);
    }
    
    public void moveProfilePicture(String username,String filename)
    {
        dao.moveProfilePicture(username, filename);
    }
    
    
    public Boolean comprobarLogin(String username, String password){
        return dao.login(username, password);
    }
    
    public Boolean comprobarUsername(String username){
        return dao.getUsername(username);
    }
    
    public Boolean isUserSuscribed(String username,String uploader)
    {
        return dao.isUserSuscribed(username,uploader);
    }
    
    public String getProfilePicture(String username)
    {
        return dao.getUserProfilePath(username);
    }
    
    public void removeSuscripcion(String username,String uploader)
    {
        dao.removeSuscripcion(username, uploader);
    }
    
    public void addSuscripcion(String username,String uploader)
    {
        dao.addSuscripcion(username, uploader);
    }
    
    public int getSuscriptores(String username)
    {
        return dao.getUserSuscriptores(username);
    }
    
    public void deleteUser(String username)
    {
        dao.deleteUser(username);
    }
    
    public List<User> getAllUsers()
    {
        return dao.getAllUsers();
    }
    
    public boolean isUserAdmin(String username)
    {
        return dao.isUserAdmin(username);
    }
    
}
