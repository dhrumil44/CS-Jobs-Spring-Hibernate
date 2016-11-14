package csjobs.model.dao;

import java.util.List;

import csjobs.model.Job;

public interface JobDao {

    Job getJob( Long id );

    List<Job> getJobs();

    List<Job> getOpenJobs();

    Job saveJob( Job job );

}
