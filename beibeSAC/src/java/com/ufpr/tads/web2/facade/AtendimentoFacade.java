/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.facade;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ufpr.tads.web2.beans.AtendimentoBean;
import com.ufpr.tads.web2.beans.ClienteBean;
import com.ufpr.tads.web2.beans.FuncionarioBean;
import com.ufpr.tads.web2.beans.SituacaoBean;
import com.ufpr.tads.web2.dao.AtendimentoDao;

public class AtendimentoFacade {
    public static ArrayList<AtendimentoBean> getLista() throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            ArrayList<AtendimentoBean> listaAtendimentos = atendimentoDao.retornaTodosAtendimentos();
            return listaAtendimentos;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException("Erro ao retornar lista de atendimentos", e);
        }
    }

    public static ArrayList<AtendimentoBean> getListaPorCliente(ClienteBean cliente) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            ArrayList<AtendimentoBean> listaAtendimentosPorCliente = atendimentoDao.retornaAtendimentosPorCliente(cliente);
            return listaAtendimentosPorCliente;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException(
                    "Erro ao retornar lista de atendimentos de cliente: " + cliente.getIdCliente(), e);
        }
    }

    public static ArrayList<AtendimentoBean> getListaPorFuncionario(FuncionarioBean funcionario) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            ArrayList<AtendimentoBean> listaAtendimentosPorFuncionario = atendimentoDao
                    .retornaAtendimentosPorFuncionario(funcionario);
            return listaAtendimentosPorFuncionario;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException(
                    "Erro ao retornar lista de atendimentos de funcionario: " + funcionario.getIdFuncionario(), e);
        }
    }

    public static ArrayList<AtendimentoBean> getListaPorSituacao(SituacaoBean situacao) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            ArrayList<AtendimentoBean> listaAtendimentosPorSituacao = atendimentoDao
                    .retornaAtendimentosPorSituacao(situacao);
            return listaAtendimentosPorSituacao;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException(
                    "Erro ao retornar lista de atendimentos de situacao: " + situacao.getIdSituacao(), e);
        }
    }

    public static AtendimentoBean adicionaAtendimento(AtendimentoBean atendimento) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            AtendimentoBean atendimentoNovo = atendimentoDao.adicionaAtendimento(atendimento);
            return atendimentoNovo;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException("Erro ao adicionar atendimento", e);
        }
    }

    public static AtendimentoBean retornaAtendimento(int id) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            AtendimentoBean atendimento = atendimentoDao.retornaAtendimentoPorId(id);
            return atendimento;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException("Erro ao buscar atendimento de id: " + id, e);
        }
    }

    public static boolean modificaAtendimento(AtendimentoBean atendimento) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            boolean confereModificacao = atendimentoDao.modificaAtendimento(atendimento);

            return confereModificacao;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException("Erro ao modificar atendimento", e);
        }
    }

    public static boolean removerAtendimento(AtendimentoBean atendimento) throws AtendimentoException {
        try {
            AtendimentoDao atendimentoDao = new AtendimentoDao();
            boolean confereRemocao = atendimentoDao.removeAtendimento(atendimento);

            return confereRemocao;
        } catch (SQLException | ClassNotFoundException e) {
            throw new AtendimentoException("Erro ao remover atendimento", e);
        }
    }
}
