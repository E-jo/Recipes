package recipes.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import recipes.Models.Recipe;
import recipes.Repositories.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void deleteById(Long id) {
        this.recipeRepository.deleteById(id);
    }

    public Page<Recipe> findAll (Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return recipeRepository.findAll(paging);
    }

    public Optional<Recipe> findById(Long id) {
        return this.recipeRepository.findById(id);
    }

    public List<Recipe> findByCategory(String category) {
        return this.recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findByName(String name) {
        return this.recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public Recipe save(Recipe quiz) {
        return this.recipeRepository.save(quiz);
    }
}