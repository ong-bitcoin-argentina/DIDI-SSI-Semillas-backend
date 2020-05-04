package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AnswerCategoryFactory {
    /*
    public static final Integer AMOUNT_CHILDREN = 11;
    public static final Integer AMOUNT_KINSMAN = 3;
    public static final String BENEFICIARY_CATEGORY_NAME="DATOS DEL BENEFICIARIO";
    public static final String SPOUSE_CATEGORY_NAME="DATOS DEL CONYUGE";
    public static final String CHILD_CATEGORY_NAME="DATOS HIJO";
    public static final String KINSMAN_CATEGORY_NAME="OTRO MIEMBRO DE LA FAMILIA";
    public static final String ENTREPRENEURSHIP_CATEGORY_NAME="EMPRENDIMIENTO";
    public static final String DWELLING_CATEGORY_NAME="VIVIENDA";
*/
    private Map<String, Class<?>> categoryMap;


    public ArrayList<Category> getCategoryList(){


        //Keep all Category options.
        ArrayList<Categories> categoryEnumList = new ArrayList<>(Categories.getCodeList());
        //Keep actual objects to return
        ArrayList<Category> categoryList = new ArrayList<>();

        //building CATEGORY_MAP
        categoryMap = new HashMap<>();
        for (Categories categoryEnum : categoryEnumList) {
            categoryMap.put(categoryEnum.getCode(), categoryEnum.getLinkedClass());
        }

        //Filling category list with objects
        for (Categories categoryEnum : categoryEnumList) {
            addCategoryInstancesToList(categoryList, categoryEnum);
        }
        return categoryList;
    }

    private void addCategoryInstancesToList(ArrayList<Category> categoryList, Categories categoryEnum) {

        Class<?> categoryClass = categoryMap.get(categoryEnum.getCode());

        if (categoryEnum.getAmount() <=0)
            return;

        if (categoryEnum.getAmount() == 1){
            try {
                categoryList.add((Category) categoryClass.getConstructor(String.class).newInstance(categoryEnum.getCode()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("No existe archivo de clase asociada a la categoria recibida, no se puede vincular con constructor: "+e.getMessage());
            }
        }
        else {
            for (int i = 1; i<categoryEnum.getAmount()+1; i++) {
                try {
                    categoryList.add((Category) categoryClass.getConstructor(String.class).newInstance(categoryEnum.getCode()+" "+i));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("No existe archivo de clase asociada a la categoria recibida, no se puede vincular con constructor: "+e.getMessage());
                }
            }
        }
    }
}


/*

CATEGORY_MAP = new HashMap<>() {{
             put(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), PersonCategory.class);
             put(Categories.SPOUSE_CATEGORY_NAME.getCode(), PersonCategory.class);
             put(Categories.CHILD_CATEGORY_NAME.getCode(), PersonCategory.class);
             put(Categories.KINSMAN_CATEGORY_NAME.getCode(), PersonCategory.class);
             put(Categories.ENTREPRENEURSHIP_CATEGORY_NAME.getCode(), EntrepreneurshipCategory.class);
             put(Categories.DWELLING_CATEGORY_NAME.getCode(), DwellingCategory.class);
         }};

        categoryList.add(new PersonCategory(Categories.BENEFICIARY_CATEGORY_NAME.getCode()));
        categoryList.add(new PersonCategory(Categories.SPOUSE_CATEGORY_NAME.getCode()));

        for (int i = 1; i < Categories.CHILD_CATEGORY_NAME.getAmount()+1; i++) {
            categoryList.add(new PersonCategory(Categories.CHILD_CATEGORY_NAME.getCode()+" "+ i));
        }
        for (int i = 1; i < Categories.KINSMAN_CATEGORY_NAME.getAmount()+1; i++) {
            categoryList.add(new PersonCategory(Categories.KINSMAN_CATEGORY_NAME.getCode()+ " " + i));
        }

        categoryList.add(new EntrepreneurshipCategory(Categories.ENTREPRENEURSHIP_CATEGORY_NAME.getCode()));
        categoryList.add(new DwellingCategory(Categories.DWELLING_CATEGORY_NAME.getCode()));
        */