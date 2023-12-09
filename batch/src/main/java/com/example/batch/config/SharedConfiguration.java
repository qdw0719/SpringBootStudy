package com.example.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SharedConfiguration {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public SharedConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job shareJob() {
        return jobBuilderFactory.get("shareJob")
                .incrementer(new RunIdIncrementer())
                .start(shareStep())
                .next(shareNextStep())
                .build();
    }

    @Bean
    public Step shareStep() {
        return stepBuilderFactory.get("shareStep")
                .tasklet((contribution, chunkContext) -> {
                    StepExecution stepExecution = contribution.getStepExecution();
                    ExecutionContext stepExcutionContext = stepExecution.getExecutionContext();
                    stepExcutionContext.putString("stepKey", "step execution context");

                    JobExecution jobExecution = stepExecution.getJobExecution();
                    JobInstance jobInstance = jobExecution.getJobInstance();
                    ExecutionContext jobExcutionContext = jobExecution.getExecutionContext();
                    jobExcutionContext.putString("jobKey", "job execution context");
                    JobParameters jobParameters = jobExecution.getJobParameters();

                    log.info("shareStep >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    log.info("jobName: {}, stepName: {}, parameter: {}",
                            jobInstance.getJobName(),
                            stepExecution.getStepName(),
                            jobParameters.getLong("run.id")
                    );

                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step shareNextStep() {
        return stepBuilderFactory.get("shareNextStep")
                .tasklet((contribution, chunkContext) -> {
                    StepExecution stepExecution = contribution.getStepExecution();
                    ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();

                    JobExecution jobExecution = stepExecution.getJobExecution();
                    ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();

                    log.info("shareNextStep >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    log.info("jobKey: {}, stepKey: {}",
                            jobExecutionContext.getString("jobKey", "emptyJobKey"),
                            stepExecutionContext.getString("stepKey", "emptyStepKey")
                    );
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
