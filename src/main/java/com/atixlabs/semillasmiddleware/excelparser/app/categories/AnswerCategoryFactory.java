package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnswerCategoryFactory {
    private static Map<String, Class<?>> CATEGORIES_TYPE = new HashMap<String, Class<?>>() {{
        put("DATOS DEL BENEFICIARIO", PersonCategory.class);
        put("DATOS DEL CONYUGE", PersonCategory.class);
        put("DATOS HIJO", PersonCategory.class);
        put("OTRO MIEMBRO DE LA FAMILIA", PersonCategory.class);
        put("EMPRENDIMIENTO", EntrepreneurshipCategory.class);
        put("VIVIENDA", DwellingCategory.class);
    }};

    private Map<String, Category> categories = new HashMap<String, Category>() {{
        put("DATOS DEL BENEFICIARIO", null);
        put("DATOS DEL CONYUGE", null);
        put("DATOS HIJO", null);
        put("OTRO MIEMBRO DE LA FAMILIA", null);
        put("EMPRENDIMIENTO", null);
        put("VIVIENDA", null);
    }};

    public Category get(String category) throws InvalidCategoryException, Exception {
        if (category == null)
            return null;

        //Removes numbers in category name to reduce the number of alternatives (i.e: DATOS HIJO 1, DATOS HIJO 2, etc)
        category =  StringUtil.cleanString(category);

        if (!categories.containsKey(category)){
            throw new InvalidCategoryException(category);
        }

        if (categories.get(category) == null){
            Category newCategory = null;
            try{
                newCategory = createCategoryByCategoryName(category);
                categories.put(category,newCategory);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new Exception(e.getMessage());
            }
            return newCategory;
        }
        else{
            return categories.get(category);
        }
    }

    //If it's a person (beneficiary, child or other kinsman): pass the type of person to the PersonCategory's constructor)
    public Category createCategoryByCategoryName(String categoryName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> categoryType = CATEGORIES_TYPE.get(categoryName);
        if (categoryType == PersonCategory.class){
            return (Category) categoryType.getConstructor(String.class).newInstance(categoryName);
        }
        else{
            return (Category) categoryType.getConstructor().newInstance();
        }
    }

}
