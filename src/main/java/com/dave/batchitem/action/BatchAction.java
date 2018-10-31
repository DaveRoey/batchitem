package com.dave.batchitem.action;

import com.dave.batchitem.common.utils.JobParamBuilder;
import com.dave.batchitem.entity.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dave on 2018/10/29
 * Describes
 */
@RestController
@RequestMapping(value = "/batch/*")
public class BatchAction {
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job job;

    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public void test() {
        try {
            /*JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                    .addString("filePath", "/data/person.csv")
                    .addString("dlimiter", ",")
                    .addString("names", "name,gender,age")
                    .addString("date", "2018-10-20");*/


            JobParameters jobParameters=new JobParamBuilder<Person>()
                    .setDelimiter(",")
                    .setResource("/data/person.csv")
                    .setFiledNames("name,gender,age")
                    .creatSql("INSERT INTO person(id,name,gender,age) VALUES (null,:name,:gender,:age)")
                    .build();
            jobLauncher.run(job, jobParameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
