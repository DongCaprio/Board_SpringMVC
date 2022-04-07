package kr.co.softcampus.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.softcampus.beans.UserBean;
import kr.co.softcampus.dao.UserDao;
import kr.co.softcampus.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Resource(name = "loginUserBean")
	private UserBean loginUserBean;

	public boolean checkUserIdExist(String user_id) {
		String user_name = userDao.checkUserIdExist(user_id);
		if (user_name == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addUserInfo(UserBean joinUserBean) {
		userDao.addUserInfo(joinUserBean);
	}
	
	public void getLoginUserInfo(UserBean tempLoginUserBean) {
		UserBean tempLoginUserBean2 = userDao.getLoginUserInfo(tempLoginUserBean);
		if(tempLoginUserBean2 != null) {
			loginUserBean.setUser_idx(tempLoginUserBean.getUser_idx());
			loginUserBean.setUser_name(tempLoginUserBean.getUser_name());
			loginUserBean.setUserLogin(true);
			
		}
	}
	
	public void getModifyUserInfo(UserBean modifyUserBean) { //오히려 controller에서 사용할꺼 역으로 파라미터에 넣어준다
		//컨트롤러 역주입? 이거 중요한듯
		UserBean tempModifyUserBean = userDao.getModifyUserInfo(loginUserBean.getUser_idx());
		modifyUserBean.setUser_id(tempModifyUserBean.getUser_id());
		modifyUserBean.setUser_name(tempModifyUserBean.getUser_name());
		//위아래 구분 
		modifyUserBean.setUser_idx(loginUserBean.getUser_idx());
	}
	
	public void modifyUserInfo(UserBean modifyUserBean) {
		//modifyUserBean.setUser_idx(loginUserBean.getUser_idx());
		userDao.modifyUserInfo(modifyUserBean);
	}
	
	
}
