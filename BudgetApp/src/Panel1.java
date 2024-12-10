import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Panel1 extends JPanel {
	public static ArrayList<User> userList = new ArrayList<>();
	public Map<String, JTextField> textLabels = new HashMap<>();
	public Map<String, JButton> buttonList = new HashMap<>();
	public Map<String, JLabel> labelList = new HashMap<>();
	public ArrayList<Clip> songList = new ArrayList<>();
	public ArrayList<Amount> houseOutcome = new ArrayList<>();
	public static double householdOutcomePerWeek = 0;

	User currentUser = userList.get(0);
	String currentPage = " ";
	double currentUserRevenue = 0;
	
	Font customFont;
	Font currentFont;
	
	Color retroColor = new Color(135, 54, 55);
	Color backgroundColor = retroColor;
	
	String addressPixel = "/zPixelifySans-SemiBold.ttf";

	BufferedImage retro1Logo = loadImage("zRRRPixel.png");
	BufferedImage officeLogo = loadImage("zOfficeLogo.png");
	BufferedImage carsLogo = loadImage("zcarsLogo.png");
	BufferedImage backgroundLogo = retro1Logo;
	
	Clip buttonSound = loadSound("BudgetApp/src/zbuttonClickSound.wav");
	Clip bottleOpeningSound = loadSound("BudgetApp/src/zopenBottleSound.wav");
	Clip retroButtonSound = loadSound("BudgetApp/src/zbitButton.wav");
	Clip currentClickSound = retroButtonSound;

	
	Panel1() {
		createCustomFont();
		currentFont = customFont;
		this.setPreferredSize(new Dimension(1000, 500));
		this.setFocusable(true);
		homeScreen();
		//defaultPage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(backgroundColor);
			
			if(currentPage != "home"){
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(backgroundLogo, 300, 50, 400, 400, null);
		}
	}
	
	public void createCustomFont(){
		try{
		InputStream is = Panel1.class.getResourceAsStream(addressPixel);
		customFont = Font.createFont(Font.TRUETYPE_FONT, is);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(customFont);
		} 
		catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

    public BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try (InputStream is = getClass().getResourceAsStream(path)) {
       		image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

	public Clip loadSound(String file) {
		Clip clip = null;
		try {
			File audioFile = new File(file);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
		} catch (Exception e){
		}
		return clip; // Return the loaded clip or null if loading failed
	}

	public void playClick(){
		currentClickSound.stop(); 
		currentClickSound.setFramePosition(0);
		currentClickSound.start(); 
	}

	public void makeButton(String text, float size, int x, int y, int width, int height, ActionListener d) {
		JButton button = new JButton();
		button.setText("<html>" + text  + "</html>");
		button.setBounds(x, y, width, height);
		Font sizedFont = currentFont.deriveFont(size);
		button.setFont(sizedFont);		button.setBackground(Color.white);
		button.setForeground(Color.black);
		button.addActionListener(d);
		button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		button.setFocusPainted(false);
		buttonList.put(text, button);
		button.addActionListener(e -> playClick());
		this.add(button);
	}
	
	public void makeLabel(String text, float size, int x, int y, int width, int height, Color backColor, Color textColor,boolean backgroundVisible){
		JLabel label = new JLabel();
		label.setText("<html>" + text  + "</html>");
		label.setBounds(x, y, width, height);
		Font sizedFont = currentFont.deriveFont(size);
		label.setFont(sizedFont);		
		label.setBackground(backColor);
		label.setForeground(textColor);
		label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		label.setOpaque(backgroundVisible);
		labelList.put(text, label);
		this.add(label);
	}
	
	public void makeTextField(String name, float size, int x, int y, int width, int height, Color backColor, Color textColor){
		JTextField textField = new JTextField();
		textField.setName("<html>" + name  + "</html>");
		textField.setBounds(x, y, width, height);
		Font sizedFont = currentFont.deriveFont(size);
		textField.setFont(sizedFont);
		textField.setBackground(backColor);
		textField.setForeground(textColor);
		this.add(textField);
		textLabels.put(name, textField);
	}

	public void clearScreen(LayoutManager man) {
		this.removeAll();
		this.repaint();
		this.setLayout(man);
		currentUser.calcIncome();
		currentUser.calcOutcome();
		calcHouseOut();
		calcHouseOutcome();
		
		calcRevenue();
		int defUser = 0;
		boolean isDefUser = false;
		for(User user : userList){
			if(user.firstName == "Default"){
				defUser = userList.indexOf(user);
				isDefUser = true;
			}
		}
		
		//creates user if none available
		//gets rid of default user if there is another profile

		if(userList.size() < 1){
			currentUser = new User("Default", "User");
		} else if(isDefUser && userList.size() > 1){
			userList.remove(userList.remove(defUser));
		}
		//switches user if the last one was deleted
		if(!userList.contains(currentUser)){
			currentUser = userList.get(0);
		}
		
	}

//this is the logo screen that plays
	public void homeScreen() {
		currentPage = "home";
		clearScreen(null);

		//starts animation and goes to default page when done
		bottleOpeningSound.start();
		Timer timer = new Timer(2000, e -> defaultPage());
		timer.start();
		timer.setRepeats(false);

		String gifPath = "zintroGif.gif";
		URL gifURL = getClass().getResource(gifPath);

		//adds gif to screen
		ImageIcon icon = new ImageIcon(gifURL);
		JLabel label = new JLabel(icon);
		label.setBounds(0, 0, 1000, 500);
		this.add(label);
	}

	public void defaultPage(){
		currentPage = "default";

		int sepAmo = 10;
		int buttonSize = 85;
		clearScreen(null);
		incomePanel();
		outcomePanel();
		housePanel();
		currentUserPanel();
		currentUserIncomeTotal();
		currentUserOutcomeTotal();
		currentUserRevenue();

		makeButton("New User", 15, 890, 10, buttonSize, buttonSize, e -> newUser());
		makeButton("Pick User", 15, 890, 10 + buttonSize + sepAmo, buttonSize, buttonSize, e -> chooseUser());
		makeButton("Delete User", 15, 890,  10 + buttonSize * 2 + sepAmo * 2, buttonSize, buttonSize, e -> deleteUser());
		makeButton("Settings", 15, 890, 10+ buttonSize * 3 + sepAmo * 3, buttonSize, buttonSize, e -> settings());
		
		makeButton("Income", 15, 795, 10 + buttonSize + sepAmo, buttonSize, buttonSize, e -> addIncome());
		makeButton("Outcome", 15, 795,  10 + buttonSize * 2 + sepAmo * 2, buttonSize, buttonSize, e -> addOutcome());
		makeButton("House", 15, 795, 10+ buttonSize * 3 + sepAmo * 3, buttonSize, buttonSize, e -> addHouse());
	}

	public void newUser(){
		currentPage = "";
		clearScreen(null);

		makeLabel("First Name", 15, 10,400, 100, 50, Color.white, Color.white, false);
		makeLabel("Last Name", 15, 120,400, 100, 50, Color.white, Color.white, false);

		makeTextField("Enter First Name", 20, 10, 440, 100, 50, Color.white, Color.black);
		makeTextField("Enter Last Name", 20, 120, 440, 100, 50, Color.white, Color.black);

		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());
		makeButton("Enter", 20, 230, 440, 100, 50, e -> createUser());
	}

	public void createUser(){
		currentUser = new User(textLabels.get("Enter First Name").getText(), textLabels.get("Enter Last Name").getText());
		defaultPage();
	}

	public void chooseUser(){
		currentPage = "";
		clearScreen(null);

		ButtonGroup buttonGroup = new ButtonGroup();
		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());

		int userNum = 1;
		int buttonX = 10;
		float fontSize = 30;
		for(User user: userList){
			JRadioButton button = new JRadioButton();
			button.setText(user.firstName + " " + user.lastName);
			buttonGroup.add(button);
			Font sizedFont = currentFont.deriveFont(fontSize);
			button.setFont(sizedFont);
			button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			button.setBackground(Color.white);
			button.setForeground(Color.black);
			button.setFocusPainted(false);
			if(userNum % 10 == 1 && userNum > 2)
			{
				buttonX += 285;
				userNum -= 10;
			}
			button.setBounds(buttonX, 20 + (userNum * 40), 280, 30);
			button.addActionListener(e -> pickUser(user));
			this.add(button);
			userNum++;
		}		
		repaint();
	}

	private void pickUser(User user){
		currentUser = user;
		defaultPage();
	}

	public void deleteUser(){
		currentPage = "";

		clearScreen(null);

		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());

		int userNum = 1;
		int buttonX = 10;
		float fontSize = 30;
		for(User user: userList){
			JRadioButton button = new JRadioButton();
			button.setText(user.firstName + " " + user.lastName);
			if(userNum % 10 == 1 && userNum > 2)
			{
				buttonX += 285;
				userNum -= 10;
			}
			button.setBounds(buttonX, 20 + (userNum * 40), 280, 30);
			Font sizedFont = currentFont.deriveFont(fontSize);
			button.setFont(sizedFont);
			button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			button.setBackground(Color.white);
			button.setForeground(Color.black);
			button.setFocusPainted(false);
			button.addActionListener(e -> popUpScreen(user));
			this.add(button);
			userNum++;
		}		
		repaint();
	}
	public void popUpScreen(User user){
		clearScreen(null);
		makeLabel("Are You Sure?", 15, 400, 200, 200, 100, Color.white, Color.black, true);
		makeButton("YES", 30, 400, 300, 90, 50, e -> removeUser(user));
		makeButton("NO", 30, 510, 300, 90, 50, e-> deleteUser());
	}
	public void removeUser(User user){
		userList.remove(user);
		defaultPage();
	}

	public void addIncome(){
		currentPage = "";

		clearScreen(null);
		addCalc(790, 10);
		makeLabel("Name", 15, 10,400, 100, 50, Color.white, Color.white, false);
		makeLabel("Amount", 15, 120,400, 100, 50, Color.white, Color.white, false);

		makeTextField("Name Of Income", 20, 10, 440, 100, 50, Color.white, Color.black);
		makeTextField("Amount Of Income", 20, 120, 440, 100, 50, Color.white, Color.black);

		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());
		makeButton("1 Week", 20, 230, 440, 100, 50, e -> addInO(1));
		makeButton("2 Weeks", 20, 340, 440, 100, 50, e -> addInO(2));
		makeButton("4 Weeks", 20, 450, 440, 100, 50, e -> addInO(4));
		makeButton("26 Weeks", 20, 560, 440, 100, 50, e -> addInO(26));
		makeButton("52 Weeks", 20, 670, 440, 100, 50, e -> addInO(52));


		int userNum = 1;
		int buttonX = 10;
		float fontSize = 30;
		for(Amount amount: currentUser.income){
			JRadioButton button = new JRadioButton();
			button.setText(amount.name + " $" + amount.amount);
			
			if(userNum % 11 == 1 && userNum > 2)
			{
				buttonX += 201;
				userNum -= 11;
			}
			button.setBounds(buttonX, 10 + (userNum * 31), 200, 30);
			Font sizedFont = currentFont.deriveFont(fontSize);
			button.setFont(sizedFont);			button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			button.setBackground(Color.white);
			button.setForeground(Color.black);
			button.setFocusPainted(false);			
			button.addActionListener(e -> currentUser.removeIncome(amount));
			this.add(button);
			userNum++;
		}
		
		repaint();
	}

	public void addInO(int perHowManyWeeks){
		String temp = "";
		int temp1 = 0;
		try{
			temp = textLabels.get("Name Of Income").getText();
		 	temp1 = Integer.parseInt(textLabels.get("Amount Of Income").getText());
			if(temp.length() > 0 ){
				if(temp1> 0){
				currentUser.newIncome(temp, temp1 / perHowManyWeeks, "");
				}
			}
			defaultPage();
		}
		catch (Exception e){
			makeLabel("Please Enter Valid Information", 10, 10, 10, 10, 10, Color.black, Color.white, true);
		}
		
	}

	public void addOutcome(){
		currentPage = "";

		clearScreen(null);
		addCalc(790, 10);
		makeLabel("Name", 15, 10,400, 100, 50, Color.white, Color.white, false);
		makeLabel("Amount", 15, 120,400, 100, 50, Color.white, Color.white, false);

		makeTextField("Name Of Expense", 20, 10, 440, 100, 50, Color.white, Color.black);
		makeTextField("Amount Of Expense", 20, 120, 440, 100, 50, Color.white, Color.black);

		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());
		makeButton("1 Week", 20, 230, 440, 100, 50, e -> addOutO(1));
		makeButton("2 Weeks", 20, 340, 440, 100, 50, e -> addOutO(2));
		makeButton("4 Weeks", 20, 450, 440, 100, 50, e -> addOutO(4));
		makeButton("26 Weeks", 20, 560, 440, 100, 50, e -> addOutO(26));
		makeButton("52 Weeks", 20, 670, 440, 100, 50, e -> addOutO(52));			
		int userNum = 1;
		int buttonX = 10;
		float fontSize = 30;
		for(Amount amount: currentUser.outcome){
			JRadioButton button = new JRadioButton();
			button.setText(amount.name + " $" + amount.amount);
			if(userNum % 11 == 1 && userNum > 2)
			{
				buttonX += 201;
				userNum -= 11;
			}
			button.setBounds(buttonX, 10 + (userNum * 31), 200, 30);
			Font sizedFont = currentFont.deriveFont(fontSize);
			button.setFont(sizedFont);			button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			button.setBackground(Color.white);
			button.setForeground(Color.black);
			button.setFocusPainted(false);
			button.addActionListener(e -> currentUser.removeOutcome(amount));
			this.add(button);
			userNum++;
		}
		
		repaint();
	}

	public void addOutO(int perHowManyWeeks){
		String temp = "";
		int temp1 = 0;
		try{
			temp = textLabels.get("Name Of Expense").getText();
		 	temp1 = Integer.parseInt(textLabels.get("Amount Of Expense").getText());
			 if(temp.length() > 0 ){
				if(temp1> 0){
					currentUser.addOutcome(temp, temp1 / perHowManyWeeks, "");
				}
			}
			defaultPage();
		}
		catch (Exception e){
			makeLabel("Please Enter Valid Information", 10, 10, 10, 10, 10, Color.black, Color.white, true);
		}
		
	}

	public void calcHouseOut(){
		double tmpAmount = 0;
		for(Amount houseOut : houseOutcome){
			tmpAmount += houseOut.amount;
		}
		householdOutcomePerWeek = tmpAmount;
	}
	
	public void addHouseOutcome(String name, int amount){
		currentPage = "";

		houseOutcome.add(new Amount(name, amount, "Null"));
		calcHouseOut();
	}
	
	
	public void calcHouseOutcome(){

		double tempAmount = 0;
		for(Amount outcomeAmount : houseOutcome){
			tempAmount += outcomeAmount.amount;
		}
		householdOutcomePerWeek = tempAmount;
	}

	public void addHouse(){		
		currentPage = "";

		clearScreen(null);
		addCalc(790, 10);

		makeLabel("Name", 15, 10,400, 100, 50, Color.white, Color.white, false);
		makeLabel("Amount", 15, 120,400, 100, 50, Color.white, Color.white, false);

		makeTextField("Name Of House Expense", 20, 10, 440, 100, 50, Color.white, Color.black);
		makeTextField("Amount Of House Expense", 20, 120, 440, 100, 50, Color.white, Color.black);

		makeButton("1 Week", 20, 230, 440, 100, 50, e -> addHouseO(1));
		makeButton("2 Weeks", 20, 340, 440, 100, 50, e -> addHouseO(2));
		makeButton("4 Weeks", 20, 450, 440, 100, 50, e -> addHouseO(4));
		makeButton("26 Weeks", 20, 560, 440, 100, 50, e -> addHouseO(26));
		makeButton("52 Weeks", 20, 670, 440, 100, 50, e -> addHouseO(52));	
		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());

		int userNum = 1;
		int buttonX = 10;
		float fontSize = 30;
		
		for(Amount amount: houseOutcome){
			JRadioButton button = new JRadioButton();
			button.setText(amount.name + " $" + amount.amount);
			
			if(userNum % 11 == 1 && userNum > 2){
				buttonX += 201;
				userNum -= 11;
			}
			
			button.setBounds(buttonX, 10 + (userNum * 31), 200, 30);
			Font sizedFont = currentFont.deriveFont(fontSize);
			button.setFont(sizedFont);			
			button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			button.setBackground(Color.white);
			button.setForeground(Color.black);
			button.setFocusPainted(false);
			button.addActionListener(e -> houseOutcome.remove(amount));
			this.add(button);
			userNum++;
		}
		repaint();
	}

	public void addHouseO(int perHowManyWeeks){
		String temp = "";
		int temp1 = 0;
		try{
			temp = textLabels.get("Name Of House Expense").getText();
		 	temp1 = Integer.parseInt(textLabels.get("Amount Of House Expense").getText());
			if(temp.length() > 0 ){
				if(temp1> 0){
					addHouseOutcome(temp, temp1 / perHowManyWeeks);
				}
			}
			defaultPage();
		}
		catch (Exception e){
		}
		
	}

	public void incomePanel(){
		int userNum = 1;
		for(Amount amount : currentUser.income){
			if(userNum < 16)
			makeLabel("   " + amount.name + " $" + amount.amount, 20, 10, 55 + (userNum * 25), 300, 25, Color.green, Color.black, true);
			userNum++;
		}
		repaint();
	}

	public void outcomePanel(){
		int userNum = 1;
		for(Amount amount : currentUser.outcome){
			if(userNum < 16)
			makeLabel("   " + amount.name + " $" + amount.amount, 20, 320, 55 + (userNum * 25), 300, 25, Color.red, Color.black, true);
			userNum++;
		}
		repaint();
	}

	public void housePanel(){
		int userNum = 1;
		for(Amount amount : houseOutcome){
			if(userNum < 12)
			makeLabel("   " + amount.name + " $" + amount.amount, 15, 630, 80 + (userNum * 25), 150, 25, Color.red, Color.black, true);
			userNum++;
		}
		repaint();
	}

	public void currentUserPanel(){
		makeLabel("  " + currentUser.firstName.charAt(0) + ". " + currentUser.lastName, 40, 630, 10, 250, 85, Color.white, Color.black, true);
		labelList.get("  " + currentUser.firstName.charAt(0) + ". " + currentUser.lastName).setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	}

	public void settings(){
		clearScreen(null);
		makeButton("Change Font", 15, 10, 10, 100, 40, e -> changeFont());
		makeButton("Change Theme", 15, 10, 60, 100, 40, e -> setTheme());
		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());
		makeButton("View Stats", 15, 10, 110, 100, 40, e -> viewStats());
	}

	public void setTheme(){
		clearScreen(null);
		makeButton("Retro", 10, 10, 10, 100, 40, e -> setTTheme("retro"));
		makeButton("Office", 10, 10, 60, 100, 40, e -> setTTheme("office"));
		makeButton("Cars", 10, 10, 110, 100, 40, e -> setTTheme("cars"));
		makeButton("Back", 20, 890, 450, 100, 40, e -> defaultPage());
	}
	public void setTTheme(String theme){
		switch (theme){
			case "retro":
				backgroundColor = retroColor;
				backgroundLogo = retro1Logo;
				currentFont = customFont;
				currentClickSound = retroButtonSound;
				break;
			case "office":
				backgroundColor = Color.lightGray;
				backgroundLogo = officeLogo;
				currentFont = new Font("Georgia", 1, 20);
				currentClickSound = buttonSound;

				break;
			case "cars":
				backgroundColor = Color.red;
				backgroundLogo = carsLogo;
				currentFont = new Font("Times New Roman", 1, 20);
				currentClickSound = buttonSound;

				break;
		}
		settings();
	}


	public void viewStats(){
		
	}

	public void currentUserIncomeTotal(){
		currentUser.calcIncome();
		if(currentUser.incomePerWeek > 0){
		makeLabel("  Income Weekly: " + currentUser.incomePerWeek, 20, 10, 10, 300, 50, Color.green, Color.black, true);
		}
	}
	public void currentUserOutcomeTotal(){
		currentUser.calcOutcome();
		if(currentUser.outcomePerWeek > 0){
		makeLabel("  Outcome Weekly: " + currentUser.outcomePerWeek, 20, 320, 10, 300, 50, Color.red, Color.black, true);
		}
	}

	public void changeFont(){
		clearScreen(null);
		makeButton("Arial", 20, 10, 10, 100, 40, e -> switchFont(new Font("arial", 1, 30)));
		makeButton("Pixel", 20, 10, 60, 100, 40, e -> switchFont(customFont));
		makeButton("Courier New", 15, 10, 110, 100, 40, e -> switchFont(new Font("Courier New", 1, 30)));
		makeButton("Georgia", 20, 10, 160, 100, 40, e -> switchFont(new Font("Georgia", 1, 30)));
		makeButton("Back", 20, 890, 450, 100, 40, e -> settings());
	}
	public void switchFont(Font font){
		currentFont = font;
		defaultPage();
	}
	
	public void currentUserRevenue(){
		Color labelColor = Color.green;
		if(currentUserRevenue < 0){
			labelColor = Color.red;
		}
		if(!(currentUserRevenue == 0 && currentUser.incomePerWeek == 0 && currentUser.outcomePerWeek == 0 && householdOutcomePerWeek == 0)){
		makeLabel(currentUser.firstName + " " + currentUser.lastName + " Revenue", 10, 795, 390, 180, 30, Color.white, Color.black, true);
		makeLabel(currentUserRevenue + " ", 30, 795, 430, 180, 60, labelColor, Color.white, true);
		}
	}
	
	public void calcRevenue(){
		currentUserRevenue = currentUser.incomePerWeek - currentUser.outcomePerWeek;
	}

	public void addCalc(int x, int y){
		makeLabel("Calculator", 25, x, y, 205, 40, Color.white, Color.black, true);
		labelList.get("Calculator").setHorizontalAlignment(JLabel.CENTER);
		y+=25;
		makeLabel("Enter Number", 17, x, y+25, 100, 40, Color.white, Color.black, true);
		makeLabel("Enter Number", 17, x+105, y+25, 100, 40, Color.white, Color.black, true);
		makeTextField("Num1", 20, x, y+70, 100, 40, Color.white, Color.black);
		makeTextField("Num2", 20, x+105, y+70, 100, 40, Color.white, Color.black);
		makeLabel("(+)(-)(*)(/)", 17, x, y + 115, 100, 30, Color.white, Color.black, true);
		makeLabel("Result", 20, x +105, y + 115, 100, 30, Color.white, Color.black, true);

		makeTextField("Operator",30, x, y+150, 100, 40, Color.white, Color.black);
		makeButton("Enter", 20, x, y+200, 205, 30, e -> calcNums());
		makeLabel("0.0", 20, x+105, y+150, 100, 40, Color.white, Color.black, true);
	}

	public void calcNums(){
		double num1 = Double.parseDouble(textLabels.get("Num1").getText());
		double num2 = Double.parseDouble(textLabels.get("Num2").getText());
		String ope = textLabels.get("Operator").getText();
		switch (ope){
			case "+":
			labelList.get("0.0").setText(num1 + num2 + "");
			break;
			case "-":
			labelList.get("0.0").setText(num1 - num2 + "");
			break;
			case "*":
			labelList.get("0.0").setText(num1 * num2 + "");

			break;
			case "/":
			labelList.get("0.0").setText(num1 / num2 + "");

			break;
		}
	}
}