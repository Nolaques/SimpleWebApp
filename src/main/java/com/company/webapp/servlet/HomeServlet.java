package com.company.webapp.servlet;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    public HomeServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //forward to views/homeView.jsp
        //users can't get access directly into jsp pages that in web/views
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/views/homeView.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
