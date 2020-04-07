package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
//import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnswerCategoryFactory {
    private static Map<String, Class<?>> CATEGORIES_TYPE = new HashMap<String, Class<?>>() {{
        put("DATOS DEL BENEFICIARIO", BeneficiaryCategory.class);
        //put("DATOS DEL CONYUGE", );
        //put("DATOS HIJO", null);
        put("EMPRENDIMIENTO", EntrepreneurshipCategory.class);
        put("VIVIENDA", DwellingCategory.class);
    }};

    private Map<String, Category> categories = new HashMap<String, Category>() {{
        put("DATOS DEL BENEFICIARIO", null);
        put("DATOS DEL CONYUGE", null);
        put("DATOS HIJO", null);
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
               newCategory = (Category) CATEGORIES_TYPE.get(category).getConstructor().newInstance();
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

}
