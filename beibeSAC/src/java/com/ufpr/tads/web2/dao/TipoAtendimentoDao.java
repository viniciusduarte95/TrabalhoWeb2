/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.dao;

import com.ufpr.tads.web2.beans.TipoAtendimentoBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class TipoAtendimentoDao {
    private ConnectionFactory connectionFactory;
    private final String select = "SELECT idTipo, nome FROM TipoAtendimento;";
    private final String selectById = "SELECT idTipo, nome FROM TipoAtendimento WHERE idTipo = ?;";
    
    public TipoAtendimentoDao() {}
    
    public TipoAtendimentoDao(ConnectionFactory conFactory)
    {
        this.connectionFactory = conFactory;
    }
    
    public ArrayList<TipoAtendimentoBean> retornaListaTipoAtendimentos() throws SQLException, ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement pstm = null;

        ArrayList<TipoAtendimentoBean> tiposAtendimento = new ArrayList<>();
         try {
            con = ConnectionFactory.getConnection();
            pstm = con.prepareStatement(select);
            ResultSet rs = pstm.executeQuery();
            
            while (rs.next()) {
                TipoAtendimentoBean tipoAtendimento = new TipoAtendimentoBean();
                tipoAtendimento.setIdTipo(rs.getInt("idTipo"));
                tipoAtendimento.setNome(rs.getString("nome"));
                tiposAtendimento.add(tipoAtendimento);
            }
            return tiposAtendimento;
        } finally {
            pstm.close();
            con.close();
        }
    }
    
    public TipoAtendimentoBean retornaTipoAtendimentoPorId(int id) throws SQLException, ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement pstm = null;

        TipoAtendimentoBean tipoAtendimento = new TipoAtendimentoBean();
        try {
            con = ConnectionFactory.getConnection();
            pstm = con.prepareStatement(selectById);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            
            while (rs.next()) {
                tipoAtendimento.setIdTipo(rs.getInt("idTipo"));
                tipoAtendimento.setNome(rs.getString("nome"));
            }
            return tipoAtendimento;
        } finally {
            pstm.close();
            con.close();
        }
    }
}
