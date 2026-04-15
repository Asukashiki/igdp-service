package com.inspur.seed.service;

import java.util.List;
import java.util.Map;

public interface IDetectionCertificateService {

    void syncApprovedCertificates();

    Map<String, String> getCertificateIdMap(List<String> batchIds);

    String getCertificateId(String batchId, String seedClass, String cropType);
}
