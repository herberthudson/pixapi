package com.herbert.PixAPI.Util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorsTest {

    @Test
    void isCPF() {
        String cpf = "85261770017";
        String otherCpf = "852.617.700-17";
        String falseCpf = "06901017694";
        String otherFalseCpf = "0000000001";
        String reallyFalseCpf = "00000000000";
        boolean cpfValid = Validators.isCPF(cpf);
        boolean otherCpfValid = Validators.isCPF(otherCpf);
        boolean falseCpfValid = Validators.isCPF(falseCpf);
        boolean otherFalseCpfValid = Validators.isCPF(otherFalseCpf);
        boolean reallyFalseCpfValid = Validators.isCPF(reallyFalseCpf);

        assertTrue(cpfValid);
        assertTrue(otherCpfValid);
        assertFalse(falseCpfValid);
        assertFalse(otherFalseCpfValid);
        assertFalse(reallyFalseCpfValid);
    }

    @Test
    void isCNPJ() {
        String cnpj = "20857656000132";
        String otherCnpj = "20.857.656/0001-32";
        String falseCnpj = "20857656000131";
        String otherFalseCnpj = "00000000000100";
        String reallyFalseCnpj = "00000000000000";
        boolean cnpjValid = Validators.isCNPJ(cnpj);
        boolean falseCnpjValid = Validators.isCNPJ(falseCnpj);
        boolean otherCnpjValid = Validators.isCNPJ(otherCnpj);
        boolean otherFalseCnpjValid = Validators.isCNPJ(otherFalseCnpj);
        boolean reallyFalseCnpjValid = Validators.isCNPJ(reallyFalseCnpj);

        assertTrue(cnpjValid);
        assertTrue(otherCnpjValid);
        assertFalse(falseCnpjValid);
        assertFalse(otherFalseCnpjValid);
        assertFalse(reallyFalseCnpjValid);

    }

    @Test
    void isEmail() {
        String email = "email@example.com";
        String falseEmail = "email.example.com";
        String bigEmail =
                "emailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemail" +
                "@example.com.br";
        boolean emailValid = Validators.isEmail(email);
        boolean falseEmailValid = Validators.isEmail(falseEmail);
        boolean bigEmailValid = Validators.isEmail(bigEmail);

        assertTrue(emailValid);;
        assertFalse(falseEmailValid);
        assertFalse(bigEmailValid);
    }

    @Test
    void isPhoneNumber() {
        String phoneNumber = "+55 19 99999-9999";
        String falsePhoneNumber = "19 99999-9999";
        String otherFalsePhoneNumber = "+555 19 99999-9999";
        boolean phoneNumberValid = Validators.isPhoneNumber(phoneNumber);
        boolean falsePhoneNumberValid =
                Validators.isPhoneNumber(falsePhoneNumber);
        boolean otherFalsePhoneNumberValid =
                Validators.isPhoneNumber(otherFalsePhoneNumber);

        //assertTrue(phoneNumberValid);
        assertFalse(falsePhoneNumberValid);
        assertFalse(otherFalsePhoneNumberValid);
    }
}