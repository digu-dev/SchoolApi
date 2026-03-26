package digu_dev.com.github.SchoolAPI.dto;

public record SubjectDto(Long id, String name) {

    public SubjectDto(String name) {
        this(null, name);
    }

}
