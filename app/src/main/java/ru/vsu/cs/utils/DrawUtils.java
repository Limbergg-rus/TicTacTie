package ru.vsu.cs.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class DrawUtils {
    /**
     * Рисование строки в центре прямоугольника (x, y, width, height).
     * @param canvas - Экземпляр <code>Canvas</code> для рисования
     * @param paint - Экземпляр <code>Paint</code> для рисования
     * @param s - Строка, которую надо нарисовать
     * @param x - Координата x верхнего левого угла прямоугольника
     * @param y - Координата y верхнего левого угла прямоугольника
     * @param width - Ширина прямоугольника
     * @param height - Высота прямоугольника
     */
    public static void drawStringInCenter(Canvas canvas, Paint paint, String s, float x, float y, int width, int height) {
        paint.setTextAlign(Paint.Align.CENTER);

        Rect r = new Rect();
        paint.getTextBounds(s, 0, s.length(), r);

        float textX = x + width / 2f;
        float textY = y - r.top - r.height() / 2f + height / 2f;
        canvas.drawText(s, textX, textY, paint);
    }

    /**
     * Возвращает контрастный цвет (белый или черный) к переданному цвету
     * @param color Цвет
     * @return Контрастный цвет
     */
    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000d;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }
}
