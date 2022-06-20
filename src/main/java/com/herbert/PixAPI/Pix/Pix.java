package com.herbert.PixAPI.Pix;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Pix {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "id", nullable = false)
    private UUID id;
    @NotNull
    @NotBlank(message = "Campo tipoChave e obrigatorio")
    @Pattern(regexp = "^email|celular|cpf|cnpj$", message = "Tipo chave deve ser " +
            "email," +
            " celular, cpf, cpnj")
    private String tipoChave;

    @NotNull
    private String valorChave;
    @NotNull
    @Pattern(regexp = "^corrente|poupança$", message = "Tipo conta deve ser " +
            "corrente ou poupança")
    private String tipoConta;
    @NotNull
    @Min(value = 1, message = "Numero agencia invalido")
    @Max(value = 9999, message = "Excedeu o tamanho maximo do numero da " +
            "agencia")
    private int numeroAgencia;
    @NotNull
    @Min(value = 1, message = "Numero conta invalido")
    @Max(value = 99999999, message = "Excedeu o tamanho maximo do numero da " +
            "conta")
    private int numeroConta;
    @NotNull
    private char tipoPessoa;
    @NotNull
    @Size(min = 2, max = 30, message = "Nome correntista excedeu o tamanho " +
            "maximo")
    private String nomeCorrentista;
    @Size(min = 2, max = 45, message = "Sobrenome correntista excedeu o " +
            "tamanho maximo")
    private String sobrenomeCorrentista = "";
    @CreationTimestamp
    private LocalDateTime inclusaoChave;
    @NotNull
    private boolean inativa = false;
    private LocalDateTime inativacaoChave;

    public Pix() {
    }

    public Pix(String tipoChave,
               String valorChave,
               String tipoConta,
               int numeroAgencia,
               int numeroConta,
               char tipoPessoa,
               String nomeCorrentista,
               String sobrenomeCorrentista) {
        this.tipoChave = tipoChave;
        this.valorChave = valorChave;
        this.tipoConta = tipoConta;
        this.numeroAgencia = numeroAgencia;
        this.numeroConta = numeroConta;
        this.tipoPessoa = tipoPessoa;
        this.nomeCorrentista = nomeCorrentista;
        this.sobrenomeCorrentista = sobrenomeCorrentista;
        this.inativa = false;
    }

    public LocalDateTime getInclusaoChave() {
        return inclusaoChave;
    }

    public void setInclusaoChave(LocalDateTime inclusaoChave) {
        this.inclusaoChave = inclusaoChave;
    }

    public boolean isInativa() {
        return inativa;
    }

    public void setInativa(boolean inativa) {
        this.inativa = inativa;
    }

    public LocalDateTime getInativacaoChave() {
        return inativacaoChave;
    }

    public void setInativacaoChave(LocalDateTime inativacaoChave) {
        this.inativacaoChave = inativacaoChave;
    }

    public String getValorChave() {
        return valorChave;
    }

    public void setValorChave(String valorChave) {
        this.valorChave = valorChave;
    }

    public String getSobrenomeCorrentista() {
        return sobrenomeCorrentista;
    }

    public void setSobrenomeCorrentista(String sobrenomeCorrentista) {
        this.sobrenomeCorrentista = sobrenomeCorrentista;
    }

    public String getNomeCorrentista() {
        return nomeCorrentista;
    }

    public void setNomeCorrentista(String nomeCorrentista) {
        this.nomeCorrentista = nomeCorrentista;
    }

    public char getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(char tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public int getNumeroAgencia() {
        return numeroAgencia;
    }

    public void setNumeroAgencia(int numeroAgencia) {
        this.numeroAgencia = numeroAgencia;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(String tipoChave) {
        this.tipoChave = tipoChave;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

