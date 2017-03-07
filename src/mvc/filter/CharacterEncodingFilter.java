package mvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.jstl.core.Config;

public class CharacterEncodingFilter implements Filter {
	private String encoding;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException {
		req.setCharacterEncoding(encoding);
		filterChain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		encoding = config.getInitParameter("encoding");
		if (encoding == null) {
			encoding = "utf-8";
		}

	}

}
