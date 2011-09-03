package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class KcFileUtils {
	public static String getTextFileContent(String templateFilePath2) throws IOException {
		File file = new File(templateFilePath2);
		StringBuilder fileContent = new StringBuilder();		
    	if(file.exists() && file.isFile() && file.canRead()) 
    	{
    		String line = null;
    		BufferedReader input =  new BufferedReader(new FileReader(file));
            while (( line = input.readLine()) != null){
            	fileContent.append(line);
            	fileContent.append(System.getProperty("line.separator"));
              }
    	}
    	else 
    	{
    		throw new IOException("Le fichier \"" + file.getAbsolutePath() + "\"tpl n'existe pas ou il n'est pas accessible en lecture");
    	}
		return fileContent.toString();
	}
}
