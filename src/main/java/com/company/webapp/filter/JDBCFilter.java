package com.company.webapp.filter;

import com.company.webapp.conn.ConnectionUtils;
import com.company.webapp.utils.MyUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;


@WebFilter(filterName = "jdbcFilter", urlPatterns = {"/*"})
public class JDBCFilter implements Filter {

    public JDBCFilter() {
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }


    // Checking this Servlet is target for this request?
    private boolean needJDBC(HttpServletRequest request){
        System.out.println("JDBC Filter");

        //servlet url-pattern: /spath/*
        //-> spath
        String servletPath = request.getServletPath();
        //->/abc/mnp
        String pathInfo = request.getPathInfo();

        String urlPattern = servletPath;


        if (pathInfo != null) {
            //->/spath/*
            urlPattern = servletPath + "/*";
        }

           // key - servletName
           // value - ServletRegistration


        Map<String, ? extends ServletRegistration> servletRegistrations = request.getServletContext()
                                            .getServletRegistrations();

          //   Collects all Servlets in WebApp.
            Collection<? extends ServletRegistration> values = servletRegistrations.values();
            for (ServletRegistration sr:values
                 ) {
                Collection<String> mappings = sr.getMappings();
                if (mappings.contains(urlPattern)){
                    return true;
                }
            }

        return false;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // opening  connection only for request with special link.
        // (f.e. link to servlet, jsp)
        // avoid opening Connection for simple requests.
        // (f.e. image, css, javascript)
        if (this.needJDBC(req)){
            System.out.println("Open Connection for: "+req.getServletPath());

            Connection conn = null;

            try {
                // create object Connection connected to database.
                conn = ConnectionUtils.getConnection();
                // set autocommit false for control.
                conn.setAutoCommit(false);

                // save object Connection to attribute in request.
                MyUtils.storeConnection(request, conn);

                // allow request to move further.
                // (to next Filter or target).
                chain.doFilter(request, response);

                // call method commit() to end transaction with DB.
                conn.commit();

            }catch (Exception e){
                e.printStackTrace();
                ConnectionUtils.rollbackQuietly(conn);
                throw new ServletException();
            }finally {
                ConnectionUtils.closeQuietly(conn);
            }
        }

        // for simple requests (image,css,html,..)
        // no need to connection.
        else{
            // allow request to move further
            // (to next Filter or target).
            chain.doFilter(request, response);
        }

    }



}
