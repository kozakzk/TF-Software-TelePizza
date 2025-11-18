package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public interface DescontosService {

    double getPercentualDesconto(Usuario cliente);
}
