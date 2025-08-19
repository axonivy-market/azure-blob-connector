package com.axonivy.connector.azure.blob.internal.constant;

public final class BlobQuery {
	private BlobQuery() {
	}

	public static final class Parameter {
		public static final String COMP = "comp";
		public static final String RESTYPE = "restype";
	}

	public static final class Value {
		public static final String SERVICE = "service";
		public static final String USERDELEGATIONKEY = "userdelegationkey";
		public static final String UNDELETE = "undelete";
		public static final String CONTAINER = "container";
		public static final String PROPERTIES = "properties";		
		public static final String LIST = "list";
	}
}
