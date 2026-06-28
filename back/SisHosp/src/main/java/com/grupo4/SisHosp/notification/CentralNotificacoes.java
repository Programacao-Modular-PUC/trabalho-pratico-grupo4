package com.grupo4.SisHosp.notification;

import com.grupo4.SisHosp.config.ConfiguracaoSistema;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CentralNotificacoes {

    private final List<Observador> observadores = new ArrayList<>();

    public CentralNotificacoes() {
        
        observadores.add(new CanalEmail());
        observadores.add(new CanalSms());
        observadores.add(new CanalInterno());
    }

    
    public void adicionarObservador(Observador observador) {
        observadores.add(observador);
    }

    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    public void notificar(String evento, String mensagem) {
        ConfiguracaoSistema config = ConfiguracaoSistema.getInstancia();

        for (Observador obs : observadores) {
            if (config.canalEstaAtivo(obs.getCanal())) {
                obs.notificar(evento, mensagem);
            }
        }
    }
}