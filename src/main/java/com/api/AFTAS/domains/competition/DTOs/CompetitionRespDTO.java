package com.api.AFTAS.domains.competition.DTOs;

import com.api.AFTAS.domains.hunting.DTOs.HuntingRespDTO;
import com.api.AFTAS.domains.ranking.DTOs.RankingRespDTO;
import com.api.AFTAS.domains.ranking.DTOs.RankingRespDTOForCompetition;
import com.api.AFTAS.domains.ranking.Ranking;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CompetitionRespDTO {
    private String code;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer numberOfParticipants;
    private String location;
    private Double amount;
    private String etat;
    private List<RankingRespDTOForCompetition> rankings;
    private List<HuntingRespDTO> huntings;
}
