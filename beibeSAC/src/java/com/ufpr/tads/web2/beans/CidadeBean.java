/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.beans;

import java.io.Serializable;


public class CidadeBean implements Serializable {
    private int idCidade;
    private String nome;
    private EstadoBean estado;
    
    public CidadeBean() {}
    
    public CidadeBean(String nome, EstadoBean estado){
        this.nome   = nome;
        this.estado = estado;
    }
    
    public int getId(){
        return this.idCidade;
    }
    public void setId(int idCidade){
        this.idCidade = idCidade;
    }
    
    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public EstadoBean getEstado(){
        return this.estado;
    }
    public void setEstado(EstadoBean estado){
        this.estado = estado;
    }
}
