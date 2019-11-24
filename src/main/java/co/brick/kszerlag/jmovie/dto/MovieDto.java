package co.brick.kszerlag.jmovie.dto;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MovieDto {

    private ObjectId id;

    @Size(max = 150)
    @NotBlank(message = "Movie name can't be empty")
    private String name;

    @Size(min = 30, message = "Minimum description size is 50 chars.")
    @Size(max = 300, message = "Maximum description size is 300 chars.")
    private String description;

    private byte[] image;

    public MovieDto() {
    }

    public MovieDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public MovieDto(String name, String description, byte[] image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
