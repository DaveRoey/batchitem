package com.dave.batchitem.base;

import com.dave.batchitem.entity.Person;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @Author Dave
 * @Date 2018/10/30
 * @Description
 */
@Component
@StepScope
public class BaseItemReader implements ItemReader<FieldSet> {

    private FlatFileItemReader<FieldSet> itemReader;

    @Override
    public FieldSet read() throws Exception {
        return itemReader.read();
    }

    public BaseItemReader(@Value("#{jobParameters[filePath]}") String filePath, @Value("#{jobParameters[names]}") String names, @Value("#{jobParameters[dlimiter]}") String delimiter) {
        itemReader = new FlatFileItemReader<>();
        DefaultLineMapper<FieldSet> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
        lineTokenizer.setNames(names.split(","));
        itemReader.setLineMapper(lineMapper);
        itemReader.setResource(new ClassPathResource(filePath));
        itemReader.open(new ExecutionContext());

    }
}
