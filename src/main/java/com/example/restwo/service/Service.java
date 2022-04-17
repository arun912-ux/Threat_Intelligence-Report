package com.example.restwo.service;

import com.example.restwo.controller.Controller;
import org.json.JSONObject;
import org.mapdb.HTreeMap;

import java.util.Map;
import java.util.Objects;




public class Service {



    static HTreeMap<String, Map<String, String>> hashmap = Controller.hashmap;












    public String getIps() {
        System.out.println("\n\nRequested for IPs\n\n");
        StringBuilder retIp = new StringBuilder();
        Map<String, String> ips = hashmap.get("IP");
        retIp.append("[\n");
        for(Map.Entry<String, String> m : Objects.requireNonNull(ips).entrySet()){
            retIp.append(m.getValue()).append(",\n\n");
        }
        String substring = retIp.substring(0, retIp.length() - 3);
        return substring + "\n]";
    }











    public String getDomains() {
        System.out.println("\n\nRequested for Domains\n\n");
        StringBuilder retDomain = new StringBuilder();
        retDomain.append("[\n");
        Map<String, String> ips = hashmap.get("DOMAIN");
        for(Map.Entry<String, String> m : Objects.requireNonNull(ips).entrySet()){
            retDomain.append(m.getValue()).append(",\n\n");
        }
        String ret = retDomain.substring(0, retDomain.length() - 3);
        return ret + "\n]";
    }













    public String getUrls() {
        System.out.println("\n\nRequested for Urls\n\n");
        StringBuilder retUri = new StringBuilder();
        retUri.append("[\n");
        Map<String, String> ips = hashmap.get("URI");
        for(Map.Entry<String, String> m : Objects.requireNonNull(ips).entrySet()){
            retUri.append(m.getValue()).append(",\n\n");
        }
        String ret = retUri.substring(0, retUri.length() - 3);
        return ret + "\n]";
    }
















    public String search(String type, String input) {
        System.out.println("\n\n\nSearching : \n\n\n");
        System.out.println(type + " : " + input);
        Map<String, String> getMap = hashmap.get(type.toUpperCase());
        if(Objects.requireNonNull(getMap).containsKey(input)){
            return getMap.get(input);
        }
        JSONObject json = new JSONObject();
        json.put(type.toUpperCase() , input);
        json.put("Response", "Safe");

        String ret = json.toString(4);
        System.out.println(ret);
        return ret;
    }

















}
