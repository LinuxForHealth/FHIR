package org.linuxforhealth.fhir.model.string.util.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.string.util.StringSizeControlStrategyFactory;
import org.linuxforhealth.fhir.model.string.util.StringSizeControlStrategyFactory.Strategy;
import org.linuxforhealth.fhir.model.string.util.strategy.MaxBytesStringSizeControlStrategy;
import org.testng.annotations.Test;

public class StringSizeControlStrategyTest {
    
    
    public static final int MAX_SEARCH_STRING_BYTES = 1024;
    
    @Test
    public void testStringSizeControlStrategyFactory() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        assertNotNull(strategy);
        assertTrue(strategy instanceof MaxBytesStringSizeControlStrategy);
    }
    
    @Test(expectedExceptions= {FHIROperationException.class})
    public void testStringSizeControlStrategyFactory_BadKey() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        factory.getStrategy("test");
    }
    
    @Test(expectedExceptions= {NullPointerException.class})
    public void testStringSizeControlStrategy_WithNullInput() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = null;
        strategy.truncateString(description, MAX_SEARCH_STRING_BYTES);
    }
    
    @Test
    public void testStringSizeControlStrategy_WithInpWithinSizeLimit() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = "A record of a medication that is being consumed by a patient.";
        String truncatedDescription = strategy.truncateString(description, MAX_SEARCH_STRING_BYTES);
        assertTrue(truncatedDescription.equals(truncatedDescription));
    }
    
    @Test
    public void testStringSizeControlStrategy_WithInpExceedingSizeLimit() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = "A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains. \n"
                + "\n"
                + "The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.";
        String truncatedDescription = strategy.truncateString(description, MAX_SEARCH_STRING_BYTES);
        assertFalse(truncatedDescription.equals(description));
        assertTrue(truncatedDescription.getBytes().length == MAX_SEARCH_STRING_BYTES);
    }
    
    /**
     * Test an input string which contains a unicode character which occupies 2 bytes of memory. The expected output is 
     * to truncate the unicode character since max allowed bytes = 4.
     * @throws FHIROperationException
     */
    @Test
    public void testStringSizeControlStrategy_WithInpEqSizeLimit() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = "ab\u0080"; // padding character
        int maxBytes = 4;
        String truncatedDescription = strategy.truncateString(description, maxBytes);
        assertTrue(truncatedDescription.equals(description));
        assertTrue(truncatedDescription.getBytes().length == maxBytes);
        assertTrue(truncatedDescription.getBytes().length == description.getBytes().length);
    }
    
    /**
     * Test an input string which contains a unicode character which occupies 2 bytes of memory. The expected output is 
     * to truncate the unicode character since max allowed bytes = 3.
     * @throws FHIROperationException
     */
    @Test
    public void testStringSizeControlStrategy_WithUnicodeInpExceedingSizeLimit() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = "ab\u0080"; // padding character
        int maxBytes = 3;
        String truncatedDescription = strategy.truncateString(description, maxBytes);
        assertFalse(truncatedDescription.equals(description));
        assertTrue(truncatedDescription.getBytes().length <= maxBytes);
        assertFalse(truncatedDescription.getBytes().length == description.getBytes().length);
    }
    
    /**
     * Test an input string which contains a surrogate pair which occupies 4 bytes of memory. The expected output is 
     * to truncate the unicode character since max allowed bytes = 4.
     * @throws FHIROperationException
     */
    @Test
    public void testStringSizeControlStrategy_WithSurrogatePairInpEqSizeLimit() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = "\uD834\uDD1E"; // surrogate pair
        int maxBytes = 4;
        String truncatedDescription = strategy.truncateString(description, maxBytes);
        assertTrue(truncatedDescription.equals(description));
        assertTrue(truncatedDescription.getBytes().length == maxBytes);
        assertTrue(truncatedDescription.getBytes().length == description.getBytes().length);
    }
    
    /**
     * Test an input string which contains a surrogate pair which occupies 6 bytes of memory. The expected output is 
     * to truncate the unicode character since max allowed bytes = 4.
     * @throws FHIROperationException
     */
    @Test
    public void testStringSizeControlStrategy_WithSurrogatePairInpExceedingSizeLimit() throws FHIROperationException {
        StringSizeControlStrategyFactory factory = StringSizeControlStrategyFactory.factory();
        assertNotNull(factory);
        org.linuxforhealth.fhir.model.string.util.strategy.StringSizeControlStrategy strategy = factory.getStrategy(Strategy.MAX_BYTES.getValue());
        String description = "ab\uD834\uDD1E"; // surrogate pair
        int maxBytes = 4;
        String truncatedDescription = strategy.truncateString(description, maxBytes);
        assertFalse(truncatedDescription.equals(description));
        assertTrue(truncatedDescription.getBytes().length <= maxBytes);
        assertFalse(truncatedDescription.getBytes().length == description.getBytes().length);
    }
    
    

}
