import java.io.File;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.gen5.api.BeforeEach;

import com.hq21tl_homework.recipe_book.Ingredient;
import com.hq21tl_homework.recipe_book.Ingredient.IngredientBuilder;
import com.hq21tl_homework.recipe_book.Recipe;
import com.hq21tl_homework.recipe_book.Recipe.RecipeBuilder;
import com.hq21tl_homework.recipe_book.RecipeBook;
import com.hq21tl_homework.recipe_book.RecipeEntry;
import com.hq21tl_homework.recipe_book.RecipeEntry.RecipeEntryBuilder;
import com.hq21tl_homework.recipe_book.RecipeEntryXMLHander;

public class RecipeBookTests{

    RecipeBook recipeBook;
    @BeforeEach // does not work for some reason. called it from test
    public void buildRecipeBook(){
        recipeBook = new RecipeBook(new RecipeEntry[]{
            new RecipeEntry(
                "name", "category", "description",
                new Recipe[]{
                    new Recipe(
                        new Ingredient[]{
                            new Ingredient("ingredient", 1, "q")
                        },
                        new String[]{
                            "Instruction"
                        }
                    ),
                    new Recipe(
                        new Ingredient[]{
                            new Ingredient("ingredient2", 2, "q"),
                            new Ingredient("ingredient3", 3, "q1")
                        },
                        new String[]{
                            "Instruction"
                        }
                    )
                }),
            new RecipeEntry(
                "name2", "category2", "description2",
                new Recipe[]{
                    new Recipe(
                        new Ingredient[]{
                            new Ingredient("ingredient4", 4, "q2")
                        },
                        new String[]{
                            "Instruction"
                        }
                    ),
                })
        });
    }

    @Test
    public void CreatingRecipeBook(){
        buildRecipeBook();
        assertNotNull("Recipe exists", recipeBook.getRecipe("name"));
        RecipeEntry entry = recipeBook.getRecipe("name");
        assertEquals("name", entry.getName());
        assertEquals("category", entry.getCategory());
        assertEquals("description", entry.getDescription());
        assertEquals(2, entry.getRecipes().length);
        
        Recipe recipe = entry.getRecipes()[0];
        
        assertEquals(1, recipe.getIngredients().length);
        Ingredient ingredient = recipe.getIngredients()[0];
        assertEquals("ingredient", ingredient.getName());
        assertEquals(1, ingredient.getAmount(), 0.001);
        assertEquals("q", ingredient.getQuantifyer());
        
        assertEquals(1, recipe.getInstructions().length);
        assertEquals("Instruction", recipe.getInstructions()[0]);
    }

    @Test
    public void FilteringRecipes(){
        buildRecipeBook();

        RecipeEntry[] filter1Result = recipeBook.filterRecipesByName("name");
        assertEquals(2, filter1Result.length);

        RecipeEntry[] filter2Result = recipeBook.filterRecipesByName("name2");
        assertEquals(1, filter2Result.length);
        assertEquals("name2", filter2Result[0].getName());

        RecipeEntry[] filter3Result = recipeBook.filterRecipesByCategory("category");
        assertEquals(2, filter3Result.length);
        
        RecipeEntry[] filter4Result = recipeBook.filterRecipesByCategory("category2");
        assertEquals(1, filter4Result.length);
        assertEquals("name2", filter4Result[0].getName());
        
        RecipeEntry[] filter5Result = recipeBook.filterRecipesByAvailableIngredients(List.of("ingredient", "ingredient4"));
        assertEquals(2, filter5Result.length);

        RecipeEntry[] filter6Result = recipeBook.filterRecipesByAvailableIngredients(List.of("ingredient2", "ingredient3"));
        assertEquals(1, filter6Result.length);
        assertEquals("name", filter6Result[0].getName());
    }

    @Test
    public void GetIngredientsAndQuantifyers(){
        buildRecipeBook();
        String[] ingredients = recipeBook.getIngredients();
        String[] quantifyers = recipeBook.getQuantifyers();

        assertArrayEquals(new String[]{"ingredient", "ingredient2", "ingredient3", "ingredient4"}, ingredients);
        assertArrayEquals(new String[]{"q", "q1", "q2"}, quantifyers);
    }

    @Test 
    public void ImportExportTest(){
        buildRecipeBook();
        RecipeEntryXMLHander.exportRecipe(recipeBook.getRecipe("name"));

        File importFile = new File("name.xml");
        RecipeEntry imported = RecipeEntryXMLHander.importRecipe(importFile);
        importFile.delete();

        assertEquals("name", imported.getName());
        assertEquals("category", imported.getCategory());
        assertEquals("description", imported.getDescription());
        assertEquals(2, imported.getRecipes().length);
        
        Recipe recipe = imported.getRecipes()[0];
        assertEquals(1, recipe.getIngredients().length);
        Ingredient ingredient = recipe.getIngredients()[0];
        assertEquals("ingredient", ingredient.getName());
        assertEquals(1, ingredient.getAmount(), 0.001);
        assertEquals("q", ingredient.getQuantifyer());
        
        assertEquals(1, recipe.getInstructions().length);
        assertEquals("Instruction", recipe.getInstructions()[0]);

    }

    @Test
    public void ModifyingRecipeBook(){
        buildRecipeBook();
        RecipeEntry entry = new RecipeEntry("a", "a", "a", new Recipe[0]);
        RecipeEntry mod = new RecipeEntry("a", "b", "a", new Recipe[0]);
        
        // Adding recipe + adding exact duplicate(ignored)
        assertTrue(recipeBook.addRecipe(entry));
        assertTrue(recipeBook.addRecipe(entry));
        // Non exact duplicate
        assertFalse(recipeBook.addRecipe(mod));
        
        // Non-existant recipe
        assertNull(recipeBook.getRecipe("non-existant"));
        
        // Modify
        assertTrue(recipeBook.updateRecipe(mod));
        assertEquals(mod.getCategory(), recipeBook.getRecipe("a").getCategory());
    }

    @Test
    public void BuilderTest(){
        RecipeEntryBuilder entryBuilder = new RecipeEntry.RecipeEntryBuilder();
        entryBuilder.name = "name";
        entryBuilder.category = "category";
        entryBuilder.description = "description";

        // Adding a proper recipe
        RecipeBuilder recipeBuilder = new RecipeBuilder();
        IngredientBuilder ingredientBuilder = new IngredientBuilder();
        ingredientBuilder.name = "i1";
        ingredientBuilder.amount = 1;
        ingredientBuilder.quantifyer = "q1";
        recipeBuilder.ingredients.add(ingredientBuilder);
        recipeBuilder.instructions.add("Instruction");
        entryBuilder.recipes.add(recipeBuilder);

        // Adding an empty recipe, will be cleaned up.
        RecipeBuilder recipeBuilder2 = new RecipeBuilder();
        entryBuilder.recipes.add(recipeBuilder2);

        entryBuilder.cleanup();
        RecipeEntry built = entryBuilder.build();

        assertEquals(1, built.getRecipes().length); 
    }
}
