package com.g7.service;

import com.g7.model.Internship;
import com.g7.model.IztechUser;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileService {
    public File fillLetter(IztechUser user, Internship internship) {
        try (InputStream inputStream = new ClassPathResource("/templates/Application_Letter_Template_1.doc").getInputStream(); POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream)) {
            HWPFDocument doc = new HWPFDocument(fileSystem);
            fillLetter(doc, "+ADI+SOYADI+", user.getName() + " "+ user.getSurname());
            fillLetter(doc, "+FAKULTE+", "Mühendislik");
            fillLetter(doc, "+BOLUMU+", "Bilgisayar Mühendisliği");
            fillLetter(doc, "+SINIFI+", user.getGrade().toString());
            fillLetter(doc, "+OKUL+NUMARASI+", user.getStudentNo());
            fillLetter(doc, "+TC+", user.getTcNo());
            fillLetter(doc, "+CEP+TELEFONU+", user.getPhoneNumber());
            fillLetter(doc, "+E+POSTA+", user.getEmail());
            return saveFile("/tmp/"+user.getName()+"_"+user.getSurname()+"_"+ System.currentTimeMillis() +"_"+".doc", doc);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public File fillApplicationForm(IztechUser user, Internship internship) {
        try (InputStream inputStream = new ClassPathResource("/templates/Application_Form_Template_1.doc").getInputStream(); POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream)) {
            HWPFDocument doc = new HWPFDocument(fileSystem);
            fillLetter(doc, "+ADI+SOYADI+", user.getName() + " "+ user.getSurname());
            fillLetter(doc, "+FAKULTE+", "Mühendislik");
            fillLetter(doc, "+BOLUMU+", "Bilgisayar Mühendisliği");
            fillLetter(doc, "+SINIFI+", user.getGrade().toString());
            fillLetter(doc, "+OKUL+NUMARASI+", user.getStudentNo());
            fillLetter(doc, "+TC+", user.getTcNo());
            fillLetter(doc, "+CEP+TELEFONU+", user.getPhoneNumber());
            fillLetter(doc, "+E+POSTA+", user.getEmail());
            return saveFile("/tmp/"+user.getName()+"_"+user.getSurname()+"_"+ System.currentTimeMillis() +"_"+".doc", doc);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private void fillLetter(HWPFDocument doc, String originalText, String updatedText) {
        Range range = doc.getRange();
        range.replaceText(originalText, updatedText);
    }

    private File saveFile(String filePath, HWPFDocument doc) throws IOException {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            doc.write(out);
            doc.close();
        }
        return new File(filePath);
    }
}
