package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DialogParser {
    private Map<String, String> items;

    public DialogParser(){
        items = new HashMap<>();
        System.out.println(items);
    }

    public void parse(String fileName){
        /*InputStream in = this.getClass().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try{
            String line;
            while((line=reader.readLine()) != null){
                String[] parts = line.split("=");
                String key = parts[0].strip();
                if(items.keySet().contains(key)) continue;
                items.put(key, parts[1].strip());
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            
        }*/
        File file = new File(fileName);
        Scanner scanner = null;
        try{
            scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String[] parts = scanner.nextLine().split("=");
                if(parts.length < 2) continue;
                String key = parts[0].strip();
                if(items.keySet().contains(key)) continue;
                items.put(key, parts[1].strip());
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally{
            if(scanner != null) scanner.close();
        }
        
    }

    public String get(String key){
        return items.get(key);
    }
}
