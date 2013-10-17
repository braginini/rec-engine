package com.zibea.recommendations.util;

/**
 * @author Mikhail Bragin
 */
public class OptionUtil {

    //options names
    public static final String OUTPUT_TABLE = "outputTable";
    public static final String INPUT_TABLE = "inputTable";
    public static final String OUTPUT_FAMILY = "outputFamily";
    public static final String INPUT_FAMILY = "inputFamily";
    public static final String SIMILARITY_CLASS_NAME = "similarityClassname";
    public static final String TEMP_DIR = "tempDir";
    public static final String CLEAN_AFTER = "cleanAfter";

    //options descriptions
    public static final String OUTPUT_TABLE_DESCR = "HBase output table";
    public static final String INPUT_TABLE_DESCR = "HBase input table";
    public static final String OUTPUT_FAMILY_DESCR = "HBase output family";
    public static final String INPUT_FAMILY_DESCR = "HBase input family";
    public static final String SIMILARITY_CLASS_DESCR = "Class name of algorithm to calculate similarity";
    public static final String TEMP_DIR_DESCR = "The base temporary output directory";
    public static final String CLEAN_AFTER_DESCR = "The flag indicates whether to clean temp directory after task complete";

    //options default values
    public static final String TEMP_DIR_VALUE = "temp";
    public static final String SIMILARITY_RESULT_DIR_VALUE = "result";
}
