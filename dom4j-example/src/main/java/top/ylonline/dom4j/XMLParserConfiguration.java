package top.ylonline.dom4j;

/**
 * @author YL
 */
public class XMLParserConfiguration {
    // private static final String CDATA_TAG_NAME = "text";

    private boolean keepRoot;

    private boolean keepAttributes;

    /**
     * Default parser configuration. Does not keep strings, and the CDATA Tag Name is "text".
     */
    public XMLParserConfiguration() {
        this(false, false);
    }

    public XMLParserConfiguration(final boolean keepAttributes) {
        this(false, keepAttributes);
    }

    public XMLParserConfiguration(final boolean keepRoot, final boolean keepAttributes) {
        this.keepRoot = keepRoot;
        this.keepAttributes = keepAttributes;
    }

    public boolean isKeepRoot() {
        return keepRoot;
    }

    public boolean isKeepAttributes() {
        return keepAttributes;
    }
}
