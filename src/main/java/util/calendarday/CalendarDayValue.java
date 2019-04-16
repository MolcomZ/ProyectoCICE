package util.calendarday;

import java.awt.*;
import java.util.Objects;

public class CalendarDayValue {
    private Long id;
    private String text;
    private Color color;

    public CalendarDayValue() {
    }

    public CalendarDayValue(Long id, String text, Color color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarDayValue that = (CalendarDayValue) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, text, color);
    }

    @Override
    public String toString() {
        return "CalendarDayValue{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", color=" + color +
                '}';
    }
}
