package timer.anthony.com.loltimer.model.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import timer.anthony.com.loltimer.R;
import timer.anthony.com.loltimer.kitDev.MyApplication;

public class EventBean {

    String texte;
    long time;

    public EventBean(String texte, long time) {
        this.texte = texte;
        this.time = time;
    }

    public EventBean() {
    }

    /**
     * @return list des event de base
     */
    public static ArrayList<EventBean> getBasicEvent() {
        ArrayList<EventBean> list = new ArrayList<>();
        String[] array = MyApplication.getInstance().getResources().getStringArray(R.array.event);
        for (int i = 0; i + 1 < array.length; i += 2) {
            list.add(new EventBean(array[i], Integer.valueOf(array[i + 1])));
        }

        return list;
    }

    //trie par date
    public static void sortByTime(ArrayList<EventBean> list) {
        Collections.sort(list, new Comparator<EventBean>() {
            @Override
            public int compare(EventBean o1, EventBean o2) {
                return (int) (o1.time - o2.time);
            }
        });
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
