package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TacoCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo,
                                        UserRepository userRepo,
                                        PasswordEncoder encoder,
                                        TacoRepository tacoRepo) {
        return args -> {
            var flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
            var cornTortilla = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
            var groundBeef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
            var carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
            var dicedTomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
            var lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
            var cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
            var monterreyJack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
            var salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
            var sourCream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);
            var ingredients = List.of(
                    flourTortilla,
                    cornTortilla,
                    groundBeef,
                    carnitas,
                    dicedTomatoes,
                    lettuce,
                    cheddar,
                    monterreyJack,
                    salsa,
                    sourCream
            );
            repo.saveAll(ingredients);

            var user = new User();
            user.setUsername("blessed");
            user.setPassword(encoder.encode("secret"));
            userRepo.save(user);

            Taco taco1 = new Taco();
            taco1.setName("Carnivore");
            taco1.setIngredients(Arrays.asList(flourTortilla, groundBeef,
                    carnitas, sourCream, salsa, cheddar));
            tacoRepo.save(taco1);

            Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(Arrays.asList(
                    cornTortilla, groundBeef, cheddar,
                    monterreyJack, sourCream));

            tacoRepo.save(taco2);
            Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(Arrays.asList(
                    flourTortilla, cornTortilla, dicedTomatoes,
                    lettuce, salsa));
            tacoRepo.save(taco3);
        };
    }
}
