package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.Project;
import tech.ikora.model.Projects;
import tech.ikora.model.SourceFile;

import java.net.URI;
import java.util.*;

public class BuildResult {
    private ErrorManager errors;
    private Projects projects;

    private long parsingTime;
    private long dependencyResolutionTime;
    private long linkingTime;
    private long buildTime;

    public BuildResult(){
        this.errors = new ErrorManager();
        this.projects = new Projects();

        parsingTime = -1;
        dependencyResolutionTime = -1;
        linkingTime = -1;
        buildTime = -1;
    }

    public ErrorManager getErrors() {
        return errors;
    }

    public Projects getProjects() {
        return projects;
    }

    public Optional<Project> getProject(String name){
        return projects.findProjectByName(name);
    }

    public void setErrors(ErrorManager errors) {
        this.errors = errors;
    }

    public long getParsingTime() {
        return parsingTime;
    }

    public void setParsingTime(long parsingTime) {
        this.parsingTime = parsingTime;
    }

    public long getDependencyResolutionTime() {
        return dependencyResolutionTime;
    }

    public void setDependencyResolutionTime(long dependencyResolutionTime) {
        this.dependencyResolutionTime = dependencyResolutionTime;
    }

    public long getLinkingTime() {
        return linkingTime;
    }

    public void setLinkingTime(long linkingTime) {
        this.linkingTime = linkingTime;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public SourceFile getSourceFile(URI uri){
        for(Project project: projects){
            Optional<SourceFile> optional = project.getSourceFile(uri);

            if(optional.isPresent()){
                return optional.get();
            }
        }

        return null;
    }
}
