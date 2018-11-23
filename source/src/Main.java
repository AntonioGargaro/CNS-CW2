import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.border.LineBorder;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.bouncycastle.openpgp.PGPException;

import javafx.scene.layout.Border;



public class Main extends Applet {
	
	Button b;  
	TextField tf;
	
	String fileDirectory1;
	String fileDirectory2;
	String passString;
	String originalString;
	String signedString;
	String keyringString;
	
	Boolean originalFileBool;
	Boolean signedFileBool;
	Boolean publicKeyBool;
	
	Boolean textFileBool;
	Boolean privateKeyBool;
	Boolean passPhraseBool;
	
	Label label1 = new Label();
	Label label2 = new Label();
	Label label3 = new Label();
	Label label4 = new Label();
	
	LineBorder redBorder = new LineBorder(Color.red, 3);
	
	
	TextField textField;
	Panel p = new Panel();
	
	Button openfile;
	Button oppbutton;
	Button signfile;
	Button btmm;
	
	Button openofile;
	Button opsbutton;
	Button pubfile;
	Button validatefile;
	Button btmm2;
	
	public void init(){  
		
		originalFileBool = false;
		signedFileBool = false;
		publicKeyBool = false;
		textFileBool = false;
		privateKeyBool = false;
		passPhraseBool = false;
		
		
		Font f;
		String osname = System.getProperty("os.name","");
		if (!osname.startsWith("Windows")) {
			f = new Font("Arial",Font.BOLD,10);
		} else {
			f = new Font("Verdana",Font.BOLD,12);
		}
		p.setFont(f);
		
		p.add(new Button("Sign File"));
		p.add(new Button("Verify Signed File"));
		p.setBackground(new Color(255, 255, 255));	
		add("North",p);
		p.revalidate();
		
		} 
	
	public void renderSignPanel() {
		
		
		
		openfile = new Button("Open File...");
		openfile.setForeground(Color.red);
		p.add(openfile);
		
		oppbutton = new Button("Open PGP Private...");
		oppbutton.setForeground(Color.red);
		p.add(oppbutton);
		
		textField = new TextField("Pass Phrase");
		p.add(textField);
		
		signfile = new Button("Sign File!");
		p.add(signfile);
		
		btmm = new Button("Back to Main Menu");
		btmm.setForeground(Color.black);
		p.add(btmm);
		
		p.revalidate();
		
		
		
	}
	
	
	public void renderSignSuccess() {
		
		
		Label label1 = new Label();
		label1.setText("Succesfully Signed");
		label1.setAlignment(Label.CENTER);
		label1.setForeground(Color.GREEN);
		p.add(label1);
		p.add(new Button("Back to Main Menu"));
		p.revalidate();
	
		
	}
	public void renderVerifySuccess() {
		
		
		
		label4.setText("Succesfully Verified");
		label4.setAlignment(Label.CENTER);
		label4.setForeground(Color.GREEN);
		p.add(label4);
		p.add(new Button("Back to Main Menu"));
		p.revalidate();
	
		
	}
	
	public void renderVerifyPanel() {
		
		openofile = new Button("Open Original File...");
		openofile.setForeground(Color.red);
		p.add(openofile);
		
		opsbutton = new Button("Open Signed File...");
		opsbutton.setForeground(Color.red);
		p.add(opsbutton);
		
		
		pubfile = new Button("Open Public Keyring...");
		pubfile.setForeground(Color.red);
		p.add(pubfile);
		
		validatefile = new Button("Validate");
		p.add(validatefile);
		
		btmm2 = new Button("Back to Main Menu");
		btmm2.setForeground(Color.black);
		p.add(btmm2);
		
		p.revalidate();
		
		


		
	}
	
	@SuppressWarnings("deprecation")
	public boolean action(Event evt, Object arg) {
		
		if(arg.equals("Open File...") || arg.equals("Open PGP Private...") || arg.equals("Open Original File...") || arg.equals("Open Signed File...") || arg.equals("Open Public Keyring...")) {
			//System.out.println("OPEN CLICKED");	
			int arrlen = 10000;
			byte[] infile = new byte[arrlen];
			Frame parent = new Frame();
			FileDialog fd = new FileDialog(parent, "Please choose a file:",
			    FileDialog.LOAD);
			fd.setVisible(true);
			String selectedItem = fd.getFile();
			System.out.print(selectedItem);
			if (selectedItem == null) {
				// no file selected
			} 

			if(arg.equals("Open File...")) {
					fileDirectory1 = fd.getDirectory() + fd.getFile();
					textFileBool = true;
					openfile.setForeground(Color.green);
			}
			if(arg.equals("Open PGP Private...")) {
				fileDirectory2 = fd.getDirectory() + fd.getFile();
				privateKeyBool = true;
				oppbutton.setForeground(Color.green);
			}
			if (arg.equals("Open Original File...")) {
				
				originalString = fd.getDirectory() + fd.getFile();
				originalFileBool = true;
				openofile.setForeground(Color.green);
			}
			if (arg.equals("Open Signed File...")) {
		
				signedString = fd.getDirectory() + fd.getFile();
				signedFileBool = true;
				opsbutton.setForeground(Color.green);
			}	
			if (arg.equals("Open Public Keyring...")) {
		
				keyringString = fd.getDirectory() + fd.getFile();
				publicKeyBool = true;
				pubfile.setForeground(Color.green);
			}	
			
		}
			else if (arg.equals("Sign File")) {
				//System.out.print("sign");
				p.removeAll();
				renderSignPanel();
			}
			else if (arg.equals("Verify Signed File")) {
				
				p.removeAll();
				renderVerifyPanel();
			}
			else if(arg.equals("Sign File!")) {
				passString = textField.getText();
				System.out.println("Submit");
				
				if((textFileBool) && (privateKeyBool)) {
					try {
						//System.out.println("In try");
						Sign.signFile(fileDirectory1, fileDirectory2, passString);
						p.removeAll();
						renderSignSuccess();
					} catch (Exception e) {
						System.out.print("Must be an error with the inputs..");
						label2.setText("Error Signing");
						label2.setAlignment(Label.CENTER);
						label2.setForeground(Color.red);
						p.add(label2);
						openfile.setForeground(Color.red);
						oppbutton.setForeground(Color.red);
						p.revalidate();
					}
				}
			}
			else if (arg.equals("Validate")) {
	
				if(originalFileBool && signedFileBool && publicKeyBool) {
						try {
							Verify.verifyFile(originalString, signedString, keyringString);
							p.removeAll();
							renderVerifySuccess();
									
						} catch (Exception e) {
							label3.setText("Error Verifiying");
							label3.setAlignment(Label.CENTER);
							label3.setForeground(Color.red);
							p.add(label3);
							pubfile.setForeground(Color.red);
							opsbutton.setForeground(Color.red);
							openofile.setForeground(Color.red);
							p.revalidate();
						}
					} 
			}
			else if (arg.equals("Back to Main Menu")) {
				
				p.removeAll();
				init();
			}	
					
			
		
		return true;
	}
	
    public static void main(String[] args, Graphics g) throws Exception {
    	
    	   	
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
 
        try {
	        // Takes OpenPGP style ID and passphrase
        	// Creates a new RSA key, doesn't use existing ones
	        String[] openPGP = {"A.Gargaro ajg2@hw.ac.uk", "heriotwatt2"};
	        // Encoded private key is outputted to 'sec.bpg'
	        // Certificate is outputted to 'pub.bpg'
	        RSAMakeKeyPair.main(openPGP[0], openPGP[1]);
        } catch (Exception e) {
        	System.err.println("Exception type: " + e);
        }

        
        // Sign File and output it
        try {
        	String[] arr = {"test.txt", "sec.bpg", "heriotwatt2"};
        	// Signed file is output as 'arr[0].bpg'
        	Sign.signFile(arr[0], arr[1], arr[2]);
        } catch (PGPException e) {
        	System.err.println("PGPException: " + e.getMessage());
        } catch (FileNotFoundException e) {
        	System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (Exception e) {
        	System.err.println("Exception type: " + e);
        }

        
        try {
        // Uses original file, signed .bpg file and public keyring
        String[] verifyData = {"test.txt", "test.txt.bpg", "pub.bpg"};
        System.out.println(Verify.verifyFile(verifyData[0], verifyData[1], verifyData[2]));
        } catch (FileNotFoundException e ) {
        	System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (Exception e) {
        	System.err.println("Exception type: " + e);
        }
        
    }
    
    
    
    

}
