package scs.map.sem9_paging.dto;

import java.util.Optional;

public class MovieFilterDTO {

    private Integer year ;
    private Integer yearAfter;
    private String title ;
    private String director;

    public Integer getYear() {
        return year;
    }

    public Integer getYearAfter() {
        return yearAfter;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setYearAfter(Integer yearAfter) {
        this.yearAfter = yearAfter;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
