package org;

/**
 * Quick Jooby start up guide
 * All Official documentation can be found here: https://jooby.io/
 */

import io.jooby.FileUpload;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.ServerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * I should also mention that the official documentation has the main class extend Jooby
 * That works for testing, but if you're trying to package it as a jar, you're gonna have a bad time
 */
public class Main {

    public static void main(String [] args){

        Jooby app = new Jooby();

        /**
        * Setting properties for the jooby server
        * .setPort will denote which port the app will listen to (ipAddress:8080)
        * Only one application can listen to a port at the same time
        * If you're doing a custom port, you can use almost any port 10 - 9999
        * Except 80, 443, which are reserved for the default http:// and https:// respectfully
         */
        app.setServerOptions(new ServerOptions()
                .setPort(8080)
        );

        /**
        * Index route
        * This is the route that is initially routed to when
        * The "/" is the routing pattern which denotes which route the user is connecting to
         */
        app.get("/", ctx -> {

            // If you want to return a file, make sure you denote the file type, or just plain "file"
            ctx.setResponseType(MediaType.html);
            return new File("src/main/resources/index.html");
        });

        /**
         * Other routing example returning raw text instead of an html template
         */
        app.get("/otherPage", ctx -> {
            // This is not returning html, instead the browser will just display the text returned
            return "It's working, it's working Qui-Gon";
        });

        /**
         * Routing example returning a JSON object
         */
        app.get("/jsonExample", ctx -> {
            // This is not returning html, instead the browser will just display the text returned

            Map<String, String> dictionary = new HashMap<>();
            dictionary.put("JSONStatus", "working");

            ctx.setResponseType(MediaType.json);
            return new JSONObject(dictionary);
        });

        /**
         * Route with a custom url part
         * id could also be a string, I'm just showing it as an int
         */
        app.get("/custom/{id}/", ctx -> {
            int num = ctx.path("id").intValue();
            System.out.println(num);
            return num;
        });

        /**
         * Post and getting data from a form example
         */
        app.post("/post", ctx ->{
            String formPost = ctx.form("textBox").value();
            System.out.println(formPost);
            return formPost;
        });


        /**
         * Example posting a file
         * Must be accessed through an html form
         * File size limit is not unlimited, however that can be changed through the properties
         */
        app.post("/getFile", ctx -> {
            try {
                FileUpload inputUpload = ctx.file("file");
                String fileName = inputUpload.getFileName();
                String path = "src/" + fileName;
                FileOutputStream inputFileWriter = new FileOutputStream(path);
                inputFileWriter.write(inputUpload.bytes());
                inputFileWriter.close();
                return "Success uploading the file";
            }catch (Exception e){
                return "Failed:\n\t" + e.toString();
            }

        });


        //Essential for starting the app
        app.start();
    }
}
