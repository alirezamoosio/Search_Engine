package ir.nimbo.searchengine.crawler;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DuplicateLinkHandler {
    private static DuplicateLinkHandler ourInstance = new DuplicateLinkHandler();
    private static Logger logger = Logger.getLogger(DuplicateLinkHandler.class);


    public static DuplicateLinkHandler getInstance() {
        return ourInstance;
    }

    private static int hashPrime ;
    private static int hashTableSize;
    private byte[] linkHashTableTime;
    private byte[] twoPowers;
    private DuplicateLinkHandler() {
        hashPrime = 201326611;
        linkHashTableTime = new byte[hashPrime];
        twoPowers= new byte[]{0b1, 0b10, 0b100, 0b1000, 0b10000, 0b100000, 0b1000000, -128};//-128 = 10000000
    }

    public void loadHashTable() {
        try {
            linkHashTableTime= Files.readAllBytes(new File("hashTable.information").toPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public boolean isDuplicate(String url) {
        int hash = ((url.hashCode() % hashPrime) + hashPrime) % hashPrime;
        if (linkHashTableTime[hash] == 1) {
            return true;
        } else {
            linkHashTableTime[hash]=1;
            return false;
        }
    }
    public void saveHashTable(){
        try(FileOutputStream fileOutputStream=new FileOutputStream("hashTable.information")){
            fileOutputStream.write(linkHashTableTime);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
//must be called
//its important before downing system
    }
}
