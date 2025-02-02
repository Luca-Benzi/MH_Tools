package mhtool;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.*;

public final class View {
    private Optional<Controller> controller;
    private final JFrame mainFrame;
    private static final double WIDTH_PERC = 0.5;
    private static final double HEIGHT_PERC = 0.5;
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    String user;

    public View(Runnable onClose) {
        this.controller = Optional.empty();
        this.mainFrame = this.setupMainFrame(onClose);
    }

    private JFrame setupMainFrame(Runnable onClose) {
        var frame = new JFrame("MH Tool");
        frame.setMinimumSize(new Dimension((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC)));
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onClose.run();
                    System.exit(0);
                }
            }
        );

        return frame;
    }

    private Controller getController() {
        if (this.controller.isPresent()) {
            return this.controller.get();
        } else {
            throw new IllegalStateException(
                """
                The View's Controller is undefined, did you remember to call
                `setController` before starting the application?
                Remeber that `View` needs a reference to the controller in order
                to notify it of button clicks and other changes.
                """
            );
        }
    }

    public void setController(Controller controller) {
        Objects.requireNonNull(controller, "Set null controller in view");
        this.controller = Optional.of(controller);
    }

    public void loading() {
        freshPane(cp -> cp.add(new JLabel("Loading...", SwingConstants.CENTER)));
    }

    public void loginForm() {
        
        freshPane(cp -> {
        // Define components
        cp.setLayout(new BorderLayout());
        JLabel questionLabel = new JLabel("Admin or User", JLabel.CENTER);
        JLabel userLabel = new JLabel("Insert Username if registered:", JLabel.CENTER);
        JTextField textField1 = new JTextField(5);
        JButton adminButton = new JButton("ADMIN");
        JButton userButton = new JButton("USER");
        JButton register = new JButton("REGISTER");
        
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textField1.setFont(new Font("Arial", Font.PLAIN, 14));
        adminButton.setBackground(Color.LIGHT_GRAY);
        userButton.setBackground(Color.LIGHT_GRAY);

        textField1.setMaximumSize(new Dimension(150, 25)); 
        textField1.setPreferredSize(new Dimension(150, 25)); 

        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
        newPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        newPanel.setBackground(Color.WHITE);

        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField1.setAlignmentX(Component.CENTER_ALIGNMENT);
        register.setAlignmentX(Component.CENTER_ALIGNMENT);

        newPanel.add(questionLabel);
        newPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        newPanel.add(adminButton);
        newPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        newPanel.add(userButton);
        newPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        newPanel.add(userLabel);
        newPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        newPanel.add(textField1);
        newPanel.add(register);

        cp.add(newPanel, BorderLayout.CENTER);

        

        adminButton.addActionListener(e -> this.getController().admin());
        userButton.addActionListener(e -> {
            if(this.getController().userExists(textField1.getText()) || textField1.getText().equals("")){
                this.getController().user(textField1.getText());
            }
            else {
                JOptionPane.showMessageDialog(null,  "User not present in the system");
            }
        });
        register.addActionListener(e -> this.getController().register());
    });
}

    public void user(String username) {
    freshPane(cp -> {
        cp.setLayout(new GridBagLayout());
        this.user = username;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Welcome Label
        JLabel welcome = new JLabel("Welcome, " + (username.trim().isEmpty() ? "Guest" : username) + "!");
        gbc.gridx = 1; 
        gbc.gridy = 0; 
        cp.add(welcome, gbc);
        
        // Monsters Dropdown
        JLabel monstersLabel = new JLabel("Select a Monster:");
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        cp.add(monstersLabel, gbc);

        List<String> monsters = this.getController().getMonsters();
        JComboBox<String> monstersBox = new JComboBox<>(monsters.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(monstersBox, gbc);

        JButton monsterButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(monsterButton, gbc);
        monsterButton.addActionListener(event -> 
            this.getController().monsterView((String) monstersBox.getSelectedItem())
        );

        // Category Dropdown
        JLabel categoryLabel = new JLabel("Select a Category:");
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        cp.add(categoryLabel, gbc);

        List<String> category = this.getController().getCategoryList();
        JComboBox<String> categoryBox = new JComboBox<>(category.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(categoryBox, gbc);

        JButton categoryButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(categoryButton, gbc);
        categoryButton.addActionListener(event -> 
            this.getController().categoryView((String) categoryBox.getSelectedItem())
        );

        // Games Dropdown
        JLabel gamesLabel = new JLabel("Select a Game:");
        gbc.gridx = 0; 
        gbc.gridy = 3; 
        cp.add(gamesLabel, gbc);

        List<String> games = this.getController().getGames();
        JComboBox<String> gamesBox = new JComboBox<>(games.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(gamesBox, gbc);

        JButton gameButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(gameButton, gbc);
        gameButton.addActionListener(event -> 
            this.getController().gameView(((String) gamesBox.getSelectedItem()))
        );
        // Maps Dropdown
        JLabel mapsLabel = new JLabel("Select a Map:");
        gbc.gridx = 0; 
        gbc.gridy = 4; 
        cp.add(mapsLabel, gbc);

        List<String> maps = this.getController().getMaps(); 
        JComboBox<String> mapsBox = new JComboBox<>(maps.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(mapsBox, gbc);

        JButton mapButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(mapButton, gbc);
        mapButton.addActionListener(event -> 
        this.getController().mapView((String)mapsBox.getSelectedItem())
        );

        // Materials Dropdown
        JLabel materialsLabel = new JLabel("Select a Material:");
        gbc.gridx = 0; 
        gbc.gridy = 5; 
        cp.add(materialsLabel, gbc);

        List<String> materials = this.getController().getMaterials(); 
        JComboBox<String> materialsBox = new JComboBox<>(materials.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(materialsBox, gbc);

        JButton materialButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(materialButton, gbc);
        materialButton.addActionListener(event -> 
        this.getController().materialView((String) materialsBox.getSelectedItem())
        );

        // Quests Dropdown
        JLabel questsLabel = new JLabel("Select a Quest:");
        gbc.gridx = 0; 
        gbc.gridy = 6; 
        cp.add(questsLabel, gbc);

        List<String> quests = this.getController().getQuests().stream().sorted().collect(Collectors.toList()); 
        JComboBox<String> questsBox = new JComboBox<>(quests.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(questsBox, gbc);

        JButton questsButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(questsButton, gbc);
        questsButton.addActionListener(event -> 
        this.getController().questView((String) questsBox.getSelectedItem())
        );

        // Device Dropdown
        JLabel deviceLabel = new JLabel("Select a Device:");
        gbc.gridx = 0; 
        gbc.gridy = 7; 
        cp.add(deviceLabel, gbc);

        List<String> device = this.getController().getDeviceList(); 
        JComboBox<String> deviceBox = new JComboBox<>(device.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(deviceBox, gbc);

        JButton deviceButton = new JButton("Go");
        gbc.gridx = 2; 
        cp.add(deviceButton, gbc);
        deviceButton.addActionListener(event -> 
        this.getController().deviceView((String) deviceBox.getSelectedItem())
        );

        //Character section
        JLabel characterLabel = new JLabel("Character:");
        gbc.gridx = 0; 
        gbc.gridy = 8; 
        cp.add(characterLabel, gbc);

        JButton addCharButton = new JButton("Add new character");
        gbc.gridx = 1; 
        cp.add(addCharButton, gbc);
        addCharButton.addActionListener(event -> {
            if(username.trim().isEmpty())
            {   
                JOptionPane.showMessageDialog(null, "User not logged in, function anavailable");
            } else {
                this.getController().addCharView(this.user);
            }
            
        });
        JButton viewCharButton = new JButton("View your characters");
        gbc.gridx = 2; 
        cp.add(viewCharButton, gbc);
        viewCharButton.addActionListener(event -> {
            if(username.trim().isEmpty())
            {   
                JOptionPane.showMessageDialog(null, "User not logged in, function anavailable");
            } else {
                this.getController().viewCharView(this.user);
            }
        });

        //Hunt Request
        JLabel huntRequestLabel = new JLabel("Hunt Requests:");
        gbc.gridx = 0; 
        gbc.gridy = 9; 
        cp.add(huntRequestLabel, gbc);

        JButton huntRequestButton = new JButton("Create new Hunt Request");
        gbc.gridx = 1; 
        cp.add(huntRequestButton, gbc);
        huntRequestButton.addActionListener(event ->  {
            if(username.trim().isEmpty())
            {   
                JOptionPane.showMessageDialog(null, "User not logged in, function anavailable");
            } else {
                this.getController().createRequestView(this.user);
            }
        });
        JButton huntRequestJoinButton = new JButton("Apply to existing Hunt Request");
        gbc.gridx = 2; 
        cp.add(huntRequestJoinButton, gbc);
        huntRequestJoinButton.addActionListener(event -> {
            if(username.trim().isEmpty())
            {   
                JOptionPane.showMessageDialog(null, "User not logged in, function anavailable");
            } else {
                this.getController().joinRequestView(this.user);
            }
        });

        JLabel statisticalLabel = new JLabel("Various statistical informations");
        gbc.gridx = 0; 
        gbc.gridy = 10; 
        cp.add(statisticalLabel, gbc);
        JButton statisticalButton = new JButton("go");
        gbc.gridx = 1; 
        cp.add(statisticalButton, gbc);
        statisticalButton.addActionListener(e -> this.getController().statisticalView());

        // Bottom Panel with Back Button
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().loginForm());
        gbc.gridx = 5; 
        gbc.gridy = 10; 
        cp.add(back, gbc);
    });
}

public void monsterView(String monster) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(monster, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Info Panel (Variant + Category)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Center elements with spacing

        JLabel variantLabel = new JLabel("Variant: " + this.getController().getVariant(monster));
        variantLabel.setFont(variantLabel.getFont().deriveFont(Font.ITALIC, 18f));

        JLabel categoryLabel = clickableLabel("Category: " + this.getController().getCategory(monster), 
            () -> this.getController().categoryView(this.getController().getCategory(monster)));
        categoryLabel.setFont(categoryLabel.getFont().deriveFont(Font.PLAIN, 18f));

        infoPanel.add(variantLabel);
        infoPanel.add(categoryLabel);

        // Add both components to titlePanel
        titlePanel.add(title);
        titlePanel.add(infoPanel);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Games this monster is present in", this.getController().getGamesWith(monster), 
            game -> this.getController().gameView(game)));

        contentPanel.add(createSection("Maps this monster is present in", this.getController().getMapsWith(monster), 
            map -> this.getController().mapView(map)));

        contentPanel.add(createSection("Materials Dropped by this monster", this.getController().getMaterialsFromMonster(monster), 
            material -> this.getController().materialView(material)));

        contentPanel.add(createSection("Quests where this monster is the target", this.getController().getQuestsWithMonster(monster), 
            quest -> this.getController().questView(quest)));

        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void gameView(String game) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(game, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Info Panel (Variant + Category)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Center elements with spacing

        JLabel releaseLabel = new JLabel("Release date: " + this.getController().getReleaseDate(game));
        releaseLabel.setFont(releaseLabel.getFont().deriveFont(Font.ITALIC, 18f));

        JPanel devicePanel = new JPanel();
        devicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Ensures proper spacing

        JLabel deviceTitle = new JLabel("Devices: ");
        deviceTitle.setFont(deviceTitle.getFont().deriveFont(Font.BOLD, 18f));
        devicePanel.add(deviceTitle);

        // Get the list of devices and create clickable labels for each
        List<String> devices = this.getController().getReleasedOnList(game);
        for (String device : devices) {
            JLabel deviceLabel = clickableLabel(device, () -> this.getController().deviceView(device));
            deviceLabel.setFont(deviceLabel.getFont().deriveFont(Font.PLAIN, 18f));
            devicePanel.add(deviceLabel);
        }

        infoPanel.add(releaseLabel);
        infoPanel.add(devicePanel);

        // Add both components to titlePanel
        titlePanel.add(title);
        titlePanel.add(infoPanel);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Monsters present in this game", this.getController().getMonstersIn(game), 
            monster -> this.getController().monsterView(monster)));

        contentPanel.add(createSection("Maps present in this game", this.getController().getMapsfromGame(game), 
            map -> this.getController().mapView(map)));

        contentPanel.add(createSection("Quests present in this game", this.getController().getQuestsfromGame(game), 
            material -> this.getController().questView(material)));

         //contentPanel.add(createSection("Quests where this monster is the target", this.getController().getQuests(), 
           // quest -> this.getController().questView(quest)));

        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void mapView(String map) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(map, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Info Panel (Variant + Category)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Center elements with spacing


        JLabel gameLabel = clickableLabel("Game: " + this.getController().getGamefromMap(map), 
            () -> this.getController().gameView(this.getController().getGamefromMap(map)));
        gameLabel.setFont(gameLabel.getFont().deriveFont(Font.PLAIN, 18f));

        
        infoPanel.add(gameLabel);

        // Add both components to titlePanel
        titlePanel.add(title);
        titlePanel.add(infoPanel);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Monsters present in this map: ", this.getController().getMonstersInMap(map), 
            monster -> this.getController().monsterView(monster)));

        contentPanel.add(createSection("Quests that take place in this map: ", this.getController().getQuestsFromMap(map), 
            quest -> this.getController().questView(quest)));

        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void materialView(String material) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(material, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Info Panel (Variant + Category)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Center elements with spacing


        JLabel monsterLabel = clickableLabel("Monster: " + this.getController().getMonsterFromMaterial(material), 
            () -> this.getController().monsterView(this.getController().getMonsterFromMaterial(material)));
        monsterLabel.setFont(monsterLabel.getFont().deriveFont(Font.PLAIN, 18f));
        JLabel rarityLabel = new JLabel("Rarity: " + this.getController().getRarityfromMaterial(material));
        rarityLabel.setFont(rarityLabel.getFont().deriveFont(Font.ITALIC, 18f));


        
        infoPanel.add(monsterLabel);
        infoPanel.add(rarityLabel);

        // Add both components to titlePanel
        titlePanel.add(title);
        titlePanel.add(infoPanel);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Quests that reward this item: ", this.getController().getQuestsFromMaterial(material), 
            quest -> this.getController().questView(quest)));


        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}
   
public void questView(String quest) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(quest, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Info Panel (Variant + Category)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Center elements with spacing


        JLabel gameLabel = clickableLabel("Game: " + this.getController().getGameFromQuest(quest), 
            () -> this.getController().gameView(this.getController().getGameFromQuest(quest)));
        gameLabel.setFont(gameLabel.getFont().deriveFont(Font.PLAIN, 18f));
        JLabel mapLabel = clickableLabel("Map: " + this.getController().getMapFromQuest(quest), 
        () -> this.getController().mapView(this.getController().getMapFromQuest(quest)));
        mapLabel.setFont(mapLabel.getFont().deriveFont(Font.PLAIN, 18f));
        infoPanel.add(gameLabel);
        infoPanel.add(mapLabel);

        // Add both components to titlePanel
        titlePanel.add(title);
        titlePanel.add(infoPanel);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Monsters target of this quest: ", this.getController().getMonstersInQuest(quest), 
            monster -> this.getController().monsterView(monster)));
        contentPanel.add(createSection("Rewards from this quest: ", this.getController().getMaterialsFromQuest(quest), 
            material -> this.getController().materialView(material)));   


        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void categoryView(String category) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(category, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Add both components to titlePanel
        titlePanel.add(title);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Monsters in this category: ", this.getController().getMonstersFromcategory(category), 
            monster -> this.getController().monsterView(monster)));
         
        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void deviceView(String device) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel(device, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 60f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Add both components to titlePanel
        titlePanel.add(title);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Games released on this platform: ", this.getController().getGamesFromDevice(device), 
            game -> this.getController().gameView(game)));
         
        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void addCharView(String user) {
    freshPane(cp -> {
        cp.setLayout(new GridBagLayout());
        this.user = user;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel userLabel = new JLabel("Insert In-Game Name: ");
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        cp.add(userLabel, gbc);
        JTextField IGNField = new JTextField(20);
        gbc.gridx = 1; 
        gbc.gridy = 1; 
        cp.add(IGNField, gbc);

        JLabel gameLabel = new JLabel("Insert Game: ");
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        cp.add(gameLabel, gbc);
        List<String> games = this.getController().getGames();
        JComboBox<String> gamesBox = new JComboBox<>(games.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(gamesBox, gbc);


        JButton addButton = new JButton("Add");
        gbc.gridx = 1; 
        gbc.gridy = 3;
        cp.add(addButton, gbc);
        addButton.addActionListener(event -> {
            if( IGNField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,  "Failed to add character, one or more fields are empty");
            } else {
                this.getController().insertCharacter(user, (String) gamesBox.getSelectedItem(), IGNField.getText());
            }
        });

        // Bottom Panel with Back Button
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        gbc.gridx = 5; 
        gbc.gridy = 10; 
        cp.add(back, gbc);
    });
}

public void viewCharView(String user) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        // Create a panel to contain title and infoPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel title = new JLabel("Characters owned by: " + user, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 30f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Ensures visibility

        // Add both components to titlePanel
        titlePanel.add(title);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createSection("Characters: ", 
            this.getController().getChar().stream().filter(e -> e.username.equals(user)).map(e -> e.toString()).toList(), e -> {}
            ));
         
        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(title.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Ensure visibility and add to main panel
        cp.add(titlePanel, BorderLayout.NORTH);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}

public void createRequestView(String user) {
    freshPane(cp -> {
        cp.setLayout(new GridBagLayout());
        this.user = user;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel gamelabel = new JLabel("Game: ");
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        cp.add(gamelabel, gbc);
        List<String> games = this.getController().getGames();
        JComboBox<String> gamesBox = new JComboBox<>(games.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(gamesBox, gbc);
        

        JLabel charLabel = new JLabel("Insert character: ");
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        cp.add(charLabel, gbc);
        List<String> characters = this.getController().getChar()
                                    .stream()
                                    .filter(e -> e.username.equals(user))
                                    .filter(e -> e.gameName.equals((String) gamesBox.getSelectedItem()))
                                    .map(e -> e.ingameName)
                                    .collect(Collectors.toList());
        JComboBox<String> charBox = new JComboBox<>(characters.toArray(new String[0]));
       
        gbc.gridx = 1; 
        cp.add(charBox, gbc);

        JLabel monsterJLabel = new JLabel("Select targets: ");
        gbc.gridx = 0; 
        gbc.gridy = 3; 
        cp.add(monsterJLabel,gbc);
        List<String> mon = this.getController().getMonstersIn((String) gamesBox.getSelectedItem());
        mon.add(0, "none");
        JComboBox<String> monsterBox1 = new JComboBox<>(mon.toArray(new String[0]));
        gbc.gridx = 1; 
        cp.add(monsterBox1,gbc);
        JComboBox<String> monsterBox2 = new JComboBox<>(mon.toArray(new String[0]));
        gbc.gridx = 2; 
        cp.add(monsterBox2,gbc);
        JComboBox<String> monsterBox3 = new JComboBox<>(mon.toArray(new String[0]));
        gbc.gridx = 3; 
        cp.add(monsterBox3,gbc);



        gamesBox.addActionListener( e -> {
            List<String> chara = this.getController().getChar()
                                    .stream()
                                    .filter(i -> i.username.equals(user))
                                    .filter(i -> i.gameName.equals((String) gamesBox.getSelectedItem()))
                                    .map(i -> i.ingameName)
                                    .collect(Collectors.toList());
            charBox.removeAllItems();
            chara.forEach(charBox::addItem);
            List<String> mons = this.getController().getMonstersIn((String) gamesBox.getSelectedItem());
            mons.add(0, "none");
            monsterBox1.removeAllItems();
            monsterBox2.removeAllItems();
            monsterBox3.removeAllItems();
            mons.forEach(monsterBox1::addItem);
            mons.forEach(monsterBox2::addItem);
            mons.forEach(monsterBox3::addItem);
            
        });

        JButton addButton = new JButton("create");
        gbc.gridx = 2; 
        gbc.gridy = 4;
        cp.add(addButton, gbc);
        addButton.addActionListener(event -> {
            if (Objects.isNull(charBox.getSelectedItem()))
            {
                JOptionPane.showMessageDialog(null,  "Failed to create Request, you have no character in this game");
            }else {
                if( monsterBox1.getSelectedItem().equals("none") && monsterBox2.getSelectedItem().equals("none") && monsterBox3.getSelectedItem().equals("none")) {
                    JOptionPane.showMessageDialog(null,  "Failed to create Request, select at least one monster");
                } else {
                    List<String> monsters = new ArrayList<>();
                        if (!monsterBox1.getSelectedItem().equals("none")) { monsters.add((String)monsterBox1.getSelectedItem());}
                        if (!monsterBox2.getSelectedItem().equals("none")) { monsters.add((String)monsterBox2.getSelectedItem());}
                        if (!monsterBox3.getSelectedItem().equals("none")) { monsters.add((String)monsterBox3.getSelectedItem());}
                        this.getController().createRequest(user, (String) gamesBox.getSelectedItem(), 
                        monsters,
                        user);
                }
            }
        });

        // Bottom Panel with Back Button
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        gbc.gridx = 5; 
        gbc.gridy = 10; 
        cp.add(back, gbc);
        
        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}
public void joinRequestView(String user) {
    freshPane(cp -> {
        cp.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

        infoPanel.add(new JLabel("Game: "));
        List<String> games = this.getController().getGames();
        JComboBox<String> gamesBox = new JComboBox<>(games.toArray(new String[0]));
        infoPanel.add(gamesBox);
        infoPanel.add(new JLabel("Character: "));
        List<String> character = this.getController().getChar()
                                    .stream()
                                    .filter(e -> e.username.equals(user))
                                    .filter(e -> e.gameName.equals((String) gamesBox.getSelectedItem()))
                                    .map(e -> e.ingameName)
                                    .collect(Collectors.toList());
        JComboBox<String> charBox = new JComboBox<>(character.toArray(new String[0]));
        infoPanel.add(charBox);
        
        
        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create a wrapper panel that will hold the section content
        JPanel sectionWrapper = new JPanel(new BorderLayout());

        // Create initial content and add it to the wrapper
        JPanel sectionPanel = createSection(
            "Hunt Requests Available for this game: ", 
            this.getController().getHuntRequestsByGame((String) gamesBox.getSelectedItem()), 
            huntRequest -> {  
                int colonIndex = huntRequest.indexOf(":");
                if (colonIndex != -1) {  
                    String requestIdStr = huntRequest.substring(0, colonIndex);
                    this.getController().addCharacterOwnerToGroup((String) charBox.getSelectedItem(), Integer.valueOf(requestIdStr));
                }
            }
        );
        sectionWrapper.add(sectionPanel, BorderLayout.CENTER);
        contentPanel.add(sectionWrapper);

        // ActionListener for gamesBox
        gamesBox.addActionListener(e -> {
            // Update character list based on selected game
            List<String> chara = this.getController().getChar()
                                    .stream()
                                    .filter(i -> i.username.equals(user))
                                    .filter(i -> i.gameName.equals((String) gamesBox.getSelectedItem()))
                                    .map(i -> i.ingameName)
                                    .collect(Collectors.toList());

            charBox.removeAllItems();
            chara.forEach(charBox::addItem);

            // Get new hunt requests
            List<String> huntRequests = this.getController().getHuntRequestsByGame((String) gamesBox.getSelectedItem());

            // Remove old section content but keep the wrapper
            sectionWrapper.removeAll();

            // Create new section and add it inside the existing wrapper
            JPanel updatedSection = createSection("Hunt Requests Available for this game: ", huntRequests, 
                huntRequest -> { 
                    int colonIndex = huntRequest.indexOf(":");
                    if (colonIndex != -1) { 
                        String requestIdStr = huntRequest.substring(0, colonIndex);
                        this.getController().addCharacterOwnerToGroup((String) charBox.getSelectedItem(), Integer.valueOf(requestIdStr));
                    }
                });

            sectionWrapper.add(updatedSection, BorderLayout.CENTER);

            // Refresh UI without removing the wrapper
            sectionWrapper.revalidate();
            sectionWrapper.repaint();
        });
        // Make the content panel the same width as the title
        contentPanel.setPreferredSize(new Dimension(infoPanel.getPreferredSize().width, contentPanel.getPreferredSize().height));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cp.add(scrollPane, BorderLayout.CENTER);

        // Ensure visibility and add to main panel
        cp.add(infoPanel, BorderLayout.NORTH);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.addActionListener(e -> this.getController().user(this.user));
        bottomPanel.add(back);

        cp.add(bottomPanel, BorderLayout.SOUTH);

        this.mainFrame.pack();
        this.mainFrame.setMinimumSize(new Dimension(800, 600));
    });
}
    public void statisticalView(){
        freshPane(cp -> {
            cp.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JButton mostHunted = new JButton("View Most Hunted Monsters");
            gbc.gridx = 0; 
            gbc.gridy = 0; 
            cp.add(mostHunted, gbc);
            JButton leastHunted = new JButton("View least Hunted Monsters");
            gbc.gridx = 1; 
            cp.add(leastHunted, gbc);
            JButton mostPlayed = new JButton("View Games played the most");
            gbc.gridx = 0; 
            gbc.gridy = 1; 
            cp.add(mostPlayed, gbc);
            JButton leastPlayed = new JButton("View Games played the least");
            gbc.gridx = 1;  
            cp.add(leastPlayed, gbc);
            JButton mostQuest = new JButton("View Most Successfull quests");
            gbc.gridx = 0; 
            gbc.gridy = 2; 
            cp.add(mostQuest, gbc);
            JButton leastQuest = new JButton("View least Successfull quests");
            gbc.gridx = 1;  
            cp.add(leastQuest, gbc);
            JButton mostMonster = new JButton("View Most Successfully hunted Monsters");
            gbc.gridx = 0; 
            gbc.gridy = 3; 
            cp.add(mostMonster, gbc);
            JButton leastMonster = new JButton("View least Successfully hunter Monsters");
            gbc.gridx = 1;  
            cp.add(leastMonster, gbc);
            JButton mostPlayer = new JButton("View Most Successfull Hunters");
            gbc.gridx = 0; 
            gbc.gridy = 4; 
            cp.add(mostPlayer, gbc);
            JButton leastPlayer = new JButton("View least Successfull hunter ");
            gbc.gridx = 1;  
            cp.add(leastPlayer, gbc);

            JTextArea textArea = new JTextArea(15, 30); // Large text area (10 rows x 30 columns)
            textArea.setEditable(false); // Make it unmodifiable
            textArea.setWrapStyleWord(true); // Wraps text at word boundaries
            textArea.setLineWrap(true); // Enables line wrapping
            JScrollPane scrollPane = new JScrollPane(textArea); // Add scroll functionality
            gbc.gridx = 2; // Set it to the right of the buttons
            gbc.gridy = 0;
            gbc.gridheight = 5; // Make it span multiple rows
            gbc.fill = GridBagConstraints.BOTH; // Fill the available space
            cp.add(scrollPane, gbc);

            mostHunted.addActionListener(e -> {
                List<String> topMonsters = this.getController().getTopHuntedMonsters();
                String result = String.join("\n", topMonsters); // Join with new lines
                textArea.setText(result);
            });
            leastHunted.addActionListener(e -> {
                List<String> topMonsters = this.getController().getBotHuntedMonsters();
                String result = String.join("\n", topMonsters); // Join with new lines
                textArea.setText(result); 
            });
            mostPlayed.addActionListener(e -> {
                List<String> topGames = this.getController().getTopPlayedGames();
                String result = String.join("\n", topGames); // Join with new lines
                textArea.setText(result);
            });
            leastPlayed.addActionListener(e -> {
                List<String> leastGames = this.getController().getBotPlayedGames();
                String result = String.join("\n", leastGames); // Join with new lines
                textArea.setText(result); 
            });
            mostQuest.addActionListener(e -> {
                List<String> topQuest = this.getController().getTopQuestSuccessRates();
                String result = String.join("\n", topQuest); // Join with new lines
                textArea.setText(result);
            });
            leastQuest.addActionListener(e -> {
                List<String> botQuest = this.getController().getBotQuestSuccessRates();
                String result = String.join("\n", botQuest); // Join with new lines
                textArea.setText(result); 
            });
            mostMonster.addActionListener(e -> {
                List<String> topMonster = this.getController().getTopMonsterSuccessRates();
                String result = String.join("\n", topMonster); // Join with new lines
                textArea.setText(result);
            });
            leastMonster.addActionListener(e -> {
                List<String> botMonster = this.getController().getBotMonsterSuccessRates();
                String result = String.join("\n", botMonster); // Join with new lines
                textArea.setText(result); 
            });
            mostPlayer.addActionListener(e -> {
                List<String> topPlayer = this.getController().getTopSuccessfulHunters();
                String result = String.join("\n", topPlayer); // Join with new lines
                textArea.setText(result);
            });
            leastPlayer.addActionListener(e -> {
                List<String> botPlayer = this.getController().getBotSuccessfulHunters();
                String result = String.join("\n", botPlayer); // Join with new lines
                textArea.setText(result); 
            });


            // Bottom Panel with Back Button
            JButton back = new JButton("Back");
            back.setFont(new Font("Arial", Font.BOLD, 16));
            back.addActionListener(e -> this.getController().user(this.user));
            gbc.gridx = 5; 
            gbc.gridy = 10; 
            cp.add(back, gbc);
            
            
        });
    }

    public void admin() {
        freshPane( cp -> {
            cp.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.fill = GridBagConstraints.HORIZONTAL;

        //ADD MONSTER
            JLabel addMonster = new JLabel("Add new Monster:");
            gbc.gridx = 0; 
            gbc.gridy = 1; 
            cp.add(addMonster, gbc);
            JLabel addMonsterName = new JLabel("Name :");
            gbc.gridy = 0;
            gbc.gridx = 1;  
            cp.add(addMonsterName, gbc);
            JLabel addMonsterCat = new JLabel("Category:");
            gbc.gridx = 2;  
            cp.add(addMonsterCat, gbc);
            JLabel addMonsterVar = new JLabel("Variant:");
            gbc.gridx = 3;  
            cp.add(addMonsterVar, gbc);
            /* 
            JLabel addMonsterMap = new JLabel("Map:");
            gbc.gridx = 5;  
            cp.add(addMonsterMap, gbc);
            */
            JTextField monsterName = new JTextField(10);
            gbc.gridx = 1; 
            gbc.gridy = 1; 
            cp.add(monsterName, gbc);
            List<String> cat = this.getController().getCategoryList();
            JComboBox<String> monsterCat = new JComboBox<>(cat.toArray(new String[0]));
            gbc.gridx = 2; 
            cp.add(monsterCat, gbc);
            List<String> var = new ArrayList<>(List.of("True", "False"));
            JComboBox<String> varBox = new JComboBox<>(var.toArray(new String[0]));
            gbc.gridx = 3; 
            cp.add(varBox, gbc);
            JButton addMonsterButton = new JButton("add");
            gbc.gridx = 4; 
            cp.add(addMonsterButton, gbc); 
            
        //ADD GAME
            JLabel addGameName = new JLabel("Name:");
            gbc.gridx = 1;  
            gbc.gridy = 2;
            cp.add(addGameName, gbc);
            JLabel addGameDate = new JLabel("Release date(yyyy-mm-dd):");
            gbc.gridx = 2;  
            cp.add(addGameDate, gbc);
            JLabel addGame = new JLabel("Add new Game:");
            gbc.gridx = 0; 
            gbc.gridy = 3; 
            cp.add(addGame, gbc);
            JTextField gameName = new JTextField(10);
            gbc.gridx = 1; 
            cp.add(gameName, gbc);
            JTextField gameDate = new JTextField(10);
            gbc.gridx = 2;  
            cp.add(gameDate, gbc);
            JButton addGameButton = new JButton("add");
            gbc.gridx = 3; 

        //ADD MAP
            cp.add(addGameButton, gbc); 
            JLabel addMapName = new JLabel("Name:");
            gbc.gridx = 1;  
            gbc.gridy = 4;
            cp.add(addMapName, gbc); 
            JLabel addMapGame = new JLabel("Game:");
            gbc.gridx = 2;  
            cp.add(addMapGame, gbc);
            JLabel addMap = new JLabel("Add New Map:");
            gbc.gridx = 0;  
            gbc.gridy = 5;
            cp.add(addMap, gbc);
            JTextField mapName = new JTextField(10);
            gbc.gridx = 1; 
            cp.add(mapName, gbc);
            List<String> games = this.getController().getGames();
            JComboBox<String> gamesBox = new JComboBox<>(games.toArray(new String[0]));
            gbc.gridx = 2; 
            cp.add(gamesBox, gbc);
            JButton addMapButton = new JButton("add");
            gbc.gridx = 3; 
            cp.add(addMapButton, gbc); 

        //ADD DEVICE
            JLabel addDeviceName = new JLabel("Name:");
            gbc.gridx = 1;  
            gbc.gridy = 6;
            cp.add(addDeviceName, gbc); 
            JLabel addDevice = new JLabel("Add Device:");
            gbc.gridx = 0;  
            gbc.gridy = 7;
            cp.add(addDevice, gbc); 
            JTextField deviceName = new JTextField(10);
            gbc.gridx = 1; 
            cp.add(deviceName, gbc);
            JButton addDeviceButton = new JButton("add");
            gbc.gridx = 2; 
            cp.add(addDeviceButton, gbc); 

        //ADD GAME TO DEVICE
            JLabel addDeviceGame = new JLabel("Game:");
            gbc.gridx = 4;  
            gbc.gridy = 6;
            cp.add(addDeviceGame, gbc); 
            JLabel addDeviceDev = new JLabel("Device:");
            gbc.gridx = 5;  
            cp.add(addDeviceDev, gbc); 
            JLabel addDevGame = new JLabel("Add Game to Device:");
            gbc.gridx = 3; 
            gbc.gridy = 7; 
            cp.add(addDevGame, gbc);
            JComboBox<String> gamesBox2 = new JComboBox<>(games.toArray(new String[0]));
            gbc.gridx = 4; 
            cp.add(gamesBox2, gbc);
            List<String> devices = this.getController().getDeviceList();
            JComboBox<String> deviceBox = new JComboBox<>(devices.toArray(new String[0]));
            gbc.gridx = 5; 
            cp.add(deviceBox, gbc);
            JButton addDeviceGameButton = new JButton("add");
            gbc.gridx = 6; 
            cp.add(addDeviceGameButton, gbc); 

        //ADD MONSTER TO GAME
            JLabel addMonsterMon = new JLabel("Monster:");
            gbc.gridx = 1;  
            gbc.gridy = 8;
            cp.add(addMonsterMon, gbc); 
            JLabel addMonsterGame= new JLabel("Game:");
            gbc.gridx = 2;  
            cp.add(addMonsterGame, gbc); 
            JLabel addMonsterG = new JLabel("Add Monster to game:");
            gbc.gridx = 0; 
            gbc.gridy = 9; 
            cp.add(addMonsterG, gbc);
            List<String> monsters = this.getController().getMonsters();
            JComboBox<String> monsterBox = new JComboBox<>(monsters.toArray(new String[0]));
            gbc.gridx = 1; 
            cp.add(monsterBox, gbc);
            JComboBox<String> gamesBox3 = new JComboBox<>(games.toArray(new String[0]));
            gbc.gridx = 2; 
            cp.add(gamesBox3, gbc);
            
            JButton addMonsterGameButton = new JButton("add");
            gbc.gridx = 3; 
            cp.add(addMonsterGameButton, gbc);  

        //ADD MONSTER TO MAP
            JLabel addMonsterMonMap = new JLabel("Monster:");
            gbc.gridx = 1;  
            gbc.gridy = 10;
            cp.add(addMonsterMonMap, gbc); 
            JLabel addMonsterMap= new JLabel("Map:");
            gbc.gridx = 2;  
            cp.add(addMonsterMap, gbc); 
            JLabel addMonsterM = new JLabel("Add Monster to map:");
            gbc.gridx = 0; 
            gbc.gridy = 11; 
            cp.add(addMonsterM, gbc);
            JComboBox<String> monsterBox2 = new JComboBox<>(monsters.toArray(new String[0]));
            gbc.gridx = 1; 
            cp.add(monsterBox2, gbc);
            List<String> map = new ArrayList<>();
            for (String game : this.getController().getGamesWith((String) monsterBox2.getSelectedItem())) {
                
                map.addAll(this.getController().getMapsfromGame(game));
            }
            JComboBox<String> mapBox = new JComboBox<>(map.toArray(new String[0]));
            gbc.gridx = 2; 
            cp.add(mapBox, gbc);
            
            JButton addMonsterMapButton = new JButton("add");
            gbc.gridx = 3; 
            cp.add(addMonsterMapButton, gbc); 
        //ADD MATERIAL TO MONSTER
            JLabel AddMatname = new JLabel("Name:");
            gbc.gridx = 1;  
            gbc.gridy = 12;
            cp.add(AddMatname, gbc); 
            JLabel addMonsterMat= new JLabel("Monster:");
            gbc.gridx = 2;  
            cp.add(addMonsterMat, gbc); 
            JLabel addMatRar= new JLabel("rarity:");
            gbc.gridx = 3;  
            cp.add(addMatRar, gbc);
            JLabel addMat = new JLabel("Add Material to Monster:");
            gbc.gridx = 0; 
            gbc.gridy = 13; 
            cp.add(addMat, gbc);
            JTextField matName = new JTextField(10);
            gbc.gridx = 1; 
            cp.add(matName, gbc);
            JComboBox<String> monsterBox3 = new JComboBox<>(monsters.toArray(new String[0]));
            gbc.gridx = 2; 
            cp.add(monsterBox3, gbc);
            List<Integer> rar = IntStream.rangeClosed(1, 10)
                                                .boxed().toList();
                                                String[] rarArray = rar.stream()
                                                .map(String::valueOf)
                                                .toArray(String[]::new);
            JComboBox<String> rarBox = new JComboBox<>(rarArray);
            gbc.gridx =3; 
            cp.add(rarBox, gbc);
            
            JButton addMatButton = new JButton("add");
            gbc.gridx = 4; 
            cp.add(addMatButton, gbc); 
        //ADD QUEST
            JLabel addQuestName = new JLabel("Name:");
            gbc.gridx = 1;  
            gbc.gridy = 14;
            cp.add(addQuestName, gbc); 
            JLabel addQuestGame= new JLabel("Game:");
            gbc.gridx = 2;  
            cp.add(addQuestGame, gbc); 
            JLabel addQuestTargets= new JLabel("Targets:");
            gbc.gridx = 3;  
            cp.add(addQuestTargets, gbc);
            JLabel addQuest = new JLabel("Add Quest:");
            gbc.gridx = 0; 
            gbc.gridy = 15; 
            cp.add(addQuest, gbc);
            JTextField questName = new JTextField(10);
            gbc.gridx = 1; 
            cp.add(questName, gbc);
            JComboBox<String> gamesBox4 = new JComboBox<>(games.toArray(new String[0]));
            gbc.gridx = 2; 
            cp.add(gamesBox4, gbc);
            List<String> mapgam = this.getController().getMapsfromGame((String) gamesBox4.getSelectedItem());     
            JComboBox<String> mapBox3 = new JComboBox<>(mapgam.toArray(new String[0]));
            gbc.gridy = 17;
            gbc.gridx = 2;  
            cp.add(mapBox3, gbc);
            List<String> monsm = this.getController().getMonstersInMap((String) mapBox3.getSelectedItem() );
            monsm.add(0, "none");
            JComboBox<String> monsterBox4 = new JComboBox<>(monsm.toArray(new String[0]));
            gbc.gridy = 15;
            gbc.gridx = 3; 
            cp.add(monsterBox4, gbc);
            JComboBox<String> monsterBox5 = new JComboBox<>(monsm.toArray(new String[0]));
            gbc.gridx = 4; 
            cp.add(monsterBox5, gbc);
            JComboBox<String> monsterBox6 = new JComboBox<>(monsm.toArray(new String[0]));
            gbc.gridx = 5; 
            cp.add(monsterBox6, gbc);       
            JLabel addQuestMap= new JLabel("Map:");
            gbc.gridy = 16;
            gbc.gridx = 2;  
            cp.add(addQuestMap, gbc);  
            JButton addQuestButton = new JButton("add");
            gbc.gridx = 3; 
            gbc.gridy = 17;
            cp.add(addQuestButton, gbc); 



            addMonsterButton.addActionListener(e -> {
                if(monsterName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,  "Failed, one or more fields are empty");
                }
                else {
                    this.getController().addMonster(monsterName.getText(),(String) monsterCat.getSelectedItem(), Boolean.parseBoolean((String)varBox.getSelectedItem()));
                    monsterBox.removeAllItems();
                    monsterBox2.removeAllItems();
                    monsterBox3.removeAllItems();
                    monsterBox4.removeAllItems();
                    monsterBox5.removeAllItems();
                    monsterBox6.removeAllItems();
                    List<String> mons = this.getController().getMonsters();
                    mons.forEach(monsterBox::addItem);
                    mons.forEach(monsterBox2::addItem);
                    mons.forEach(monsterBox3::addItem);
                    List<String> monsg = this.getController().getMonstersInMap((String) mapBox.getSelectedItem() );
                    monsg.forEach(monsterBox4::addItem);
                    monsg.forEach(monsterBox5::addItem);
                    monsg.forEach(monsterBox6::addItem);
                }
            });
            addGameButton.addActionListener(e -> {
                if(gameName.getText().trim().isEmpty() || gameDate.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,  "Failed, one or more fields are empty");
                }
                else {
                    this.getController().addGame(gameName.getText(), gameDate.getText());
                    gamesBox.removeAllItems();
                    gamesBox2.removeAllItems();
                    gamesBox3.removeAllItems();
                    gamesBox4.removeAllItems();
                    List<String> gam = this.getController().getGames();
                    gam.forEach(gamesBox::addItem);
                    gam.forEach(gamesBox2::addItem);
                    gam.forEach(gamesBox3::addItem);
                    gam.forEach(gamesBox4::addItem);
                }
            });

            addMapButton.addActionListener(e -> {
                if(mapName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,  "Failed, one or more fields are empty");
                }
                else {
                    this.getController().addMap(mapName.getText(), (String) gamesBox.getSelectedItem());
                }
            });
            addDeviceButton.addActionListener(e -> {
                if(deviceName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,  "Failed, one or more fields are empty");
                }
                else {
                    this.getController().addDevice(deviceName.getText());
                    deviceBox.removeAllItems();
                    List<String> devi = this.getController().getDeviceList();
                    devi.forEach(deviceBox::addItem);
                }
            });
            addDeviceGameButton.addActionListener(e -> {
                this.getController().addGameDev((String) gamesBox2.getSelectedItem(), (String) deviceBox.getSelectedItem());
            });
            addMonsterGameButton.addActionListener(e -> {
                this.getController().addMongame((String) monsterBox.getSelectedItem(), (String) gamesBox3.getSelectedItem());
            });
            addMonsterMapButton.addActionListener(e -> {
                this.getController().addMonMap((String) monsterBox2.getSelectedItem(), (String) mapBox.getSelectedItem(), 
                this.getController().getGamefromMap((String) mapBox.getSelectedItem()));
            });
            addMatButton.addActionListener(e -> {
                if(matName.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(null,  "Failed, one or more fields are empty");
                }
                else {
                    this.getController().addMaterial(matName.getText(), (String) monsterBox3.getSelectedItem(), Integer.parseInt((String)rarBox.getSelectedItem()) );
                }
            });
            addQuestButton.addActionListener(e -> {
                if( questName.getText().trim().isEmpty() || Objects.isNull(mapBox3.getSelectedItem())) {
                    JOptionPane.showMessageDialog(null,  "Failed, one or more fields are empty");
                }
                else {
                    if( monsterBox4.getSelectedItem().equals("none") && monsterBox5.getSelectedItem().equals("none") && monsterBox6.getSelectedItem().equals("none")) {
                        JOptionPane.showMessageDialog(null,  "Failed to create quest, select at least one monster");
                    } else {
                        List<String> monq = new ArrayList<>();
                        if (!monsterBox4.getSelectedItem().equals("none")) { monq.add((String)monsterBox4.getSelectedItem());}
                        if (!monsterBox5.getSelectedItem().equals("none")) { monq.add((String)monsterBox5.getSelectedItem());}
                        if (!monsterBox6.getSelectedItem().equals("none")) { monq.add((String)monsterBox6.getSelectedItem());}
                        this.getController().addQuest(questName.getText(), (String) gamesBox4.getSelectedItem(), 
                        (String) mapBox3.getSelectedItem(), monq);
                        
                    }
                }

            });




            monsterBox2.addActionListener(e -> {
                mapBox.removeAllItems();
                List<String> mapup = new ArrayList<>();
                for (String game : this.getController().getGamesWith((String) monsterBox2.getSelectedItem())) {
                
                mapup.addAll(this.getController().getMapsfromGame(game));
                mapup.forEach(mapBox::addItem);
                }
            });
            gamesBox4.addActionListener(e -> {
                mapBox3.removeAllItems();
                List<String> map2 = this.getController().getMapsfromGame((String) gamesBox4.getSelectedItem());
                map2.forEach(mapBox3::addItem);
            });
            mapBox3.addActionListener(e ->{
                monsterBox3.removeAllItems();
                monsterBox4.removeAllItems();
                monsterBox5.removeAllItems();
                monsterBox6.removeAllItems();
                List<String> monsg = this.getController().getMonstersInMap((String) mapBox3.getSelectedItem() );
                monsg.add(0, "none");
                monsg.forEach(monsterBox4::addItem);
                monsg.forEach(monsterBox5::addItem);
                monsg.forEach(monsterBox6::addItem);
            });
        
            

            





                    // Bottom Panel with Back Button
            JButton back = new JButton("Back");
            back.setFont(new Font("Arial", Font.BOLD, 16));
            back.addActionListener(e -> this.getController().loginForm());
            gbc.gridx = 5; 
            gbc.gridy = 20; 
            cp.add(back, gbc);
        });
    }

    public void register() {
        freshPane(cp -> {
            cp.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel userLabel = new JLabel("Insert UserName: ");
            gbc.gridx = 0; 
            gbc.gridy = 1; 
            cp.add(userLabel, gbc);
            JTextField usernameField = new JTextField(20);
            gbc.gridx = 1; 
            gbc.gridy = 1; 
            cp.add(usernameField, gbc);
    
            JLabel nameLabel = new JLabel("Insert Name: ");
            gbc.gridx = 0; 
            gbc.gridy = 2; 
            cp.add(nameLabel, gbc);
            JTextField nameField = new JTextField(20);
            gbc.gridx = 1; 
            cp.add(nameField, gbc);

            JLabel surnameLabel = new JLabel("Insert Surname: ");
            gbc.gridx = 0; 
            gbc.gridy = 3; 
            cp.add(surnameLabel, gbc);
            JTextField surnameField = new JTextField(20);
            gbc.gridx = 1; 
            cp.add(surnameField, gbc);

            JLabel mailLabel = new JLabel("Insert Email: ");
            gbc.gridx = 0; 
            gbc.gridy = 4; 
            cp.add(mailLabel, gbc);
            JTextField mailField = new JTextField(20);
            gbc.gridx = 1; 
            cp.add(mailField, gbc);

            JLabel passLabel = new JLabel("Insert Password: ");
            gbc.gridx = 0; 
            gbc.gridy = 5; 
            cp.add(passLabel, gbc);
            JTextField passField = new JTextField(20);
            gbc.gridx = 1; 
            cp.add(passField, gbc);
    
    
            JButton addButton = new JButton("Add");
            gbc.gridx = 1; 
            gbc.gridy = 6;
            cp.add(addButton, gbc);
            addButton.addActionListener(event -> {
                if( usernameField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() || surnameField .getText().trim().isEmpty() || mailField.getText().trim().isEmpty() || passField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,  "Failed to add character, one or more fields are empty");
                } else {
                    this.getController().insertUser(usernameField.getText(), nameField.getText(), surnameField .getText(), mailField.getText(), passField.getText());
                }
            });
    
            // Bottom Panel with Back Button
            JButton back = new JButton("Back");
            back.setFont(new Font("Arial", Font.BOLD, 16));
            back.addActionListener(e -> this.getController().loginForm());
            gbc.gridx = 5; 
            gbc.gridy = 10; 
            cp.add(back, gbc);
        });
    }

    // Helper method to create labeled sections with clickable labels
    private JPanel createSection(String title, List<String> items, java.util.function.Consumer<String> action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create a wrapper panel for the list items
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        if (items.isEmpty()) {
            contentPanel.add(new JLabel("No data available"));
        } else {
            for (String item : items) {
                contentPanel.add(clickableLabel(item, () -> action.accept(item)));
            }
        }

        // Wrap content in a JScrollPane to ensure visibility
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);  // Remove default border to keep it clean
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(500, Math.min(200, items.size() * 30))); // Adjust height dynamically

        panel.add(scrollPane, BorderLayout.CENTER); // Ensure content is visible

        return panel;
    }

    private JLabel clickableLabel(String labelText, Runnable action) {
        var label = new JLabel(labelText);
        label.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        action.run();
                    });
                }
            }
        );
        return label;
    }

    private void freshPane(Consumer<Container> consumer) {
        var cp = this.mainFrame.getContentPane();
        cp.removeAll();
        consumer.accept(cp);
        cp.validate();
        cp.repaint();
        this.mainFrame.pack();
    }
}
