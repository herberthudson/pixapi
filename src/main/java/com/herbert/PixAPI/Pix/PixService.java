package com.herbert.PixAPI.Pix;

import com.herbert.PixAPI.Util.Validators;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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

        // TODO: Mover validacoes para uma implementacao de Bean Validation
        if(!this.validLimit(personType, quantity)){
            response.put("message", "Limite de cadastro de pix atingido!");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        // validando tipo de chave pix
        // TODO: Mover validacoes para uma implementacao de Bean Validation
        String chave = pix.getValorChave();
        boolean validKey = validKeyValuePix(chave, pix.getTipoChave(), response);

        if (!validKey) {
            response.put("message", "Valor da chave invalido");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Pix>> violations = validator.validate(pix);

        try{
            Pix savedPix = pixRepository.save(pix);
            response.put("id", savedPix.getId().toString());

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            processValidationErros(response, violations);
            return ResponseEntity.unprocessableEntity().body(response);
        }
    }

    public ResponseEntity<?> updatePix(@NotNull Pix pix) {
        HashMap<String, String> response = new HashMap<>();
        Optional<Pix> pixToUpdateOptional = pixRepository.findById(pix.getId());

        if(pixToUpdateOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
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
        pixToUpdate.setInclusaoChave(LocalDateTime.now());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Pix>> violations = validator.validate(pix);

        try {
            Pix updatedPix = pixRepository.save(pixToUpdate);
            return ResponseEntity.ok(updatedPix);
        } catch (Exception e) {
            processValidationErros(response, violations);
            return ResponseEntity.unprocessableEntity().body(response);
        }
    }

    public ResponseEntity<?> disablePix(UUID id) {
        Optional<Pix> pixToDisableOptional = pixRepository.findById(id);
        if(pixToDisableOptional.isEmpty()){
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Cadastro Pix nao encontrado");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        Pix pixToDisable = pixToDisableOptional.get();

        pixToDisable.setInativa(true);
        pixToDisable.setInativacaoChave(LocalDateTime.now());

        Pix disabledPix = pixRepository.save(pixToDisable);

        return ResponseEntity.ok(disabledPix);
    }

    public ResponseEntity<Object> find(@NotNull Map<String, Object> findParams) {
        HashMap<String, String> response = new HashMap<>();
        // removendo nulos
        findParams.keySet().removeAll(Collections.singleton(null));
        int paramsLength = findParams.size();
        Object paramId = findParams.get("id");
        Object paramNomeCorrentista = findParams.get("nomeCorrentista");
        Object paramTipoChave = findParams.get("tipoChave");
        Object paramInclusaoChave = findParams.get("inclusaoChave");
        Object paramDesativacaoChave = findParams.get("desativacaoChave");
        
        if (paramId != null && paramsLength == 1){
            UUID parsedId = UUID.fromString((String) paramId);
            Optional<Pix> pixFound = pixRepository.findById(parsedId);

            if(pixFound.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pixFound.get());
        } else if (paramId != null && paramsLength > 1){
            response.put("message", "id e filtros nao podem ser informados " +
                    "juntos");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        if (paramInclusaoChave != null && paramDesativacaoChave != null) {
                response.put("message","Campos inclusaoChave e desativacaoChave nao " +
                        "podem ser incluidos juntos" );

                return  ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(response);
        }

        if(paramsLength > 0) {
            List<Pix> result;
            boolean containsTipoChave = findParams.containsKey("tipoChave");
            boolean containsNomeCorrentista = findParams.containsKey(
                    "nomeCorrentista");

            if( containsNomeCorrentista && containsTipoChave ) {
                result = pixRepository
                        .findByNomeCorrentistaAndTipoChave(
                                findParams.get("nomeCorrentista"),
                                findParams.get("tipoChave"));
            } else if (containsNomeCorrentista) {
                result = pixRepository.findByNomeCorrentista(findParams.get(
                        "nomeCorrentista"));
            } else if (containsTipoChave){
                result = pixRepository.findByTipoChave(findParams.get(
                        "tipoChave"));
            } else {
                response.put("message", "filtros invalidos");
                return ResponseEntity.unprocessableEntity().body(response);
            }

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.unprocessableEntity().build();
    }

    private boolean validLimit(char personType, int quantity) {
        // Pessoa Fisica(F): Limite de 5 cadastro de pix
        // Pessoa Juridica(J): Limite de 20 cadastro de pix
        return (personType != 'F' && quantity <= 5)
                || (personType != 'J' && quantity != 20);
    }

    private boolean validKeyValuePix(String chave,
                                     String tipoChave,
                                     HashMap<String, String> response) {
        boolean valid = true;
        switch (tipoChave) {
            case "cpf":
                if(!Validators.isCPF(chave)){
                    response.put("cpf", "Campo cpf invalido");
                    valid = false;
                }
                break;
            case "cnpj":
                if(!Validators.isCNPJ(chave)){
                    response.put("cnpj", "Campo cnpj invalido");
                    valid = false;
                }
                break;
            case "email":
                if(!Validators.isEmail(chave)){
                    response.put("email", "Campo email invalido");
                    valid = false;
                };
                break;
            case "celular":
                if(!Validators.isPhoneNumber(chave)){
                    response.put("celular", "Campo celuar invalido");
                    valid = false;
                };
                break;
        };
        return valid;
    }
    private void processValidationErros(HashMap<String, String> response,
                                        Set<ConstraintViolation<Pix>> violations){
        for (ConstraintViolation<Pix> violation : violations) {
            response.put(String.valueOf(violation.getPropertyPath()),
                    violation.getMessage());
        }
    }
}
