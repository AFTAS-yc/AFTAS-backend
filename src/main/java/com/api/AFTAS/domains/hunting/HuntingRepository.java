package com.api.AFTAS.domains.hunting;

import com.api.AFTAS.domains.competition.Competition;
import com.api.AFTAS.domains.fish.Fish;
import com.api.AFTAS.security.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuntingRepository extends JpaRepository<Hunting,Integer> {
    Optional<Hunting> findByCompetitionAndAndFishAndMember(Competition competition, Fish fish , User member);
    List<Hunting> findAllByCompetition(Competition competition);
}
