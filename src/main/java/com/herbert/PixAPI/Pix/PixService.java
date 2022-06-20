package com.herbert.PixAPI.Pix;

import com.herbert.PixAPI.Util.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PixService {
    private final PixRepository pixRepository;

    @Autowired
    public PixService(PixRepository pixRepository) {
        this.pixRepository = pixRepository;
    }

    public ResponseEntity<HashMap<String, String>> addNewPix(@NotNull Pix pix) {
        HashMap<String, String> response = new HashMap<>();

        // Validando limite de cadastro por tipo de pessoa fisica ou juridica
        List<Pix> searchPix = pixRepository.findByNumeroContaAndNumeroAgencia(
                                            pix.getNumeroConta(),
                                            pix.getNumeroAgencia());
        char personType = pix.getTipoPessoa();
        int quantity = searchPix.size();

        // TODO: Mover validacoes para uma implementacao de Bean Validation,
        //  e adicionar direto na Entidade
        if(!this.validLimit(personType, quantity)){
            response.put("message", "Limite de cadastro de pix atingido!");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        // validando tipo de chave pix
        String chave = pix.getValorChave();
        boolean validKey = validKeyValuePix(chave, pix.getTipoChave());

        if (!validKey) {
            response.put("message", "Tipo da chave ou valor invalidos");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        // TODO: processar e retornar mensagens de erros
        // Salvando e realizando validacao dos campos
        try{
            Pix savedPix = pixRepository.save(pix);
            response.put("id", savedPix.getId().toString());

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            response.put("message","Error ao adicionar pix, favor validar " +
                    "campos");
            return ResponseEntity.unprocessableEntity().body(response);
        }
    }

    public ResponseEntity<Object> updatePix(@NotNull Pix pix) {
        HashMap<String, String> response = new HashMap<>();
        Optional<Pix> pixToUpdateOptional = pixRepository.findById(pix.getId());

        if(pixToUpdateOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Pix pixToUpdate = pixToUpdateOptional.get();

        if (pixToUpdate.isInativa()) {
            response.put("message", "Cadastro ja se encontra inativo");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        pixToUpdate.setTipoConta(pix.getTipoConta());
        pixToUpdate.setNumeroAgencia(pix.getNumeroAgencia());
        pixToUpdate.setNumeroConta(pix.getNumeroConta());
        pixToUpdate.setNomeCorrentista(pix.getNomeCorrentista());
        pixToUpdate.setSobrenomeCorrentista(pix.getSobrenomeCorrentista());

        try {
            Pix updatedPix = pixRepository.save(pixToUpdate);
            return ResponseEntity.ok(updatedPix);
        } catch (Exception e) {
            response.put("message","Error ao adicionar pix, favor validar " +
                    "campos");
            return ResponseEntity.unprocessableEntity().body(response);
        }
    }

    public ResponseEntity<Object> disablePix(UUID id) {
        Optional<Pix> pixToDisableOptional = pixRepository.findById(id);
        if(pixToDisableOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(pixToDisableOptional);
        }
        Pix pixToDisable = pixToDisableOptional.get();

        pixToDisable.setInativa(true);
        pixToDisable.setInativacaoChave(LocalDateTime.now());

        Pix disabledPix = pixRepository.save(pixToDisable);

        return ResponseEntity.ok(disabledPix);
    }

    public ResponseEntity<Object> find(@NotNull Map<String, Object> findParams) {
        findParams.keySet().removeAll(Collections.singleton(null));

        if (findParams.get("id") != null && findParams.size() == 1){
            Optional<Pix> pixFound = pixRepository.findById((UUID) findParams.get("id"));

            if(pixFound.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pixFound.get());
        }

        if (findParams.get("inclusaoChave") != null &&
                findParams.get("desativacaoChave") != null) {
                return  ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Campos inclusaoChave e desativacaoChave nao " +
                                "podem ser incluidos juntos");
        }

        if(findParams.size() > 0) {
            List<Pix> listaDePix = new ArrayList<>();
            // TODO: implementar busca agregada
//            for (String key: findParams.keySet()) {
//                switch(key){
//
//                }
//            }
            return ResponseEntity.ok(pixRepository.findAll());
        }

        return ResponseEntity.notFound().build();
    }

    private boolean validLimit(char personType, int quantity) {
        // Pessoa Fisica(F): Limite de 5 cadastro de pix
        // Pessoa Juridica(J): Limite de 20 cadastro de pix
        return (personType != 'F' || quantity != 5)
                && (personType != 'J' || quantity != 20);
    }

    private boolean validKeyValuePix(String chave, String tipoChave) {
        return switch (tipoChave) {
            case "cpf" -> Validator.isCPF(chave);
            case "cnpj" -> Validator.isCNPJ(chave);
            case "email" -> Validator.isEmail(chave);
            case "celular" -> Validator.isPhoneNumber(chave);
            default -> false;
        };
    }
}
