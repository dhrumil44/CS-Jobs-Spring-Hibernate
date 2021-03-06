package csjobs.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csjobs.model.File;
import csjobs.model.dao.FileDao;
import csjobs.util.FileIO;

@Controller
public class FileController {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private ServletContext servletContext;

    @RequestMapping("/download.html")
    public String download( @RequestParam Long fileId,
        HttpServletResponse response )
    {
        String fileDir = servletContext.getRealPath( "/WEB-INF/files" );
        File file = fileDao.getFile( fileId );
        fileIO.write( fileDir, file, response );
        return null;
    }
    
    @RequestMapping("/search.html")
    public String search( @RequestParam(required = false) String term,
        ModelMap models )
    {
        if( StringUtils.hasText( term ) )
            models.addAttribute( "file", fileDao.searchCourses( term, -1 ) );

        return "search";
    }


}
