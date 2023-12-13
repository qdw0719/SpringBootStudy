package com.example.batch_practice.batchConfig;

import com.example.batch_practice.execution.JobParametersDecide;
import com.example.batch_practice.listener.LevelSetJobExecutionListener;
import com.example.batch_practice.model.OrderStatistics;
import com.example.batch_practice.model.User;
import com.example.batch_practice.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserConfiguration {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final UserRepository userRepository;
    private final DataSource dataSource;

    private String currentDate = "2023-11";

    public UserConfiguration(JobBuilderFactory jobBuilderFactory,
                             StepBuilderFactory stepBuilderFactory,
                             EntityManagerFactory entityManagerFactory,
                             UserRepository userRepository, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.userRepository = userRepository;
        this.dataSource = dataSource;
    }

    @Bean
    public Job userJob() throws Exception {
        return this.jobBuilderFactory.get("userJob")
                .incrementer(new RunIdIncrementer())
                .start(this.userStep())
                .next(this.userLevelSettingStep())
                .next(this.orderStatisticsStep())
                .listener(new LevelSetJobExecutionListener(this.userRepository))
                .build();
    }

    @Bean
    public Step userStep() {
        return this.stepBuilderFactory.get("userStep")
                .tasklet(new SaveUserTasklet(this.userRepository))
                .build();
    }

    @Bean
    public Step userLevelSettingStep() throws Exception {
        return this.stepBuilderFactory.get("userLevelSettingStep")
                .<User, User>chunk(CHUNK_SIZE)
                .reader(this.itemReader())
                .processor(this.itemProcessor())
                .writer(this.itemWriter())
                .build();
    }

    @Bean
//    @JobScope
//    public Step orderStatisticsStep(@Value("#{jobParameters.date}") String date) throws Exception {
    public Step orderStatisticsStep() throws Exception {
        return this.stepBuilderFactory.get("orderStatisticsStep")
                .<OrderStatistics, OrderStatistics>chunk(CHUNK_SIZE)
                .reader(orderStatisticsItemReader(this.currentDate))
                .writer(orderStatisticsItemWriter(this.currentDate))
                .build();
    }

    private ItemReader<User> itemReader() throws Exception {
        JpaPagingItemReader<User> itemReader = new JpaPagingItemReaderBuilder<User>()
                .queryString("select u from User u")
                .entityManagerFactory(this.entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .name("userItemReader")
                .build();
        itemReader.afterPropertiesSet();
        return itemReader;
    }

    private ItemProcessor<User, User> itemProcessor() {
        return user -> {
            if (user.availableLevelSet()) {
                return user;
            }
            return null;
        };
    }

    private ItemWriter<User> itemWriter() {
        return users -> {
            users.forEach(
                    user -> {
                        user.levelSet();
                        userRepository.save(user);
                    });
        };
    }

    private ItemReader<OrderStatistics> orderStatisticsItemReader(String date) throws Exception {
        YearMonth yearMonth = YearMonth.parse(date);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", yearMonth.atDay(1));
        parameters.put("endDate", yearMonth.atEndOfMonth());

        Map<String, Order> sortKey = new HashMap<>();
        sortKey.put("created_date", Order.ASCENDING);

        JdbcPagingItemReader<OrderStatistics> itemReader = new JdbcPagingItemReaderBuilder<OrderStatistics>()
                .dataSource(this.dataSource)
                .rowMapper((resultSet, i) ->
                        OrderStatistics.builder()
                        .date(LocalDate.parse(resultSet.getString(1), DateTimeFormatter.ISO_LOCAL_DATE))
                        .amount(resultSet.getString(2))
                        .build()
                )
                .pageSize(CHUNK_SIZE)
                .name("statisticsItemWriter")
                .selectClause("created_date, sum(amount)")
                .fromClause("orders")
                .whereClause("created_date between :startDate and :endDate")
                .groupClause("created_date")
                .parameterValues(parameters)
                .sortKeys(sortKey)
                .build();
        itemReader.afterPropertiesSet();
        return itemReader;
    }

    private ItemWriter<OrderStatistics> orderStatisticsItemWriter(String date) throws Exception {
        YearMonth yearMonth = YearMonth.parse(date);
        String fileName = yearMonth.getYear() + "년_" + yearMonth.getMonthValue() + "월_일별_주문_금액.csv";

        BeanWrapperFieldExtractor<OrderStatistics> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"date", "amount"});

        DelimitedLineAggregator<OrderStatistics> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(", ");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<OrderStatistics> itemWriter = new FlatFileItemWriterBuilder<OrderStatistics>()
                .resource(new FileSystemResource("output/" + fileName))
                .lineAggregator(lineAggregator)
                .name("statisticsItemWriter")
                .encoding("UTF-8")
                .headerCallback(writer -> writer.write("date, total_amount"))
                .build();
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
