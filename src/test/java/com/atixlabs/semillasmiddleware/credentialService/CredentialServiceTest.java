package com.atixlabs.semillasmiddleware.credentialService;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CredentialServiceTest {

    @InjectMocks
    private AnswerCategoryFactory answerCategoryFactory;

    @InjectMocks
    private CredentialService credentialService;

    @Mock
    private CredentialRepository credentialRepository;
    @Mock
    private CredentialStateRepository credentialStateRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private CredentialIdentityRepository credentialIdentityRepository;
    @Mock
    private CredentialDwellingRepository credentialDwellingRepository;
    @Mock
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;

    private DateUtil util = new DateUtil();

    private Person getBeneficiaryMock(){
        Person person = new Person();
        person.setId(1L);
        person.setDocumentNumber(29302594L);
        person.setFirstName("Pepito");
        return person;
    }
    private List<Credential> credentialsMock(){
        List<Credential> credentials = new ArrayList<>();

        Person beneficiary = getBeneficiaryMock();


        CredentialCredit credential1 = new CredentialCredit();
        credential1.setId(1L);
        credential1.setIdDidiCredential(2L);
        credential1.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_CREDIT.getCode());
        credential1.setDateOfIssue(LocalDateTime.now());
        credential1.setDateOfRevocation(LocalDateTime.now().plusDays(14));
        credential1.setBeneficiaryDni(29302594L);
        credential1.setCreditState("Estado");
        credential1.setCreditHolder(beneficiary);
        credential1.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        credentials.add(credential1);

        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setId(2L);
        credentialIdentity.setCreditHolderDni(34534534L);
        credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
        credentialIdentity.setBeneficiaryName("Pepito");
        credentialIdentity.setDateOfRevocation(util.getLocalDateTimeNow());
        credentialIdentity.setDateOfIssue(util.getLocalDateTimeNow().minusDays(14));
        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        credentials.add(credentialIdentity);

        CredentialIdentity credentialIdentity2 = new CredentialIdentity();
        credentialIdentity2.setId(3L);
        credentialIdentity2.setCreditHolderDni(34534534L);
        credentialIdentity2.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
        credentialIdentity2.setBeneficiaryName("Pepito");
        credentialIdentity2.setDateOfRevocation(util.getLocalDateTimeNow());
        credentialIdentity2.setDateOfIssue(util.getLocalDateTimeNow().minusDays(14));
        credentialIdentity2.setBeneficiary(beneficiary);
        credentialIdentity2.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        credentials.add(credentialIdentity2);



        return credentials;
    }

    private List<Credential> credentialsFilteredActiveMock(){
       return credentialsMock().stream().filter(credential -> credential.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode())).collect(Collectors.toList());
    }

    private List<Credential> credentialsFilteredRevokedMock(){
        return credentialsMock().stream().filter(credential -> credential.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode())).collect(Collectors.toList());
    }



    private Row createRowMock(String category, String question, String answer){
        Workbook wb;
        Sheet sheet;

        wb = new HSSFWorkbook();
        sheet = wb.createSheet();
        Row row = sheet.createRow(8);
        System.out.println(row.getRowNum());
        row.setRowNum(8);
        Cell surveyCell = row.createCell(7);
        surveyCell.setCellValue("SURVEY-1");
        Cell dateCell = row.createCell(9);
        dateCell.setCellValue("12/12/2019");
        Cell pdvCell = row.createCell(10);
        pdvCell.setCellValue(5456580);
        Cell categoryCell = row.createCell(14);
        categoryCell.setCellValue(category);
        Cell questionCell = row.createCell(15);
        questionCell.setCellValue(question);
        Cell answerCell = row.createCell(16);
        answerCell.setCellValue(answer);
        return row;
    }

    private ArrayList<AnswerRow> createAnswerRowListMock() throws InvalidRowException {
        ArrayList<AnswerRow> answerRowArrayList = new ArrayList<>();
        answerRowArrayList.add(new AnswerRow(createRowMock("EMPRENDIMIENTO", "FECHA DE INICIO / REINICIO","03/04/2020")));
        answerRowArrayList.add(new AnswerRow(createRowMock("EMPRENDIMIENTO", "ACTIVIDAD PRINCIPAL","Comercio")));
        answerRowArrayList.add(new AnswerRow(createRowMock("EMPRENDIMIENTO", "DIRECCION","Direccion 123")));
        answerRowArrayList.add(new AnswerRow(createRowMock("EMPRENDIMIENTO", "FIN DE LA ACTIVIDAD","03/04/2020")));
        answerRowArrayList.add(new AnswerRow(createRowMock("EMPRENDIMIENTO", "NOMBRE EMPRENDIMIENTO","Panaderia pepe")));
        answerRowArrayList.add(new AnswerRow(createRowMock("EMPRENDIMIENTO", "TIPO DE EMPRENDIMIENTO","Producto")));

        answerRowArrayList.add(new AnswerRow(createRowMock("DATOS DEL BENEFICIARIO", "NOMBRE","Pedro")));
        answerRowArrayList.add(new AnswerRow(createRowMock("DATOS DEL BENEFICIARIO", "APELLIDO","Picapiedra")));
        answerRowArrayList.add(new AnswerRow(createRowMock("DATOS DEL BENEFICIARIO", "TIPO DE DOCUMENTO","Dni")));
        answerRowArrayList.add(new AnswerRow(createRowMock("DATOS DEL BENEFICIARIO", "NUMERO DE DOCUMENTO","30697455")));
        answerRowArrayList.add(new AnswerRow(createRowMock("DATOS DEL BENEFICIARIO", "GENERO","Masculino")));
        answerRowArrayList.add(new AnswerRow(createRowMock("DATOS DEL BENEFICIARIO", "FECHA DE NACIMIENTO","03/04/2020")));

        answerRowArrayList.add(new AnswerRow(createRowMock("VIVIENDA", "VIVIENDA","Casa")));
        answerRowArrayList.add(new AnswerRow(createRowMock("VIVIENDA", "TIPO DE TENENCIA","Picapiedra")));
        answerRowArrayList.add(new AnswerRow(createRowMock("VIVIENDA", "DISTRITO DE RESIDENCIA","Barrio 31")));

        return answerRowArrayList;
    }

    private SurveyForm createSurveyFormMock(ArrayList<AnswerRow> answerRowArrayList, ProcessExcelFileResult processExcelFileResult){

        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setCategoryList(answerCategoryFactory.getCategoryList());
        surveyForm.setSurveyFormCode("TEST-CREATE-CREDENTIALS");
        surveyForm.setSurveyDate(LocalDate.now());
        surveyForm.setPdv(1L);

        for (AnswerRow answerRow : answerRowArrayList) {
            surveyForm.setCategoryData(answerRow, processExcelFileResult);
        }
        //surveyForm.isValid(processExcelFileResult);
        //log.info(surveyForm.toString());
        //log.info(processExcelFileResult.toString());
        return surveyForm;
    }

    private Optional<CredentialState> createCredentialStateActiveMock(){
        CredentialState credentialState = new CredentialState();
        credentialState.setId(1L);
        credentialState.setStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        return Optional.of(credentialState);
    }

    private Person createPersonMock(){
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("PepeMock");
        person.setLastName("GrilloMock");
        person.setDocumentNumber(99999999L);
        return person;
    }

    @Test
    public void getActiveCredentials() {
        when(credentialRepository.findCredentialsWithFilter(null,null,null, null, null, null, Arrays.asList("Vigente"), null)).thenReturn((List<Credential>) credentialsFilteredActiveMock());

        List<Credential> credentials = credentialService.findCredentials(null,null,null, null, null, null, Arrays.asList("Vigente"), null);

        verify(credentialRepository).findCredentialsWithFilter(null,null,null, null, null, null,Arrays.asList("Vigente"), null);

        //List<CredentialDto> credentialsDto = credentials.stream().map(aCredential -> new CredentialDto(aCredential)).collect(Collectors.toList());
        log.info("credenciales " +credentials.toString());


        Assertions.assertTrue(credentials.size() == credentialsFilteredActiveMock().size()); // check if the amount of credentials filtered in the service is the correct one
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getId() ,credentials.get(0).getId());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getCredentialState().getStateName(), credentials.get(0).getCredentialState().getStateName());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getCreditHolder().getDocumentNumber() ,credentials.get(0).getCreditHolder().getDocumentNumber());
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getIdDidiCredential() ,credentials.get(0).getIdDidiCredential());
        Assertions.assertTrue(credentials.get(0).getDateOfRevocation() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        Assertions.assertEquals(credentialsFilteredActiveMock().get(0).getCreditHolder().getFirstName() ,credentials.get(0).getCreditHolder().getFirstName());
    }


    @Test
    public void getRevokedCredentials() {
        when(credentialRepository.findCredentialsWithFilter(null,null,null, null, null, null, Arrays.asList("Revocada"), null)).thenReturn((List<Credential>) credentialsFilteredRevokedMock());

        List<Credential> credentials = credentialService.findCredentials(null,null,null, null, null, null, Arrays.asList("Revocada"), null);

        verify(credentialRepository).findCredentialsWithFilter(null,null,null, null, null, null,Arrays.asList("Revocada"), null);

        log.info("credenciales " +credentials.toString());


        Assertions.assertTrue(credentials.size() == credentialsFilteredRevokedMock().size()); // check if the amount of credentials filtered in the service is the correct one
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getId() ,credentials.get(0).getId());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getCredentialState().getStateName(), credentials.get(0).getCredentialState().getStateName());
        //Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getCreditHolder().getDocumentNumber() ,credentials.get(0).getCreditHolder().getDocumentNumber());
        Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getIdDidiCredential() ,credentials.get(0).getIdDidiCredential());
        Assertions.assertTrue(credentials.get(0).getDateOfRevocation() != null);
        Assertions.assertTrue(credentials.get(0).getDateOfIssue() != null);
        //Assertions.assertEquals(credentialsFilteredRevokedMock().get(0).getCreditHolder().getFirstName() ,credentials.get(0).getCreditHolder().getFirstName());
    }

    @Test
    public void buildAllCredentialsFromFormOK() throws InvalidRowException {
        log.info("buildAllCredentialsFromFormOK");
        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        SurveyForm surveyForm = createSurveyFormMock(createAnswerRowListMock(), processExcelFileResult);

        when(credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode())).thenReturn(createCredentialStateActiveMock());
        when(personRepository.findByDocumentNumber(any(Long.class))).thenReturn(Optional.of(new Person()));
        when(personRepository.save(any(Person.class))).thenReturn(createPersonMock());

        credentialService.buildAllCredentialsFromForm(surveyForm, processExcelFileResult);

        Assertions.assertEquals(processExcelFileResult.getErrorRows().size(), 0);
    }

    @Test
    public void buildAllCredentialsDetectDuplicatedCredential() throws InvalidRowException {
        log.info("buildAllCredentialsDetectDuplicatedCredential");
        ProcessExcelFileResult processExcelFileResult = new ProcessExcelFileResult();
        SurveyForm surveyForm = createSurveyFormMock(createAnswerRowListMock(), processExcelFileResult);

        when(credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode())).thenReturn(createCredentialStateActiveMock());
        when(personRepository.findByDocumentNumber(any(Long.class))).thenReturn(Optional.of(new Person()));
        when(personRepository.save(any(Person.class))).thenReturn(createPersonMock());

        when(credentialRepository.findByBeneficiaryDniAndAndCredentialCategoryAndCredentialState(
                anyLong(),//beneficiaryDni,
                anyString(),//credentialCategoryCode,
                any(CredentialState.class)//credentialStateActive
        )).thenReturn(Optional.of(credentialsFilteredActiveMock().get(1)));

        log.info(credentialsFilteredActiveMock().get(1).toString());

        credentialService.buildAllCredentialsFromForm(surveyForm, processExcelFileResult);

        log.info(processExcelFileResult.toString());

        Assertions.assertEquals(processExcelFileResult.getErrorRows().size(), 3);
        Assertions.assertEquals(processExcelFileResult.getErrorRows().get(0).getErrorHeader(), "Warning CREDENCIAL DUPLICADA");

    }

}
