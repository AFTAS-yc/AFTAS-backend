package com.api.AFTAS.domains.hunting;

import com.api.AFTAS.domains.competition.Competition;
import com.api.AFTAS.domains.fish.Fish;
import com.api.AFTAS.security.User.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Hunting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer numberOfFish;
    @ManyToOne
    @JoinColumn(name = "fish_name",referencedColumnName = "name")
    private Fish fish;
    @ManyToOne
    @JoinColumn(name = "member_num",referencedColumnName = "id")
    private User member;
    @ManyToOne
    @JoinColumn(name = "competition_code",referencedColumnName = "code")
    private Competition competition;
}
