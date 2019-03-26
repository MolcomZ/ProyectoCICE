package util;

import jpa.secciones.SeccionEntity;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InclusionFilter<T> extends RowFilter {
    private HashMap<Integer,ArrayList<T>> map;
    public InclusionFilter(){
        super();
        map=new HashMap<>();
    }

    @Override
    public boolean include(Entry entry) {
        boolean returnValue=true; // SI true MUESTRA EL REGISTRO, SI false NO LO MUESTRA
        int column;

        //COMPRUEBA LOS INCLUDES
        if(map!=null){
            for(column=0;column<entry.getValueCount();column++){
                if(map.containsKey(column)){
                    if(!map.get(column).contains(entry.getValue(column))){
                        returnValue=false;
                    }
                }
            }
            if(map.size()==0){
                return false;
            }
        }
        return returnValue;
    }
    public void add(int column, T t){
        if(map.containsKey(column)){
            map.get(column).add(t);
        }else{
            ArrayList<T> stringList=new ArrayList<>();
            stringList.add(t);
            map.put(column,stringList);
        }
    }
    public void clear(){
        map.clear();
    }
}
