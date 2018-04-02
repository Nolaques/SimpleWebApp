package com.company.webapp.utils;





import com.company.webapp.beans.UserAccount;


import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class MyUtils {

    public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";
    public static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

    // save Connection to attribute in request.
    // This info's saving lives only during (request)
    // until data returns to user's application
    public static void storeConnection(ServletRequest request, Connection conn){
        request.setAttribute(ATT_NAME_CONNECTION, conn);
    }

    // get object Connection that saved in attribute in request.
    public static Connection getStoredConnection(ServletRequest request){
        Connection conn = (Connection) request.getAttribute(ATT_NAME_CONNECTION);
        return conn;
    }

    // save user's info, that entered in system (login) in Session.
    public static void storeLoginedUser(HttpSession session, UserAccount loginedUser){
        // in JSP access allowed with ${loginedUser}
        session.setAttribute("loginedUser", loginedUser);
    }

    // get user's info, that saved in Session.
    public static UserAccount getLoginedUser(HttpSession session){
        UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
        return loginedUser;
    }

    // save user's info in Cookie.
    public static void storeUserCookie(HttpServletResponse response, UserAccount user){
        System.out.println("Store user cookie");
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
        // 1 day (converted in seconds)
        cookieUserName.setMaxAge(24*60*60);
        response.addCookie(cookieUserName);
    }

    public static String getUserNameInCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie: cookies
                 ) {
                if (ATT_NAME_USER_NAME.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // delete user's Cookie
    public static void deleteUserCookie(HttpServletResponse response){
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);
        // 0 second. (current Cookie will be invalid immediately)
        cookieUserName.setMaxAge(0);
        response.addCookie(cookieUserName);
    }
}
