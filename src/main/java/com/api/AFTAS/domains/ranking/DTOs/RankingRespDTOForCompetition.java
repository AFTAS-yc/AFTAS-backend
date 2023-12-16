package com.api.AFTAS.domains.ranking.DTOs;

import lombok.Data;

@Data
public class RankingRespDTOForCompetition {
    private RankingIdRespForCompetition id;
    private Integer score;
    private Integer rank;
}
