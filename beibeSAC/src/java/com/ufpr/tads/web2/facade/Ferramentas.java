/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import com.ufpr.tads.web2.beans.AtendimentoBean;
import com.ufpr.tads.web2.beans.SituacaoBean;
import com.ufpr.tads.web2.beans.TipoAtendimentoBean;
import com.ufpr.tads.web2.dao.AtendimentoDao;
import com.ufpr.tads.web2.dao.FerramentasDao;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;


public class Ferramentas {
    public static String criptografaSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
        
        StringBuilder hexString = new StringBuilder();
        for(byte b : messageDigest)
        {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        
        String senhaCriptografada = hexString.toString();
        return senhaCriptografada;
    }
    
    public static boolean confereEmail(String email) throws FerramentasException
    {
        boolean existeEmail = false;
        try
        {
            FerramentasDao ferramentasDao = new FerramentasDao();
            existeEmail = ferramentasDao.confereEmail(email);
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new FerramentasException("Erro ao conferir existencia de email", e);
        }
        return existeEmail;
    }
    
    public static int qtdAtendimentos() throws FerramentasException
    {
        try
        {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            ArrayList<AtendimentoBean> listaAtendimentos = atendimentoDao.retornaTodosAtendimentos();
            int qtdAtendimentos = 0;
            
            for(AtendimentoBean a : listaAtendimentos)
            {
                qtdAtendimentos++;
            }
                  
            return qtdAtendimentos;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new FerramentasException("Erro ao contar quantidade total de atendimentos", e);
        }
    }
    
    public static int qtdAtendimentosAbertos() throws FerramentasException
    {
        try
        {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            SituacaoBean emAberto = SituacaoFacade.retornaSituacao(1);
            ArrayList<AtendimentoBean> listaAtendimentosAbertos = atendimentoDao.retornaAtendimentosPorSituacao(emAberto);
            int qtdAtendimentosAbertos = 0;
            
            for(AtendimentoBean a : listaAtendimentosAbertos)
            {
                qtdAtendimentosAbertos++;
            }
            
            return qtdAtendimentosAbertos;
        }
        catch(SQLException | ClassNotFoundException | SituacaoException e)
        {
            throw new FerramentasException("Erro ao contar quantidade total de atendimentos em aberto", e);
        }
    }
    
    public static float calculaPercentual(int parcial, int total) throws FerramentasException
    {
        try
        {
           int percentual = ((parcial * 100)/total);
           return percentual;
        }
        catch(NumberFormatException e)
        {
            throw new FerramentasException("Valores para calculo percentual invalidos", e);
        }
    }
    
    public static int qtdAtendimentosTipo(TipoAtendimentoBean tipo) throws FerramentasException
    {
       try
       {
           AtendimentoDao atendimentoDao = new AtendimentoDao();
           ArrayList<AtendimentoBean> listaAtendimentosTipo = atendimentoDao.retornaAtendimentosPorTipo(tipo);
           int qtdAtendimentosTipo = 0;
           
           for(AtendimentoBean a : listaAtendimentosTipo)
           {
               qtdAtendimentosTipo++;
           }
           
           return qtdAtendimentosTipo;
       }
       catch(SQLException | ClassNotFoundException e)
       {
           throw new FerramentasException("Erro ao contar quantidade total de atendimentos do tipo: " + tipo.getNome(), e);
       }
    }
    
    public static int qtdAtendimentosAbertosTipo(TipoAtendimentoBean tipo) throws FerramentasException
    {
        try
        {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            ArrayList<AtendimentoBean> listaAtendimentosAbertosTipo = atendimentoDao.retornaAtendimentosPorTipo(tipo);
            int qtdAtendimentosAbertosTipo = 0;
            
            for(AtendimentoBean a : listaAtendimentosAbertosTipo)
            {
                SituacaoBean situacao = a.getSituacao();
                if (situacao.getIdSituacao() == 1)
                    qtdAtendimentosAbertosTipo++;
            }
            
            return qtdAtendimentosAbertosTipo;
        }
        catch(SQLException | ClassNotFoundException e)
        {
            throw new FerramentasException("Erro ao contar quantiidade total de atendimentos abertos do tipo: " + tipo.getNome(), e);
        }
    }
}
