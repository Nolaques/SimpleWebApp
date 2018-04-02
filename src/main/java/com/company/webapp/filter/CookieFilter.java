package com.company.webapp.filter;


import com.company.webapp.beans.UserAccount;
import com.company.webapp.utils.DBUtils;
import com.company.webapp.utils.MyUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter(filterName = "cookieFilter", urlPatterns = {"/*"})
public class CookieFilter implements Filter{

    public CookieFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        UserAccount userInSession = MyUtils.getLoginedUser(session);

        if (userInSession != null){
            session.setAttribute("COOKIE_CHECKED","CHECKED");
            chain.doFilter(request, response);
            return;
        }

        // Connection created in JDBCFilter.
        Connection conn = MyUtils.getStoredConnection(request);

        // flag for checking Cookie.
        String checked = (String) session.getAttribute("COOKIE_CHECKED");
        if (checked == null && conn != null){
            String userName = MyUtils.getUserNameInCookie(req);
            try {
                UserAccount user = DBUtils.findUser(conn,userName);
                MyUtils.storeLoginedUser(session,user);
            }catch (SQLException e){
                e.printStackTrace();
            }

            // cancel checked Cookie.
            session.setAttribute("COOKIE_CHECKED", "CHECKED");
        }

        chain.doFilter(request, response);
    }


}
