package com.chinappa.indexer.test;


public class LuceneUtilTest {

//	private final RAMDirectory ramDirectory = new RAMDirectory();
//	private static final String TERM_POSITION_PROVIDER = "term position provider";
//	private AtomicReader atomicReader;
//	private DirectoryReader dr;
//
//	public void init() throws IOException {
//		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_41,
//				new StandardAnalyzer(Version.LUCENE_41));
//		iwc.setOpenMode(OpenMode.CREATE);
//		IndexWriter writer = new IndexWriter(ramDirectory, iwc);
//		FieldType fieldType = new FieldType();
//		IndexOptions indexOptions = IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS;
//		fieldType.setIndexOptions(indexOptions);
//		fieldType.setIndexed(true);
//		fieldType.setStoreTermVectors(true);
//		fieldType.setStored(true);
//		Document doc = new Document();
//		doc.add(new Field("content",
//				"one quick brown fox jumped over one lazy dog", fieldType));
//		writer.addDocument(doc);
//		writer.commit();
//		writer.close();
//		dr = DirectoryReader.open(ramDirectory);
//		atomicReader = dr.leaves().get(0).reader();
//
//	}
//
//	public Object[][] getTermPositions() {
//		List<Object[]> data = new ArrayList<Object[]>();
//		data.add(new Object[] { "brown", new int[] { 2 } });
//		data.add(new Object[] { "dog", new int[] { 8 } });
//		data.add(new Object[] { "fox", new int[] { 3 } });
//		data.add(new Object[] { "jumped", new int[] { 4 } });
//		data.add(new Object[] { "lazy", new int[] { 7 } });
//		data.add(new Object[] { "one", new int[] { 0, 6 } });
//		data.add(new Object[] { "over", new int[] { 5 } });
//		data.add(new Object[] { "quick", new int[] { 1 } });
//		return data.toArray(new Object[data.size()][]);
//	}
//
//	public void destroy() throws IOException {
//		atomicReader.close();
//	}
//
//	public void testGetTermPositionMap(String query, int[] position)
//			throws IOException, ParseException {
//		Map<String, int[]> posMap = LuceneUtil.getTermPositionMap(atomicReader,
//				0, "content");
//		Assert.assertEquals(posMap.get(query)[0], position[0]);
//	}

}

//public final class LuceneUtil {
//	/**
//	 * Private constructor to prevent instantiation.
//	 */
//	private LuceneUtil() {
//	}
//
//	/**
//	 *
//	 * @param reader
//	 *            the index reader
//	 * @param fieldName
//	 *            name of the field of interest
//	 * @param docId
//	 *            internal doc ID of the document of interest
//	 * @return all Terms present in the requested field
//	 * @throws IOException
//	 *             on IndexReader error
//	 */
//	public Terms getTerms(final AtomicReader reader, final String fieldName,
//			final int docId) throws IOException {
//		return reader.getTermVector(docId, fieldName);
//	}
//
//	/**
//	 * Returns a map of the terms and their token positions for a field in a
//	 * document. The map may be empty because vector information is not
//	 * available for the requested field, or because the analyzer assigned to it
//	 * found no terms in the original document field at index time.
//	 *
//	 * @param reader
//	 *            Lucene index reader (for access to term vector info)
//	 * @param docId
//	 *            the internal Lucene ID of the document of interest
//	 * @param fieldName
//	 *            name of the field of interest
//	 * @return a map of term/positions pairs; the map may be empty.
//	 * @throws IOException
//	 *             on IndexReader error
//	 */
//	public Map<String, int[]> getTermPositionMap(final AtomicReader reader,
//			final int docId, final String fieldName) throws IOException {
//		Map<String, int[]> termPosMap = new HashMap<String, int[]>();
//		Terms terms = LuceneUtil.getTerms(reader, fieldName, docId);
//		if (terms != null) {
//			TermsEnum termsEnum = terms.iterator(TermsEnum.EMPTY);
//			BytesRef term;
//			while ((term = termsEnum.next()) != null) {
//				String docTerm = term.utf8ToString();
//				DocsAndPositionsEnum docPosEnum = termsEnum.docsAndPositions(
//						reader.getLiveDocs(), null,
//						DocsAndPositionsEnum.FLAG_OFFSETS);
//				docPosEnum.nextDoc();
//				int freq = docPosEnum.freq();
//				int[] posArray = new int[freq];
//				for (int i = 0; i < freq; i++) {
//					int position = docPosEnum.nextPosition();
//					posArray[i] = position;
//				}
//				termPosMap.put(docTerm, posArray);
//			}
//		}
//		return termPosMap;
//	}
//
//	// to avoid special cases, it is assumed that
//	// the last element of each list is the Integer.MAX_VALUE
//	// the algorithm returns the position of each term of
//	// the minimum window
//	static int[] solve(int[][] lists) {
//		int m = lists.length;
//		// the current selected element from each list
//		int[] pos = new int[m];
//		// the current best solution positions
//		int[] sol = new int[m];
//		// the score (window length) of current solution
//		int currSol = Integer.MAX_VALUE;
//		while (true) {
//			// select the list that has the increasing minimum element
//			int minList = argmin(pos, lists);
//			// if you can't increase the minimum, stop
//			if (minList == -1)
//				break;
//			// calculate the window size
//			int minValue = lists[minList][pos[minList]];
//			int maxValue = max(pos, lists);
//			int nextSol = maxValue - minValue;
//			// update the solution if necessary
//			if (nextSol < currSol) {
//				currSol = nextSol;
//				System.arraycopy(pos, 0, sol, 0, m);
//			}
//			// update the current minumum element
//			pos[minList]++;
//		}
//		return sol;
//	}
//
//	private static int argmin(int[] pos, int[][] v) {
//		int min = Integer.MAX_VALUE;
//		int arg = -1;
//		for (int i = 0; i < v.length; ++i) {
//			if (v[i][pos[i]] < min) {
//				min = v[i][pos[i]];
//				arg = i;
//			}
//		}
//		return arg;
//	}
//
//	private static int argmax(int[] pos, int[][] v) {
//		int max = -1;
//		int arg = -1;
//		for (int i = 0; i < v.length; ++i) {
//			if (v[i][pos[i]] > max) {
//				max = v[i][pos[i]];
//				arg = i;
//			}
//		}
//		return arg;
//	}
//
//	private static int max(int[] pos, int[][] v) {
//	int arg = argmax(pos, v);
//	return v[arg][pos[arg]];
//	}
