/*
 * Copyright (C) 2010 - 2012 Jenia Software.
 *
 * This file is part of Sinekarta
 *
 * Sinekarta is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sinekarta is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */
package org.sinekartads.share.webscripts.sign;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.sinekartads.dto.domain.DocumentDTO;
import org.sinekartads.dto.domain.SignatureDTO;
import org.sinekartads.dto.domain.TimeStampRequestDTO;
import org.sinekartads.dto.request.SkdsDocumentDetailsRequest;
import org.sinekartads.dto.response.SkdsDocumentDetailsResponse;
import org.sinekartads.dto.share.SignWizardDTO;
import org.sinekartads.model.domain.SignatureType.SignCategory;
import org.sinekartads.share.util.AlfrescoException;

public class SkdsSignInitWS extends BaseSignController {
	
	@Override
	protected void processData ( 
			SignWizardDTO signWizard ) 
					throws AlfrescoException {
		
		DocumentDTO[] documents = signWizard.getDocuments();
		SignatureDTO signature = signWizard.getSignature();
		String[] nodeRefs = signWizard.getNodeRefs();
		
		
		signWizard.setNodeRefs(nodeRefs);	
		
		// Retrieve from the repository-tier the document details
		SkdsDocumentDetailsRequest ddreq = new SkdsDocumentDetailsRequest();
		ddreq.setNodeRefs(nodeRefs);
		SkdsDocumentDetailsResponse ddresp = postJsonRequest ( ddreq, SkdsDocumentDetailsResponse.class );
		documents = ddresp.documentsFromBase64();
		signWizard.setDocuments(documents);
		
		// Evaluate the general mimeType and extract the document baseName and extension 
		DocumentDTO document;
		String fileName;
		String baseName;
		String extension;
		String mimetype = null;
		boolean sameType = true;
		for ( int i=0; i<documents.length; i++ ) {
			document = documents[i];
			if ( i == 0 ) {
				mimetype = document.getBaseDocument().getMimetype();
			} else {
				sameType &= StringUtils.equalsIgnoreCase ( 
						mimetype, document.getBaseDocument().getMimetype() );
			}
			
			fileName = document.getBaseDocument().getFileName(); 
			baseName = FilenameUtils.getBaseName ( fileName );
			extension =  FilenameUtils.getExtension ( fileName );
			
			document.setBaseName(baseName);
			document.setExtension(extension);
		}
		
		// Choose the "octet-stream" mimeType if there is more than one type 
		if ( !sameType ) {
			mimetype = "application/octet-stream";
		}
		signWizard.setMimetype(mimetype);
		
		// Evaluate the best signatureType for the mimetypes to be signed
		SignCategory signCategory;
		if ( mimetype.equals("application/pdf") ) {
			signCategory = SignCategory.PDF;
		} else if ( mimetype.equals("text/xml") ) {
			signCategory = SignCategory.XML;
		} else {
			signCategory = SignCategory.CMS;
		}
		
		// Init the default tsCredential
		TimeStampRequestDTO timeStampRequest = signature.getTimeStampRequest(); 
		timeStampRequest.setTsUrl ( conf.getTsaUrl() );
		timeStampRequest.setTsUsername ( conf.getTsaUser() );
		timeStampRequest.setTsPassword ( conf.getTsaPassword() );
		
		// Create a default signature
		signWizard.getSignature().signCategoryToString(signCategory);
		signature.digestAlgorithmToName ( conf.getDigestAlgorithm() );
		signature.signAlgorithmToString ( conf.getSignatureAlgorithm() );
		signature.signingTimeToString(new Date());
		signature.setLocation(getMessage("signature.location"));
		signature.setReason(getMessage("signature.reason"));
		
		// Set the known driver names and descriptions
		signWizard.setScDriverNames(conf.getDriverNames());
		signWizard.setScDriverDescriptions(conf.getDriverDescriptions());
		
		// set KEYSTORE as default clientType
		signWizard.setClientType("KEYSTORE");
	}
	
	@Override
	protected int currentStep() {
		return STEP_INIT;
	}
}
