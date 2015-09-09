/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
// START SNIPPET: service
package com.tests.hl7.cxf.jaxws;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.jws.WebMethod;
import javax.jws.WebService;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.Parser;

@WebService(endpointInterface = "com.tests.hl7.cxf.jaxws.MySOAP")
public class MySOAPImpl implements MySOAP {

	@Override
	@WebMethod
    public String saveHL7(byte[] bytes) {
    	
    	if (bytes == null)
			return "data is null";
    	
    	String msg = new String (bytes);
    	
    	if (msg.isEmpty())
			return "data is empty";

		String error = "";

		HapiContext ctx = new DefaultHapiContext();

		Parser p = ctx.getGenericParser();
		Message hapiMsg = null;

		try {
			hapiMsg = p.parse(msg);
		} catch (EncodingNotSupportedException e) {
			e.printStackTrace();
			error = "EncodingNotSupportedException";
		} catch (HL7Exception e) {
			e.printStackTrace();
			error = "HL7Exception";
		}

		if (!error.isEmpty())
			return error;

		try {
			serializeMsg(hapiMsg);
		} catch (IOException e) {
			e.printStackTrace();
			error = "IOException";
		}

		if (!error.isEmpty())
			return error;
		else
			return "1";
    }
    
    
    private void serializeMsg(Message msg) throws IOException {
		long curTime = System.currentTimeMillis(); 
		String curStringDate = new SimpleDateFormat("ddMMyyyy_HHmmss").format(curTime); 
		FileOutputStream fos = new FileOutputStream("hl7msg" + curStringDate);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(msg);
		oos.flush();
		oos.close();
	}
}
