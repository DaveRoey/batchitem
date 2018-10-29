package com.dave.batchitem.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import javax.batch.api.listener.JobListener;

/**
 * Created by Dave on 2018/10/29
 * Describes
 */
public class MyJobListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getCreateTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getEndTime());
        if( jobExecution.getStatus() == BatchStatus.COMPLETED ){
            //job success
            System.out.println("JOB执行成功。。。");
        }
        else if(jobExecution.getStatus() == BatchStatus.FAILED){
            //job failure
            System.out.println("JOB执行失败。。。");

        }
    }
}
