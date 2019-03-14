package yx.pay.system.dao;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.User;

import java.util.List;
@Component
public interface UserMapper extends MyMapper<User> {

	List<User> findUserDetail(User user);

	int addUserAndReturnId(User user);
}