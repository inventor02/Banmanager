package me.shawlaf.banmanager.managers.config;

import java.io.*;

/**
 * Created by Florian on 29.12.2016.
 */
public abstract class AbstractConfiguration {
    
    public enum Format {
        JSON("json"), CUSTOM("banmanager");
        
        public final String extension;
        
        Format(String extension) {
            this.extension = extension;
        }
        
        public boolean hasFormat(File file) {
            String absolutePath = file.getAbsolutePath();
            
            String extension = absolutePath.substring(absolutePath.lastIndexOf('.'));
            
            return extension.equalsIgnoreCase(this.extension);
        }
    }
    
    public final Format format;
    public final File file;
    
    public AbstractConfiguration(File file, Format format) {
        this.format = format;
        this.file = file;
        
        if (!format.hasFormat(file))
            return;
        
        try {
            StringBuilder fileDataBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            
            while ((line = reader.readLine()) != null)
                fileDataBuilder.append(line + "\n");
            
            parse(fileDataBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected abstract void parse(String fileData);
    
    public final void save() {
        try {
            saveImplementation(new BufferedWriter(new FileWriter(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected abstract void saveImplementation(BufferedWriter writer) throws Exception;
    
    
    
}
