package yx.pay.system.dao;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.User;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

	List<User> findUserDetail(User user);
}