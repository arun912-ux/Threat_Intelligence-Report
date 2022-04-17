package com.example.restwo.controller;

import com.example.restwo.HelloApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class Controller {

    static File file = HelloApplication.file;
    static boolean flag = false;

    static DB mapdb = DBMaker.fileDB(file).fileLockDisable().checksumHeaderBypass().closeOnJvmShutdown().make();
    public static HTreeMap<String, Map<String, String>> hashmap = (HTreeMap<String, Map<String, String>>) mapdb.hashMap("hashmapfile").createOrOpen();

//    static DB mapdb = DBMaker.memoryDB().make();
//    public static HTreeMap<String, Map<String, String>> hashmap = (HTreeMap<String, Map<String, String>>) mapdb.hashMap("hashmap").createOrOpen();


















    public static void getJson() throws IOException {

        System.out.println("Inside getJson() Method");


        Map<String, String> ipMap = new HashMap<>();
        Map<String, String> uriMap = new HashMap<>();
        Map<String, String> domainMap = new HashMap<>();

        // ! getting raw XML data from source
        String ret = getXMLFromSource();
//        System.out.println(ret);
        // ! converting into JSON
        String jsonString = XML.toJSONObject(ret).toString(4);
//        System.out.println(jsonString);

        // ! extracting required data from JSON
        JSONArray contentBlock = new JSONObject(jsonString)
                .getJSONObject("taxii_11:Poll_Response")
                .getJSONArray("taxii_11:Content_Block");

        // ! IP, DOMAIN, URI : saving them into respective Maps
        for(int i=0; i< contentBlock.length(); i++){

            try {
                JSONObject jsonObj = (JSONObject) contentBlock.get(i);
                JSONObject observables = jsonObj.getJSONObject("taxii_11:Content")
                        .getJSONObject("stix:STIX_Package")
                        .getJSONObject("stix:Observables")
                        .getJSONObject("cybox:Observable");

                String cyboxTitle = observables.get("cybox:Title").toString();


                if (cyboxTitle.contains("IP")) {
                    ipMap.put(cyboxTitle.substring(4), observables.toString(4));
                } else if (cyboxTitle.contains("URI")) {
                    uriMap.put(cyboxTitle.substring(5), observables.toString(4));
                } else if (cyboxTitle.contains("Domain")) {
                    domainMap.put(cyboxTitle.substring(8), observables.toString(4));
                }


            } catch (Exception e){
                System.out.print("");
            }

        }

        // ! save in MapDB
        save(ipMap, uriMap, domainMap);

    }




















    static String getXMLFromSource() throws IOException {

        System.out.println("Inside getXMLFromSource() Method");

        // ! Discovery Service
//        url = new URL("http://hailataxii.com/taxii-discovery-service");
        // ! Collection Information, POLL
        URL url = new URL("http://hailataxii.com/taxii-data");

        // * Headers
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setConnectTimeout(50000);
        http.setReadTimeout(50000);
        http.setRequestProperty("Content-Type", "application/xml");
        http.setRequestProperty("Accept", "*/*");
        http.setRequestProperty("X-TAXII-Content-Type", "urn:taxii.mitre.org:message:xml:1.1");
        http.setRequestProperty("X-TAXII-Accept", "urn:taxii.mitre.org:message:xml:1.1");
        http.setRequestProperty("X-TAXII-Services", "urn:taxii.mitre.org:services:1.1");
        http.setRequestProperty("X-TAXII-Protocol", "urn:taxii.mitre.org:protocol:http:1.0");


        // ! POLL
        String data = "\n" +
                "<taxii_11:Poll_Request \n" +
                "    xmlns:taxii_11=\"http://taxii.mitre.org/messages/taxii_xml_binding-1.1\"\n" +
                "    message_id=\"123456\"\n" +
                "    collection_name=\"guest.Abuse_ch\">\n" +
                "    <taxii_11:Exclusive_Begin_Timestamp>" + "2017-04-20" + "T15:00:00Z \n" +
                "    </taxii_11:Exclusive_Begin_Timestamp>\n" +
                "    <taxii_11:Inclusive_End_Timestamp>2018-05-25T15:18:00Z\n" +
                "    </taxii_11:Inclusive_End_Timestamp>\n" +
                "    <taxii_11:Poll_Parameters allow_async=\"false\">\n" +
                "        <taxii_11:Response_Type>FULL</taxii_11:Response_Type>\n" +
                "    </taxii_11:Poll_Parameters>\n" +
                "</taxii_11:Poll_Request>\n" +
                "\n";


        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);
        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));

        System.out.println();
        System.out.println("Connection Established ...");
        System.out.println(http.getResponseCode() + " : " + http.getResponseMessage());
        StringBuilder xmlString = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            xmlString.append(line);
        }

        http.disconnect();
        stream.close();

        return xmlString.toString();

    }




















    static void save(Map<String, String> ipMap, Map<String, String> uriMap, Map<String, String> domainMap){

        System.out.println("in Controller.save()");

        System.out.println(ipMap.size());
        System.out.println(uriMap.size());
        System.out.println(domainMap.size());

        // ! Saving
        hashmap.put("IP", ipMap);
        hashmap.put("URI", uriMap);
        hashmap.put("DOMAIN", domainMap);

        System.out.println(hashmap.size());
        mapdb.commit();


    }












}

