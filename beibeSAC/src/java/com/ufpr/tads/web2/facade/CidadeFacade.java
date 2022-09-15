/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import com.ufpr.tads.web2.beans.CidadeBean;
import com.ufpr.tads.web2.beans.EstadoBean;
import com.ufpr.tads.web2.dao.CidadeDao;
import java.sql.SQLException;
import java.util.ArrayList;


public class CidadeFacade {
    public static ArrayList<CidadeBean> getLista(EstadoBean estado) throws CidadeException
    {
        try
        {
            CidadeDao cidadeDao = new CidadeDao();
            ArrayList<CidadeBean> listaCidades = cidadeDao.retornaListaCidadesPorEstado(estado);
            return listaCidades;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CidadeException("Erro ao retornar lista de cidades", e);
        }
    }
    
    public static CidadeBean retornaCidade(int id) throws CidadeException
    {
        try
        {
            CidadeDao cidadeDao = new CidadeDao();
            CidadeBean cidade = cidadeDao.retornaCidadePorId(id);
            return cidade;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CidadeException("Erro ao buscar cidade de id: " + id, e);
        }
    }
}
