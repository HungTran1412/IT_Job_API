package backend.main.utils;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class OtherUtils {
	public <T> Page<T> listToPage(List<T> list, Pageable pageable) {

	    int start = (int) pageable.getOffset();
	    int end = Math.min(start + pageable.getPageSize(), list.size());

	    if (start > list.size()) {
	        return Page.empty(pageable);
	    }

	    return new PageImpl<>(
	            list.subList(start, end),
	            pageable,
	            list.size()
	    );
	}
	
}
