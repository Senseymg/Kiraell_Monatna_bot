package ru.kiraell.Kiraell_Monatna_bot.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kiraell.Kiraell_Monatna_bot.service.FolderUrlCollector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Service
public class FolderUrlCollectorImpl implements FolderUrlCollector {

    @Override
    public List<String> getFolderUrls(String FolderPath) {
        File folder = new File(FolderPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }


}
