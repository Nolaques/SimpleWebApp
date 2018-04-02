package com.company.webapp.servlet;

import com.company.webapp.beans.Product;
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

@WebServlet(urlPatterns = {"/editProduct"})
public class EditProductServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public EditProductServlet() {
        super();
    }

    // show page of product's editing

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = MyUtils.getStoredConnection(req);

        String code = (String)req.getParameter("code");

        Product product = null;

        String errorString = null;

        try {
            product = DBUtils.findProduct(conn,code);
        }catch (SQLException e){
            e.printStackTrace();
            errorString=e.getMessage();
        }

        // there are no errors
        // product doesn't exist fo edit.
        // Redirect
        if (errorString != null && product == null){
            resp.sendRedirect(req.getServletPath() + "/productList");
            return;
        }

        // save info's request attribute before forward to views.
        req.setAttribute("errorString", errorString);
        req.setAttribute("product", product);

        RequestDispatcher dispatcher = req.getServletContext()
                                .getRequestDispatcher("/views/editProductView.jsp");
        dispatcher.forward(req,resp);

    }


    // After user has edited product's info and pressed on Submit.
    // this method will be done
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = MyUtils.getStoredConnection(req);

        String code = (String)req.getParameter("code");
        String name = (String)req.getParameter("name");
        String priceStr = (String)req.getParameter("price");
        float price = 0;
        try {
            price = Float.parseFloat(priceStr);
        }catch (Exception e){

        }
        Product product = new Product(code, name, price);

        String errorString = null;

        try {
            DBUtils.updateProduct(conn, product);
        }catch (SQLException e){
            e.printStackTrace();
            errorString = e.getMessage();
        }
        // save info's request attribute before forward to views.
        req.setAttribute("errorString", errorString);
        req.setAttribute("product", product);

        // if there is error, forward to page edit.
        if (errorString != null){
            RequestDispatcher dispatcher = req.getServletContext()
                                .getRequestDispatcher("/views/editProductView.jsp");
            dispatcher.forward(req,resp);
        }
        // if everything is fine
        // Redirect to page with product's list
        else{
            resp.sendRedirect(req.getContextPath()+ "/productList");
        }
    }
}
