/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import com.ufpr.tads.web2.beans.ProdutoBean;
import com.ufpr.tads.web2.dao.ProdutoDao;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProdutoFacade {
    public static ArrayList<ProdutoBean> getLista() throws ProdutoException
    {
        try
        {
            ProdutoDao produtoDao = new ProdutoDao();
            ArrayList<ProdutoBean> listaProdutos = produtoDao.retornaListaProdutos();
            return listaProdutos;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new ProdutoException("Erro ao retornar lista de produtos", e);
        }
    }
    
    public static ProdutoBean adicionaProduto(ProdutoBean produto) throws ProdutoException
    {
        try
        {
            ProdutoDao produtoDao = new ProdutoDao();
            ProdutoBean produtoNovo = produtoDao.adicionaProduto(produto);
            return produtoNovo;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new ProdutoException("Erro ao adicionar produto", e);
        }
    }
    
    public static ProdutoBean retornaProduto(int id) throws ProdutoException
    {
        try
        {
            ProdutoDao produtoDao = new ProdutoDao();
            ProdutoBean produto = produtoDao.retornaProdutoPorId(id);
            return produto;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new ProdutoException("Erro ao buscar produtos de id: " + id, e);
        }
    }
    
    public static boolean modificaProduto(ProdutoBean produto) throws ProdutoException
    {
        try
        {
            ProdutoDao produtoDao = new ProdutoDao();
            boolean confereModificacao = produtoDao.modificaProduto(produto);
            
            return confereModificacao;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new ProdutoException("Erro ao modificar produto", e);
        }
    }
    
    public static boolean removerProduto(ProdutoBean produto) throws ProdutoException
    {
        try
        {
            ProdutoDao produtoDao = new ProdutoDao();
            boolean confereRemocao = produtoDao.removeProduto(produto);
            
            return confereRemocao;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new ProdutoException("Erro ao remover produto", e);
        }
    }
}
