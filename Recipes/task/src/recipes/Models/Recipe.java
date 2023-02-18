package recipes.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import recipes.Views;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdOnly.class)
    private long id;

    @JsonIgnore
    private int createdBy;

    @NotBlank(message = "Name must not be empty")
    @JsonView(Views.Normal.class)
    private String name;

    @NotBlank(message = "Description must not be empty")
    @JsonView(Views.Normal.class)
    private String description;

    @NotBlank(message = "Category must not be empty")
    @JsonView(Views.Normal.class)
    private String category;

    @JsonView(Views.Normal.class)
    private String date;

    @NotNull
    @Size(min = 1, message = "Please provide one or more answer options")
    @JsonView(Views.Normal.class)
    private String[] ingredients;

    @NotNull
    @Size(min = 1, message = "Please provide one or more answer options")
    @JsonView(Views.Normal.class)
    private String[] directions;

}
