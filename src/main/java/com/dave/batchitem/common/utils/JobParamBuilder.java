package com.dave.batchitem.common.utils;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Dave
 * @Date 2018/10/31
 * @Description
 */
public class JobParamBuilder<T> {
    private String filePath;
    private String delimiter = ",";
    private String[] filedNames;
    private String sql;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String[] getFiledNames() {
        return filedNames;
    }

    public JobParamBuilder<T> setFiledNames(String... filedNames) {
        this.filedNames = filedNames;
        return this;
    }

    public JobParamBuilder() {

    }

    public JobParamBuilder<T> setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public JobParamBuilder<T> creatSql(String sql) {
        this.sql = sql;
        return this;
    }
    public JobParamBuilder<T> setResource(String filePath) {
        this.filePath = filePath;
        return this;
    }


    public JobParameters build() {
        String str=Stream.of(filedNames).collect(Collectors.joining(","));
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                .addString("filePath", this.filePath)
                .addString("dlimiter", this.delimiter)
                .addString("names",str )
                .addString("sql",this.sql)
                .addString("date","xcxc");
        return jobParametersBuilder.toJobParameters();
    }
}
