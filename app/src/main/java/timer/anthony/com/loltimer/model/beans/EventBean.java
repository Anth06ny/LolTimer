package timer.anthony.com.loltimer.model.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import timer.anthony.com.loltimer.R;
import timer.anthony.com.loltimer.kitDev.MyApplication;

public class EventBean {

    String texte;
    long timeInSec;

    public EventBean(String texte, long timeInSec) {
        this.texte = texte;
        this.timeInSec = timeInSec;
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
                return (int) (o1.timeInSec - o2.timeInSec);
            }
        });
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public long getTimeInSec() {
        return timeInSec;
    }

    public void setTimeInSec(long timeInSec) {
        this.timeInSec = timeInSec;
    }
}
