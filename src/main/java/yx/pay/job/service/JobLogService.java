package yx.pay.job.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.IService;
import yx.pay.job.domain.JobLog;

import java.util.List;

public interface JobLogService extends IService<JobLog> {

    List<JobLog> findJobLogs(QueryRequest request, JobLog jobLog);

    void saveJobLog(JobLog log);

    void deleteJobLogs(String[] jobLogIds);
}
