package com.example.batch_practice.listener;

import com.example.batch_practice.model.User;
import com.example.batch_practice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class LevelSetJobExecutionListener implements JobExecutionListener {

    private final UserRepository userRepository;

    public LevelSetJobExecutionListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        List<User> items = userRepository.findAllByUpdatedDate(LocalDate.now());

        long operationTime = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();

        log.info("==================================================================================================================");
        log.info("금일 처리된 데이터 => {} (건)", items.size());
        log.info("데이터 처리 시간 => {} (ms)", operationTime);
        log.info("==================================================================================================================");
    }
}
