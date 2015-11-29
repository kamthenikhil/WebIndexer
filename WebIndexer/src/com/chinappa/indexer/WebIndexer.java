package com.chinappa.indexer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;

import com.chinappa.indexer.configuration.IndexerConfiguration;
import com.chinappa.information.retrieval.constant.CommonConstants;
import com.chinappa.information.retrieval.util.FileHandlerUtil;

public class WebIndexer {

	public WebIndexer() {
		init();
	}

	public void init() {

		IndexerConfiguration.getInstance();
	}

	public void buildIndex() {

		Directory directory = null;
		try {
			directory = FSDirectory.open(new File(IndexerConfiguration
					.getInstance().getIndexLocation()));
			IndexWriterConfig config = getIndexWriterConfig();
			createIndexForDocuments(directory, config);
		} catch (IOException e) {
		} finally {
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void createIndexForDocuments(Directory directory,
			IndexWriterConfig config) {
		IndexWriter indexWriter = null;
		try {
			if (IndexWriter.isLocked(directory)) {
				IndexWriter.unlock(directory);
			}
			indexWriter = new IndexWriter(directory, config);
			Properties properties = FileHandlerUtil.readFromPropertiesFile(
					IndexerConfiguration.getInstance().getDocumentDirectory(),
					CommonConstants.DEFAULT_MAPPINGS_FILENAME);
			for (Object object : properties.keySet()) {
				String url = (String) object;
				String filename = properties.getProperty(url);
				File file = new File(IndexerConfiguration.getInstance()
						.getDocumentDirectory() + File.separator + filename);
				if (file.exists()) {
					org.jsoup.nodes.Document doc = null;
					if (IndexerConfiguration.getInstance()
							.isCompressionEnabled()) {
						String decompressedContent = FileHandlerUtil
								.fetchFromCompressedHTMLFile(
										IndexerConfiguration.getInstance()
												.getDocumentDirectory(),
										filename);
						doc = Jsoup.parse(decompressedContent);
					} else {
						doc = Jsoup.parse(file,
								CommonConstants.ENCODING_CHARSET);
					}
					String content = FileHandlerUtil.fetchDocumentText(doc);
					String anchorText = FileHandlerUtil.fetchAnchorText(doc);
					String metaData = getMetaData(doc);
					addDocumentFieldsToIndex(indexWriter, doc.title(),
							metaData, content, anchorText, url);
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				if (indexWriter != null)
					indexWriter.commit();
				indexWriter.close();
			} catch (IOException e) {
			}
		}
	}

	private void addDocumentFieldsToIndex(IndexWriter indexWriter,
			String title, String metadata, String content, String anchorText,
			String url) throws IOException {
		Document doc = new Document();
		doc.add(new Field(CommonConstants.TITLE_FIELD, title , Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS));
		doc.add(new Field(CommonConstants.METADATA_FIELD, metadata , Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS));
		doc.add(new Field(CommonConstants.CONTENT_FIELD, content , Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS));
		doc.add(new Field(CommonConstants.ANCHOR_TEXT_FIELD, anchorText , Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS));
		doc.add(new Field(CommonConstants.INDEX_FIELD, url , Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS));
		indexWriter.addDocument(doc);
	}

	private IndexWriterConfig getIndexWriterConfig() {
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
				getAnalyzer());
		return config;
	}

	private Analyzer getAnalyzer() {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		return analyzer;
	}

	private String getMetaData(org.jsoup.nodes.Document doc) {
		StringBuilder metadata = new StringBuilder();
		metadata.append(FileHandlerUtil.fetchDocumentMetadata(doc,
				CommonConstants.HTML_META_KEYWORDS));
		metadata.append(FileHandlerUtil.fetchDocumentMetadata(doc,
				CommonConstants.HTML_META_DESCRIPTION));
		return metadata.toString();
	}
}
