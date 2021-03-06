package org.wecancodeit.recipebox.controllers;

import java.util.Collection;

import javax.annotation.Resource;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wecancodeit.recipebox.models.Category;
import org.wecancodeit.recipebox.models.Ingredient;
import org.wecancodeit.recipebox.models.Recipe;
import org.wecancodeit.recipebox.repositories.CategoryRepository;
import org.wecancodeit.recipebox.repositories.IngredientRepository;
import org.wecancodeit.recipebox.repositories.RecipeRepository;

@CrossOrigin
@RestController
@RequestMapping("/recipes")
public class RecipeController {
	
	@Resource
	CategoryRepository categoryRepo;
	
	@Resource
	RecipeRepository recipeRepo;
	
	@Resource
	IngredientRepository ingredientRepo;
	
	@GetMapping("")
	public Collection<Recipe> getAllRecipes() {
		return (Collection<Recipe>) recipeRepo.findAll();
	}
	
	@GetMapping("/{id}")
	public Recipe getRecipe(@PathVariable Long id) {
		return recipeRepo.findById(id).get();
	}
	
	@PostMapping("/add/{id}")
	public Category addRecipeToCategory(@RequestBody String body, @PathVariable Long id) throws JSONException {
		JSONObject json = new JSONObject(body);
		String recipeName = json.getString("recipeName");
		String recipeImage = json.getString("recipeImage");
		String instructions = json.getString("instructions");
		Category categoryToAddTo = categoryRepo.findById(id).get();
		Recipe recipeToAdd = new Recipe(recipeName, instructions, categoryToAddTo, recipeImage);
		recipeRepo.save(recipeToAdd);
		categoryToAddTo.addRecipe(recipeToAdd);
		categoryRepo.save(categoryToAddTo);
		return categoryToAddTo;
	}
	
	@PostMapping("/delete/{id}")
	public Category deleteRecipeFromCategory(@PathVariable Long id) throws JSONException {
		Recipe recipeToDelete = recipeRepo.findById(id).get();
		Category category = recipeToDelete.getCategory();
		recipeToDelete.removeIngredientsInCollection();
		recipeRepo.save(recipeToDelete);
		category.removeRecipe(recipeToDelete);
		recipeRepo.delete(recipeToDelete);
		categoryRepo.save(category);		
		return category;
	}
	
	@PostMapping("/edit/{id}")
    public Recipe editRecipie(@PathVariable Long id, @RequestBody String body) throws JSONException {
        Recipe recipeToUpdate = recipeRepo.findById(id).get();
        JSONObject json = new JSONObject(body);
        String newInstructions = json.getString("instructions");
        recipeToUpdate.editInstructions(newInstructions);
        recipeRepo.save(recipeToUpdate);
        return recipeToUpdate;
    }
}
