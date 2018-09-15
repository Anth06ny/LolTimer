package timer.anthony.com.loltimer;

import android.view.View;
import android.widget.ImageView;

import timer.anthony.com.loltimer.kitDev.MyApplication;

public class GestionRowPlayer {

    View root;

    ImageView ivPrevious, ivNext, ivChamp, ivSpell1, ivSpell2;

    public GestionRowPlayer(View root) {
        this.root = root;

        ivPrevious = root.findViewById(R.id.ivPrevious);
        ivNext = root.findViewById(R.id.ivNext);
        ivChamp = root.findViewById(R.id.ivChamp);
        ivSpell1 = root.findViewById(R.id.ivSpell1);
        ivSpell2 = root.findViewById(R.id.ivSpell2);

        ivPrevious.setColorFilter(MyApplication.getInstance().getResources().getColor(R.color.colorAccent));
        ivNext.setColorFilter(MyApplication.getInstance().getResources().getColor(R.color.colorAccent));
    }
}
