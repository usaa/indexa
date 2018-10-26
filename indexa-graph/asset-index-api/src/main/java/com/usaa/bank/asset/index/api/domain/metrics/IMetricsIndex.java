package com.usaa.bank.asset.index.api.domain.metrics;

import com.usaa.bank.asset.index.api.domain.asset.IAssetIndex;

public interface IMetricsIndex extends IAssetIndex<IMetrics> {

    IMetrics create(String thirdPartyRestResource, String demaRestResource, String thirdPartySoapService, String demaSoapService, String totalRestService, String totalSoapService);

    IMetrics get(String key);

}
