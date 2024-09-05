package ru.vsu.cs.course1.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Класс, реализующий логику игры
 */
public class Game implements Serializable {
    /**
     * объект Random для генерации случайных чисел
     * (можно было бы объявить как )
     */
    private final Random rnd = new Random();

    /**
     * двумерный массив для хранения игрового поля
     * (в данном случае цветов, 0 - пусто; создается / пересоздается при старте игры)
     */
    private int[][] field = null;
    private int[][] scoreField = new int[10][10];
    /**
     * Максимальное кол-во цветов
     */
    private int colorCount = 0;
    public final int MAX_DEPTH = 3;


    private final HashMap<Integer, Integer> AWARD = new HashMap<>();

    public Game() {
        AWARD.put(0, 0);
        AWARD.put(1, -1);
        AWARD.put(2, 1);
    }

    public void newGame(int rowCount, int colCount, int colorCount) {
        // создаем поле
        field = new int[rowCount][colCount];
        this.colorCount = colorCount;
    }

    public int leftMouseClick(int row, int col) {

        if (field[row][col] != 0) {
            return 0;
        }
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return 0;
        }
        field[row][col] = 1;
        int bestScore = Integer.MIN_VALUE;
        int score;
        int[] position = new int[2];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == 0) {
                    field[i][j] = 2;
                    score = minimax(0, false, i, j, 2,
                            Integer.MIN_VALUE, Integer.MAX_VALUE);
                    field[i][j] = 0;
                    if (score > bestScore) {
                        bestScore = score;
                        position[0] = i;
                        position[1] = j;
                    }
                }
            }
        }
        field[position[0]][position[1]] = 2;
        int win = checkWinner(row, col, 1);
        if (win == 1) {
            return 1;
        } else if (win == 2) {
            return 2;
        } else {
            return checkWinner(position[0], position[1], 2);
        }
    }

    public int minimax(int depth, boolean isMaximizing, int row, int col, int team, int alpha, int beta) {
        int bestScore, score;
        int winner = checkWinner(row, col, team);

        // Условие победы или завершения
        if (winner != 0 || depth == MAX_DEPTH) {
            return AWARD.get(winner) * 10 - AWARD.get(winner) * depth;
        }

        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    if (field[i][j] == 0) {
                        field[i][j] = 2;  // Максимизирующий игрок
                        score = minimax(depth + 1, false, i, j, 2, alpha, beta);
                        field[i][j] = 0;  // Откатываем ход

                        bestScore = Math.max(bestScore, score);
                        alpha = Math.max(alpha, bestScore);

                        // Альфа-бета отсечение
                        if (beta <= alpha) {
                            break;  // Разомкнуть цикл, если мы можем отсеять ветвь
                        }
                    }
                }
            }
            return bestScore;

        } else {
            bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    if (field[i][j] == 0) {
                        field[i][j] = 1;  // Минимизирующий игрок
                        score = minimax(depth + 1, true, i, j, 1, alpha, beta);
                        field[i][j] = 0;  // Откатываем ход
                        bestScore = Math.min(bestScore, score);
                        beta = Math.min(beta, bestScore);

                        // Альфа-бета отсечение
                        if (beta <= alpha) {
                            break;  // Разомкнуть цикл, если мы можем отсеять ветвь
                        }
                    }
                }
            }
            return bestScore;


        }
    }



    public int getRowCount() {
        return field == null ? 0 : field.length;
    }

    public int getColCount() {
        return field == null ? 0 : field[0].length;
    }

    public int getColorCount() {
        return colorCount;
    }

    public int getCell(int row, int col) {
        return (row < 0 || row >= getRowCount() || col < 0 || col >= getColCount()) ? 0 : field[row][col];
    }


    public int checkRow(int row, int team) {
        int cnt = 0;
        for (int i = 0; i < 10; i++) {
            if (field[row][i] == team) {
                cnt++;
                if (cnt == 5) {
                    return team;
                }
            } else {
                cnt = 0;
            }
        }
        return 0;
    }

    public int checkCol(int col, int team) {
        int cnt = 0;
        for (int i = 0; i < 10; i++) {
            if (field[i][col] == team) {
                cnt++;
                if (cnt == 5) {
                    return team;
                }
            } else {
                cnt = 0;
            }
        }
        return 0;
    }


    public int checkDiagonal(int row, int col, int team) {
        int trow = 0, tcol = 0, maxElemtn = 0;
        if (row + col > 2 && row + col < 16) {
            if (row + col < 10) {
                tcol = row + col;
                maxElemtn = row + col + 1;

            } else if (row + col < 16) {
                trow = row + col - 9;
                tcol = 9;
                maxElemtn = 9 - trow + 1;
            }
            int cnt = 0;
            for (int i = 0; i < maxElemtn; i++) {
                if (field[trow + i][tcol - i] == team) {
                    cnt++;
                    if (cnt == 5) {
                        return team;
                    }
                } else {
                    cnt = 0;
                }
            }
        }
        return 0;
    }

    public int checkReverseDiagonal(int row, int col, int team) {
        int maxElemtn;
        maxElemtn = 10 - Math.abs(row - col);
        if (row > col) {
            row -= col;
            col = 0;
        } else {
            col -= row;
            row = 0;
        }
        int cnt = 0;
        for (int i = 0; i < maxElemtn; i++) {
            if (field[row + i][col + i] == team) {
                cnt++;
                if (cnt == 5) {
                    return team;
                }
            } else {
                cnt = 0;
            }
        }
        return 0;
    }


    public int checkWinner(int row, int col, int team) {
        int[] winners = new int[]{checkCol(col, team), checkRow(row, team), checkDiagonal(row, col, team), checkReverseDiagonal(row, col, team)};
        for (int side : winners) {
            if (side != 0) {
                return side;
            }
        }
        return 0;
    }

    public void rightMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }

        field[row][col] = 0;
    }
}
