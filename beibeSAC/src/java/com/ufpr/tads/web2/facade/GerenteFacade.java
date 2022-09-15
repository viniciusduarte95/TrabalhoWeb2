/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import com.ufpr.tads.web2.beans.GerenteBean;
import com.ufpr.tads.web2.dao.GerenteDao;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;


public class GerenteFacade {
    public static GerenteBean logaGerente(String email, String senha) throws GerenteException
    {
        try
        {
            String senhaCriptografada = Ferramentas.criptografaSenha(senha);
            GerenteDao gerenteDao = new GerenteDao();
            GerenteBean gerente = gerenteDao.logaGerente(email, senhaCriptografada);
            
            return gerente;
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException | SQLException | ClassNotFoundException e)
        {
            throw new GerenteException("Gerente nao cadastrados ou dados incorretos", e);
        }
    }
    
    public static ArrayList<GerenteBean> getLista() throws GerenteException
    {
        try
        {
            GerenteDao gerenteDao = new GerenteDao();
            ArrayList<GerenteBean> listaGerentes = gerenteDao.retornaListaGerentes();
            return listaGerentes;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new GerenteException("Erro ao retornar lista de Gerentes", e);
        }
    }
    
    public static GerenteBean adicionaGerente(GerenteBean gerente) throws GerenteException
    {
        try
        {
            String senhaCriptografada = Ferramentas.criptografaSenha(gerente.getSenha());
            gerente.setSenha(senhaCriptografada);
            GerenteDao gerenteDao = new GerenteDao();
            GerenteBean gerenteNovo = gerenteDao.adicionaGerente(gerente);
            return gerenteNovo;
        }
        catch(SQLException | NoSuchAlgorithmException | UnsupportedEncodingException | ClassNotFoundException e)
        {
            throw new GerenteException("Erro ao adicionar gerente", e);
        }
    }
    
    public static GerenteBean retornaGerente(int id) throws GerenteException
    {
        try
        {
            GerenteDao gerenteDao = new GerenteDao();
            GerenteBean gerente = gerenteDao.retornaGerentePorId(id);
            return gerente;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new GerenteException("Erro ao buscar gerente de id: " + id, e);
        }
    }
    
    public static boolean modificaGerente(GerenteBean gerente) throws GerenteException
    {
        try
        {
            String senhaCriptografada = Ferramentas.criptografaSenha(gerente.getSenha());
            GerenteBean gerenteAntigo = GerenteFacade.retornaGerente(gerente.getIdGerente());
            String novaSenha = gerente.getSenha();
            String senhaAntiga = gerenteAntigo.getSenha();
            if (!novaSenha.equals(senhaAntiga))
                    gerente.setSenha(senhaCriptografada);
            
            GerenteDao gerenteDao = new GerenteDao();
            boolean confereModificacao = gerenteDao.modificaGerente(gerente);
            
            return confereModificacao;
        }
        catch(SQLException | ClassNotFoundException | NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            throw new GerenteException("Erro ao modificar gerente", e);
        }      
    }
    
    public static boolean removeGerente(GerenteBean gerente) throws GerenteException
    {
        try
        {
            GerenteDao gerenteDao = new GerenteDao();
            boolean confereRemocao = gerenteDao.removeGerente(gerente);
            
            return confereRemocao;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new GerenteException("Erro ao remover gerente", e);
        }
    }
}
