package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            boolean cancelado = pedidoService.cancelarPedidoAprovadoNaoPago(id);
            if (cancelado) {
                return ResponseEntity.ok().body("Pedido cancelado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Pedido não pode ser cancelado. Apenas pedidos APROVADOS e NÃO PAGOS podem ser cancelados.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}