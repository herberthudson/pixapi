package com.herbert.PixAPI.Pix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PixTest {

    @InjectMocks
    private Pix pix;

    @BeforeEach
    void init(){
        this.pix = new Pix("email",
                "herberthudson@gmail.com",
                "corrente",
            4321,
            12345678,
                'F',
        "Herbert",
        "Hudson");
    }

    @Test
    void isInativa() {
        assertEquals(pix.isInativa(), false);
    }

    @Test
    void setInativa() {
        pix.setInativa(true);
        assertEquals(pix.isInativa(), true);
    }

    @Test
    void getValorChave() {
        assertEquals(pix.getValorChave(), "herberthudson@gmail.com");
    }

    @Test
    void setValorChave() {
        String novoEmail = "herbert@gmail.com";
        pix.setValorChave(novoEmail);
        assertEquals(pix.getValorChave(), novoEmail);
    }

    @Test
    void getSobrenomeCorrentista() {
        assertEquals(pix.getSobrenomeCorrentista(), "Hudson");
    }

    @Test
    void setSobrenomeCorrentista() {
        String novoSobrenome = "Trindade";
        pix.setSobrenomeCorrentista((novoSobrenome));
        assertEquals(pix.getSobrenomeCorrentista(), novoSobrenome);
    }

    @Test
    void getNomeCorrentista() {
        assertEquals(pix.getNomeCorrentista(), "Herbert");
    }

    @Test
    void setNomeCorrentista() {
        String novoNome = "Juan";
        pix.setNomeCorrentista(novoNome);
        assertEquals(pix.getNomeCorrentista(), novoNome);
    }

    @Test
    void getTipoPessoa() {
        assertEquals(pix.getTipoPessoa(), 'F');
    }

    @Test
    void setTipoPessoa() {
        pix.setTipoPessoa('J');
        assertEquals(pix.getTipoPessoa(), 'J');
    }

    @Test
    void getNumeroAgencia() {
        assertEquals(pix.getNumeroAgencia(), 4321);
    }

    @Test
    void setNumeroAgencia() {
        int novoNumeroAngencia = 1234;
        pix.setNumeroAgencia(novoNumeroAngencia);
        assertEquals(pix.getNumeroAgencia(), novoNumeroAngencia);
    }

    @Test
    void getNumeroConta() {
        assertEquals(pix.getNumeroConta(), 12345678);
    }

    @Test
    void setNumeroConta() {
        int novoNumeroConta = 87654321;
        pix.setNumeroConta(novoNumeroConta);
        assertEquals(pix.getNumeroConta(), novoNumeroConta);
    }

    @Test
    void getTipoConta() {
        assertEquals(pix.getTipoConta(), "corrente");
    }

    @Test
    void setTipoConta() {
        String novoTipoConta = "poupanca";
        pix.setTipoConta(novoTipoConta);
        assertEquals(pix.getTipoConta(), novoTipoConta);
    }

    @Test
    void getTipoChave() {
        assertEquals(pix.getTipoChave(), "email");
    }

    @Test
    void setTipoChave() {
        String novoTipoChave = "cpf";
        pix.setTipoChave(novoTipoChave);
        assertEquals(pix.getTipoChave(), novoTipoChave);
    }

    @Test
    void setInclusaoChave() {
        LocalDateTime agora = LocalDateTime.now();
        pix.setInclusaoChave(agora);
        assertEquals(pix.getInclusaoChave(), agora);
    }

    @Test
    void setInativacaoChave() {
        LocalDateTime agora = LocalDateTime.now();
        pix.setInativacaoChave(agora);
        assertEquals(pix.getInativacaoChave(), agora);
    }

    @Test
    void setId() {
        UUID novoId = UUID.randomUUID();
        pix.setId(novoId);
        assertEquals(pix.getId(), novoId);
    }
}