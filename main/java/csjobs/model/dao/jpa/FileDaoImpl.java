package csjobs.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csjobs.model.File;
import csjobs.model.Job;
import csjobs.model.dao.FileDao;

@Repository
public class FileDaoImpl implements FileDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public File getFile( Long id )
    {
        return entityManager.find( File.class, id );
    }

    @Override
    public List<File> searchCourses( String term, int maxResults )
    {
        TypedQuery<File> query = entityManager.createNamedQuery(
            "file.search", File.class );
        if( maxResults > 0 ) query.setMaxResults( maxResults );
        return query.setParameter( "term", term ).getResultList();
    }
    
    @Override
    @Transactional
    public File saveFile( File file )
    {
        return entityManager.merge( file );
    }

}
