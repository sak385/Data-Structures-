package graph;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

public class Driver extends JFrame {

    private SocialNetwork graph;

    private JTabbedPane tabPanel;
     
    private JPanel profilePanel;
    private JPanel controlPanel;
    private JPanel profileList;

    private Profile currentProfile;
    private SocialNetworkPanel graphPanel;

    public Driver() {
        this.setTitle("Social Network");
        this.setSize(750, 800);    
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        graph = new SocialNetwork();
        
        tabPanel = new JTabbedPane();
        controlPanel = createControlPanel();

        profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.add(controlPanel, BorderLayout.NORTH);
        profileList = new JPanel();
        profileList.setLayout(new BoxLayout(profileList, BoxLayout.Y_AXIS));
        profilePanel.add(new JScrollPane(profileList), BorderLayout.CENTER); 

        JPanel buttonPanel = new JPanel();
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);
        JButton nextProfileButton = new JButton("Next Profile >");
        nextProfileButton.setFont(nextProfileButton.getFont().deriveFont(16.0f)); 
        JButton prevProfileButton = new JButton("< Previous Profile");
        prevProfileButton.setFont(prevProfileButton.getFont().deriveFont(16.0f)); 
        prevProfileButton.addActionListener(e -> {
            if (graph.profiles == null || graph.profiles.isEmpty()) return;
            int index = graph.profiles.indexOf(currentProfile);
            index = (index - 1 + graph.profiles.size()) % graph.profiles.size();
            updateCurrProfile(graph.profiles.get(index));
        });
        nextProfileButton.addActionListener(e -> {
            if (graph.profiles == null || graph.profiles.isEmpty()) return;
            int index = graph.profiles.indexOf(currentProfile);
            index = (index + 1) % graph.profiles.size();
            updateCurrProfile(graph.profiles.get(index));
        });
        buttonPanel.add(prevProfileButton); 
        buttonPanel.add(Box.createHorizontalStrut(60));
        buttonPanel.add(nextProfileButton); 
        
        graphPanel = new SocialNetworkPanel();

        if (graph.profiles != null && !graph.profiles.isEmpty()) {
            updateCurrProfile(graph.profiles.get(0));
        }
 
        tabPanel.addTab("Browse Profiles", profilePanel);
        tabPanel.addTab("Social Network Graph", graphPanel);
        
        this.add(tabPanel);
        this.setVisible(true);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(7);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(7);
        JLabel statusLabel = new JLabel("Status:");
        JTextField statusField = new JTextField(7);
        JButton addButton = new JButton("Add Profile");

        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String status = statusField.getText().trim();
            if (!username.isEmpty() && !name.isEmpty() && !status.isEmpty()) {
                if (graph.profiles.stream().anyMatch(p -> p.getUsername().equals(username))) {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                    return;
                }
                Profile newProfile = new Profile(username, name, status);
                graph.addProfile(newProfile); 
                usernameField.setText("");
                nameField.setText("");
                statusField.setText("");
                graphPanel.rebuild();
                updateCurrProfile(newProfile);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(statusLabel);
        panel.add(statusField);
        panel.add(addButton);

        return panel;
    }

    private void updateCurrProfile(Profile current) {
        profileList.removeAll();
        currentProfile = current;
 
        JLabel nameLabel = new JLabel("name: " + current.getName() + " (" + current.getUsername() + ")");
        nameLabel.setFont(nameLabel.getFont().deriveFont(30.0f));
        nameLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        profileList.add(nameLabel);  
        
        JLabel statusLabel = new JLabel("status: " + current.getStatus());
        statusLabel.setFont(statusLabel.getFont().deriveFont(24.0f));
        statusLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        profileList.add(statusLabel);
 
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        separator.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        profileList.add(separator); 

        JPanel friendsPanel = new JPanel();
        friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.X_AXIS));
        // ensure the friends columns align at the top and the whole friends block is left-aligned
        friendsPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        friendsPanel.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
 
        JPanel allFriendsPanel = new JPanel();
        allFriendsPanel.setLayout(new BoxLayout(allFriendsPanel, BoxLayout.Y_AXIS)); 
        // fix column width so headers remain static and the list grows downward
        allFriendsPanel.setPreferredSize(new Dimension(330, 200));
        allFriendsPanel.setMaximumSize(new Dimension(330, Integer.MAX_VALUE));
        allFriendsPanel.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
        allFriendsPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        ArrayList<Profile> friends = graph.allFriends(current);
        JLabel friendsHeader = new JLabel("Friends:");
        friendsHeader.setFont(friendsHeader.getFont().deriveFont(26.0f));
        friendsHeader.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        allFriendsPanel.add(friendsHeader);
        if (friends != null) {
            for (Profile friend : friends) {
                JPanel friendPanel = new JPanel();
                friendPanel.setLayout(new BoxLayout(friendPanel, BoxLayout.X_AXIS));
                friendPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                JLabel friendLabel = new JLabel(friend.getName() + " (" + friend.getUsername() + ")");
                friendLabel.setFont(friendLabel.getFont().deriveFont(20.0f));
                friendLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                JButton removeButton = new JButton("Remove Friend");
                removeButton.addActionListener(e -> {
                    graph.setFriend(current, friend, false);
                    graphPanel.rebuild();
                    updateCurrProfile(current);
                });
                friendPanel.add(friendLabel);
                friendPanel.add(Box.createHorizontalStrut(20));
                friendPanel.add(removeButton);
                friendPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                allFriendsPanel.add(friendPanel);
            }
        }

        JPanel newFriendsPanel = new JPanel();
        newFriendsPanel.setLayout(new BoxLayout(newFriendsPanel, BoxLayout.Y_AXIS)); 
        newFriendsPanel.setPreferredSize(new Dimension(330, 200));
        newFriendsPanel.setMaximumSize(new Dimension(330, Integer.MAX_VALUE));
        newFriendsPanel.setAlignmentY(java.awt.Component.TOP_ALIGNMENT);
        newFriendsPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JLabel addFriendsHeader = new JLabel("Add Friends:");
        addFriendsHeader.setFont(addFriendsHeader.getFont().deriveFont(26.0f));
        addFriendsHeader.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        newFriendsPanel.add(addFriendsHeader);
        if (graph.profiles != null) {
            for (Profile potentialFriend : graph.profiles) {
                if (potentialFriend.equals(current) || graph.searchFriend(current, potentialFriend)) {
                    continue;
                }
                JPanel potentialFriendPanel = new JPanel();
                potentialFriendPanel.setLayout(new BoxLayout(potentialFriendPanel, BoxLayout.X_AXIS));
                potentialFriendPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                JLabel potentialFriendLabel = new JLabel(potentialFriend.getName() + " (" + potentialFriend.getUsername() + ")");
                potentialFriendLabel.setFont(potentialFriendLabel.getFont().deriveFont(20.0f));
                potentialFriendLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                JButton addButton = new JButton("Add Friend");
                addButton.addActionListener(e -> {
                    graph.setFriend(current, potentialFriend, true);
                    graphPanel.rebuild();
                    updateCurrProfile(current);
                });
                potentialFriendPanel.add(potentialFriendLabel);
                potentialFriendPanel.add(Box.createHorizontalStrut(20));
                potentialFriendPanel.add(addButton);
                potentialFriendPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                newFriendsPanel.add(potentialFriendPanel);
            }
        }

        friendsPanel.add(allFriendsPanel);
        friendsPanel.add(new JSeparator(SwingConstants.VERTICAL)); 
        friendsPanel.add(newFriendsPanel);

        profileList.add(friendsPanel);

        profileList.revalidate();
        profileList.repaint();
    }

    private class SocialNetworkPanel extends JPanel { 

        public void rebuild() {
            com.mxgraph.view.mxGraph mx = new com.mxgraph.view.mxGraph();
            Object parent = mx.getDefaultParent();

            java.util.Map<String, Object> vertexMap = new java.util.HashMap<>();

            mx.getModel().beginUpdate();
            try {
                // create a vertex for each profile
                int i = 0;
                int cols = Math.max(1, (int) Math.ceil(Math.sqrt(Math.max(1, graph.profiles.size()))));
                int gapX = 150, gapY = 120;
                for (Profile profileObj : graph.profiles) {
                    String username = profileObj.getUsername() != null ? profileObj.getUsername() : "";
                    String name = profileObj.getName() != null ? profileObj.getName() : "";

                    String label = username.isEmpty() ? name : (username + (name.isEmpty() ? "" : " (" + name + ")"));
                    int x = (i % cols) * gapX + 40;
                    int y = (i / cols) * gapY + 40;
                    Object v = mx.insertVertex(parent, null, label, x, y, 120, 40);
                    vertexMap.put(username, v);
                    i++;
                }
 
                for (Profile profileObj : graph.profiles) { 
                    ArrayList<Profile> friendsIterable = new ArrayList<>();
                    try {
                        int pid = graph.profiles.indexOf(profileObj);
                        for (int j = 0; j < graph.profiles.size(); j++) {
                            if (j == pid) {
                                continue;  
                            } else if (graph.graph[pid][j]) {
                                friendsIterable.add(graph.profiles.get(j));
                            }
                        }   
                    } catch (Throwable t) { 
                        friendsIterable = null;
                    }

                    if (friendsIterable != null) {
                        String srcUsername = profileObj.getUsername() != null ? profileObj.getUsername() : "";
                        Object srcVertex = vertexMap.get(srcUsername);
                        for (Object friendObj : friendsIterable) {
                            if (friendObj == null) continue;
                            String friendUsername = null;
                            if (friendObj instanceof Profile) {
                                friendUsername = ((Profile) friendObj).getUsername();
                            } else if (friendObj instanceof String) {
                                friendUsername = (String) friendObj;
                            } else { 
                                friendUsername = friendObj.toString();
                            }
                            Object tgtVertex = vertexMap.get(friendUsername);
                            if (srcVertex != null && tgtVertex != null) {
                                mx.insertEdge(parent, null, "", srcVertex, tgtVertex);
                            }
                        }
                    }
                }
            } finally {
                mx.getModel().endUpdate();
            }

            new com.mxgraph.layout.mxCircleLayout(mx).execute(parent);

            // replace existing graph component if present
            removeAll();
            com.mxgraph.swing.mxGraphComponent comp = new com.mxgraph.swing.mxGraphComponent(mx);
            comp.setConnectable(false);
            comp.getGraphControl().setPreferredSize(new java.awt.Dimension(600, 400));
            add(comp, BorderLayout.CENTER);

            // double-click a vertex to show profile details (direct typed access)
            comp.getGraphControl().addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Object cell = comp.getCellAt(e.getX(), e.getY());
                        if (cell instanceof com.mxgraph.model.mxCell && ((com.mxgraph.model.mxCell) cell).isVertex()) {
                            String label = String.valueOf(((com.mxgraph.model.mxCell) cell).getValue());
                            // find matching profile by username prefix or name substring
                            graph.profiles.stream().filter(p -> {
                                String u = p.getUsername() != null ? p.getUsername() : "";
                                String n = "";
                                try { n = p.getName() != null ? p.getName() : ""; } catch (Exception ignored) {}
                                return (!u.isEmpty() && label.startsWith(u)) || (!n.isEmpty() && label.contains(n));
                            }).findFirst().ifPresent(p -> {
                                String username = p.getUsername() != null ? p.getUsername() : "";
                                String name = p.getName() != null ? p.getName() : "";
                                String status = p.getStatus() != null ? p.getStatus() : "";
                                JOptionPane.showMessageDialog(Driver.this,
                                        "Username: " + username + "\nName: " + name + "\nStatus: " + status,
                                        "Profile", JOptionPane.INFORMATION_MESSAGE);
                            });
                        }
                    }
                }
            });
        }

        public SocialNetworkPanel() {
            setLayout(new BorderLayout());
            rebuild();
        }
    }


    public static void main(String[] args) { 
        new Driver();
    }
}