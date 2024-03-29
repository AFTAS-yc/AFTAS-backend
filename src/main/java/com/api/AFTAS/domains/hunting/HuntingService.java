package com.api.AFTAS.domains.hunting;

import com.api.AFTAS.domains.competition.Competition;
import com.api.AFTAS.domains.competition.CompetitionRepository;
import com.api.AFTAS.domains.fish.Fish;
import com.api.AFTAS.domains.fish.FishRepository;
import com.api.AFTAS.domains.hunting.DTOs.HuntingReqDTO;
import com.api.AFTAS.domains.hunting.DTOs.HuntingRespDTO;
import com.api.AFTAS.security.User.User;
import com.api.AFTAS.security.User.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HuntingService implements HuntingServiceInterface {
    private final HuntingRepository huntingRepository;
    private final ModelMapper modelMapper;
    private final FishRepository fishRepository;
    private final UserRepository memberRepository;
    private final CompetitionRepository competitionRepository;
    private Optional<Fish> fish;
    private Optional<User> member;
    private Optional<Competition> competition;

    @Override
    public HuntingRespDTO create(HuntingReqDTO huntingReq) {
        Hunting huntingE = modelMapper.map(huntingReq, Hunting.class);
        fish = fishRepository.findById(huntingReq.getFish_name());
        member = memberRepository.findById(huntingReq.getMember_num());
        competition = competitionRepository.findById(huntingReq.getCompetition_code());

        if (fish.isPresent() && member.isPresent() && competition.isPresent()) {
            huntingE.setFish(fish.get());
            huntingE.setMember(member.get());
            huntingE.setCompetition(competition.get());
            Optional<Hunting> existHunting = huntingRepository.findByCompetitionAndAndFishAndMember(competition.get(), fish.get(), member.get());
            if (existHunting.isPresent()) {
                huntingE.setId(existHunting.get().getId());
                huntingE.setNumberOfFish(huntingE.getNumberOfFish() + existHunting.get().getNumberOfFish());
            }
            huntingE = huntingRepository.save(huntingE);
            return modelMapper.map(huntingE, HuntingRespDTO.class);
        }
        return null;
    }

    @Override
    public HuntingRespDTO update(HuntingReqDTO huntingReq, Integer id) {

        Optional<Hunting> huntingE = huntingRepository.findById(id);

        if (huntingE.isPresent()) {
            fish = fishRepository.findById(huntingReq.getFish_name());
            member = memberRepository.findById(huntingReq.getMember_num());
            competition = competitionRepository.findById(huntingReq.getCompetition_code());

            if (fish.isPresent() && member.isPresent() && competition.isPresent()) {
                huntingE.get().setFish(fish.get());
                huntingE.get().setMember(member.get());
                huntingE.get().setCompetition(competition.get());
            }

            huntingE = Optional.of(huntingRepository.save(huntingE.get()));
            return modelMapper.map(huntingE.get(), HuntingRespDTO.class);
        }
        return null;
    }

    @Override
    public Integer delete(Integer id) {
        Optional<Hunting> hunting = huntingRepository.findById(id);
        if (hunting.isPresent()) {
            huntingRepository.delete(hunting.get());
            return 1;
        } else return 0;
    }

    @Override
    public List<HuntingRespDTO> getAll() {
        return huntingRepository.findAll()
                .stream()
                .map(hunting -> modelMapper.map(hunting, HuntingRespDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HuntingRespDTO getOne(Integer id) {
        Optional<Hunting> hunting = huntingRepository.findById(id);
        return hunting.map(value -> modelMapper.map(value, HuntingRespDTO.class)).orElse(null);
    }

    public List<HuntingRespDTO> getAllByCompetition(String code) {
        competition = competitionRepository.findById(code);
        if(competition.isPresent()){
            List<Hunting> huntings = huntingRepository.findAllByCompetition(competition.get());
        return huntings
                .stream()
                    .map(hunting -> modelMapper.map(hunting, HuntingRespDTO.class))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
