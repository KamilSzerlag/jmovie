package co.brick.kszerlag.jmovie.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonPropertyOrder({"id", "name", "description", "image"})
public class MovieDto {

    private String id;

    @NotBlank(message = "Movie name can't be empty")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Description must contains at least 30 chars")
    @Size(min = 30, message = "Minimum description size is 30 chars.")
    @Size(max = 300, message = "Maximum description size is 300 chars.")
    private String description;

    private String image;

    public MovieDto() {
    }

    public MovieDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public MovieDto(String id, String name, String description, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
