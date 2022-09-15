/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import com.ufpr.tads.web2.beans.EstadoBean;
import com.ufpr.tads.web2.dao.EstadoDao;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;


public class EstadoFacade {
    public static ArrayList<EstadoBean> getLista() throws EstadoException
    {
        try
        {
            EstadoDao estadoDao = new EstadoDao();
            ArrayList<EstadoBean> listaEstados = estadoDao.retornaListaEstados();
            return listaEstados;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new EstadoException("Erro ao retornar lista de estados", e);
        }
    }
    
    public static EstadoBean retornaEstado(int id) throws EstadoException
    {
        try
        {
            EstadoDao estadoDao = new EstadoDao();
            EstadoBean estado = estadoDao.retornaEstadoPorId(id);
            return estado;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new EstadoException("Erro ao buscar estado de id: " + id, e);
        }
    }
}
