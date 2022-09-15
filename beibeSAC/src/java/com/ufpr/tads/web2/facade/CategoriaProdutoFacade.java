/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import com.ufpr.tads.web2.beans.CategoriaProdutoBean;
import com.ufpr.tads.web2.dao.CategoriaProdutoDao;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;


public class CategoriaProdutoFacade {
    public static ArrayList<CategoriaProdutoBean> getLista() throws CategoriaProdutoException
    {
        try
        {
            CategoriaProdutoDao categoriaProdutoDao = new CategoriaProdutoDao();
            ArrayList<CategoriaProdutoBean> listaCategorias = categoriaProdutoDao.retornaListaCategorias();
            return listaCategorias;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CategoriaProdutoException("Erro ao retornar lista de categorias", e);
        }
    }
    
    public static CategoriaProdutoBean adicionaCategoria(CategoriaProdutoBean categoriaProduto) throws CategoriaProdutoException
    {
        try
        {
            CategoriaProdutoDao categoriaProdutoDao = new CategoriaProdutoDao();
            CategoriaProdutoBean categoriaProdutoNovo = categoriaProdutoDao.adicionaCategoria(categoriaProduto);
            return categoriaProdutoNovo;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CategoriaProdutoException("Erro ao adicionar categoria", e);
        }
    }
    
    public static CategoriaProdutoBean retornaCategoria(int id) throws CategoriaProdutoException
    {
        try
        {
            CategoriaProdutoDao categoriaProdutoDao = new CategoriaProdutoDao();
            CategoriaProdutoBean categoriaProduto = categoriaProdutoDao.retornaCategoriaPorId(id);
            return categoriaProduto;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CategoriaProdutoException("Erro ao buscar categoria de id: " + id, e);
        }
    }
    
    public static boolean modificaCategoria(CategoriaProdutoBean categoriaProduto) throws CategoriaProdutoException
    {
        try
        {
            CategoriaProdutoDao categoriaProdutoDao = new CategoriaProdutoDao();
            boolean confereModificacao = categoriaProdutoDao.modificaCategoria(categoriaProduto);
            
            return confereModificacao;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CategoriaProdutoException("Erro ao modificar categoria", e);
        }
    }
    
    public static boolean removerCategoria(CategoriaProdutoBean categoriaProduto) throws CategoriaProdutoException
    {
        try
        {
            CategoriaProdutoDao categoriaProdutoDao = new CategoriaProdutoDao();
            boolean confereRemocao = categoriaProdutoDao.removeCategoria(categoriaProduto);
            
            return confereRemocao;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new CategoriaProdutoException("Erro ao remover categoria", e);
        }
    }
}
