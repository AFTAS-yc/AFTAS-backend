package com.api.AFTAS.domains.competition;

import com.api.AFTAS.domains.competition.DTOs.CompetitionReqDTO;
import com.api.AFTAS.domains.competition.DTOs.CompetitionRespDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionService implements CompetitionServiceInterface{
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CompetitionRespDTO create(CompetitionReqDTO competitionReq) {
        Competition competition = modelMapper.map(competitionReq, Competition.class);
            competition = competitionRepository.save(competition);
            return modelMapper.map(competition, CompetitionRespDTO.class);
    }

    @Override
    public CompetitionRespDTO update(CompetitionReqDTO competitionReq, String code) {
        Optional<Competition> existCompetition = competitionRepository.findById(code);
        if (existCompetition.isPresent()) {
            competitionReq.setCode(existCompetition.get().getCode());
            return modelMapper.map(competitionRepository.save(modelMapper.map(competitionReq, Competition.class)), CompetitionRespDTO.class);
        }
        return null;
    }

    @Override
    public Integer delete(String code) {
        Optional<Competition> competition = competitionRepository.findById(code);
        if(competition.isPresent()) {
            competitionRepository.delete(competition.get());
            return 1;
        }else return 0;
    }

    @Override
    public List<CompetitionRespDTO> getAll() {
        return competitionRepository.findAll()
                .stream()
                .map(competition -> modelMapper.map(competition,CompetitionRespDTO.class))
                .collect(Collectors.toList());
    }

    public Page<CompetitionRespDTO> getAllWithPagination(Pageable pageable) {
        Page<Competition> competitions = competitionRepository.findAll(pageable);
        Page<CompetitionRespDTO> competitionRespDTOS= competitions
                .map(competition -> modelMapper.map(competition, CompetitionRespDTO.class));
        competitionRespDTOS.getContent().forEach(competitionRespDTO -> {
            int result = competitionRespDTO.getDate().compareTo(LocalDate.now());
            long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(),competitionRespDTO.getDate());
            if (result < 0) {
                competitionRespDTO.setEtat("close");
            } else if (result > 0) {
                if(daysDifference == 1){
                    competitionRespDTO.setEtat("one day remaining");
                }
                competitionRespDTO.setEtat("waiting");
            } else {
                competitionRespDTO.setEtat("Pending ...");
            }
        });
        return competitionRespDTOS;
    }

    @Override
    public CompetitionRespDTO getOne(String code) {
        Optional<Competition> competition = competitionRepository.findById(code);
        CompetitionRespDTO competitionRespDTO = competition.map(value -> modelMapper.map(value, CompetitionRespDTO.class)).orElse(null);;
        assert competitionRespDTO != null;

        int result = competitionRespDTO.getDate().compareTo(LocalDate.now());
        long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(),competitionRespDTO.getDate());
        if (result < 0) {
            competitionRespDTO.setEtat("close");
        } else if (result > 0) {
            if(daysDifference == 1){
                competitionRespDTO.setEtat("one day remaining");
            }
            competitionRespDTO.setEtat("waiting");
        } else {
            competitionRespDTO.setEtat("Pending");
        }
        return competitionRespDTO;
    }
}
