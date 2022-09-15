/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.dao;

import com.ufpr.tads.web2.beans.SituacaoBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SituacaoDao {
    private ConnectionFactory connectionFactory;
    private final String select = "SELECT idSituacao, estado FROM Situacao;";
    private final String selectById = "SELECT idSituacao, estado FROM Situacao WHERE idSituacao = ?;";
    
    public SituacaoDao() {}
    
    public SituacaoDao(ConnectionFactory conFactory)
    {
        this.connectionFactory = conFactory;
    }
    
    public ArrayList<SituacaoBean> retornaListaSituacoes() throws SQLException, ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement pstm = null;

        ArrayList<SituacaoBean> situacoes = new ArrayList<>();
         try {
            con = ConnectionFactory.getConnection();
            pstm = con.prepareStatement(select);
            ResultSet rs = pstm.executeQuery();
            
            while (rs.next()) {
                SituacaoBean situacao = new SituacaoBean();
                situacao.setIdSituacao(rs.getInt("idSituacao"));
                situacao.setEstado(rs.getString("estado"));
                situacoes.add(situacao);
            }
            return situacoes;
        } finally {
            pstm.close();
            con.close();
        }
    }
    
    public SituacaoBean retornaSituacaoPorId(int id) throws SQLException, ClassNotFoundException
    {
        Connection con = null;
        PreparedStatement pstm = null;

        SituacaoBean situacao = new SituacaoBean();
        try {
            con = ConnectionFactory.getConnection();
            pstm = con.prepareStatement(selectById);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            
            while (rs.next()) {
                situacao.setIdSituacao(rs.getInt("idSituacao"));
                situacao.setEstado(rs.getString("estado"));
            }
            return situacao;
        } finally {
            pstm.close();
            con.close();
        }
    }
}
