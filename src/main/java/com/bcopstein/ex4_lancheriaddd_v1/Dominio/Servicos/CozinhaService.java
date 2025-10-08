package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class CozinhaService {

    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;
    private Map<Long, Pedido> pedidosById;

    private ScheduledExecutorService scheduler;

    private long contadorId;

    public CozinhaService() {
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        pedidosById = new ConcurrentHashMap<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        contadorId = 1L;
    }

    private synchronized void colocaEmPreparacao(Pedido pedido) {
        pedido.setStatus(Pedido.Status.PREPARACAO);
        emPreparacao = pedido;
        System.out.println("Pedido em preparacao: " + pedido);
        // Agenda pedidoPronto para ser chamado em 2 segundos
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    public synchronized void chegadaDePedido(Pedido p) {
        pedidosById.put(p.getId(), p);
        /* filaEntrada.add(p);
        System.out.println("Pedido na fila de entrada: " + p);
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        } */
    }

    public synchronized long geraIdPedido() {
        return contadorId++;
    }

    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saida: " + emPreparacao);
        emPreparacao = null;
        // Se tem pedidos na fila, programa a preparação para daqui a 1 segundo
        if (!filaEntrada.isEmpty()) {
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}
