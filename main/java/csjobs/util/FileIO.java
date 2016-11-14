package csjobs.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import csjobs.model.File;
import csjobs.model.User;
import csjobs.model.dao.FileDao;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
@Component
public class FileIO {

    @Autowired
    FileDao fileDao;

    private static final Logger logger = LoggerFactory
        .getLogger( FileIO.class );

    public File save( String fileDir, MultipartFile uploadedFile, User user ) throws IOException, InvalidFormatException, OpenXML4JException, XmlException
    {
        if( uploadedFile.isEmpty() ) return null;

        File file = new File();
        file.setName( uploadedFile.getOriginalFilename() );
        file.setType( uploadedFile.getContentType() );
        file.setSize( uploadedFile.getSize() );
        file.setDate( new Date() );
        file.setOwner( user );
        file = fileDao.saveFile( file );

        java.io.File diskFile = new java.io.File( fileDir,
            file.getId().toString() );
        try
        {
            uploadedFile.transferTo( diskFile );
        }
        catch( Exception e )
        {
            logger.error( "Failed to save uploaded file", e );
        }
        
//        if(uploadedFile.getContentType()=="pdf")
        

             // Load the file into a PDDocument object
             PDDocument document = PDDocument.load( diskFile );

             // Create a PDFTextStripper
             PDFTextStripper stripper = new PDFTextStripper();

             // Extract text and store it in a String
             StringWriter sw = new StringWriter();
             stripper.writeText( document, sw );
             String text = sw.toString();
             file.setContent(text);
        
        
//        else if(uploadedFile.getContentType()=="doc")
//        {
//            java.io.File files = new java.io.File( fileDir, uploadedFile.getOriginalFilename() );
//
//            // Create a TextExtractor
//            POITextExtractor extractor = ExtractorFactory.createExtractor( files );
//
//            // Extract text and store it in a String
//            String text = extractor.getText();
//
//            // Print out the text
//            System.out.print( text );
//        }
        file = fileDao.saveFile( file );
      
        return file;
    }

    public void write( String fileDir, File file, HttpServletResponse response )
    {
        try
        {
            response.setContentType( file.getType() );
            response.setHeader( "Content-Disposition",
                "inline; filename=" + file.getName() );
            FileInputStream in = new FileInputStream(
                new java.io.File( fileDir, file.getId().toString() ) );
            OutputStream out = response.getOutputStream();

            byte buffer[] = new byte[2048];
            int bytesRead;
            while( (bytesRead = in.read( buffer )) > 0 )
                out.write( buffer, 0, bytesRead );

            in.close();
        }
        catch( Exception e )
        {
            logger.error( "Fail to write file to response", e );
        }
    }

}
