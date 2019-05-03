package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityListenerManager {
    private static ArrayList<Pair> list=new ArrayList<>();

    public static void addListener(Class entityClass,EntityListener listener){
        list.add(new Pair(entityClass,listener));
    }
    public static void removeListener(Class entityClass,EntityListener listener){
        list.remove(new Pair(entityClass,listener));
    }
    public static void fireEntityUpdated(Class entityClass){
        for(Pair pair:list){
            if (pair.getEntityClass().equals(entityClass)) {
                pair.getEntityListener().entityUpdated();
            }
        }
    }
}
class Pair{
    private Class entityClass;
    private EntityListener listener;

    public Pair(Class entityClass,EntityListener listener){
        set(entityClass,listener);
    }

    public void set(Class entityClass,EntityListener listener){
        this.entityClass=entityClass;
        this.listener=listener;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public void setListener(EntityListener listener) {
        this.listener = listener;
    }

    public Class getEntityClass(){
        return entityClass;
    }
    public EntityListener getEntityListener(){
        return listener;
    }
}
