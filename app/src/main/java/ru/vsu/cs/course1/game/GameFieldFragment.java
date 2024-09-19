package ru.vsu.cs.course1.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import ru.vsu.cs.course1.game.databinding.FragmentGameFieldBinding;
import ru.vsu.cs.utils.AndroidUtils;
import ru.vsu.cs.utils.DrawUtils;


public class GameFieldFragment extends Fragment {

    private FragmentGameFieldBinding binding;

    private static final int DEFAULT_COL_COUNT = 10;
    private static final int DEFAULT_ROW_COUNT = 10;
    private static final int DEFAULT_COLOR_COUNT = 7;

    private static final int DEFAULT_GAP = 8;
    private static final int DEFAULT_CELL_SIZE = 30;
    private static final float LINE_WIDTH = 1;

    private static final int[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.MAGENTA,
            Color.CYAN,
            Color.argb(0, 255, 200, 0),   // ORANGE
            Color.argb(0, 255, 175, 175), // PINK,
            Color.WHITE,
            Color.GRAY
    };

    private AndroidUtils utils = null;

    private GameParams params = new GameParams(DEFAULT_ROW_COUNT, DEFAULT_COL_COUNT, DEFAULT_COLOR_COUNT);
    private Game game = new Game();
    private GameFieldView gameFieldView;





    public static class CellCoord {
        public int row;
        public int col;

        public CellCoord(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static class GameFieldView extends View {
        private final AndroidUtils utils;
        private GameFieldFragment parentView;
        private float lastTouchX = -1, lastTouchY = -1;

        @SuppressLint("ClickableViewAccessibility")
        public GameFieldView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);

            setOnTouchListener((view, motionEvent) -> {
                lastTouchX = motionEvent.getX();
                lastTouchY = motionEvent.getY();
                return super.onTouchEvent(motionEvent);
            });
            setOnClickListener(view -> {
                CellCoord cell = getCellByXY((int) lastTouchX, (int) lastTouchY);
                if (cell != null) {
                    parentView.onClick(cell.row, cell.col);
                }
            });
            setOnLongClickListener(view -> {
                CellCoord cell = getCellByXY((int) lastTouchX, (int) lastTouchY);
                if (cell != null) {
                    parentView.onLongClick(cell.row, cell.col);
                    return true;
                }
                return false;
            });

            utils = new AndroidUtils(getContext());
        }

        public CellCoord getCellByXY(int x, int y) {
            int cellSize = parentView.getCellSize();
            int row = (int) ((y - utils.dpToPx(LINE_WIDTH) / 2) / cellSize);
            int col = (int) ((x - utils.dpToPx(LINE_WIDTH) / 2) / cellSize);
            if (row < 0 || row >= getGameParams().getRowCount() || col < 0 || col >= getGameParams().getColCount()) {
                return null;
            }

            return new CellCoord(row, col);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float lineWidth = utils.dpToPx(LINE_WIDTH);

            @SuppressLint("DrawAllocation")
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(Color.BLACK);

            canvas.save();
            canvas.translate(utils.dpToPx(LINE_WIDTH) / 2, utils.dpToPx(LINE_WIDTH) / 2);

            if (isInEditMode()) {
                // просто режим разработки
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            } else {

                Game game = getGame();
                int cellSize = parentView.getCellSize();

                int width = cellSize * game.getColCount();
                for (int r = 0; r <= game.getRowCount(); r++) {
                    int y = r * cellSize;
                    canvas.drawLine(0, y, width, y, paint);
                }
                int height = cellSize * game.getRowCount();
                for (int c = 0; c <= game.getColCount(); c++) {
                    int x = c * cellSize;
                    canvas.drawLine(x, 0, x, height, paint);
                }
                for (int r = 0; r <= game.getRowCount(); r++) {
                    for (int c = 0; c <= game.getColCount(); c++) {
                        canvas.save();
                        canvas.translate(c * cellSize, r * cellSize);
                        parentView.drawCell(r, c, cellSize, cellSize, canvas);
                        canvas.restore();
                    }
                }

            }


//            @SuppressLint("UseCompatLoadingForDrawables")
//            Drawable d = getResources().getDrawable(R.drawable.mine, null);
//            d.setBounds(0, 0, utils.dpToPxInt(50), utils.dpToPxInt(50));
//            d.draw(canvas);

            canvas.restore();
        }


        public Game getGame() {
            return parentView.game;
        }

        public GameParams getGameParams() {
            return parentView.params;
        }

        void setParentView(GameFieldFragment parentView) {
            this.parentView = parentView;
        }
    }

    {
        game.newGame(params.getRowCount(), params.getColCount(), params.getColorCount());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentGameFieldBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utils = new AndroidUtils(getContext());

        gameFieldView = Objects.requireNonNull(binding.viewGameField);
        gameFieldView.setParentView(this);

        binding.buttonFirst.setOnClickListener(view1 ->
                NavHostFragment.findNavController(GameFieldFragment.this)
                        .navigate(R.id.action_GameFieldFragment_to_ParamsFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void restartGame() {
        game.newGame(10,10,2);
    }

    protected void onClick(int row, int col) {
        int win = game.leftMouseClick(row, col);
        if (win == 1 || win == 2) {
            String text = win == 1 ? "игрок" : "бот";
            Toast toast = Toast.makeText(gameFieldView.getContext(), "Победил " + text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 160);   // import android.view.Gravity;
            toast.show();
            game.newGame(10, 10, 2);
        }

        updateView();
    }

    protected void onLongClick(int row, int col) {

        game.rightMouseClick(row, col);
        updateView();
    }

    private int getCellSize() {
        int cellSize = utils.dpToPxInt(params.getCellSize());
        if (params.isDefaultCellSize()) {
            cellSize = Math.min(
                    (getView().getWidth() - utils.dpToPxInt(2 * 10)) / game.getColCount(),
                    getView().getHeight() / game.getRowCount()
            );
        }
        return cellSize;
    }

    private Paint paint = null;

    protected void drawCell(int row, int col, int cellWidth, int cellHeight, Canvas canvas) {
        int value = game.getCell(row, col);
        if (value > 0) {
            int size = Math.min(cellWidth, cellHeight);
            int bound = (int) Math.round(size * 0.1);

            if (paint == null) {
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }

            int color = COLORS[value - 1];

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(COLORS[value - 1]);
            canvas.drawRoundRect(bound, bound, size - bound, size - bound, bound * 3, bound * 3, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(utils.dpToPx(0.5f));
            paint.setColor(Color.DKGRAY);
            canvas.drawRoundRect(bound, bound, size - bound, size - bound, bound * 3, bound * 3, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(DrawUtils.getContrastColor(color));
            paint.setTextSize(cellHeight * 0.7f);
            if (value == 1) {
                DrawUtils.drawStringInCenter(canvas, paint, "X", 0, 0, cellWidth, cellHeight);
            } else {
                DrawUtils.drawStringInCenter(canvas, paint, "O", 0, 0, cellWidth, cellHeight);

            }
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            game = (Game) savedInstanceState.getSerializable("game");
        } catch (Exception ignored) {
        }
        try {
            params = (GameParams) savedInstanceState.getSerializable("params");
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
        outState.putSerializable("params", params);
    }

    public void startNewGame() {
        game.newGame(10, 10, 2);
        updateView();

    }


    public void updateView() {
        int cellSize = getCellSize();
        gameFieldView.setMinimumWidth(cellSize * game.getRowCount() + utils.dpToPxInt(LINE_WIDTH));
        gameFieldView.setMinimumHeight(cellSize * game.getColCount() + utils.dpToPxInt(LINE_WIDTH));

        gameFieldView.invalidate();
    }
}