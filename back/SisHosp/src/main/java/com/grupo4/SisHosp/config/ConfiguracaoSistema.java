package com.grupo4.SisHosp.config;

import java.util.Arrays;
import java.util.List;


public class ConfiguracaoSistema {


    private static ConfiguracaoSistema instancia;

    private String nomeSistema;
    private String moeda;
    private List<String> canaisAtivos;


    private ConfiguracaoSistema() {
        this.nomeSistema = "SisHosp Maraú";
        this.moeda = "BRL";
        this.canaisAtivos = Arrays.asList("EMAIL", "INTERNA");
    }

    public static ConfiguracaoSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracaoSistema();
        }
        return instancia;
    }

    public String getNomeSistema() {
        return nomeSistema;
    }

    public void setNomeSistema(String nomeSistema) {
        this.nomeSistema = nomeSistema;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public List<String> getCanaisAtivos() {
        return canaisAtivos;
    }

    public void setCanaisAtivos(List<String> canaisAtivos) {
        this.canaisAtivos = canaisAtivos;
    }

    public boolean canalEstaAtivo(String canal) {
        return canaisAtivos.contains(canal.toUpperCase());
    }
}
