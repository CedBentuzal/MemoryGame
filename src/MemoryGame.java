
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;



public class MemoryGame {
    class Card {
        String cardName;
        ImageIcon cardImage;

        Card(String cardName, ImageIcon cardImage) {
            this.cardName = cardName;
            this.cardImage = cardImage;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardNames = {
            "Brook","Chopper","Frankie","Junbei",
            "Luffy","Nami","Robin","Sanji","Ussop","Zoro"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 100;
    int cardHeight = 100;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImage;
    
    int boardWidth = cardWidth * columns;
    int boardHeight = cardHeight * rows;

    JFrame frame = new JFrame("One Piece Memory Game");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartPanel = new JPanel();
    JButton restartButton = new JButton();

    int errorcount = 0;
    ArrayList<JButton> board;
    Timer timer;
    boolean isFlipping = false;
    JButton firstCard;
    JButton secondCard;

    MemoryGame() {
        SetUpcards();
        ShuffleCards();

        //frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Error: " + Integer.toString(errorcount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 50));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);



        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < rows * columns; i++) {
            JButton cardButton = new JButton();
            cardButton.setPreferredSize(new Dimension(cardWidth, cardHeight));
            cardButton.setOpaque(true);
            cardButton.setIcon(cardSet.get(i).cardImage);
            cardButton.setFocusable(false);
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    if (!isFlipping){
                        return;
                    }
                    JButton cardButton= (JButton) e.getSource();  // Fixed variable name
                    if (cardButton.getIcon() == cardBackImage){
                        if (firstCard == null){
                            firstCard = cardButton;
                            int index = board.indexOf(firstCard);
                            firstCard.setIcon(cardSet.get(index).cardImage);
                        }
                        else if (secondCard == null){
                            secondCard = cardButton;
                            int index = board.indexOf(secondCard);
                            secondCard.setIcon(cardSet.get(index).cardImage);
                            
                            if (firstCard.getIcon() != secondCard.getIcon()) {  // Fixed missing parenthesis
                                errorcount += 1;
                                textLabel.setText("Error: " + Integer.toString(errorcount));
                                timer.start();  // Fixed variable name
                            }
                            else {
                                firstCard = null;
                                secondCard = null;
                            }
                        }
                    }
                }
            });
            board.add(cardButton);
            boardPanel.add(cardButton);
        }
        frame.add(boardPanel, BorderLayout.CENTER);

        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setText("Restart");
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.setPreferredSize(new Dimension(boardWidth, 50));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (!isFlipping){
                    return;
                }

                isFlipping = false;
                restartButton.setEnabled(false);
                firstCard = null;
                secondCard = null;
                ShuffleCards();

                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImage);
                }
                errorcount = 0;
                textLabel.setText("Error: " + Integer.toString(errorcount));
                timer.start();
            }
        });
        frame.add(restartButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                timers();
            }
            
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    void SetUpcards(){
        cardSet = new ArrayList<Card>();
        for (String cardName : cardNames){
            Image cardImg = new ImageIcon(getClass().getResource("./img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImage = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            Card card = new Card(cardName, cardImage);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet); // Add the same set again for pairs

        Image cardBackImg = new ImageIcon(getClass().getResource("./img/flag.jpg")).getImage();
        cardBackImage = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));


    }

    void ShuffleCards() {

        for (int i = 0; i < cardSet.size(); i++) {
            int randomIndex = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(randomIndex));
            cardSet.set(randomIndex, temp);
        }
    }
    void timers() {

        if (isFlipping && firstCard != null && secondCard != null) {
            firstCard.setIcon(cardBackImage);
            firstCard = null;
            secondCard.setIcon(cardBackImage);
            secondCard = null;
        }
        else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImage);
            }
            isFlipping = true;
            restartButton.setEnabled(true);
        }

    }
}
