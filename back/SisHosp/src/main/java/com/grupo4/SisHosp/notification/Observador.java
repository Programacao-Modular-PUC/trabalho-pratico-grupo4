package com.grupo4.SisHosp.notification;

public interface Observador {

    void notificar(String evento, String mensagem);

    String getCanal();
}
