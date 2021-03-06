package org.wecancodeit.recipebox.controllers;

import java.util.Collection;

import javax.annotation.Resource;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/ingredients")
public class IngredientController {
	
	@Resource
	CategoryRepository categoryRepo;
	
	@Resource
	RecipeRepository recipeRepo;
	
	@Resource
	IngredientRepository ingredientRepo;
	
	@GetMapping("")
	public Collection<Ingredient> getAllIngredients() {
		return (Collection<Ingredient>) ingredientRepo.findAll();
	}
	
	@PostMapping("/add/{id}")
	public Recipe addIngredientToRecipe(@RequestBody String body, @PathVariable Long id) throws JSONException {
		JSONObject json = new JSONObject(body);
		String measurement = json.getString("measurement");
		String ingredient = json.getString("ingredientName");
		Recipe recipeToAddTo = recipeRepo.findById(id).get();
		Ingredient ingredientToAdd = new Ingredient(measurement, ingredient);
		ingredientRepo.save(ingredientToAdd);
		recipeToAddTo.addIngredient(ingredientToAdd);
		recipeRepo.save(recipeToAddTo);
		return recipeToAddTo;
}

}
