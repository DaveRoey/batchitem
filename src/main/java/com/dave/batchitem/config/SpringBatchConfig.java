package com.dave.batchitem.config;

import com.dave.batchitem.base.BaseItemReader;
import com.dave.batchitem.entity.Person;
import com.dave.batchitem.listener.MyJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.time.Period;

/**
 * Created by Dave on 2018/10/29
 * Describes
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Value("${resource.read.path}")
    private String path;

    @Autowired
    private  JobBuilderFactory jobBuilderFactory;
    @Autowired
    private  StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ItemReader<Person> baseItemReader;



    @Bean
    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }


    @Bean
    JobRepository createJobRepository(DataSource dataSource,PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setMaxVarCharLength(1000);
        return factory.getObject();
    }


    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return this.jobBuilderFactory.get("sampleJob")
                .repository(jobRepository)
                .listener(new MyJobListener())
                .start(sampleStep)
                .build();
    }

    @Bean
    public Step sampleStep(PlatformTransactionManager transactionManager) {
        return this.stepBuilderFactory.get("sampleStep")
                .transactionManager(transactionManager)
                .<Person, Person>chunk(10)
                .reader(baseItemReader)
                .writer(customerItemWriter())
                .build();
    }

    /**
     * @StepScope SpringBatch的一个后绑定技术，就是在生成Step的时候，才去创建bean，因为这个时候jobparameter才传过来。
     * @return
     */
    @Bean
    @StepScope
     public JdbcBatchItemWriter<Person> customerItemWriter() {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO person(id,name,gender,age) VALUES (null,:name,:gender,:age)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    ItemReader<Person> fileReader(){
        FlatFileItemReader<Person> reader=new FlatFileItemReader<Person>();
        reader.setResource(createResource());

        DefaultLineMapper<Person> lineMapper=new DefaultLineMapper<>();
        BeanWrapperFieldSetMapper<Person> mapper=new BeanWrapperFieldSetMapper<Person>();
        mapper.setTargetType(Person.class);
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("name","gender","age");
        lineMapper.setFieldSetMapper(mapper);
        lineMapper.setLineTokenizer(lineTokenizer);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    Resource createResource(){
        return new ClassPathResource(getPath());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
