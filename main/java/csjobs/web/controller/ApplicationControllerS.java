package csjobs.web.controller;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import csjobs.model.Application;
import csjobs.model.Degree;
import csjobs.model.Job;
import csjobs.model.User;
import csjobs.model.dao.ApplicationDao;
import csjobs.model.dao.JobDao;
import csjobs.security.SecurityUtils;
import csjobs.util.FileIO;

@Controller
@SessionAttributes("application")
public class ApplicationControllerS {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private FileIO fileIO;

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/application/apply.html",
        method = RequestMethod.GET)
    public String apply( @RequestParam Long jobId, ModelMap models )
    {
        Job job = jobDao.getJob( jobId );
        User applicant = SecurityUtils.getUser();
        Application application = applicationDao.getApplication( job,
            applicant );

        if( application != null && application.isSubmitted() )
        {
            models.put( "errorCode", "error.application.submitted" );
            return "error";
        }

        if( application == null )
            application = new Application( job, applicant );

        models.put( "application", application );
        return "application/apply";
    }

    @RequestMapping(value = "/application/apply.html",
        method = RequestMethod.POST)
    public String apply( @ModelAttribute Application application,
        @RequestParam(value = "cvfile", required = false ) MultipartFile cvFile,
        @RequestParam(value = "rsfile", required = false) MultipartFile rsFile,
        @RequestParam(value = "tsfile", required = false) MultipartFile tsFile,
        SessionStatus sessionStatus) throws IOException, InvalidFormatException, OpenXML4JException, XmlException
    {
        User user = SecurityUtils.getUser();
        String fileDir = servletContext.getRealPath( "/WEB-INF/files" );

        if( cvFile != null && !cvFile.isEmpty() )
            application.setCv( fileIO.save( fileDir, cvFile, user ) );
        if( rsFile != null && !rsFile.isEmpty() ) application
            .setResearchStatement( fileIO.save( fileDir, rsFile, user ) );
        if( tsFile != null && !tsFile.isEmpty() ) application
            .setTeachingStatement( fileIO.save( fileDir, tsFile, user ) );

        application = applicationDao.saveApplication( application );
        sessionStatus.setComplete();
        return "redirect:addDegree.html?applicationId=" + application.getId();
    }

    @RequestMapping(value = "/application/addDegree.html",
        method = RequestMethod.GET)
    public String addDegree( @RequestParam Long applicationId, ModelMap models )
    {
        models.put( "application",
            applicationDao.getApplication( applicationId ) );
        models.put( "degree", new Degree() );
        return "application/addDegree";
    }

    @RequestMapping(value = "/application/addDegree.html",
        method = RequestMethod.POST)
    public String addDegree( @ModelAttribute Application application,
        @ModelAttribute Degree degree, SessionStatus sessionStatus )
    {
        application.getDegrees().add( degree );
        application = applicationDao.saveApplication( application );
        sessionStatus.setComplete();
        return "redirect:addDegree.html?applicationId=" + application.getId();
    }

}
