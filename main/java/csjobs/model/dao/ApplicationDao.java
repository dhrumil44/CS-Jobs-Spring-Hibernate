package csjobs.model.dao;

import csjobs.model.Application;
import csjobs.model.Job;
import csjobs.model.User;

public interface ApplicationDao {

    Application getApplication( Long id );

    Application getApplication( Job job, User applicant );

    Application saveApplication( Application application );

}
