package edu.cnm.deepdive.security;

import org.junit.Assert;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.ResourceBundle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.cnm.deepdive.security.Diceware.InsufficientPoolException;

public class DicewareTest {

  private Diceware diceware;
  private ResourceBundle bundle;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @Before
  public void setUp() throws Exception {
    bundle = ResourceBundle.getBundle("wordlist");
    diceware = new Diceware(bundle);
}
  
  @Test
  public void testSetRng() {
    Random rng = new SecureRandom();
    diceware.setRng(rng);
    try {
      Assert.assertSame("Unexpected Random instance returned by getRng()", rng, diceware.getRng());
    } catch (NoSuchAlgorithmException e) {
      Assert.fail(e.toString());
    }
    
  }
  @Test
  public void testGenerateLength() {
    try {
      for (int i = 1; i <= 10; i++) {
        Assert.assertEquals(i, diceware.generate(i).length);
         }
    }  catch (NoSuchAlgorithmException | IllegalArgumentException e) {
      Assert.fail(e.toString());
    } 
  }

  @Test
  public void testGenerateInvalidLength() {
    for(int i= -10; i <= 0; i++) {
      try {
        diceware.generate(i);
        Assert.fail("Expected exception not thrown");
      } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
        // Do nothing; exception expected.
      }
    }
  }
@Test
public void testDelimiter() {
  try {
    String passphrase = diceware.generate(10, "-");
    Assert.assertTrue("Found a space", passphrase.indexOf(' ') <= 0);
  } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
    Assert.fail(e.toString());
  }
}

@Test
public void testNoDuplicates() {
  String[] words = null;
  try {
    words = diceware.generate(5000, false);
  } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
    Assert.fail(e.toString());
  }
  for (int i = 0; i < words.length - 1; i++) {
    String test = words[i];
    for (int j = i + 1; j < words.length; j++) {
      Assert.assertNotEquals("Found duplicated string", test, words[j]);
    }
  }
}
@Test
public void testInsufficientPool() {
  int length = bundle.keySet().size() + 1;
  try {
    diceware.generate(length, false);
   Assert.fail("Should have thrown InsufficientPoolException");
  } catch (InsufficientPoolException e) {
    // Do nothing expected exception caught.
  } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
    Assert.fail(e.toString());
  } 
 
  }
  
}




