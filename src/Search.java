import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class Search {

	protected Shell shell;
	private Text txtID;
	private Text txtLoop;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Search window = new Search();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(278, 216);
		shell.setText("SWT Application");
		
		Label lblProteinId = new Label(shell, SWT.NONE);
		lblProteinId.setBounds(10, 22, 55, 15);
		lblProteinId.setText("Protein ID:");
		
		txtID = new Text(shell, SWT.BORDER);
		txtID.setBounds(71, 22, 76, 21);
		
		ListViewer listViewer = new ListViewer(shell, SWT.BORDER | SWT.V_SCROLL);
		List listID = listViewer.getList();
		listID.setBounds(27, 49, 218, 92);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				//if(txtID == null)
					
				if(txtID != null && txtLoop != null)
					listID.add(txtID.getText());
				
			}
		});
		btnAdd.setBounds(156, 22, 75, 25);
		btnAdd.setText("Add ");
		
		Label lblLoop = new Label(shell, SWT.NONE);
		lblLoop.setBounds(10, 152, 55, 15);
		lblLoop.setText("Loop");
		
		txtLoop = new Text(shell, SWT.BORDER);
		txtLoop.setBounds(71, 149, 76, 21);
		
		Button btnFind = new Button(shell, SWT.NONE);
		Sheet currentSheet;
		Sheet previousSheet = null;
		ArrayList<Sheetpairs> sheetpairList = new ArrayList<Sheetpairs>();
		
		btnFind.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				
				Sheet currentSheet;
				Sheet previousSheet = null;
				ArrayList<Sheetpairs> sheetpairList = new ArrayList<Sheetpairs>();
				try 
		        {
		          String sheetString = "SHEET";
		          String path = "C:/Users/marab/workspace/";
		          String file = txtID.getText();
		          BufferedReader bf = new BufferedReader(new FileReader(path+file+".pdb"));  
				  String line;
				  while (( line = bf.readLine()) != null)
				  {
				      int indexfound = line.indexOf(sheetString);
				      if (indexfound == 0) 
				      {
				    	  currentSheet = BuildSheet(line);
				    	  if(previousSheet != null)
				    	  {
				    		  CheckAndAddToList(sheetpairList, previousSheet,currentSheet, Integer.parseInt(txtLoop.getText()));
				    	  }
				    	  previousSheet = currentSheet;
					} 		      
				  }
				  bf.close();
				  for (Sheetpairs pair : sheetpairList) 
				  {
						String atomString = "ATOM";
						BufferedReader bfAtoms = new BufferedReader(new FileReader(path+file+".pdb"));
						String atomLine;
						  while (( atomLine = bfAtoms.readLine()) != null)
						  {
						      int indexfound = atomLine.indexOf(atomString);
						      
						      
						      if (indexfound == 0) 
						      {
						    	  String aa = atomLine.substring(22,26).trim();
						    	  if (Integer.parseInt(aa)>= pair.pair1.aa1 &&  Integer.parseInt(aa)<= pair.pair1.aa2)
						    		  System.out.println(atomLine);
						    	  if (Integer.parseInt(aa)>= pair.pair2.aa1 &&  Integer.parseInt(aa)<= pair.pair2.aa2)
						    		  System.out.println(atomLine);
						    	  
						    		  
						      }
						  }
						  bfAtoms.close();
				  }
				  
		        }
				catch (IOException ex) 
				{
				  System.out.println("IO Error Occurred: " + ex.toString());
				}
				
			}

			private void CheckAndAddToList(ArrayList<Sheetpairs> sheetpairList, Sheet previousSheet,Sheet currentSheet, int looplength) 
			{
				if(currentSheet.aa1 - previousSheet.aa2 == looplength)
				{
					Sheetpairs newPairs = new Sheetpairs(previousSheet,currentSheet, looplength);
					sheetpairList.add(newPairs);
					System.out.println(previousSheet.line);
					System.out.println(currentSheet.line);	
							
				}		
			}
			private Sheet BuildSheet(String line) {
				int num1;
				int num2;
				String aa1;
				String aa2;		
		  	  	aa1 =line.substring(22,26).trim();
		  	  	aa2=line.substring(33,37).trim();
		  	  	num1= Integer.parseInt(aa1);
		  	  	num2= Integer.parseInt(aa2);
		  	    Sheet sheet = new Sheet(line,num1, num2); 	  	
				return sheet;
			}
			
			
		});
		
		btnFind.setBounds(156, 147, 75, 25);
		btnFind.setText("Find SLSs");
		
		

	}
}
