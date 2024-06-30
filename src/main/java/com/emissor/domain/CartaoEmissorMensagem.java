package com.emissor.domain;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class CartaoEmissorMensagem implements Serializable {
    private String numero;
    private String dataExpiracao;
    private String cvv;
    private String email;
}
