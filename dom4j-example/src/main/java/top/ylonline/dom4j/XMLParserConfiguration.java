package top.ylonline.dom4j;

import java.util.List;

/**
 * @author YL
 */
public class XMLParserConfiguration {
    private static final String CDATA_TAG_NAME = "content";

    private boolean keepRoot;
    /**
     * When parsing the XML into JSON, specifies if values should be kept as strings (true), or if
     * they should try to be guessed into JSON values (numeric, boolean, string)
     */
    private boolean keepAttributes;

    private List<String> keepAttributeNames;
    /**
     * The name of the key in a JSON Object that indicates a CDATA section. Historically this has
     * been the value "content" but can be changed. Use <code>null</code> to indicate no CDATA
     * processing.
     */
    private String cdataTagName;

    /**
     * Default parser configuration. Does not keep strings, and the CDATA Tag Name is "content".
     */
    public XMLParserConfiguration() {
        this(false, false, CDATA_TAG_NAME);
    }

    /**
     * Configure the parser string processing and use the default CDATA Tag Name as "content".
     *
     * @param keepAttributes <code>true</code> to parse all values as string.
     *                       <code>false</code> to try and convert XML string values into a JSON value.
     */
    public XMLParserConfiguration(final boolean keepAttributes) {
        this(false, keepAttributes, CDATA_TAG_NAME);
    }

    /**
     * Configure the parser string processing to try and convert XML values to JSON values and
     * use the passed CDATA Tag Name the processing value. Pass <code>null</code> to
     * disable CDATA processing
     *
     * @param cdataTagName<code>null</code> to disable CDATA processing. Any other value
     *                                      to use that value as the JSONObject key name to process as CDATA.
     */
    public XMLParserConfiguration(final String cdataTagName) {
        this(false, false, cdataTagName);
    }

    /**
     * Configure the parser to use custom settings.
     *
     * @param keepAttributes                <code>true</code> to parse all values as string.
     *                                      <code>false</code> to try and convert XML string values into a JSON value.
     * @param cdataTagName<code>null</code> to disable CDATA processing. Any other value
     *                                      to use that value as the JSONObject key name to process as CDATA.
     */
    public XMLParserConfiguration(final boolean keepRoot, final boolean keepAttributes, final String cdataTagName) {
        this.keepRoot = keepRoot;
        this.keepAttributes = keepAttributes;
        this.cdataTagName = cdataTagName;
    }

    public XMLParserConfiguration(final boolean keepRoot, final boolean keepAttributes,
                                  final List<String> keepAttributeNames,
                                  final String cdataTagName) {
        this(keepRoot, keepAttributes, cdataTagName);
        this.keepAttributeNames = keepAttributeNames;
    }

    public XMLParserConfiguration(final boolean keepAttributes,
                                  final List<String> keepAttributeNames,
                                  final String cdataTagName) {
        this(false, keepAttributes, cdataTagName);
        this.keepAttributeNames = keepAttributeNames;
    }

    public boolean isKeepRoot() {
        return keepRoot;
    }

    public boolean isKeepAttributes() {
        return keepAttributes;
    }

    public List<String> getKeepAttributeNames() {
        return keepAttributeNames;
    }

    public String getCdataTagName() {
        return cdataTagName;
    }
}
