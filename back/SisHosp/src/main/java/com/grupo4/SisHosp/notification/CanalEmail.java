package com.grupo4.SisHosp.notification;


public class CanalEmail implements Observador {

    @Override
    public void notificar(String evento, String mensagem) {
        // num sistema real enviaria um e-mail; aqui registramos no console
        System.out.println("[E-MAIL] (" + evento + ") " + mensagem);
    }

    @Override
    public String getCanal() {
        return "EMAIL";
    }
}