package com.dave.batchitem.common.utils;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Arrays;

/**
 * @Author Dave
 * @Date 2018/10/31
 * @Description
 */
public class JobParamBuilder<T> {
    private String filePath;
    private String delimiter = ",";
    private String[] filedNames;
    private Class<T> bean;

    public Class<T> getBean() {
        return bean;
    }

    public JobParamBuilder<T> setBean(Class<T> bean) {
        this.bean = bean;
        return this;
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


    public JobParameters build() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                .addString("filePath", this.filePath)
                .addString("dlimiter", this.delimiter)
                .addString("names", Arrays.toString(this.filedNames))
                .addString("bean",this.bean.toGenericString()) ;
        return jobParametersBuilder.toJobParameters();
    }
}
