
import java.io.*;
import java.security.*;
import java.util.Iterator;
import org.bouncycastle.bcpg.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.jcajce.*;

public class Sign {
	public static void signFile(String file, String PGP_private, String passphrase) throws Exception {
		//System.out.println("Sign");
		Security.addProvider(new BouncyCastleProvider());
		//System.out.println("past security");
		FileInputStream fin = new FileInputStream(PGP_private);
		InputStream in = PGPUtil.getDecoderStream(fin);
		PGPSecretKeyRingCollection skrc = new PGPSecretKeyRingCollection(in, new JcaKeyFingerprintCalculator());
		Iterator<PGPSecretKeyRing> kri = skrc.getKeyRings();
		PGPSecretKey key = null;
		
		while (key == null && kri.hasNext()) {
			PGPSecretKeyRing keyRing = (PGPSecretKeyRing) kri.next();
			
			Iterator<PGPSecretKey> ki = keyRing.getSecretKeys();
			
			while (key == null && ki.hasNext()) {
				PGPSecretKey k = (PGPSecretKey) ki.next();
				if (k.isSigningKey()) {
					key = k;
				}
			}
		}
		
		if (key == null)
			throw new IllegalArgumentException("Cant find key");
		
		
		PGPPrivateKey pgpPrivKey = key.extractPrivateKey(
				new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(passphrase.toCharArray()));
		
		
		PGPSignatureGenerator sGen = new PGPSignatureGenerator(
				new JcaPGPContentSignerBuilder(key.getPublicKey().getAlgorithm(), PGPUtil.SHA1).setProvider("BC"));
		
		sGen.init(PGPSignature.BINARY_DOCUMENT, pgpPrivKey);
		
		PGPCompressedDataGenerator cGen = new PGPCompressedDataGenerator(PGPCompressedData.ZLIB);
		
		FileOutputStream out = new FileOutputStream(file + ".bpg");
		BCPGOutputStream bOut = new BCPGOutputStream(cGen.open(out));
		FileInputStream fIn = new FileInputStream(file);
		
		int ch;
		while ((ch = fIn.read()) >= 0) {
			sGen.update((byte) ch);
		}
		
		sGen.generate().encode(bOut);
		cGen.close();
		out.close();
		fIn.close();
	}
}
