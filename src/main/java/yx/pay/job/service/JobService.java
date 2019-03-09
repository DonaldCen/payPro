package yx.pay.job.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.IService;
import yx.pay.job.domain.Job;

import java.util.List;

public interface JobService extends IService<Job> {

    Job findJob(Long jobId);

    List<Job> findJobs(QueryRequest request, Job job);

    void createJob(Job job);

    void updateJob(Job job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

}
