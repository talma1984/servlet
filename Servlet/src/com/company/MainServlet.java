package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[2048];
        int actuallyRead;
        InputStream inputStream = request.getInputStream();
        while ((actuallyRead = inputStream.read(buffer)) != -1){
            stringBuilder.append(new String(buffer,0, actuallyRead));
        }
        System.out.println(stringBuilder.toString());
        response.getOutputStream().write("thank you client!".getBytes());
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        JSONArray jsonArray = new JSONArray();


        DB.query("SELECT id, company, last_name,first_name FROM suppliers", (statement -> {
            //statement.setString(1, "elad");
        }), (resultSet,params) -> {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray1 = (JSONArray)params[0];
            try {
                jsonObject.put("supplierID", resultSet.getInt(1));
                jsonObject.put("company", resultSet.getString(2));
                jsonObject.put("lastName", resultSet.getString(3));
                jsonObject.put("firstName", resultSet.getString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray1.put(jsonObject);
            return true;
        }, jsonArray);


        response.getWriter().write(jsonArray.toString());

    }



}