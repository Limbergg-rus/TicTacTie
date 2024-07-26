package ru.vsu.cs.course1.game;

import java.io.Serializable;
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
    /**
     * Максимальное кол-во цветов
     */
    private int colorCount = 0;


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

    private int checkAndGrade(int row, int col, int[] position) {
        int bestScore = Integer.MIN_VALUE;
        if (field[row][col] == 0) {
            field[row][col] = 2;
            int score = minimax(0, false, row, col, 2);
            field[row][col] = 0;
            if (score > bestScore) {
                bestScore = score;
                position[0] = row;
                position[1] = col;
            }
        }
        return bestScore;
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
                    score = minimax(0, false, i, j, 2);
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

//        for (int i = 0; i < 10; i++) {
//            checkAndGrade(row, i, position);
////            if (field[row][i] == 0) {
////                field[row][i] = 2;
////                score = minimax(0, false, row, i, 2);
////                field[row][i] = 0;
////                bestScore = Math.max(bestScore, score);
////                if (score >= bestScore) {
////                    bestScore = score;
////                    position[0] = row;
////                    position[1] = i;
////                }
////            }
//        }
//
//        for (int i = 0; i < 10; i++) {
//            checkAndGrade(i, col, position);
////            if (field[i][col] == 0) {
////                field[i][col] = 2;
////                score = minimax(0, false, i, col, 2);
////                field[i][col] = 0;
////                bestScore = Math.max(bestScore, score);
////                if (score >= bestScore) {
////                    bestScore = score;
////                    position[0] = i;
////                    position[1] = col;
////                }
////            }
//        }
//
//
//        int trow = 0, tcol = 0, maxElemtn = 0;
//        if (row + col > 2 && row + col < 16) {
//            if (row + col < 10) {
//                tcol = row + col;
//                maxElemtn = row + col + 1;
//
//            } else if (row + col < 16) {
//                trow = row + col - 9;
//                tcol = 9;
//                maxElemtn = 9 - trow + 1;
//            }
//            for (int i = 0; i < maxElemtn; i++) {
//                checkAndGrade(trow + i, tcol - i, position);
////                if (field[trow + i][tcol - i] == 0) {
////                    field[trow + i][tcol - i] = 2;
////                    score = minimax(0, false, trow + i, tcol - i, 2);
////                    field[trow + i][tcol - i] = 0;
////                    bestScore = Math.max(bestScore, score);
////                    if (score >= bestScore) {
////                        bestScore = score;
////                        position[0] = trow + i;
////                        position[1] = tcol - i;
////                    }
////                }
//            }
//        }
//
//        maxElemtn = 10 - Math.abs(row - col);
//        if (row > col) {
//            trow = row - col;
//            tcol = 0;
//        } else {
//            tcol = col - row;
//            trow = 0;
//        }
//        for (int i = 0; i < maxElemtn; i++) {
//            checkAndGrade(trow + i, tcol + i, position);
//
////            if (field[trow + i][tcol + i] == 0) {
////                field[trow + i][tcol + i] = 2;
////                score = minimax(0, false, trow + i, tcol + i, 2);
////                field[trow + i][tcol + i] = 0;
////                bestScore = Math.max(bestScore, score);
////                if (score >= bestScore) {
////                    bestScore = score;
////                    position[0] = trow + i;
////                    position[1] = tcol + i;
////                }
////            }
//        }
//
//
////        for (int i = 0; i < field.length; i++) {
////            for (int j = 0; j < field[0].length; j++) {
////                if (field[i][j] == 0) {
////                    field[i][j] = 2;
////                    score = minimax(0, false, i, j, 2);
////                    field[i][j] = 0;
////                    if (score >= bestScore) {
////                        bestScore = score;
////                        position[0] = i;
////                        position[1] = j;
////                    }
////                }
////            }
////        }
//        field[position[0]][position[1]] = 2;
//        int win = checkWinner(startrow, startcol, 1);
//        if (win == 1) {
//            return 1;
//        } else if (win == 2) {
//            return 2;
//        } else {
//            return checkWinner(position[0], position[1], 2);
//        }
    }
//    public int minimax(int depth, boolean isMaximizing, int row, int col, int team) {
//        int bestScore;
//        int score;
//        int winner = checkWinner(row, col, team);
//        if ( winner != 0 || depth == 2) {
//            return AWARD.get(winner);
//        } else {
//            if (isMaximizing) {
//                bestScore = Integer.MIN_VALUE;
//
//                //check Row
//                for (int i = 0; i < 10; i++) {
//                    if (field[row][i] == 0) {
//                        field[row][i] = 2;
//                        score = minimax(depth + 1, false, row, i, 2);
//                        field[row][i] = 0;
//                        bestScore = Math.max(bestScore, score);
//                        if (bestScore == 1) {
//                            return bestScore;
//                        }
//                    }
//                }
//                //check Column
//                for (int i = 0; i < 10; i++) {
//                    if (field[i][col] == 0) {
//                        field[i][col] = 2;
//                        score = minimax(depth + 1, false, i, col, 2);
//                        field[i][col] = 0;
//                        bestScore = Math.max(bestScore, score);
//                        if (bestScore == 1) {
//                            return bestScore;
//
//                        }
//                    }
//                }
//                //check Diagonal
//                int trow = 0, tcol = 0, maxElemtn = 0;
//                if (row + col > 2 && row + col < 16) {
//                    if (row + col < 10) {
//                        tcol = row + col;
//                        maxElemtn = row + col + 1;
//
//                    } else if (row + col < 16) {
//                        trow = row + col - 9;
//                        tcol = 9;
//                        maxElemtn = 9 - trow + 1;
//                    }
//                    for (int i = 0; i < maxElemtn; i++) {
//                        if (field[trow + i][tcol - i] == 0) {
//                            field[trow + i][tcol - i] = 2;
//                            score = minimax(depth + 1, false, trow + i, tcol - i, 2);
//                            field[trow + i][tcol - i] = 0;
//                            bestScore = Math.max(bestScore, score);
//                            if (bestScore == 1) {
//                                return bestScore;
//                            }
//                        }
//                    }
//                }
//                //check R-Diagonal
//
//                maxElemtn = 10 - Math.abs(row - col);
//                if (row > col) {
//                    trow = row - col;
//                    tcol = 0;
//                } else {
//                    tcol = col - row;
//                    trow = 0;
//                }
//                for (int i = 0; i < maxElemtn; i++) {
//                    if (field[trow + i][tcol + i] == 0) {
//                        field[trow + i][tcol + i] = 2;
//                        score = minimax(depth + 1, false, trow + i, tcol + i, 2);
//                        field[trow + i][tcol + i] = 0;
//                        bestScore = Math.max(bestScore, score);
//                        if (bestScore == 1) {
//                            return bestScore;
//
//                        }
//                    }
//                }
//
//
//            } else {
//                bestScore = Integer.MAX_VALUE;
//                for (int i = 0; i < field.length; i++) {
//                    for (int j = 0; j < field[0].length; j++) {
//                        if (field[i][j] == 0) {
//                            field[i][j] = 1;
//                            score = minimax(depth + 1, true, i, j, 1);
//                            field[i][j] = 0;
//                            bestScore = Math.min(bestScore, score);
//                            if (bestScore == -1) {
//                                return bestScore;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return bestScore;
//
//    }

//    public int minimax(int depth, boolean isMaximizing, int row, int col, int team) {
//        int bestScore;
//        int score;
//        int winner = checkWinner(row, col, team);
//        if (winner != 0 || depth == 2) {
//            return AWARD.get(winner);
//        } else {
//            if (isMaximizing) {
//                bestScore = Integer.MIN_VALUE;
//
//                //check Row
//                for (int i = 0; i < 10; i++) {
//                    if (field[row][i] == 0) {
//                        field[row][i] = 2;
//                        score = minimax(depth + 1, false, row, i, 2);
//                        field[row][i] = 0;
//                        bestScore = Math.max(bestScore, score);
//                        if (bestScore == 1) {
//                            return bestScore;
//
//                        }
//                    }
//                }
//                //check Column
//                for (int i = 0; i < 10; i++) {
//                    if (field[i][col] == 0) {
//                        field[i][col] = 2;
//                        score = minimax(depth + 1, false, i, col, 2);
//                        field[i][col] = 0;
//                        bestScore = Math.max(bestScore, score);
//                        if (bestScore == 1) {
//                            return bestScore;
//
//                        }
//                    }
//                }
//                //check Diagonal
//                int trow = 0, tcol = 0, maxElemtn = 0;
//                if (row + col > 2 && row + col < 16) {
//                    if (row + col < 10) {
//                        tcol = row + col;
//                        maxElemtn = row + col + 1;
//
//                    } else if (row + col < 16) {
//                        trow = row + col - 9;
//                        tcol = 9;
//                        maxElemtn = 9 - trow + 1;
//                    }
//                    for (int i = 0; i < maxElemtn; i++) {
//                        if (field[trow + i][tcol - i] == 0) {
//                            field[trow + i][tcol - i] = 2;
//                            score = minimax(depth + 1, false, trow + i, tcol - i, 2);
//                            field[trow + i][tcol - i] = 0;
//                            bestScore = Math.max(bestScore, score);
//                            if (bestScore == 1) {
//                                return bestScore;
//
//                            }
//                        }
//                    }
//                }
//                //check R-Diagonal
//
//                maxElemtn = 10 - Math.abs(row - col);
//                if (row > col) {
//                    trow = row - col;
//                    tcol = 0;
//                } else {
//                    tcol = col - row;
//                    trow = 0;
//                }
//                for (int i = 0; i < maxElemtn; i++) {
//                    if (field[trow + i][tcol + i] == 0) {
//                        field[trow + i][tcol + i] = 2;
//                        score = minimax(depth + 1, false, trow + i, tcol + i, 2);
//                        field[trow + i][tcol + i] = 0;
//                        bestScore = Math.max(bestScore, score);
//                        if (bestScore == 1) {
//                            return bestScore;
//
//                        }
//                    }
//                }
//
//
//            } else {
//                bestScore = Integer.MAX_VALUE;
//                //check Row
//                for (int i = 0; i < 10; i++) {
//                    if (field[row][i] == 0) {
//                        field[row][i] = 1;
//                        score = minimax(depth + 1, true, row, i, 1);
//                        field[row][i] = 0;
//                        bestScore = Math.min(bestScore, score);
//                        if (bestScore == -1) {
//                            return bestScore;
//                        }
//                    }
//                }
//                //check Column
//                for (int i = 0; i < 10; i++) {
//                    if (field[i][col] == 0) {
//                        field[i][col] = 1;
//                        score = minimax(depth + 1, true, i, col, 1);
//                        field[i][col] = 0;
//                        bestScore = Math.min(bestScore, score);
//                        if (bestScore == -1) {
//                            return bestScore;
//                        }
//                    }
//                }
//                //check Diagonal
//                int trow = 0, tcol = 0, maxElemtn = 0;
//                if (row + col > 2 && row + col < 16) {
//                    if (row + col < 10) {
//                        tcol = row + col;
//                        maxElemtn = row + col + 1;
//                    } else if (row + col < 16) {
//                        trow = row + col - 9;
//                        tcol = 9;
//                        maxElemtn = 9 - trow + 1;
//                    }
//                    for (int i = 0; i < maxElemtn; i++) {
//                        if (field[trow + i][tcol - i] == 0) {
//                            field[trow + i][tcol - i] = 1;
//                            score = minimax(depth + 1, true, trow + i, tcol - i, 1);
//                            field[trow + i][tcol - i] = 0;
//                            bestScore = Math.min(bestScore, score);
//                            if (bestScore == -1) {
//                                return bestScore;
//                            }
//                        }
//                    }
//                }
//                //check R-Diagonal
//                maxElemtn = 10 - Math.abs(row - col);
//                if (row > col) {
//                    trow = row - col;
//                    tcol = 0;
//                } else {
//                    tcol = col - row;
//                    trow = 0;
//                }
//                for (int i = 0; i < maxElemtn; i++) {
//                    if (field[trow + i][tcol + i] == 0) {
//                        field[trow + i][tcol + i] = 1;
//                        score = minimax(depth + 1, true, trow + i, tcol + i, 1);
//                        field[trow + i][tcol + i] = 0;
//                        bestScore = Math.min(bestScore, score);
//                        if (bestScore == -1) {
//                            return bestScore;
//                        }
//                    }
//                }
//            }
//        }
//        return bestScore;
//
//    }

    public int minimax(int depth, boolean isMaximizing, int row, int col, int team) {
        int bestScore;
        int score;
        int winner = checkWinner(row, col, team);
        if ( winner != 0 || depth == 2) {
            return AWARD.get(winner);
        } else {
            if (isMaximizing) {
                bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < field.length; i++) {
                    for (int j = 0; j < field[0].length; j++) {
                        if (field[i][j] == 0) {
                            field[i][j] = 2;
                            score = minimax(depth + 1, false, i, j, 2);
                            field[i][j] = 0;
                            bestScore = Math.max(bestScore, score);
                        }
                    }
                }
            } else {
                bestScore = Integer.MAX_VALUE;
                for (int i = 0; i < field.length; i++) {
                    for (int j = 0; j < field[0].length; j++) {
                        if (field[i][j] == 0) {
                            field[i][j] = 1;
                            score = minimax(depth + 1, true, i, j, 1);
                            field[i][j] = 0;
                            bestScore = Math.min(bestScore, score);
                        }
                    }
                }
            }
        }
        return bestScore;

    }
    public int evaluateBoard() {
        int score = 0;

        // Проверим строки
        for (int row = 0; row < 10; row++) {
            score += evaluateLine(field[row]); // Оценка каждой строки
        }

        // Проверим столбцы
        for (int col = 0; col < 10; col++) {
            int[] column = new int[10];
            for (int row = 0; row < 10; row++) {
                column[row] = field[row][col];
            }
            score += evaluateLine(column); // Оценка каждого столбца
        }

        // Проверим диагонали
        score += evaluateDiagonals(field);

        return score;
    }
    private int evaluateLine(int[] line) {
        int score = 0;
        int player1Count = 0;
        int player2Count = 0;

        for (int cell : line) {
            if (cell == 1) {
                player1Count++;
            } else if (cell == 2) {
                player2Count++;
            }
        }

        // Оценка линии на основе количества символов
        if (player1Count > 0 && player2Count == 0) {
            score += Math.pow(10, player1Count); // Большее количество символов - больше очков
        } else if (player2Count > 0 && player1Count == 0) {
            score -= Math.pow(10, player2Count); // Игрок 2 - отрицательные очки
        }

        return score;
    }
    private int evaluateDiagonals(int[][] field) {
        int score = 0;

        // Проверка диагоналей с верхнего левого в нижний правый
        for (int d = -9; d <= 9; d++) {
            int[] diagonal = new int[10];
            for (int row = 0; row < 10; row++) {
                int col = row + d;
                if (col >= 0 && col < 10) {
                    diagonal[row] = field[row][col];
                }
            }
            score += evaluateLine(diagonal);
        }

        // Проверка диагоналей с верхнего правого в нижний левый
        for (int d = 0; d < 10; d++) {
            int[] diagonal = new int[10];
            for (int row = 0; row < 10; row++) {
                int col = d - row;
                if (col >= 0 && col < 10) {
                    diagonal[row] = field[row][col];
                }
            }
            score += evaluateLine(diagonal);
        }

        return score;
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
                if (cnt == 4) {
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
                if (cnt == 4) {
                    return team;
                }
            } else {
                cnt = 0;
            }
        }
        return 0;
    }

    public int newCheckCol(int col, int team) {
        int cnt = 0;
        int totalPoints = 0;
        int points = 1;
        int oponent = team == 1 ? 2 : 1;
        int lastMove = 0;
        boolean emptyPlace = false;
        for (int i = 0; i < 10; i++) {
            if (field[i][col] == team) {
                emptyPlace = false;
                cnt++;
                points = lastMove == team ? points *= 10 : 1;
                lastMove = team;
                if (cnt == 4) {
                    return team;
                }
            } else if (team == oponent) {
                emptyPlace = false;
                points = lastMove == oponent ? points *= 10 : 1;
                points = -Math.abs(points);
                lastMove = oponent;
                cnt = 0;
            } else {
                emptyPlace = true;
                totalPoints += points;
                points = 1;
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
                    if (cnt == 4) {
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
                if (cnt == 4) {
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
//        int trow = 0, tcol = 0, maxElemtn = 0;
//         COL
//        for (int team = 1; team < 3; team++) {
//            int cnt = 0;
//            for (int i = 0; i < 10; i++) {
//                if (field[i][col] == team) {
//                    cnt++;
//                    if (cnt == 4) {
//                        return team;
//                    }
//                } else {
//                    cnt = 0;
//                }
//            }
//        }
//        ROW
//        for (int team = 1; team < 3; team++) {
//            int cnt = 0;
//            for (int i = 0; i < 10; i++) {
//                if (field[row][i] == team) {
//                    cnt++;
//                    if (cnt == 4) {
//                        return team;
//                    }
//                } else {
//                    cnt = 0;
//                }
//            }
//        }

//        if(row + col > 2 && row + col <16) {
//            if (row + col < 10) {
//                tcol = row + col;
//                maxElemtn = row + col + 1;
//
//            } else if (row + col < 16) {
//                trow = row + col - 9;
//                tcol = 9;
//                maxElemtn = 9 - trow + 1;
//            }
//            for (int team = 1; team < 3; team++) {
//                int cnt = 0;
//                for (int i = 0; i < maxElemtn; i++) {
//                    if (field[trow + i][tcol - i] == team) {
//                        cnt++;
//                        if (cnt == 4) {
//                            return team;
//                        }
//                    } else {
//                        cnt = 0;
//                    }
//                }
//            }
//        }

//        maxElemtn = 10 - Math.abs(row - col);
//        if (row > col){
//            row -= col;
//            col = 0;
//        }else{
//            col -=row;
//            row = 0;
//        }
//        for (int team = 1; team < 3; team++) {
//            int cnt = 0;
//            for (int i = 0; i < maxElemtn; i++) {
//                if (field[row + i][col + i] == team) {
//                    cnt++;
//                    if (cnt == 4) {
//                        return team;
//                    }
//                } else {
//                    cnt = 0;
//                }
//            }
//        }
//        return 0;
//    }

    public void rightMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }

        field[row][col] = 0;
    }
}
