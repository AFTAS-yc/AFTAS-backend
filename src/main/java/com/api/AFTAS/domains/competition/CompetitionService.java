package com.api.AFTAS.domains.competition;

import com.api.AFTAS.domains.competition.DTOs.CompetitionReqDTO;
import com.api.AFTAS.domains.competition.DTOs.CompetitionRespDTO;
import com.api.AFTAS.domains.ranking.RankingJob;
import org.modelmapper.ModelMapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionService implements CompetitionServiceInterface {
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Scheduler scheduler;

    @Override
    public CompetitionRespDTO create(CompetitionReqDTO competitionReq) {
        Competition competition = modelMapper.map(competitionReq, Competition.class);
        competition = competitionRepository.save(competition);
        startTriggerJob(competition);
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
        if (competition.isPresent()) {
            competitionRepository.delete(competition.get());
            return 1;
        } else return 0;
    }

    @Override
    public List<CompetitionRespDTO> getAll() {
        return competitionRepository.findAll()
                .stream()
                .map(competition -> modelMapper.map(competition, CompetitionRespDTO.class))
                .collect(Collectors.toList());
    }

    public Page<CompetitionRespDTO> getAllWithPagination(Pageable pageable) {
        Page<Competition> competitions = competitionRepository.findAll(pageable);
        competitions.getContent().forEach(competition -> {
            int result = competition.getDate().compareTo(LocalDate.now());

            long hoursDifference = ChronoUnit.HOURS.between(LocalDateTime.now(), LocalDateTime.of(
                    competition.getDate(),
                    competition.getEndTime()
            ));
            if (result < 0) {
                competition.setEtat(Etat.close);
            } else if (result > 0) {
                if (hoursDifference <= 24) {
                    competition.setEtat(Etat.one_day_remaining);
                }else {
                    competition.setEtat(Etat.waiting);
                }
            } else {
                competition.setEtat(Etat.Pending);
            }
            competitionRepository.save(modelMapper.map(competition,Competition.class));
        });
        Page<CompetitionRespDTO> competitionRespDTOS = competitions
                .map(competition -> modelMapper.map(competition, CompetitionRespDTO.class));
        return competitionRespDTOS;
    }

    public Page<CompetitionRespDTO> getAllWithPaginationByEtat(Pageable pageable,Etat etat) {
        Page<Competition> competitions = competitionRepository.findAllByEtat(etat, pageable);
        return competitions
                .map(competition -> modelMapper.map(competition, CompetitionRespDTO.class));
    }

    @Override
    public CompetitionRespDTO getOne(String code) {
        Optional<Competition> competition = competitionRepository.findById(code);
        CompetitionRespDTO competitionRespDTO = competition.map(value -> modelMapper.map(value, CompetitionRespDTO.class)).orElse(null);
        assert competitionRespDTO != null;

        int result = competitionRespDTO.getDate().compareTo(LocalDate.now());
        long hoursDifference = ChronoUnit.HOURS.between(LocalDateTime.now(), LocalDateTime.of(
                competitionRespDTO.getDate(),
                competitionRespDTO.getEndTime()
        ));
        if (result < 0) {
            competitionRespDTO.setEtat(Etat.close);
        } else if (result > 0) {
            if (hoursDifference <= 24) {
                competitionRespDTO.setEtat(Etat.one_day_remaining);
            }else{
            competitionRespDTO.setEtat(Etat.waiting);
            }
        } else {
            competitionRespDTO.setEtat(Etat.Pending);
        }
        return competitionRespDTO;
    }

    public void startTriggerJob(Competition competition) {
        LocalDate competitionDate = competition.getDate();
        LocalTime competitionEndTime = competition.getEndTime();

        LocalDateTime competitionEndDateTime =
                LocalDateTime.of(
                        competitionDate,
                        competitionEndTime
                );

        // Validate if the competitionEndDateTime is in the future
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (competitionEndDateTime.isBefore(currentDateTime)) {
            // Handle the case where the end time is not in the future
            throw new RuntimeException("Competition end time must be in the future");
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobID", "Job-" + competition.getCode());
        jobDataMap.put("competitionCode", competition.getCode());

        JobDetail job = JobBuilder.newJob(RankingJob.class)
                .withIdentity("jobIdentity-" + competition.getCode())
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("triggerIdentity-" + competition.getCode())
                .startAt(
                        Date.from(
                                competitionEndDateTime.atZone(
                                        ZoneId.systemDefault()
                                ).toInstant()
                        )
                )
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }
}
