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
     * Default parser configuration. Does not keep strings, and the CDATA Tag Name is "content".
     */
    public XMLParserConfiguration() {
        this(false, false);
    }

    /**
     * Configure the parser string processing and use the default CDATA Tag Name as "content".
     *
     * @param keepAttributes <code>true</code> to parse all values as string.
     *                       <code>false</code> to try and convert XML string values into a JSON value.
     */
    public XMLParserConfiguration(final boolean keepAttributes) {
        this(false, keepAttributes);
    }

    /**
     * Configure the parser to use custom settings.
     *
     * @param keepAttributes <code>true</code> to parse all values as string.
     *                       <code>false</code> to try and convert XML string values into a JSON value.
     */
    public XMLParserConfiguration(final boolean keepRoot, final boolean keepAttributes) {
        this.keepRoot = keepRoot;
        this.keepAttributes = keepAttributes;
    }

    public XMLParserConfiguration(final boolean keepRoot, final boolean keepAttributes,
                                  final List<String> keepAttributeNames) {
        this(keepRoot, keepAttributes);
        this.keepAttributeNames = keepAttributeNames;
    }

    public XMLParserConfiguration(final boolean keepAttributes, final List<String> keepAttributeNames) {
        this(false, keepAttributes);
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
}
