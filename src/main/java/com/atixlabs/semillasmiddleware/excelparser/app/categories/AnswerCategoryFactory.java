package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AnswerCategoryFactory {
    private Map<String, Class<?>> categoryMap;


    public List<Category> getCategoryList(boolean createCredential){
        //Keep all Category options.
        List<Categories> categoryEnumList = new ArrayList<>(Categories.getCodeList());
        //Keep actual objects to return
        List<Category> categoryList = new ArrayList<>();

        //building CATEGORY_MAP
        categoryMap = new HashMap<>();
        for (Categories categoryEnum : categoryEnumList) {
            if (categoryEnum.getCode().equals("VIVIENDA") && (!createCredential) ){
                categoryMap.put(categoryEnum.getCode(), DwellingCategoryWithoutCredentials.class);
            }else {
                categoryMap.put(categoryEnum.getCode(), categoryEnum.getLinkedClass());
            }
        }

        //Filling category list with objects
        for (Categories categoryEnum : categoryEnumList) {
            addCategoryInstancesToList(categoryList, categoryEnum);
        }
        return categoryList;
    }

    private void addCategoryInstancesToList(List<Category> categoryList, Categories categoryEnum) {

        Class<?> categoryClass = categoryMap.get(categoryEnum.getCode());

        if (categoryEnum.getAmount() <=0)
            return;

        if (categoryEnum.getAmount() == 1){
            try {
                categoryList.add((Category) categoryClass.getConstructor(String.class, Categories.class).newInstance(categoryEnum.getCode(), categoryEnum));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("No existe archivo de clase asociada a la categoria recibida, no se puede vincular con constructor: "+e.getMessage());
            }
        }
        else {
            for (int i = 1; i<categoryEnum.getAmount()+1; i++) {
                try {
                    categoryList.add((Category) categoryClass.getConstructor(String.class, Categories.class).newInstance(categoryEnum.getCode()+" "+i, categoryEnum));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("No existe archivo de clase asociada a la categoria recibida, no se puede vincular con constructor: "+e.getMessage());
                }
            }
        }
    }
}