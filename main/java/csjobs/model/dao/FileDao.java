package csjobs.model.dao;

import java.util.List;

import csjobs.model.File;
import csjobs.model.Job;

public interface FileDao {

    File getFile( Long id );

    File saveFile( File file );
    
    public List<File> searchCourses( String term, int maxResults );

}
