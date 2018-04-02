package com.company.webapp.servlet;


import com.company.webapp.beans.UserAccount;
import com.company.webapp.utils.MyUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/userInfo"})
public class UserInfoServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    public UserInfoServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // checking if user entered to system (login) or not.
        UserAccount loginedUser = MyUtils.getLoginedUser(session);

        // if hasn't entered (login).
        if (loginedUser == null){
            // Redirect to page login.
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // save info's request attribute before forward.
        req.setAttribute("user", loginedUser);

        // if user has already entered to system (login), then forward to page
        // /WEB-INF/views/userInfoView.jsp
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/views/userInfoView.jsp");
        dispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
