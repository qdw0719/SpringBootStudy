package com.example.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ChunkProcessingConfiguration {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public ChunkProcessingConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job chunkProcessingJob() {
        return jobBuilderFactory.get("chunkProcessingJob")
                .incrementer(new RunIdIncrementer())
                .start(taskBaseStep())
                .next(chunkBaseStep())
                .build();
    }

    @Bean
    public Step chunkBaseStep() {
        return stepBuilderFactory.get("chunkBaseStep")
                .<String, String>chunk(20)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWirter())
                .build();
    }

    private ItemWriter<String> itemWirter() {
        return items -> log.info("chunk item size: {}", items.size());
//        return items -> items.forEach(log::info);
    }

    private ItemProcessor<String, String> itemProcessor() {
        return item -> item + ", Spring Batch";
    }

    private ListItemReader<String> itemReader() {
        return new ListItemReader<>(getItems());
    }

    @Bean
    public Step taskBaseStep() {
        return stepBuilderFactory.get("taskBaseStep")
                .tasklet(tasklet())
                .build();
    }

    private Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            List<String> items = getItems();
            log.info("task item size: {}", items.size());

            return RepeatStatus.FINISHED;
        };
    }

    private List<String> getItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0, size = 100; i < size; i++) {
            items.add(i + " item");
        }
        return items;
    }

}
