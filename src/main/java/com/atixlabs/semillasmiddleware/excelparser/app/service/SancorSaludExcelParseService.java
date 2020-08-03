package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import com.atixlabs.semillasmiddleware.app.sancor.service.SancorPolicyService;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SancorSaludExcelParseService extends ExcelParseService {

    private List<SancorPolicy> sancorPolicys = new ArrayList<SancorPolicy>();

    private SancorPolicyService sancorPolicyService;

    @Autowired
    public SancorSaludExcelParseService(SancorPolicyService sancorPolicyService){
        this.sancorPolicyService = sancorPolicyService;
    }

    @Override
    public ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult){

        processExcelFileResult.addTotalReadRow();

        try {
            SancorPolicy sancorPolicy = this.parseRow(currentRow);
            this.sancorPolicys.add(sancorPolicy);
            processExcelFileResult.addTotalValidRows();

        } catch (Exception e) {
            processExcelFileResult.addRowError(currentRow.getRowNum(), e.toString());
        }

        return processExcelFileResult;
    }

    public SancorPolicy parseRow(Row row) throws InvalidRowException {
        SancorPolicyRow sancorPolicyRow = new SancorPolicyRow(row);
        SancorPolicy sancorPolicy = this.buildSancorPolicy(sancorPolicyRow);

        return  sancorPolicy;
    }


    public ProcessExcelFileResult processAndSaveSingleSheetFile(String filePath) throws Exception {

        log.info("Import and save Sancor Policies form {} ", filePath);

        ProcessExcelFileResult processExcelFileResult =this.processSingleSheetFile(filePath);

        log.info("Sancor policies to save {}",(sancorPolicys!=null ? sancorPolicys.size() : 0));

        for(SancorPolicy sancorPolicy : sancorPolicys){
            this.sancorPolicyService.save(sancorPolicy);
        }

        return processExcelFileResult;
    }

    public SancorPolicy buildSancorPolicy(SancorPolicyRow sancorPolicyRow){
        SancorPolicy sancorPolicy = new SancorPolicy();
        sancorPolicy.setBranchDescription(sancorPolicyRow.getBranchDescription()!= null ? sancorPolicyRow.getBranchDescription().trim() : null);
        sancorPolicy.setIdProduct(sancorPolicyRow.getProductId());
        sancorPolicy.setPolicyNumber(sancorPolicyRow.getPolicy());
        sancorPolicy.setPolicyClient(sancorPolicyRow.getPolicyClient());
        sancorPolicy.setPolicyClientName(sancorPolicyRow.getPolicyClientName());
        sancorPolicy.setCertificateNumber(sancorPolicyRow.getCertificateNumber());
        sancorPolicy.setCertificateClient(sancorPolicyRow.getCertificateClient());
        sancorPolicy.setValidityFrom(sancorPolicyRow.getValidityFrom());
        sancorPolicy.setValidityTo(sancorPolicyRow.getValidityTo());
        sancorPolicy.setCertificateClientName(sancorPolicyRow.getCertificateClientName());
        sancorPolicy.setCertificateClientAddress(sancorPolicyRow.getCertificateClientAddress());
        sancorPolicy.setCertificateClientDni(this.parseDni(sancorPolicyRow.getCertificateClient()));
      //  private Long certificateClientDni;

        return sancorPolicy;

    }

    /**
     * expect format
     * Dnnnnnnnnnn..
     *
     * @return
     */
    public Long parseDni(String certificateClient){

        String result = certificateClient.replaceFirst("D", "").replaceFirst("^0+(?!$)", "");

        return Long.parseLong(result);
    }
}
