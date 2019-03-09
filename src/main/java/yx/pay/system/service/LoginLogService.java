package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.LoginLog;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog (LoginLog loginLog);
}
