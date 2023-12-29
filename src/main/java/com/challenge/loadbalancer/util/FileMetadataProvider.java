package com.challenge.loadbalancer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMetadataProvider implements IMetadataProvider {

    private final String filename;

    public FileMetadataProvider(String filename) {
        this.filename = filename;
    }

    @Override
    public List<String> getServerList() {
        return readFile();
    }

    private List<String> readFile() {
        List<String> serverList = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(this.filename))) {

            String url;
            while ((url = bufferedReader.readLine()) != null) {
                url = url.trim();
                if (checkIsValidURL(url)) {
                    serverList.add(url);
                } else {
                    System.out.println("Does not match pattern"); // TODO: use a logger
                }
            }

        } catch (IOException e) {
            System.out.println(e); // TODO: use a logger
        }

        return serverList;
    }

   private boolean checkIsValidURL(String url) {
       String regex = "^http://localhost:\\d{1,5}$";
       Pattern pattern = Pattern.compile(regex);
       Matcher matcher = pattern.matcher(url);

       if (matcher.matches()) {
           return true;
       }
       return false;
   }
}
