
import java.io.*;
import java.security.*;
import java.util.Iterator;
import org.bouncycastle.bcpg.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.*;

public class Verify {
  public static boolean verifyFile(String originalFile, String signedFile, String keyring) throws Exception {
	System.out.println("Verifying");
    Security.addProvider(new BouncyCastleProvider());
    FileInputStream fin = new FileInputStream(signedFile);
    FileInputStream keyIn = new FileInputStream(keyring);
    
    InputStream in = PGPUtil.getDecoderStream(fin);
    
    PGPObjectFactory pgpFact = new PGPObjectFactory(in, 
                                new JcaKeyFingerprintCalculator());
    
    PGPCompressedData c1 = (PGPCompressedData)pgpFact.nextObject();
    
    pgpFact = new PGPObjectFactory(c1.getDataStream(), 
                                new JcaKeyFingerprintCalculator());
    
    PGPPublicKeyRingCollection pgpPubRingCollection =
         new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(keyIn),
                                     new JcaKeyFingerprintCalculator());
    
    InputStream dIn = new FileInputStream(originalFile);
    
    int ch;
    
    PGPSignatureList p3 = (PGPSignatureList)pgpFact.nextObject();
    PGPSignature sig = p3.get(0);
    PGPPublicKey pk = pgpPubRingCollection.getPublicKey(sig.getKeyID());
    
    sig.init(
      new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), pk);
    
    while ((ch = dIn.read()) >= 0) sig.update((byte)ch);
    
    if (sig.verify()) { 
    	System.out.println("Veried....");
         return true;
    }
    else {
    	System.out.println("N Veried....");	
    return false;
    }
  }
}