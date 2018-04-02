package com.company.webapp.servlet;


import com.company.webapp.beans.UserAccount;
import com.company.webapp.utils.DBUtils;
import com.company.webapp.utils.MyUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    // show page Login.

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to page /views/loginView.jsp
        // (user can't get access right to
        // pages JSP that in category web).
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/views/loginView.jsp");

        dispatcher.forward(request,response);
    }

    // if user enters userName & password and presses Submit.
    // this method will invoke
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String rememberMeStr = request.getParameter("rememberMe");
        boolean remember = "Y".equals(rememberMeStr);

        UserAccount user = null;
        boolean hasError = false;
        String errorString = null;

        if (userName == null || password == null || userName.length() == 0 || password.length() == 0){
        hasError=true;
        errorString = "Required username and password!";
        }else {
            Connection conn = MyUtils.getStoredConnection(request);
            try {
                // find user in DB.
                user = DBUtils.findUser(conn, userName, password);

                if (user == null){
                    hasError = true;
                    errorString = "User Name or password invalid";
                }
            }catch (SQLException e){
                e.printStackTrace();
                hasError = true;
                errorString = e.getMessage();
            }
        }

        // if there is error
        // forward to /views/login.jsp
        if (hasError){
            user = new UserAccount();
            user.setUserName(userName);
            user.setPassword(password);

            // save info's request attribute before forward.
            request.setAttribute("errorString", errorString);
            request.setAttribute("user", user);

            // Forward to page /views/login.jsp
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/views/loginView.jsp");
            dispatcher.forward(request, response);
        }

        // if there is no error
        // save's user's info Session.
        // and redirect to userInfo.
        else{
            HttpSession session = request.getSession();
            MyUtils.storeLoginedUser(session, user);

            // if user's selects "Remember me".
            if (remember){
                MyUtils.storeUserCookie(response, user);
            }
            // delete Cookie
            else{
                MyUtils.deleteUserCookie(response);
            }
        // Redirect to page /userInfo.
            response.sendRedirect(request.getContextPath() + "/userInfo");
        }
    }
}
