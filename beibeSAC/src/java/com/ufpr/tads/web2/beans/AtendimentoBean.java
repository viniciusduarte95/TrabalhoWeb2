/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.beans;

import java.io.Serializable;
import java.util.Calendar;


public class AtendimentoBean implements Serializable{
    private int idAtendimento;
    private ClienteBean cliente;
    private FuncionarioBean funcionario;
    private ProdutoBean produto;
    private TipoAtendimentoBean tipoAtendimento;
    private Calendar dataHoraInicio;
    private Calendar dataHoraFim;
    private String reclamacao;
    private String solucao;
    private SituacaoBean estado;
    
    public AtendimentoBean() {}
    
    public AtendimentoBean(ClienteBean cliente, ProdutoBean produto, TipoAtendimentoBean tipoAtendimento, 
                       String reclamacao, Calendar dataHoraInicio, SituacaoBean estado){
        this.cliente         = cliente;
        this.produto         = produto;
        this.tipoAtendimento = tipoAtendimento;
        this.reclamacao      = reclamacao;
        this.dataHoraInicio  = dataHoraInicio;
        this.estado          = estado;
    }
    
    public AtendimentoBean(ClienteBean cliente, FuncionarioBean funcionario,ProdutoBean produto, TipoAtendimentoBean tipoAtendimento, 
                       String reclamacao, String solucao, Calendar dataHoraInicio, Calendar dataHoraFim,SituacaoBean estado){
        this.cliente         = cliente;
        this.funcionario     = funcionario;
        this.produto         = produto;
        this.tipoAtendimento = tipoAtendimento;
        this.reclamacao      = reclamacao;
        this.solucao         = solucao;
        this.dataHoraInicio  = dataHoraInicio;
        this.dataHoraFim     = dataHoraFim;
        this.estado          = estado;
    }  
    
    public int getIdAtendimento(){
        return this.idAtendimento;
    }
    public void setIdAtendimento(int idAtendimento){
        this.idAtendimento = idAtendimento;
    }
    public ClienteBean getCliente(){
        return this.cliente;
    }
    public void setCliente(ClienteBean cliente){
        this.cliente = cliente;
    }
    public FuncionarioBean getFuncionario(){
        return this.funcionario;
    }
    public void setFuncionario(FuncionarioBean funcionario){
        this.funcionario = funcionario;
    }
    public ProdutoBean getProduto(){
        return this.produto;
    }
    public void setProduto(ProdutoBean produto){
        this.produto = produto;
    }
    
    public TipoAtendimentoBean getTipoAtendimento(){
        return this.tipoAtendimento;
    }
    public void setTipoAtendimento(TipoAtendimentoBean tipoAtendimento){
        this.tipoAtendimento = tipoAtendimento;
    }
    public Calendar getDataHoraInicio(){
        return this.dataHoraInicio;
    }
    public void setDataHoraInicio(Calendar dataHoraInicio){
        this.dataHoraInicio = dataHoraInicio;
    }
    
    public Calendar getDataHoraFim(){
        return this.dataHoraFim;
    }
    public void setDataHoraFim(Calendar dataHoraFim){
        this.dataHoraFim = dataHoraFim;
    }
    public String getReclamacao(){
        return this.reclamacao;
    }
    public void setReclamacao(String reclamacao){
        this.reclamacao = reclamacao;
    }
    public String getSolucao(){
        return this.solucao;
    }
    public void setSolucao(String solucao){
        this.solucao = solucao;
    }
    public SituacaoBean getSituacao(){
        return this.estado;
    }
    public void setSituacao(SituacaoBean estado){
        this.estado = estado;
    }
}
