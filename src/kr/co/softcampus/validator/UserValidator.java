package kr.co.softcampus.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.softcampus.beans.UserBean;

public class UserValidator implements Validator {
	// 구현한 Validetor종류가 여러가지인데 org.springfamework.validation 를 골라주도록 하자
	// 밑에 2개의 메소드는 알아서 나온다.

	@Override
	public boolean supports(Class<?> clazz) {
		// 밑에 코드는 정해져있는것이므로 그냥 이렇게 쓰자.
		// return 바로뒤에 클래스만 내가 만든것으로 바꾸면 된다.
		return UserBean.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) target;

		// 밑에 if문을 쓰기위해서 추가해준다.
		// 에러 오브젝트명을 얻어서 분기해줘야지만 에러를 피할수 있다.
		// 51강 24분 이해 어려움
		String beanName = errors.getObjectName();

		if (beanName.equals("joinUserBean") || beanName.equals("modifyUserBean")) {
			if (userBean.getUser_pw().equals(userBean.getUser_pw2()) == false) {
				errors.rejectValue("user_pw", "NotEquals");
				errors.rejectValue("user_pw2", "NotEquals2");
			}
		}
		if (beanName.equals("joinUserBean")) {
			if (userBean.isUserIdExist() == false) {
				errors.rejectValue("user_id", "DontCheckUserIdExist");
			}
		}
	}
}
