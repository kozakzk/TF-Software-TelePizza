package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostosService {
    
    public double calcularImpostos(double valorTotalItens) {
        return valorTotalItens * 0.10;
    }
}