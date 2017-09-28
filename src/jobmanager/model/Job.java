package jobmanager.model;

public class Job {
    private int num; //job number
    private String name; //first name
    private String desc; //description of job
    private int totalAssemblies; //total assemblies needed
    private int completedAssemblies; //total assemblies completed
    private int estimatedHours; //estimated time until completion
    private int totalHours; //actual time taken
    
    public Job(int num, String name, String desc, int totalAssemblies, int estimatedHours) {
        this.num = num;
        this.name = name;
        this.desc = desc;
        this.totalAssemblies = totalAssemblies;
        this.estimatedHours = estimatedHours;
    }
    
    public Job() {
        
    }
    
    public void setJobNumber(int num) {
        this.num = num;
    }
    
    public int getJobNumber() {
        return num;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDescription(String desc) {
        this.desc = desc;
    }
    
    public String getDescription() {
        return desc;
    }
    
    public void setTotalAssemblies(int totalAssemblies) {
        this.totalAssemblies = totalAssemblies;
    }
    
    public int getTotalAssemblies() {
        return totalAssemblies;
    }
    
    public void setCompletedAssemblies(int completedAssemblies) {
        this.completedAssemblies = completedAssemblies;
    }
    
    public int getCompletedAssemblies() {
        return completedAssemblies;
    }
    
    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
    
    public int getEstimatedHours() {
        return estimatedHours;
    }
    
    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }
    
    public int getTotalHours() {
        return totalHours;
    }
}
