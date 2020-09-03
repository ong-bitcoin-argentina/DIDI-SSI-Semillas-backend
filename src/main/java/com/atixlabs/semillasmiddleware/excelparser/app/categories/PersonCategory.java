package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public class PersonCategory implements Category {

    String categoryUniqueName;
    private Categories categoryName;

    private AnswerDto name;
    private AnswerDto surname;
    private AnswerDto idType;
    private AnswerDto documentNumber;
    private AnswerDto gender;
    private AnswerDto birthDate;
    private AnswerDto relation;
    private PersonType personType;
    private AnswerDto occupation;
    private AnswerDto studies;
    private AnswerDto works;
    private AnswerDto age;

    private AnswerDto residenceTimeInCountry;
    private AnswerDto facebook;
    private AnswerDto address;
    private AnswerDto betweenStreets;
    private AnswerDto neighborhood;
    private AnswerDto zone;
    private AnswerDto locality;
    private AnswerDto referenceContactName;
    private AnswerDto referenceContactSurname;
    private AnswerDto referenceContactRelation;
    private AnswerDto referenceContactPhone;
    private AnswerDto nationality;
    private AnswerDto primary;
    private AnswerDto highSchool;
    private AnswerDto tertiary;
    private AnswerDto university;
    private AnswerDto workshops;
    private AnswerDto courses;
    private AnswerDto landLine;
    private AnswerDto cellPhone;
    private AnswerDto civilStatus;
    private AnswerDto email;

    public PersonCategory(String categoryUniqueName){
        this.name = new AnswerDto(PersonQuestion.NAME);
        this.surname = new AnswerDto(PersonQuestion.SURNAME);
        this.idType = new AnswerDto(PersonQuestion.ID_TYPE);
        this.documentNumber = new AnswerDto(PersonQuestion.ID_NUMBER);
        this.gender = new AnswerDto(PersonQuestion.GENDER);
        this.birthDate = new AnswerDto(PersonQuestion.BIRTH_DATE);
        this.relation = new AnswerDto(PersonQuestion.RELATION);
        this.occupation = new AnswerDto(PersonQuestion.OCCUPATION);
        this.studies = new AnswerDto(PersonQuestion.STUDIES);
        this.works = new AnswerDto(PersonQuestion.WORKS);
        this.age = new AnswerDto(PersonQuestion.AGE);

        this.residenceTimeInCountry = new AnswerDto(PersonQuestion.RESIDENCE_TIME_IN_COUNTRY);
        this.facebook = new AnswerDto(PersonQuestion.FACEBOOK);
        this.address = new AnswerDto(PersonQuestion.ADDRESS);
        this.betweenStreets = new AnswerDto(PersonQuestion.BETWEEN_STREETS);
        this.neighborhood = new AnswerDto(PersonQuestion.NEIGHBORHOOD);
        this.zone = new AnswerDto(PersonQuestion.ZONE);
        this.locality = new AnswerDto(PersonQuestion.LOCALITY);
        this.referenceContactName = new AnswerDto(PersonQuestion.REFERENCE_CONTACT_NAME);
        this.referenceContactSurname = new AnswerDto(PersonQuestion.REFERENCE_CONTACT_SURNAME);
        this.referenceContactRelation = new AnswerDto(PersonQuestion.RELATION);
        this.referenceContactPhone = new AnswerDto(PersonQuestion.REFERENCE_CONTACT_PHONE);
        this.nationality = new AnswerDto(PersonQuestion.NATIONALITY);
        this.primary = new AnswerDto(PersonQuestion.PRIMARY);
        this.highSchool = new AnswerDto(PersonQuestion.HIGH_SCHOOL);
        this.tertiary = new AnswerDto(PersonQuestion.TERTIARY);
        this.university = new AnswerDto(PersonQuestion.UNIVERSITY);
        this.workshops = new AnswerDto(PersonQuestion.WORKSHOPS);
        this.courses = new AnswerDto(PersonQuestion.COURSES);
        this.landLine = new AnswerDto(PersonQuestion.LAND_LINE);
        this.cellPhone = new AnswerDto(PersonQuestion.CELLPHONE);
        this.civilStatus = new AnswerDto(PersonQuestion.CIVIL_STATUS);
        this.email = new AnswerDto(PersonQuestion.EMAIL);

        this.categoryUniqueName = categoryUniqueName;
        this.categoryName = Categories.BENEFICIARY_CATEGORY_NAME;//TODO:CREAR TIPO PERSONA O RESOLVER AGRUPACION

        String personTypeString = StringUtil.removeNumbers(StringUtil.toUpperCaseTrimAndRemoveAccents(categoryUniqueName.replaceAll("DATOS|DEL","")));

        try{
            this.personType = PersonType.get(personTypeString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            log.error("ERROR AL INTENTAR OBTENER EL TIPO DE PERSONA: "+e.getMessage());
        }
    }

    @Override
    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());
        PersonQuestion questionMatch = PersonQuestion.getEnumByStringValue(question);

        if (questionMatch == null)
            return;

        switch (questionMatch) {
            case ID_TYPE:
                this.idType.setAnswer(answerRow, processExcelFileResult);
                break;
            case ID_NUMBER:
                this.documentNumber.setAnswer(answerRow, processExcelFileResult);
                break;
            case NAME:
                this.name.setAnswer(answerRow, processExcelFileResult);
                break;
            case SURNAME:
                this.surname.setAnswer(answerRow, processExcelFileResult);
                break;
            case GENDER:
                this.gender.setAnswer(answerRow, processExcelFileResult);
                break;
            case BIRTH_DATE:
                this.birthDate.setAnswer(answerRow, processExcelFileResult);
                break;
            case RELATION:
                this.relation.setAnswer(answerRow, processExcelFileResult);
                this.referenceContactRelation.setAnswer(answerRow, processExcelFileResult);
                break;
            case OCCUPATION:
                this.occupation.setAnswer(answerRow, processExcelFileResult);
                break;
            case WORKS:
                this.works.setAnswer(answerRow, processExcelFileResult);
                break;
            case STUDIES:
                this.studies.setAnswer(answerRow, processExcelFileResult);
                break;
            case AGE:
                this.age.setAnswer(answerRow, processExcelFileResult);
                break;
            case RESIDENCE_TIME_IN_COUNTRY:
                this.residenceTimeInCountry.setAnswer(answerRow, processExcelFileResult);
                break;
            case FACEBOOK:
                this.facebook.setAnswer(answerRow, processExcelFileResult);
                break;
            case ADDRESS:
                this.address.setAnswer(answerRow, processExcelFileResult);
                break;
            case BETWEEN_STREETS:
                this.betweenStreets.setAnswer(answerRow, processExcelFileResult);
                break;
            case NEIGHBORHOOD:
                this.neighborhood.setAnswer(answerRow, processExcelFileResult);
                break;
            case ZONE:
                this.zone.setAnswer(answerRow, processExcelFileResult);
                break;
            case LOCALITY:
                this.locality.setAnswer(answerRow, processExcelFileResult);
                break;
            case REFERENCE_CONTACT_NAME:
                this.referenceContactName.setAnswer(answerRow, processExcelFileResult);
                break;
            case REFERENCE_CONTACT_SURNAME:
                this.referenceContactSurname.setAnswer(answerRow, processExcelFileResult);
                break;
            case REFERENCE_CONTACT_PHONE:
                this.referenceContactPhone.setAnswer(answerRow, processExcelFileResult);
                break;
            case NATIONALITY:
                this.nationality.setAnswer(answerRow, processExcelFileResult);
                break;
            case PRIMARY:
                this.primary.setAnswer(answerRow, processExcelFileResult);
                break;
            case HIGH_SCHOOL:
                this.highSchool.setAnswer(answerRow, processExcelFileResult);
                break;
            case TERTIARY:
                this.tertiary.setAnswer(answerRow, processExcelFileResult);
                break;
            case UNIVERSITY:
                this.university.setAnswer(answerRow, processExcelFileResult);
                break;
            case WORKSHOPS:
                this.workshops.setAnswer(answerRow, processExcelFileResult);
                break;
            case COURSES:
                this.courses.setAnswer(answerRow, processExcelFileResult);
                break;
            case LAND_LINE:
                this.landLine.setAnswer(answerRow, processExcelFileResult);
                break;
            case CELLPHONE:
                this.cellPhone.setAnswer(answerRow, processExcelFileResult);
                break;
            case CIVIL_STATUS:
                this.civilStatus.setAnswer(answerRow, processExcelFileResult);
                break;
            case EMAIL:
                this.email.setAnswer(answerRow, processExcelFileResult);
                break;
        }
    }

    @Override
    public String getCategoryUniqueName(){
        return categoryUniqueName;
    }

    @Override
    public Categories getCategoryName(){return categoryName;}

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(this.name);
        answers.add(this.surname);
        answers.add(this.documentNumber);
        answers.add(this.gender);
        answers.add(this.birthDate);
        answers.add(this.relation);
        answers.add(this.occupation);
        answers.add(this.works);
        answers.add(this.studies);
        answers.add(this.age);

        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult, categoryUniqueName)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
    }

    @Override
    public boolean isEmpty(){
        return documentNumber.answerIsEmpty();
    }

    @Override
    public boolean isRequired(){
        switch (personType){
            case BENEFICIARY:
                return true;
            case CHILD:
            case SPOUSE:
            case OTHER_KINSMAN:
                return !(this.isEmpty());
        }
        return true;
    }

    public String getName() {
        return (String) name.getAnswer();
    }

    public String getSurname() {
        return (String) surname.getAnswer();
    }

    public String getIdType() {
        return (String) idType.getAnswer();
    }

    public Long getDocumentNumber() {
        return (Long) documentNumber.getAnswer();
    }

    public String getGender() {
        return (String) gender.getAnswer();
    }

    public LocalDate getBirthDate() {
        return (LocalDate) birthDate.getAnswer();
    }

    public String getRelation() {
        return (String) relation.getAnswer();
    }


    @Override
    public String toString() {
        return "PersonCategory{" +
                "categoryUniqueName='" + categoryUniqueName + '\'' +
                ", categoryName=" + categoryName +
                ", name=" + name +
                ", surname=" + surname +
                ", idType=" + idType +
                ", documentNumber=" + documentNumber +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", relation=" + relation +
                ", personType=" + personType +
                ", occupation=" + occupation +
                ", studies=" + studies +
                ", works=" + works +
                ", age=" + age +
                '}';
    }

    @Override
    public List<AnswerDto> getAnswersList(){
        return Arrays.asList(name, surname, idType, documentNumber, gender, birthDate, relation,
                occupation, studies, works, age, residenceTimeInCountry, facebook, address, betweenStreets, neighborhood,
                zone, locality, referenceContactName, referenceContactSurname, referenceContactRelation, referenceContactPhone,
                nationality, primary, highSchool, tertiary, university, workshops, courses, landLine, cellPhone, civilStatus, email);
    }

}
