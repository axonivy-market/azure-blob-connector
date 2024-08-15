package com.axonivy.connector.azure.blob.internal.helper;

import java.time.Duration;
import java.time.OffsetDateTime;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.UserDelegationKey;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

public class BlobSASHelper {
		
	public static String createServiceSASBlob(BlobClient blobClient, Duration liveTime) {	
		
		OffsetDateTime now = OffsetDateTime.now();		
        UserDelegationKey key = blobClient.getBlockBlobClient()
        		.getContainerClient()
        		.getServiceClient()
        		.getUserDelegationKey(now.minusMinutes(1), now.plusSeconds(liveTime.getSeconds()));

        OffsetDateTime expiryTime = OffsetDateTime.now().plusSeconds(liveTime.toSeconds());
        
		// Assign read permissions to the SAS token
		BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

		var sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
				.setStartTime(now.minusMinutes(1));

		String sasToken = blobClient.generateUserDelegationSas(sasSignatureValues, key);
		return sasToken;
	}
}
