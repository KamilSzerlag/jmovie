package co.brick.kszerlag.jmovie.entity;

import com.google.common.base.Objects;
import org.bson.types.ObjectId;

public class MovieEntity {

    private ObjectId id;
    private String name;
    private String description;
    private byte[] image;

    public MovieEntity() {
    }

    public MovieEntity(String name, String description, byte[] image) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieEntity that = (MovieEntity) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, description);
    }
}
