package com.atixlabs.semillasmiddleware.pdfparser.surveyPdfParser.service;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.pdfparser.util.PDFUtil;
import com.atixlabs.semillasmiddleware.util.EmailTemplatesUtil;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class PdfParserService {
    private static final String TEMPLATE_NAME = "excell_survey_data.html";
    private static final String PDF_SUFFIX = "survey_data";
    private static final String CATEGORY_NAME_PARAM = "{{categoryName}}";
    private static final String TABLE_CONTENT_PARAM = "{{tableContent}}";
    private static final String QUESTION_PARAM = "{{question}}";
    private static final String ANSWER_PARAM = "{{answer}}";

    private static final String style = "style=\"border: 1px solid #FFFFFF ; border-collapse: collapse; padding: 5px\"\"";
    private String rowTemplate = "        <tr "+style+">\n" +
                                 "            <td>{{question}}</td>\n" +
                                 "            <td>{{answer}}</td>\n" +
                                 "        </tr>";

    private String headerTemplate = "        <tr "+style+">\n" +
                                    "            <th colspan=\"2\">\n" +
                                    "                <br />"+CATEGORY_NAME_PARAM+"\n" +
                                    "            </th>\n" +
                                    "        </tr>\n" +
                                    "        <th>PREGUNTA</th>\n" +
                                    "        <th>RESPUESTA</th>\n";

    public String generatePdfFromSurvey(SurveyForm surveyForm){
        Stack<Category> categoriesStack = new Stack<>();
        //top down order
        categoriesStack.push(surveyForm.getCategoryByUniqueName(Categories.DWELLING_CATEGORY_NAME.getCode(), null));
        categoriesStack.push(surveyForm.getCategoryByUniqueName(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), null));

        String html = generateHtmlFromCategories(categoriesStack);
        String template = EmailTemplatesUtil.getTemplate(TEMPLATE_NAME).replace(TABLE_CONTENT_PARAM, html);
        return PDFUtil.createTemporaryPdf(PDF_SUFFIX, template);
    }

    private String generateHtmlFromCategories(Stack<Category> categoriesStack){
        String htmlStack = "";
        while (!categoriesStack.empty()){
            Category category = categoriesStack.pop();
            String header = headerTemplate.replace(CATEGORY_NAME_PARAM, category.getCategoryUniqueName());
            String rows = category.getHtmlFromTemplate(rowTemplate, QUESTION_PARAM, ANSWER_PARAM);
            htmlStack += header+rows;
        }
        return htmlStack;
    }

}
