package com.grupo4.SisHosp.notification;

public class CanalSms implements Observador {

    @Override
    public void notificar(String evento, String mensagem) {
        System.out.println("[SMS] (" + evento + ") " + mensagem);
    }

    @Override
    public String getCanal() {
        return "SMS";
    }
}