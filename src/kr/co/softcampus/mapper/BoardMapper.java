package kr.co.softcampus.mapper;

import org.apache.ibatis.annotations.Insert;

import kr.co.softcampus.beans.ContentBean;

public interface BoardMapper {
	
	@Insert("insert into content_table(content_idx, content_subject, content_text, " +
			"content_file, content_writer_idx, content_board_idx, content_date) " +
			"values (content_seq.nextval, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, " +
			"#{content_writer_idx}, #{content_board_idx}, sysdate)")
	//위에보면 content_type에 jdbcType=VARCHAR 가 있는데 db상에서 null을 허용해도 마이바티스에서 거부가 되어
	//꼭 저것을 추가해주어야지 null값을 정상적으로 처리할 수 있다.
	void addContentInfo(ContentBean writeContentBean);
}
