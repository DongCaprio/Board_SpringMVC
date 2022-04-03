package kr.co.softcampus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kr.co.softcampus.service.UserService;

@RestController
public class RestApiController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/checkUserIdExist/{user_id}")
	public String checkUserIdExist(@PathVariable String user_id) {
		boolean chk = userService.checkUserIdExist(user_id);
		//문자열이 아닌 다른것은 반환이 복잡하므로 가능하다면! String을 반환해주도록 하자
		return chk + "";
	}
}
