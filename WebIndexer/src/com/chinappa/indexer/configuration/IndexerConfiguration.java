package com.chinappa.indexer.configuration;

import java.util.ResourceBundle;

import com.chinappa.indexer.constant.IndexerConstants;
import com.chinappa.information.retrieval.util.FileHandlerUtil;

public class IndexerConfiguration {

	private static IndexerConfiguration uniqueInstance = null;
	/**
	 * The following attribute represents the location of index.
	 */
	private String indexLocation = null;
	/**
	 * The following attribute represents the location of crawled documents.
	 */
	private String documentDirectory = null;
	/**
	 * The following attribute represents if the compression is enabled.
	 */
	private boolean isCompressionEnabled = false;
	
	private IndexerConfiguration(){
		init();
	}

	/**
	 * The following method returns the unique instance.
	 * 
	 * @return
	 */
	public static IndexerConfiguration getInstance() {
		if (uniqueInstance == null) {
			synchronized (IndexerConfiguration.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new IndexerConfiguration();
				}
			}
		}
		return uniqueInstance;
	}

	/**
	 * The following method initializes the parameters required to run the
	 * crawler. The parameters are read from a property file stored in config
	 * folder. In case of a missing parameter or any errors while reading, the
	 * parameters are initialized with their default values.
	 */
	private void init() {
		ResourceBundle rb = ResourceBundle
				.getBundle(IndexerConstants.CONFIGURATION_FILENAME);
		String param = IndexerConstants.RB_INDEX_DIRECTORY;
		indexLocation = FileHandlerUtil.readStringFromResourceBundle(rb, param);
		param = IndexerConstants.RB_DOCUMENT_DIRECTORY;
		documentDirectory = FileHandlerUtil.readStringFromResourceBundle(rb,
				param);
		param = IndexerConstants.RB_COMPRESSION_ENABLED;
		isCompressionEnabled = FileHandlerUtil.readBooleanFromResourceBundle(rb, param);
	}

	public String getIndexLocation() {
		return indexLocation;
	}

	public void setIndexLocation(String indexLocation) {
		this.indexLocation = indexLocation;
	}

	public String getDocumentDirectory() {
		return documentDirectory;
	}

	public void setDocumentDirectory(String documentDirectory) {
		this.documentDirectory = documentDirectory;
	}

	public boolean isCompressionEnabled() {
		return isCompressionEnabled;
	}

	public void setCompressionEnabled(boolean isCompressionEnabled) {
		this.isCompressionEnabled = isCompressionEnabled;
	}
}
