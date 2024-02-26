package com.api.AFTAS.domains.ranking;


import com.api.AFTAS.domains.competition.CompetitionServiceInterface;
import com.api.AFTAS.domains.competition.DTOs.CompetitionRespDTO;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class RankingJob implements Job {
    @Autowired
    private RankingService rankingService;
    @Autowired
    private CompetitionServiceInterface competitionService;


    /**
     * Executes the ranking calculation job.
     *
     * @param jobExecutionContext The execution context of the Quartz job.
     * @throws JobExecutionException If an error occurs during job execution.
     */
    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            CompetitionRespDTO competition = competitionService.getOne(
                    (String) jobDataMap.get("competitionCode")
                    );
            rankingService.calculateAndFetchRankings(competition.getCode());
            String jobId = (String) jobDataMap.get("jobID");
            System.out.println(jobId);
        } catch (Exception e) {
            throw new JobExecutionException(e.getMessage());
        }
    }
}