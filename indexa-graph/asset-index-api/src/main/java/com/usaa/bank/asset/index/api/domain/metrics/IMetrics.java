package com.usaa.bank.asset.index.api.domain.metrics;

import com.usaa.bank.asset.index.api.domain.asset.IDigitalAsset;

public interface IMetrics extends IDigitalAsset {

    /**
     * Get the count of the third-party soap services present in the WWW
     *
     * @return count of third-party soap services
     */
    String getThirdPartySoapService();

    /**
     * Get the count of the dema soap services present in the WWW
     *
     * @return count of usaa soap services
     */
    String getDeMaSoapService();

    /**
     * Get the count of the third-party rest resources present in the WWW
     *
     * @return count of third-party rest resources
     */
    String getThirdPartyRestResource();

    /**
     * Get the count of the third-party rest resources present in the WWW
     *
     * @return count of usaa rest resources
     */
    String getDeMaRestResource();

    /**
     * Get the count of the third-party rest resources present in the WWW
     *
     * @return total number of soap services
     */
    String getTotalSoapService();

    /**
     * Get the count of the third-party rest resources present in the WWW
     *
     * @return total number of rest resources
     */
    String getTotalRestResources();

}
