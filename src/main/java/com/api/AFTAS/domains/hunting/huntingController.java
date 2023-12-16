package com.api.AFTAS.domains.hunting;

import com.api.AFTAS.config.CrudController;
import com.api.AFTAS.domains.competition.DTOs.CompetitionReqDTO;
import com.api.AFTAS.domains.competition.DTOs.CompetitionRespDTO;
import com.api.AFTAS.domains.hunting.DTOs.HuntingReqDTO;
import com.api.AFTAS.domains.hunting.DTOs.HuntingRespDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("hunting")
public class huntingController extends CrudController<HuntingReqDTO, HuntingRespDTO,Integer,HuntingServiceInterface> {
    @GetMapping("/competition/{code}")
    public ResponseEntity<List<HuntingRespDTO>> getAllByCompetition(@PathVariable String code)
    {
        return ResponseEntity.ok().body(this.service.getAllByCompetition(code));
    }
}
