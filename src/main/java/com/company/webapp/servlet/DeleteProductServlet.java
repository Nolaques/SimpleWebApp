package com.company.webapp.servlet;


import com.company.webapp.utils.DBUtils;
import com.company.webapp.utils.MyUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/deleteProduct"})
public class DeleteProductServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public DeleteProductServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = MyUtils.getStoredConnection(req);

        String code = req.getParameter("code");

        String errorString = null;

        try {
            DBUtils.deleteProduct(conn, code);
        }catch (SQLException e){
            e.printStackTrace();
            errorString = e.getMessage();
        }

        // if there is an error, forward to page that shows error.
        if (errorString != null){
            // save info's request attribute before forward to views.
            req.setAttribute("errorString", errorString);

            RequestDispatcher dispatcher = req.getServletContext()
                            .getRequestDispatcher("/views/deleteProductErrorView.jsp");
            dispatcher.forward(req,resp);
        }
        // if everything is good
        // Redirect to product's page
        else{
            resp.sendRedirect(req.getContextPath() + "/productList");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
