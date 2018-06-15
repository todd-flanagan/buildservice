package buildservice.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import buildservice.Build;
import buildservice.processor.BuildItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BuildRowMapper rowMapper;

    // tag::readerwriterprocessor[]
    @Bean
    public JdbcCursorItemReader<Build> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Build>()
            .name("buildItemReader")
            .sql("Select id, toolbox, ctf from builds")
            .dataSource(dataSource)
            .rowMapper(rowMapper)
            .build();
    }

    @Bean
    public BuildItemProcessor buildProcessor() {
        return new BuildItemProcessor();
    }


    @Bean
    public JdbcBatchItemWriter<Build> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Build>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO completedbuilds (toolbox, ctf) VALUES (:toolbox, :ctf)")
            .dataSource(dataSource)
            .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    //invoke build step
    @Bean
    public Step step1(JdbcCursorItemReader<Build> reader, JdbcBatchItemWriter<Build> writer) {
        return stepBuilderFactory.get("step1")
            .<Build, Build> chunk(10)
            .reader(reader)
            .processor(buildProcessor())
            .writer(writer)
            .build();
    }
    // end::jobstep[]

}
