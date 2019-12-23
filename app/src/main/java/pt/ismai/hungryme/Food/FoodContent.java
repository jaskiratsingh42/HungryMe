package pt.ismai.hungryme.Food;

import android.content.Context;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Serving;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FoodContent {
    private Context context;
    public static Food food = new Food();
    public static List<CompactFood> foods = new ArrayList<>();
    public static List<Serving> servings = new ArrayList<>();
    public static List<String> servingUnits = new ArrayList<>();
    public static List<Diary> diary = new ArrayList<>();
    public static void computeServingUnits() {
        for (int i = 0; i <= servings.size(); i++) {
            servingUnits.add(servings.get(i).getMetricServingUnit());
        }
    }
    class Diary
    {
        protected String name;
        protected String description;
        protected BigDecimal quantity;
        protected BigDecimal calories;

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

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getCalories() {
            return calories;
        }

        public void setCalories(BigDecimal calories) {
            this.calories = calories;
        }
    }


}
