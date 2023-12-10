package com.api.AFTAS.domains.ranking;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Ranking {
    @EmbeddedId
    private RankingId id;
    private Integer score;
    private Integer rank;
}
