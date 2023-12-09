package com.example.batch.config;

import com.example.batch.custom.CustomItemReader;
import com.example.batch.dto.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ItemWriterConfiguration {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    private final EntityManagerFactory entityManagerFactory;

    public ItemWriterConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job itemWriterJob() throws Exception {
        return this.jobBuilderFactory.get("itemWriterJob")
                .incrementer(new RunIdIncrementer())
                .start(this.itemWriterStep())
                .next(this.jdbcBatchItemWriterStep())
                .next(this.jpaBatchItemWriterStep())
                .build();
    }

    @Bean
    public Step itemWriterStep() throws Exception {
        return this.stepBuilderFactory.get("itemWriterStep")
                .<Person, Person>chunk(25)
                .reader(itemReader())
                .writer(csvFileItemWriter())
                .build();
    }

    @Bean
    public Step jdbcBatchItemWriterStep() throws Exception {
        return this.stepBuilderFactory.get("jdbcBatchItemWriterStep")
                .<Person, Person>chunk(25)
                .reader(itemReader())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public Step jpaBatchItemWriterStep() throws Exception {
        return this.stepBuilderFactory.get("jpaBatchItemWriterStep")
                .<Person, Person>chunk(25)
                .reader(itemReader())
                .writer(jpaBatchItemWriter())
                .build();
    }

    private ItemWriter<Person> csvFileItemWriter() throws Exception {
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"id", "name", "age", "address"});

        DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(", ");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<Person> itemWriter = new FlatFileItemWriterBuilder<Person>()
                .name("csvFileItemWriter")
                .encoding("UTF-8")
                .resource(new FileSystemResource("output/csvOutputTest.csv"))
                .lineAggregator(lineAggregator)
                .headerCallback(writer -> writer.write("id, 이름, 나이, 거주지"))
                .footerCallback(writer -> writer.write("=================================================================\n"))
                .append(true)
                .build();
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    private ItemWriter<Person> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriterBuilder<Person>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into person(name, age, address) values(:name, :age, :address)")
                .build();
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    private ItemWriter<Person> jpaBatchItemWriter() throws Exception {
        JpaItemWriter<Person> itemWriter = new JpaItemWriterBuilder<Person>()
                .entityManagerFactory(entityManagerFactory)
                .build();
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    private ItemReader<Person> itemReader() {
        return new CustomItemReader<>(getItems());
    }

    private List<Person> getItems() {
        List<Person> items = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            items.add(new Person(i,"park", i, "address" + i));
        }

        return items;
    }
}
