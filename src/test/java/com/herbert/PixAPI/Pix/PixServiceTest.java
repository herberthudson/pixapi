package com.herbert.PixAPI.Pix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PixServiceTest {

    @InjectMocks
    private Pix pix;

    @Autowired
    private PixService pixService;

    @Autowired
    private PixRepository pixRepository;

    @BeforeEach
    void init(){
        this.pix = new Pix("email",
                "herberthudson@example.com",
                "corrente",
                1234,
                23132343,
                'F',
                "Herbert",
                "Hudson");
    }
    @Test
    void testAddNewPix() {
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);
        boolean containId =
                Objects.requireNonNull(result.getBody()).toString().contains("id");
        assertTrue(containId);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testNewPixNotCpf(){
        pix.setTipoChave("cpf");
        pix.setValorChave("06901017694");
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        assertEquals(result.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testNewPixNotCnpj(){
        pix.setTipoChave("cnpj");
        pix.setValorChave("00000000000000");
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        assertEquals(result.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testNewPixNotEmail(){
        pix.setTipoChave("email");
        pix.setValorChave("herberthudsonexamplo.com");
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        assertEquals(result.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testNewPixNotCelular(){
        pix.setTipoChave("celular");
        pix.setValorChave("");
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        assertEquals(result.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testNewPixError(){
        pix.setNumeroAgencia(12345);
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        assertEquals(result.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testNewPixNomeCorrentista(){
        pix.setNomeCorrentista(
                "alkdfladjldkjflsdajflsdkjfsdlkfjasdlfjasdfkasdjfasdlfkdskfjdalfjkasdlfj");
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        assertEquals(result.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testUpdatePix() {
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        UUID id = UUID.fromString(Objects.requireNonNull(result.getBody()).get("id").toString());

        pix.setId(id);

        ResponseEntity<?> resultUpdate =
                pixService.updatePix(this.pix);
        assertEquals(resultUpdate.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void testUpdatePixNotFound() {
        UUID id = UUID.randomUUID();

        pix.setId(id);

        ResponseEntity<?> resultUpdate =
                pixService.updatePix(this.pix);

        assertEquals(resultUpdate.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdatePixInativo() {
//        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);
//
//        UUID id = UUID.fromString(Objects.requireNonNull(result.getBody()).get("id").toString());
//
//        pix.setId(id);
//        pix.setInativa(true);
//
//        ResponseEntity<?> updatedPix =
//                pixService.updatePix(this.pix);
//
//        ResponseEntity<?> resultUpdate =
//                pixService.updatePix((Pix) Objects.requireNonNull(updatedPix.getBody()));
//
//        assertEquals(resultUpdate.getStatusCode(),
//                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testDisablePix() {
        ResponseEntity<HashMap<String, String>> result = pixService.addNewPix(this.pix);

        UUID id = UUID.fromString(Objects.requireNonNull(result.getBody()).get("id").toString());

        pix.setId(id);

        ResponseEntity<?> resultUpdate =
                pixService.disablePix(pix.getId());
        assertEquals(resultUpdate.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testDisablePixNotFound() {
        UUID id = UUID.randomUUID();

        ResponseEntity<?> resultUpdate =
                pixService.disablePix(id);
        assertEquals(resultUpdate.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testFindById() {
        ResponseEntity<HashMap<String, String>> result =
                pixService.addNewPix(this.pix);

        String id = Objects.requireNonNull(result.getBody()).get("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testFindByIdNotFound() {
        String id = UUID.randomUUID().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindByInclusaoDesativacao() {
        Map<String, Object> params = new HashMap<>();
        params.put("inclusaoChave", "");
        params.put("desativacaoChave", "");
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testFindByUniqueId() {
        Map<String, Object> params = new HashMap<>();
        String id = UUID.randomUUID().toString();
        params.put("id", id);
        params.put("tipoChave", "email");
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testFindByTipoChave() {
        Map<String, Object> params = new HashMap<>();
        params.put("tipoChave", "email");
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testFindByNomeCorrentista() {
        Map<String, Object> params = new HashMap<>();
        params.put("nomeCorrentista", "Herbert");
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testFindByNomeCorrentistaAndTipoChave() {
        Map<String, Object> params = new HashMap<>();
        params.put("nomeCorrentista", "Herbert");
        params.put("tipoChave", "email");
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testFindByInvalidFilters() {
        Map<String, Object> params = new HashMap<>();
        params.put("field", "invalid");
        ResponseEntity<Object> resultFind = pixService.find(params);

        assertEquals(resultFind.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}