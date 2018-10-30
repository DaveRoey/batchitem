package com.dave.batchitem.base;

import com.dave.batchitem.common.utils.GenericsUtils;
import com.dave.batchitem.entity.Person;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

/**
 * @Author Dave
 * @Date 2018/10/30
 * @Description
 */
@Component
@StepScope
public class BaseItemReader<T> implements ItemReader<T> {

    private FlatFileItemReader<T> itemReader;

    @Override
    public T read() throws Exception {
        return itemReader.read();
    }

    public BaseItemReader(@Value("#{jobParameters[filePath]}") String filePath, @Value("#{jobParameters[names]}") String names, @Value("#{jobParameters[dlimiter]}") String delimiter) {
        itemReader = new FlatFileItemReader<>();
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);
        Class[] interfaces = BaseItemReader.class.getInterfaces();

        lineTokenizer.setNames(names.split(","));
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<T> mapper = new BeanWrapperFieldSetMapper<>();
        Class<T> entityClass = GenericsUtils.getSuperClassGenricType(BaseItemReader.class, 0);
        System.out.println("entityClass"+entityClass);

        mapper.setTargetType();
        mapper.setPrototypeBeanName("");
        lineMapper.setFieldSetMapper(mapper);
        itemReader.setLineMapper(lineMapper);
        itemReader.setResource(new ClassPathResource(filePath));
        itemReader.open(new ExecutionContext());

    }
}
