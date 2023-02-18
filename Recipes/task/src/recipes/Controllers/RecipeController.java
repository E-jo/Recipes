package recipes.Controllers;

import recipes.Models.Recipe;
import recipes.Models.User;
import recipes.Services.RecipeService;
import recipes.Views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class RecipeController {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RecipeService recipeService;

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> createRecipe(@Valid @RequestBody Recipe recipe,
                                          @AuthenticationPrincipal User user) throws JsonProcessingException {
    /*
        ** commented-out methods were for testing purposes with Postman,
        ** since I could not figure out how to send a User object **
    public ResponseEntity<?> createRecipe(@RequestParam String name,
                                          @RequestParam String description,
                                          @RequestParam String[] ingredients,
                                          @RequestParam String[] directions) throws JsonProcessingException {


        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);
        recipe.setDirections(directions);

     */
        recipe.setCreatedBy(user.getId());
        recipe.setDate(LocalDateTime.now().toString());
        this.recipeService.save(recipe);

        return new ResponseEntity<>(mapper.writerWithView(Views.IdOnly.class)
                .writeValueAsString(recipe), HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id,
                                          @AuthenticationPrincipal User user) {
        Recipe originalRecipe = recipeService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (originalRecipe.getCreatedBy() != user.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        originalRecipe.setName(recipe.getName());
        originalRecipe.setDescription(recipe.getDescription());
        originalRecipe.setCategory(recipe.getCategory());
        originalRecipe.setDirections(recipe.getDirections());
        originalRecipe.setIngredients(recipe.getIngredients());
        originalRecipe.setDate(LocalDateTime.now().toString());
        recipeService.save(originalRecipe);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) throws JsonProcessingException {
        Optional<Recipe> result = recipeService.findById(id);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.writerWithView(Views.Normal.class)
                .writeValueAsString(result.get()), HttpStatus.OK);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> searchRecipes(@RequestParam(required = false) String category,
                                              @RequestParam(required = false) String name) throws JsonProcessingException {
        if ((category == null && name == null)
            || (category != null && name != null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Recipe> result;
        if (category != null) {
            result = recipeService.findByCategory(category);
        } else {
            result = recipeService.findByName(name);
        }
        return new ResponseEntity<>(mapper.writerWithView(Views.Normal.class)
                .writeValueAsString(result), HttpStatus.OK);
    }

    @DeleteMapping("api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Recipe recipe = recipeService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (recipe.getCreatedBy() == user.getId()) {
            recipeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
