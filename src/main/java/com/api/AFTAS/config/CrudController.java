package com.api.AFTAS.config;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class CrudController<DTOReq,DTOResp,Id,Service extends CrudInterface<DTOReq,DTOResp,Id>> {
    @Autowired
    protected Service service;
    @Autowired
    protected ResponseMessage responseMessage;

    @PostMapping
    public ResponseEntity<DTOResp> create(@Valid @RequestBody DTOReq dtoReq) {
        DTOResp level = service.create(dtoReq);
        if(level != null)
        {
            return ResponseEntity.ok().body(level);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTOResp> update(@Valid @RequestBody DTOReq dtoReq,@PathVariable Id id) {
        DTOResp level1 = service.update(dtoReq,id);
        if(level1 != null)
        {
            return ResponseEntity.ok().body(level1);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Id id) {
        Integer deleted = service.delete(id);
        if(deleted == 1)
        {
            this.responseMessage.setMessage("deleted");
            return ResponseEntity.ok().body(this.responseMessage);
        }
        this.responseMessage.setMessage("not deleted");
        return ResponseEntity.badRequest().body(this.responseMessage);
    }


    @GetMapping
    public ResponseEntity<List<DTOResp>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DTOResp> getOne(@PathVariable Id id) {
        return ResponseEntity.ok().body(service.getOne(id));
    }
}
