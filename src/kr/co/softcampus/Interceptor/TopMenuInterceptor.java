package kr.co.softcampus.Interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.softcampus.beans.BoardInfoBean;
import kr.co.softcampus.service.TopMenuService;

//모든 곳에서 탑메뉴를 써야하므로 HandlerInterceptor를 implements 해준다
//그리고 ServletAppContext에 addInterceptors메소드(스프링메서드)를 통해서 지정을 해주어야한다
public class TopMenuInterceptor implements HandlerInterceptor{
	
	//인터셉터에서는 자동주입을 통해 Bean을 주입받지 못한다
	//때문에 객체사용시 여기서 Bean주입말고 생성자를 통해 Bean을 주입해주자
	private TopMenuService topMenuService;
	
	//@Autowired 이거없어도 생성자 하나면 자동으로 Autowired된다.
	public TopMenuInterceptor(TopMenuService topMenuService) {
		this.topMenuService = topMenuService;
	}



	@Override
	// preHandle 메세지를 통해서 모든 곳에서 반응할 수 있도록 해줌(인터셉터 기능 중 하나)
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		List<BoardInfoBean> topMenuList = topMenuService.getTopMenuList();
		//리퀘스트 영역에 들어가게 지정해줌
		request.setAttribute("topMenuList", topMenuList);
		//다음단계로 나아갈 수 있도록 preHandle return을 true로 지정해준다
		return true;
	}
	
}
