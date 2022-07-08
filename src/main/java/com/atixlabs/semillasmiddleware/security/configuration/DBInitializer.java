package com.atixlabs.semillasmiddleware.security.configuration;

import com.atixlabs.semillasmiddleware.app.didi.model.CertTemplate;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.RevocationReason;
import com.atixlabs.semillasmiddleware.app.model.CredentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderCategoryRepository;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.repository.CertTemplateRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialStateRepository;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import com.atixlabs.semillasmiddleware.security.model.Role;
import org.codehaus.plexus.util.StringUtils;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.repository.ProcessControlRepository;
import com.atixlabs.semillasmiddleware.security.dto.UserEditRequest;
import com.atixlabs.semillasmiddleware.security.exceptions.ExistUserException;
import com.atixlabs.semillasmiddleware.security.exceptions.InexistentUserException;
import com.atixlabs.semillasmiddleware.security.repository.PermissionRepository;
import com.atixlabs.semillasmiddleware.security.model.Permission;
import com.atixlabs.semillasmiddleware.security.repository.MenuRepository;
import com.atixlabs.semillasmiddleware.security.model.Menu;
import com.atixlabs.semillasmiddleware.security.service.RoleService;
import com.atixlabs.semillasmiddleware.security.service.UserService;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import com.google.common.collect.Sets;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DBInitializer implements CommandLineRunner {

    private UserService userService;

    private RoleService roleService;

    private PermissionRepository permissionRepository;

    private MenuRepository menuRepository;

    private CredentialStateRepository credentialStateRepository;

    private ParameterConfigurationRepository parameterConfigurationRepository;

    private RevocationReasonRepository revocationReasonRepository;

    private ProcessControlRepository processControlRepository;

    private String enviroment;

    private ProviderCategoryRepository providerCategoryRepository;

private CertTemplateRepository certTemplateRepository;

    @Value("${didi.semillas.template_code_identity}")
    private String didiTemplateCodeIdentity;

    @Value("${didi.semillas.template_code_entrepreneurship}")
    private String didiTemplateCodeEntrepreneurship;

    @Value("${didi.semillas.template_code_dwelling}")
    private String didiTemplateCodeDwelling;

    @Value("${didi.semillas.template_code_benefit}")
    private String didiTemplateCodeBenefit;

    @Value("${didi.semillas.template_code_credit}")
    private String didiTemplateCodeCredit;

    @Value("${didi.semillas.template_code_sancor_salud}")
    private String didiTemplateCodeSancorSalud;

    @Autowired
    public DBInitializer(UserService userService, RoleService roleService, PermissionRepository permissionRepository, MenuRepository menuRepository, CredentialStateRepository credentialStateRepository, ParameterConfigurationRepository parameterConfigurationRepository, RevocationReasonRepository revocationReasonRepository, ProcessControlRepository processControlRepository, @Value("${deploy.enviroment}") String enviroment, CertTemplateRepository certTemplateRepository, ProviderCategoryRepository providerCategoryRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionRepository = permissionRepository;
        this.menuRepository = menuRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
        this.revocationReasonRepository = revocationReasonRepository;
        this.processControlRepository = processControlRepository;
        this.enviroment = enviroment;
        this.certTemplateRepository = certTemplateRepository;
        this.providerCategoryRepository = providerCategoryRepository;
    }

   @Override
    public void run(String... args) throws Exception {
        if (roleService.findByCode(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role()) == null) {
            menuRepository.deleteAll();
            Menu mainUserMenu =
                    menuRepository.save(
                            Menu.builder().name("Profile").code("USERS").order(3).build());
            Menu viewUserMenu =
                    menuRepository.save(
                            Menu.builder()
                                    .name("View Profile")
                                    .code(Constants.VIEW_PROFILE)
                                    // .icon("user")
                                    .parent(mainUserMenu)
                                    .uri("/user-information")
                                    .order(0)
                                    .type("GET")
                                    .build());
            Menu logoutMenu =
                    menuRepository.save(
                            Menu.builder()
                                    .name("Logout")
                                    .code("LOGOUT")
                                    .parent(mainUserMenu)
                                    .order(1)
                                    .uri("/login")
                                    .type("GET")
                                    .build());

            Menu manageUsersMenu =
                    menuRepository.save(
                            Menu.builder()
                                    .name("Manage Users")
                                    .code("MANAGE_USERS")
                                    .order(2)
                                    .uri("/administration")
                                    .type("GET")
                                    .build());

            Permission viewProfilePermission =
                    permissionRepository.save(
                            Permission.builder().code(Constants.VIEW_PROFILE).description("Permiso ver perfil").build());

            Permission createUserPermission =
                    permissionRepository.save(
                            Permission.builder()
                                    .code("CREATE_USER")
                                    .description("Permiso crear usuario")
                                    .build());

            Permission viewListUserPermission =
                    permissionRepository.save(
                            Permission.builder()
                                    .code("LIST_USERS")
                                    .description("Permiso Listar usuarios")
                                    .build());

            Permission modifyUserPermission =
                    permissionRepository.save(
                            Permission.builder()
                                    .code("MODIFY_USERS")
                                    .description("Permiso Modificar usuarios")
                                    .build());

            Role roleAdmin =
                    roleService.save(
                            Role.builder()
                                    .code(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role())
                                    .description("Administrator")
                                    .menus(Sets.newHashSet(mainUserMenu, manageUsersMenu, viewUserMenu, logoutMenu))
                                    .permissions(
                                            Sets.newHashSet(
                                                    createUserPermission,
                                                    modifyUserPermission,
                                                    viewListUserPermission,
                                                    viewProfilePermission))
                                    .build());
            Role roleViewer =
                    roleService.save(
                            Role.builder()
                                    .code(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_VIEWER.role())
                                    .description(Constants.VIEWER)
                                    .menus(Sets.newHashSet(mainUserMenu, viewUserMenu, logoutMenu))
                                    .permissions(Sets.newHashSet(modifyUserPermission, viewProfilePermission))
                                    .build());
        }

        if(enviroment!=null && enviroment.equals("production")){
            this.createProductionUsers();
        }else
            this.createUsers();

        Optional<Menu> menu = menuRepository.findByCode("USERS");
        if (menu.isPresent() && menu.get().getName().contains("Profile")) {
            menu.get().setName("My account");
            menuRepository.save(menu.get());
        }

        if (menuRepository.findByCode(Constants.VIEW_PROFILE).isPresent()) {
            menuRepository.save(menu.orElse(new Menu()));
        }

        //Credential States
        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        if (credentialStateOptional.isEmpty()) {
            credentialStateRepository.save(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        }
        Optional<CredentialState> credentialStateRevoke = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode());
        if (credentialStateRevoke.isEmpty()) {
            credentialStateRepository.save(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        }

        Optional<CredentialState> credentialStatePending = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
        if (credentialStatePending.isEmpty()) {
            credentialStateRepository.save(new CredentialState(CredentialStatesCodes.PENDING_DIDI.getCode()));
        }

       String holderActiveKinsmanPendingCode = CredentialStatesCodes.HOLDER_ACTIVE_KINSMAN_PENDING.getCode();
       Optional<CredentialState> credentialHolderActiveKinsmanPending = credentialStateRepository.findByStateName(holderActiveKinsmanPendingCode);
        if (credentialHolderActiveKinsmanPending.isEmpty()) {
           credentialStateRepository.save(new CredentialState(holderActiveKinsmanPendingCode));
        }

        if(!parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.MAX_EXPIRED_AMOUNT.getCode()).isPresent()){
            ParameterConfiguration configuration = new ParameterConfiguration();
            configuration.setValue("500");
            configuration.setConfigurationName(ConfigurationCodes.MAX_EXPIRED_AMOUNT.getCode());
            parameterConfigurationRepository.save(configuration);
        }

        if(!parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode()).isPresent()){
            ParameterConfiguration configuration = new ParameterConfiguration();
            configuration.setValue("1234567890");
            configuration.setConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode());
            parameterConfigurationRepository.save(configuration);
        }

        //revocation reasons
       saveRevocationCodes();

        setProcessControlRepository();
	
	    this.loadCertTemplatesValues();
        this.saveCategories();
    }

    private void setProcessControlRepository(){
        if(processControlRepository.findByProcessName(ProcessNamesCodes.BONDAREA.getCode()).isEmpty()){
            ProcessControl process = new ProcessControl();
            process.setProcessName(ProcessNamesCodes.BONDAREA.getCode());
            process.setStatus(ProcessControlStatusCodes.OK.getCode());
            processControlRepository.save(process);
        }
        if(processControlRepository.findByProcessName(ProcessNamesCodes.CREDENTIALS.getCode()).isEmpty()){
            ProcessControl process = new ProcessControl();
            process.setProcessName(ProcessNamesCodes.CREDENTIALS.getCode());
            process.setStatus(ProcessControlStatusCodes.OK.getCode());
            //set an initial time (then will be use to compare)
            process.setStartTime(DateUtil.getLocalDateTimeNowWithFormat("yyyy-MM-dd HH:mm:ss"));
            processControlRepository.save(process);
        }
        if(processControlRepository.findByProcessName(ProcessNamesCodes.CHECK_DEFAULTERS.getCode()).isEmpty()){
            ProcessControl process = new ProcessControl();
            process.setProcessName(ProcessNamesCodes.CHECK_DEFAULTERS.getCode());
            process.setStatus(ProcessControlStatusCodes.OK.getCode());
            //set an initial time (then will be use to compare)
            process.setStartTime(DateUtil.getLocalDateTimeNowWithFormat("yyyy-MM-dd HH:mm:ss"));
            processControlRepository.save(process);
        }
    }

    private void saveRevocationCodes() {
        String[] revocationNames = Arrays.stream(RevocationReasonsCodes.values()).map(RevocationReasonsCodes::getCode).toArray(String[]::new);
        for (String revocationName : revocationNames) {
            if (!revocationReasonRepository.findByReason(revocationName).isPresent()) {
                revocationReasonRepository.save(new RevocationReason(revocationName));
            }
        }
    }

    private void saveCategories(){
        String[] categories = new String[]{"Salud", "Oportunidad", "Saber", "Sueño", "Finanza"};
        for (String category : categories) {
            if(!providerCategoryRepository.findByName(category).isPresent()) {
                providerCategoryRepository.save(new ProviderCategory(category));
            }
        }


    }

    private void createUsers() throws ExistUserException, InexistentUserException {
        //TODO change pass
        if (!userService.findByUsername(Constants.ADMIN_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.ADMIN_MAIL);
            userResponse.setEmail(Constants.ADMIN_MAIL);
            userResponse.setPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setNewPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setConfirmNewPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(Constants.ADMIN);
            userResponse.setLastName(Constants.ADMIN);
            userService.createOrEdit(userResponse);
        }
//TODO users only for test, delete
        if (!userService.findByUsername(Constants.FLOR_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.FLOR_MAIL);
            userResponse.setEmail(Constants.FLOR_MAIL);
            userResponse.setPassword("flor");
            userResponse.setNewPassword("flor");
            userResponse.setConfirmNewPassword("flor");
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName("Florencia");
            userResponse.setLastName("Atix");
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.FACU_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.FACU_MAIL);
            userResponse.setEmail(Constants.FACU_MAIL);
            userResponse.setPassword("facu");
            userResponse.setNewPassword("facu");
            userResponse.setConfirmNewPassword("facu");
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName("Facundo");
            userResponse.setLastName("Atix");
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.TAMARA_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.TAMARA_MAIL);
            userResponse.setEmail(Constants.TAMARA_MAIL);
            userResponse.setPassword(Constants.USER_TAMARA);
            userResponse.setNewPassword(Constants.USER_TAMARA);
            userResponse.setConfirmNewPassword(Constants.USER_TAMARA);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName("Tamara");
            userResponse.setLastName("Semillas");
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.CRON_USER_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.CRON_USER_MAIL);
            userResponse.setEmail(Constants.CRON_USER_MAIL);
            userResponse.setPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setNewPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setConfirmNewPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(Constants.USER_CRON);
            userResponse.setLastName(Constants.USER_CRON);
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.DIDI_USER_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.DIDI_USER_MAIL);
            userResponse.setEmail(Constants.DIDI_USER_MAIL);
            userResponse.setPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setNewPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setConfirmNewPassword(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(Constants.USER_DIDI);
            userResponse.setLastName(Constants.USER_DIDI);
            userService.createOrEdit(userResponse);
        }
        if (!userService.findByUsername(Constants.VIEWER_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.VIEWER_MAIL);
            userResponse.setEmail(Constants.VIEWER_MAIL);
            userResponse.setPassword(StringUtils.uncapitalise(Constants.VIEWER));
            userResponse.setNewPassword(StringUtils.uncapitalise(Constants.VIEWER));
            userResponse.setConfirmNewPassword(StringUtils.uncapitalise(Constants.VIEWER));
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_VIEWER.role());
            userResponse.setName(Constants.VIEWER);
            userResponse.setLastName(Constants.VIEWER);
            userService.createOrEdit(userResponse);
        }
    }


    private void createProductionUsers() throws ExistUserException, InexistentUserException {
        //TODO change pass
        if (!userService.findByUsername(StringUtils.uncapitalise(Constants.ADMIN)).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(StringUtils.uncapitalise(Constants.ADMIN));
            userResponse.setEmail("admin@semillas.org");
            userResponse.setPassword(Constants.PASS_ADMIN);
            userResponse.setNewPassword(Constants.PASS_ADMIN);
            userResponse.setConfirmNewPassword(Constants.PASS_ADMIN);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(Constants.ADMIN);
            userResponse.setLastName(Constants.ADMIN);
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername("atixlabs").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("atixlabs");
            userResponse.setEmail("info@atixlabs.com");
            userResponse.setPassword(Constants.PASS_ATIX);
            userResponse.setNewPassword(Constants.PASS_ATIX);
            userResponse.setConfirmNewPassword(Constants.PASS_ATIX);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName("Atix");
            userResponse.setLastName("Labs");
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.TAMARA_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.TAMARA_MAIL);
            userResponse.setEmail(Constants.TAMARA_MAIL);
            userResponse.setPassword(Constants.PASS_TAMARA);
            userResponse.setNewPassword(Constants.PASS_TAMARA);
            userResponse.setConfirmNewPassword(Constants.PASS_TAMARA);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName("Tamara");
            userResponse.setLastName("Semillas");
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.CRON_USER_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.CRON_USER_MAIL);
            userResponse.setEmail(Constants.CRON_USER_MAIL);
            userResponse.setPassword(Constants.PASS_CRON);
            userResponse.setNewPassword(Constants.PASS_CRON);
            userResponse.setConfirmNewPassword(Constants.PASS_CRON);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(Constants.USER_CRON);
            userResponse.setLastName(Constants.USER_CRON);
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername(Constants.DIDI_USER_MAIL).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(Constants.DIDI_USER_MAIL);
            userResponse.setEmail(Constants.DIDI_USER_MAIL);
            userResponse.setPassword(Constants.PASS_DIDI);
            userResponse.setNewPassword(Constants.PASS_DIDI);
            userResponse.setConfirmNewPassword(Constants.PASS_DIDI);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(Constants.USER_DIDI);
            userResponse.setLastName(Constants.USER_DIDI);
            userService.createOrEdit(userResponse);
        }
        if (!userService.findByUsername(StringUtils.uncapitalise(Constants.VIEWER)).isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(StringUtils.uncapitalise(Constants.VIEWER));
            userResponse.setEmail("viewer@semillas.com");
            userResponse.setPassword(Constants.PASS_VIEWER);
            userResponse.setNewPassword(Constants.PASS_VIEWER);
            userResponse.setConfirmNewPassword(Constants.PASS_VIEWER);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_VIEWER.role());
            userResponse.setName(Constants.VIEWER);
            userResponse.setLastName(Constants.VIEWER);
            userService.createOrEdit(userResponse);
        }
    }

/**
     *    IDENTITY("Identidad"),
     *     DWELLING("Vivienda"),
     *     ENTREPRENEURSHIP("Emprendimiento"),
     *     BENEFIT("Beneficio Semillas"),
     *     CREDIT("Crediticia");
     */
    private void loadCertTemplatesValues(){
        if(!this.isCertTemplateValueExists(CredentialCategoriesCodes.IDENTITY)){
            CertTemplate certTemplate = new CertTemplate(CredentialCategoriesCodes.IDENTITY,didiTemplateCodeIdentity,"Semillas Identidad" );
            certTemplateRepository.save(certTemplate);
        }
        if(!this.isCertTemplateValueExists(CredentialCategoriesCodes.DWELLING)){
            CertTemplate certTemplate = new CertTemplate(CredentialCategoriesCodes.DWELLING,didiTemplateCodeDwelling,"Semillas Vivienda" );
            certTemplateRepository.save(certTemplate);
        }
        if(!this.isCertTemplateValueExists(CredentialCategoriesCodes.ENTREPRENEURSHIP)){
            CertTemplate certTemplate = new CertTemplate(CredentialCategoriesCodes.ENTREPRENEURSHIP,didiTemplateCodeEntrepreneurship,"Semillas Emprendimiento" );
            certTemplateRepository.save(certTemplate);
        }
        if(!this.isCertTemplateValueExists(CredentialCategoriesCodes.CREDIT)){
            CertTemplate certTemplate = new CertTemplate(CredentialCategoriesCodes.CREDIT,didiTemplateCodeCredit,"Semillas Crediticia" );
            certTemplateRepository.save(certTemplate);
        }
        if(!this.isCertTemplateValueExists(CredentialCategoriesCodes.BENEFIT)){
            CertTemplate certTemplate = new CertTemplate(CredentialCategoriesCodes.BENEFIT,didiTemplateCodeBenefit,"Semillas Beneficio" );
            certTemplateRepository.save(certTemplate);
        }

        if(!this.isCertTemplateValueExists(CredentialCategoriesCodes.BENEFIT_SANCOR)){
            CertTemplate certTemplate = new CertTemplate(CredentialCategoriesCodes.BENEFIT_SANCOR,didiTemplateCodeSancorSalud,"Sancor Salud" );
            certTemplateRepository.save(certTemplate);
        }
    }
    private boolean isCertTemplateValueExists(CredentialCategoriesCodes credentialCategoriesCodes){

        Optional<CertTemplate> certTemplate =  certTemplateRepository.findByCredentialCategoriesCodes(credentialCategoriesCodes);
        return certTemplate.isPresent();
    }

    private static class Constants{
        public static final String ADMIN = "Admin";
        public static final String VIEWER = "Viewer";

        public static final String VIEW_PROFILE = "VIEW_PROFILE";

        //Mails
        public static final String ADMIN_MAIL = "admin@atixlabs.com";
        public static final String FLOR_MAIL = "flor@atixlabs.com";
        public static final String FACU_MAIL = "facu@atixlabs.com";
        public static final String TAMARA_MAIL = "tamara@semillas.com";
        public static final String CRON_USER_MAIL = "cronUser@atixlabs.com";
        public static final String DIDI_USER_MAIL = "didiUser@atixlabs.com";
        public static final String VIEWER_MAIL = "viewer@atixlabs.com";

        // NAMES
        public static final String USER_TAMARA = "tamara";
        public static final String USER_DIDI = "didiUser";
        public static final String USER_CRON = "cronUser";

        // PASS
        public static final String PASS_TAMARA = "tama1q2w3e4r";
        public static final String PASS_DIDI = "didi1q2w3e4r";
        public static final String PASS_CRON = "cron1q2w3e4r";
        public static final String PASS_ADMIN = "admin1q2w3e4r";
        public static final String PASS_ATIX = "atix1q2w3e4r";
        public static final String PASS_VIEWER = "viewer1q2w3e4r";
    }
}
