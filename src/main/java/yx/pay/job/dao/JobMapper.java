package yx.pay.job.dao;


import yx.pay.common.config.MyMapper;
import yx.pay.job.domain.Job;

import java.util.List;

public interface JobMapper extends MyMapper<Job> {
	
	List<Job> queryList();
}