/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.web2.beans;

import java.io.Serializable;


public class TipoAtendimentoBean implements Serializable {
    private int idTipo;
    private String nome;
    
    public TipoAtendimentoBean() {}
    
    public TipoAtendimentoBean(String nome){
        this.nome = nome;
    }
    public int getIdTipo(){
        return this.idTipo;
    }
    public void setIdTipo(int idTipo){
        this.idTipo = idTipo;
    }
    public String getNome(){
        return this.nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
}
