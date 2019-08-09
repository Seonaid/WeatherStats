import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
/**
 * This project reads a number of files of weather data and produces summary and stats
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FileProcessing {
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord coldestHourSoFar = null;
        for (CSVRecord currentRow: parser){
            if (coldestHourSoFar == null){
                coldestHourSoFar = currentRow;
            } else {
                double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
                double coldestTempSoFar = Double.parseDouble(coldestHourSoFar.get("TemperatureF"));
                if ((currentTemp < coldestTempSoFar) && currentTemp != -9999){
                    coldestHourSoFar = currentRow;
                }
            }
        }
        return coldestHourSoFar;
    }
    
    public void testColdestHourInFile(){
        // known outcome: coldest hour is at 7:51 AM, temp F = 15.1
        System.out.println("known outcome: coldest hour is at 7:51 AM, temp F = 15.1");
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-08.csv");
        CSVRecord coldest = coldestHourInFile(fr.getCSVParser());
        String coldTemp = coldest.get("TemperatureF");
        String coldTime = coldest.get("TimeEST"); 
        System.out.println("Result from test:");
        System.out.println("coldest temperature was: " + coldTemp + " at "+ coldTime);
        
        // Quiz question:
        FileResource fr1 = new FileResource("nc_weather/2014/weather-2014-05-01.csv");
        coldest = coldestHourInFile(fr1.getCSVParser());
        coldTemp = coldest.get("TemperatureF");
        coldTime = coldest.get("TimeEDT");         
        System.out.println("Result from quiz:");
        System.out.println("coldest temperature was: " + coldTemp + " at "+ coldTime);        
    }
    
    public String fileWithColdestTemperature(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord coldestHourSoFar = null;
        String filename = null;
        // iterate over files and find coldest temperature in each
        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            // use method to get coldest in the file
            CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
            if (coldestHourSoFar == null){
                coldestHourSoFar = currentRow;
            } else {
                double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
                double coldestTempSoFar = Double.parseDouble(coldestHourSoFar.get("TemperatureF"));
                if ((currentTemp < coldestTempSoFar) && currentTemp != -9999){
                    coldestHourSoFar = currentRow;
                    filename = f.getName();
                }
            }            
        }
        return filename;
    }
    
    public void testFileWithColdestTemperature(){
        // 
        String filename = fileWithColdestTemperature();
        System.out.println(filename);
        
        FileResource fr = new FileResource("nc_weather/2014/" + filename);
        CSVRecord coldest = coldestHourInFile(fr.getCSVParser());
        String coldTemp = coldest.get("TemperatureF");
        System.out.println("Result from test:");
        System.out.println("coldest temperature was: " + coldTemp + " on date " + filename.substring(8, 18));
        
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowestHumiditySoFar = null;
        for (CSVRecord currentRow: parser){
            if (lowestHumiditySoFar == null){
                lowestHumiditySoFar = currentRow;
            } else {
                if (!currentRow.get("Humidity").equals("N/A")){
                    int currentHumidity = Integer.parseInt(currentRow.get("Humidity"));
                    int lowestHumidity = Integer.parseInt(lowestHumiditySoFar.get("Humidity"));
                    if ((currentHumidity < lowestHumidity)){
                        lowestHumiditySoFar = currentRow;
                    }
                }
            }
        }
        return lowestHumiditySoFar;
    }
    
    public void testLowestHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csv = lowestHumidityInFile(parser);
        
        System.out.println(csv.get("Humidity") + " at " + csv.get("DateUTC"));
    }
    
    public CSVRecord lowestHumidityInManyFiles(){
        CSVRecord lowestHumiditySoFar = null;
        DirectoryResource dr = new DirectoryResource();

        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
            if (lowestHumiditySoFar == null){
                lowestHumiditySoFar = currentRow;
            } else {
                if (!currentRow.get("Humidity").equals("N/A")){
                    int currentHumidity = Integer.parseInt(currentRow.get("Humidity"));
                    int lowestHumidity = Integer.parseInt(lowestHumiditySoFar.get("Humidity"));
                    if ((currentHumidity < lowestHumidity)){
                        lowestHumiditySoFar = currentRow;
                    }
                }
            }
        }
        return lowestHumiditySoFar;
    }
    
    public void testLowestHumidityInManyFiles(){
        CSVRecord lowestHumidity = lowestHumidityInManyFiles();
        System.out.println("Lowest Humidity was " + lowestHumidity.get("Humidity") + " at " + lowestHumidity.get("DateUTC"));
    }
}
