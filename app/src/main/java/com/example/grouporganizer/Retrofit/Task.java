package com.example.grouporganizer.Retrofit;

//returns schedule of user for unfinished tasks
public class Task {
    private String description;
    private String title;
    private String deadline;
    private String[] tags;

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }

    public String getDeadline(){
        return deadline;
    }

    public String getTags(){
        return String.join(", ", tags);
    }

}
