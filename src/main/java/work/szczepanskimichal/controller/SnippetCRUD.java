package work.szczepanskimichal.controller;

import work.szczepanskimichal.jsonConverter.JsonConverter;
import work.szczepanskimichal.model.Snippet;
import work.szczepanskimichal.model.SnippetList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "SnippetCRUD", value = "/snippetAPI")
public class SnippetCRUD extends HttpServlet {
    
    // json converter object has methods to parse json objects and objects
    JsonConverter jsonConverter = new JsonConverter();
    // for now initialize snippetList with two snippets (later communicate with DAO classes and DB)
    SnippetList snippetList = new SnippetList();
    // static final api key for the time being
    static final String apiKey = "123456789";

    // get all snippets or snippet by id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getHeader("authentication").equals(apiKey)) {
            if (request.getParameter("id") != null) {
                // if parameter present - return specific snippet
                String snippet = jsonConverter.convertObjectToJson(snippetList.getSnippetById(Integer.valueOf(request.getParameter("id"))));
                response.getWriter().append(snippet);
            } else {
                // if parameter not present - return full list of snippets
                String[] snippetArray = new String[snippetList.getSnippetList().size()];
                for (int i = 0; i < snippetList.getSnippetList().size(); i++) {
                    snippetArray[i] = jsonConverter.convertObjectToJson(snippetList.getSnippetList().get(i));
                }
                response.getWriter().append(Arrays.toString(snippetArray));
            }
        } else {
            response.getWriter().append("no api key found or api key doesn't exist");
        }

    }

    // post snippet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getHeader("authentication").equals(apiKey)) {
            // tbc details why and how this works --->
            String newSnippetJsonString = request.getReader().lines().collect(Collectors.joining());
            // <---
            snippetList.addSnippet(jsonConverter.convertJsonToSnippet(newSnippetJsonString));
        } else {
            response.getWriter().append("no api key found or api key doesn't exist");
        }
    }

    // update snippet by id
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getHeader("authentication").equals(apiKey)) {
            String updatedSnippetJsonString = request.getReader().lines().collect(Collectors.joining());
            snippetList.updateSnippetById(Integer.parseInt(request.getParameter("id")), jsonConverter.convertJsonToSnippet(updatedSnippetJsonString));
        } else {
            response.getWriter().append("no api key found or api key doesn't exist");
        }
    }

    // delete snippet by id
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getHeader("authentication").equals(apiKey)) {
            snippetList.deleteSnippet(Integer.parseInt(request.getParameter("id")));
        } else {
            response.getWriter().append("no api key found or api key doesn't exist");
        }
    }



}
