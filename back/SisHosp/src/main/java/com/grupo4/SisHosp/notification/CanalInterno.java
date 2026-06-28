package com.grupo4.SisHosp.notification;


public class CanalInterno implements Observador {

    @Override
    public void notificar(String evento, String mensagem) {
        System.out.println("[INTERNA] (" + evento + ") " + mensagem);
    }

    @Override
    public String getCanal() {
        return "INTERNA";
    }
}
