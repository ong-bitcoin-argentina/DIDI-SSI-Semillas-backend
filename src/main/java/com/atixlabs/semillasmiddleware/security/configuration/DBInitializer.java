package com.atixlabs.semillasmiddleware.security.configuration;

import com.atixlabs.semillasmiddleware.app.didi.model.CertTemplate;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderCategoryRepository;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.model.credentialState.RevocationReason;
import com.atixlabs.semillasmiddleware.app.model.credentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.repository.ProcessControlRepository;
import com.atixlabs.semillasmiddleware.app.repository.CertTemplateRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialStateRepository;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import com.atixlabs.semillasmiddleware.security.dto.UserEditRequest;
import com.atixlabs.semillasmiddleware.security.exceptions.ExistUserException;
import com.atixlabs.semillasmiddleware.security.model.Role;
import com.atixlabs.semillasmiddleware.security.repository.PermissionRepository;
import com.atixlabs.semillasmiddleware.security.model.Permission;
import com.atixlabs.semillasmiddleware.security.repository.MenuRepository;
import com.atixlabs.semillasmiddleware.security.model.Menu;
import com.atixlabs.semillasmiddleware.security.service.RoleService;
import com.atixlabs.semillasmiddleware.security.service.UserService;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class DBInitializer implements CommandLineRunner {

    public static final String MENU_VIEW_PROFILE = "VIEW_PROFILE";

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionRepository permissionRepository;
    private final MenuRepository menuRepository;
    private final CredentialStateRepository credentialStateRepository;
    private final ParameterConfigurationRepository parameterConfigurationRepository;
    private final RevocationReasonRepository revocationReasonRepository;
    private final ProcessControlRepository processControlRepository;
    private final ProviderCategoryRepository providerCategoryRepository;
    private final CertTemplateRepository certTemplateRepository;

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
    @Value("${semillas.username}")
    private String semillasUserName;
    @Value("${semillas.email}")
    private String semillasEmail;
    @Value("${semillas.password}")
    private String semillasPassword;
    @Value("${semillas.name}")
    private String semillasName;
    @Value("${semillas.lastname}")
    private String semillasLastName;

    @Autowired
    public DBInitializer(UserService userService, RoleService roleService, PermissionRepository permissionRepository,
                         MenuRepository menuRepository, CredentialStateRepository credentialStateRepository,
                         ParameterConfigurationRepository parameterConfigurationRepository,
                         RevocationReasonRepository revocationReasonRepository,
                         ProcessControlRepository processControlRepository, CertTemplateRepository certTemplateRepository,
                         ProviderCategoryRepository providerCategoryRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionRepository = permissionRepository;
        this.menuRepository = menuRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
        this.revocationReasonRepository = revocationReasonRepository;
        this.processControlRepository = processControlRepository;
        this.certTemplateRepository = certTemplateRepository;
        this.providerCategoryRepository = providerCategoryRepository;
    }

   @Override
    public void run(String... args) throws Exception {
        saveRolesAndPermissions();
        createUser();

        Optional<Menu> menu = menuRepository.findByCode("USERS");
        if (menu.isPresent()) {
            if (menu.get().getName().contains("Profile")) {
                menu.get().setName("My account");
                menuRepository.save(menu.get());
            }
            if (menuRepository.findByCode(MENU_VIEW_PROFILE).isPresent()) {
                menuRepository.save(menu.get());
            }
        }
        //Credential States
        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(
                CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        if (credentialStateOptional.isEmpty()) {
            credentialStateRepository.save(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        }
        Optional<CredentialState> credentialStateRevoke = credentialStateRepository.findByStateName(
                CredentialStatesCodes.CREDENTIAL_REVOKE.getCode());
        if (credentialStateRevoke.isEmpty()) {
            credentialStateRepository.save(new CredentialState(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode()));
        }
        Optional<CredentialState> credentialStatePending = credentialStateRepository.findByStateName(
                CredentialStatesCodes.PENDING_DIDI.getCode());
        if (credentialStatePending.isEmpty()) {
            credentialStateRepository.save(new CredentialState(CredentialStatesCodes.PENDING_DIDI.getCode()));
        }
        String holderActiveKinsmanPendingCode = CredentialStatesCodes.HOLDER_ACTIVE_KINSMAN_PENDING.getCode();
        Optional<CredentialState> credentialHolderActiveKinsmanPending = credentialStateRepository.findByStateName(
                holderActiveKinsmanPendingCode);
        if (credentialHolderActiveKinsmanPending.isEmpty()) {
            credentialStateRepository.save(new CredentialState(holderActiveKinsmanPendingCode));
        }

        if(!parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.MAX_EXPIRED_AMOUNT.getCode())
                .isPresent()){
            ParameterConfiguration configuration = new ParameterConfiguration();
            configuration.setValue("500");
            configuration.setConfigurationName(ConfigurationCodes.MAX_EXPIRED_AMOUNT.getCode());
            parameterConfigurationRepository.save(configuration);
        }

        if(!parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode())
                .isPresent()){
            ParameterConfiguration configuration = new ParameterConfiguration();
            configuration.setValue("1234567890");
            configuration.setConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode());
            parameterConfigurationRepository.save(configuration);
        }

        saveRevocationCodes();

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
	
        this.loadCertTemplatesValues();
        this.saveCategories();
    }

    private void createUser() throws ExistUserException {
        if (userService.findByUsername(semillasUserName).isEmpty()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername(semillasUserName);
            userResponse.setEmail(semillasEmail);
            userResponse.setPassword(semillasPassword);
            userResponse.setNewPassword(semillasPassword);
            userResponse.setConfirmNewPassword(semillasPassword);
            userResponse.setRole(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role());
            userResponse.setName(semillasName);
            userResponse.setLastName(semillasLastName);
            userService.createOrEdit(userResponse);
        }
    }

    private void saveRolesAndPermissions(){
        if (roleService.findByCode(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role()) == null) {

            menuRepository.deleteAll();
            Menu mainUserMenu = menuRepository.save(Menu.builder().name("Profile").code("USERS").order(3).build());
            Menu viewUserMenu = menuRepository.save(Menu.builder().name("View Profile").code(MENU_VIEW_PROFILE)
                    .parent(mainUserMenu).uri("/user-information").order(0).type("GET").build());
            Menu logoutMenu = menuRepository.save(Menu.builder().name("Logout").code("LOGOUT").parent(mainUserMenu)
                    .order(1).uri("/login").type("GET").build());
            Menu manageUsersMenu = menuRepository.save(Menu.builder().name("Manage Users").code("MANAGE_USERS").order(2)
                    .uri("/administration").type("GET").build());

            Permission viewProfilePermission = permissionRepository.save(Permission.builder().code(MENU_VIEW_PROFILE)
                    .description("Permiso ver perfil").build());
            Permission createUserPermission = permissionRepository.save(Permission.builder().code("CREATE_USER")
                    .description("Permiso crear usuario").build());
            Permission viewListUserPermission = permissionRepository.save(Permission.builder().code("LIST_USERS")
                    .description("Permiso Listar usuarios").build());
            Permission modifyUserPermission = permissionRepository.save(Permission.builder().code("MODIFY_USERS")
                    .description("Permiso Modificar usuarios").build());

            roleService.save(Role.builder().code(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_ADMIN.role())
                    .description("Administrator")
                    .menus(Sets.newHashSet(mainUserMenu, manageUsersMenu, viewUserMenu, logoutMenu))
                    .permissions(Sets.newHashSet(createUserPermission, modifyUserPermission, viewListUserPermission,
                            viewProfilePermission))
                    .build());
            roleService.save(Role.builder()
                    .code(com.atixlabs.semillasmiddleware.security.enums.Role.ROLE_VIEWER.role())
                    .description("Viewer")
                    .menus(Sets.newHashSet(mainUserMenu, viewUserMenu, logoutMenu))
                    .permissions(Sets.newHashSet(modifyUserPermission, viewProfilePermission))
                    .build());
        }
    }

    private void saveRevocationCodes() {
        String[] revocationNames = Arrays.stream(RevocationReasonsCodes.values()).map(RevocationReasonsCodes::getCode)
                .toArray(String[]::new);
        for (String revocationName : revocationNames) {
            if (revocationReasonRepository.findByReason(revocationName).isEmpty())
                revocationReasonRepository.save(new RevocationReason(revocationName));
        }
    }

    private void saveCategories(){
        String[] categories = new String[]{"Salud", "Oportunidad", "Saber", "Sueño", "Finanza"};
        for (String category : categories) {
            if(providerCategoryRepository.findByName(category).isEmpty())
                providerCategoryRepository.save(new ProviderCategory(category));
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
        Optional<CertTemplate> certTemplate =  certTemplateRepository
                .findByCredentialCategoriesCodes(credentialCategoriesCodes);
        return certTemplate.isPresent();
    }
}
