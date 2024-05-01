package dev.littleprogrammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
Todo add game ending
Todo alternate green each snake segment
*/

public class Game extends JPanel implements ActionListener {

    private Timer timer;
    private final int DELAY = 100; // milliseconds

    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final int SEGMENT_SIZE = 10;
    private java.util.List<Point> snakeSegments;
    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;

    private boolean isGameOver = false;

    private int randomPosX = (int)(Math.random() * (WIDTH - SEGMENT_SIZE + 1)) / SEGMENT_SIZE * SEGMENT_SIZE, randomPosY = (int)(Math.random() * (HEIGHT - SEGMENT_SIZE + 1)) / SEGMENT_SIZE * SEGMENT_SIZE;

    private int length = 1;

    // Possible directions
    public enum Direction { UP, DOWN, LEFT, RIGHT }

    public Game() {
        // Set screen size and focusability
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        initGame();
    }

    private void initGame() {
        snakeSegments = new ArrayList<>();
        snakeSegments.add(new Point(WIDTH / 2, HEIGHT / 2));

        setUpKeyBind();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPoints(g);
        drawFood(g);
        drawSnake(g);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Game Over", WIDTH / 2 - 40, HEIGHT / 2 + 5);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point segment : snakeSegments) {
            g.fillRect(segment.x, segment.y, SEGMENT_SIZE, SEGMENT_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(randomPosX, randomPosY, SEGMENT_SIZE, SEGMENT_SIZE);
    }

    private void drawPoints(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Length: " + length, 9, 20);
    }

    private void eatFood() {
        if (snakeSegments.getFirst().y == randomPosY && snakeSegments.getFirst().x == randomPosX) {
            length++;
            Point tail = snakeSegments.getLast();
            Point newSegment = new Point(tail);
            snakeSegments.add(newSegment);
            placeFood();
        }
    }

    // Checks random position until free spot to place.
    private void placeFood() {
        boolean isOccupied;
        do {
            randomPosX = (int)(Math.random() * (WIDTH - SEGMENT_SIZE + 1)) / SEGMENT_SIZE * SEGMENT_SIZE;
            randomPosY = (int)(Math.random() * (HEIGHT - SEGMENT_SIZE + 1)) / SEGMENT_SIZE * SEGMENT_SIZE;

            isOccupied = false;
            for (Point segment : snakeSegments) {
                if (segment.x == randomPosX && segment.y == randomPosY) {
                    isOccupied = true;
                    break;
                }
            }
        } while (isOccupied);
    }

    private void move() {
        if (snakeSegments.isEmpty()) return;

        Point head = new Point(snakeSegments.getFirst());

        if (!oppositeDir(nextDirection)) {
            direction = nextDirection;
        }

        switch (direction)  {
            case UP -> head.y -= SEGMENT_SIZE;
            case DOWN -> head.y += SEGMENT_SIZE;
            case LEFT -> head.x -= SEGMENT_SIZE;
            case RIGHT -> head.x += SEGMENT_SIZE;
        }

        boolean wallCollision = head.y < 0 || head.y > HEIGHT-SEGMENT_SIZE || head.x < 0 || head.x > WIDTH-SEGMENT_SIZE;
        boolean selfCollision = snakeSegments.stream().skip(1).anyMatch(segment -> segment.x == head.x && segment.y == head.y);

        if (wallCollision || selfCollision) {
            timer.stop();
            isGameOver = true;
        } else {
            snakeSegments.addFirst(head);
            snakeSegments.removeLast();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        eatFood();
        repaint();
    }

    // Declare keys to bind
    private void setUpKeyBind() {
        bindKeyToAction("UP", KeyStroke.getKeyStroke("UP"), Direction.UP);
        bindKeyToAction("UP", KeyStroke.getKeyStroke("W"), Direction.UP);

        bindKeyToAction("DOWN", KeyStroke.getKeyStroke("DOWN"), Direction.DOWN);
        bindKeyToAction("DOWN", KeyStroke.getKeyStroke("S"), Direction.DOWN);

        bindKeyToAction("RIGHT", KeyStroke.getKeyStroke("RIGHT"), Direction.RIGHT);
        bindKeyToAction("RIGHT", KeyStroke.getKeyStroke("D"), Direction.RIGHT);

        bindKeyToAction("LEFT", KeyStroke.getKeyStroke("LEFT"), Direction.LEFT);
        bindKeyToAction("LEFT", KeyStroke.getKeyStroke("A"), Direction.LEFT);
    }

    // Binds the keys // not to familiar with this
    private void bindKeyToAction(String name, KeyStroke keyStroke, Direction newDir) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!oppositeDir(newDir)) {
                    nextDirection = newDir;
                }
            }
        });
    }

    // Declares what directions are opposites
    private boolean  oppositeDir(Direction newDir) {
        return (direction == Direction.UP && newDir == Direction.DOWN) ||
                (direction == Direction.DOWN && newDir == Direction.UP) ||
                (direction == Direction.LEFT && newDir == Direction.RIGHT) ||
                (direction == Direction.RIGHT && newDir == Direction.LEFT);
    }
}
