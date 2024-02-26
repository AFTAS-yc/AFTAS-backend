package com.api.AFTAS.domains.ranking;

import com.api.AFTAS.domains.competition.Competition;
import com.api.AFTAS.security.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking,RankingId> {
    @Query("SELECT NEW Ranking(h.member, c, SUM(f.level.points * h.numberOfFish) AS  score) " +
            "FROM Hunting h " +
            "JOIN h.fish f " +
            "JOIN h.competition c " +
            "WHERE c.code = :competitionCode " +
            "GROUP BY h.member, c " +
            "ORDER BY score DESC")
    List<Ranking> calculateRankingsForCompetition(@Param("competitionCode") String competitionCode);
    @Query("select NEW Ranking(r.id.member,r.id.competition,r.score as score,r.rank) from Ranking r WHERE r.id.competition.code =:competitionCode ORDER BY score DESC")
    List<Ranking> getAllByCompetition(@Param("competitionCode") String competitionCode);
    Optional<Ranking> findById_CompetitionAndId_Member(Competition competition, User member);
}
