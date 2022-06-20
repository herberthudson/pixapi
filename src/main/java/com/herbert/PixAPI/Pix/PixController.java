package com.herbert.PixAPI.Pix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/pix")
public class PixController {
    private final PixService pixService;

    @Autowired
    public PixController(PixService pixService) {
        this.pixService = pixService;
    }

    @PostMapping
    public  ResponseEntity<HashMap<String, String>> add(@RequestBody Pix pix) {
        return pixService.addNewPix(pix);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Pix pix) {
        return pixService.updatePix(pix);
    }

    @DeleteMapping(path = "id")
    public ResponseEntity<Object> delete(@PathVariable("id")UUID id){
        return pixService.disablePix(id);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Object> find(@RequestParam Map<String, Object> findParams) {
        return pixService.find(findParams);
    }
}
