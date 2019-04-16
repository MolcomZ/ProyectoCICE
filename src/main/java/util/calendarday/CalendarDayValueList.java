package util.calendarday;

import java.util.ArrayList;

public class CalendarDayValueList{
    private ArrayList<CalendarDayValue> list;

    public CalendarDayValueList(){
        list=new ArrayList<>();
    }
    public CalendarDayValueList(ArrayList<CalendarDayValue> list){
        this.list=list;
    }
    public void add(CalendarDayValue value){
        list.add(value);
    }
    public void remove(CalendarDayValue value){
        list.remove(value);
    }
    public void remove(int index){
        list.remove(index);
    }
    public ArrayList<CalendarDayValue> getList(){
        return list;
    }
}
