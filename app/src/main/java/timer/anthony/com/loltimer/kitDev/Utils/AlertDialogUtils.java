package timer.anthony.com.loltimer.kitDev.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.Voice;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Set;

import timer.anthony.com.loltimer.kitDev.SharedPreferenceUtils;

public class AlertDialogUtils {

    /**
     * Dialog pour séléctionner une voie
     */
    public static void showSelectOneDialog(Context context, final Set<Voice> list, final SelectVoiceListener selectVoiceListener) {
        final ArrayList<Voice> voices = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        //La voie sauvegarder
        String saveVoicename = SharedPreferenceUtils.getSaveVoice();
        int indexSelectedVoice = 0;

        int i = 0;
        for (Voice p : list) {
            if (p.getLocale().getCountry().equals("FR")) {
                if (p.getName().equals(saveVoicename)) {
                    indexSelectedVoice = i;
                }
                voices.add(p);
                strings.add(p.getName());
                i++;
            }
        }

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("Séléctionner une voie");
        alt_bld.setSingleChoiceItems(strings.toArray(new String[0]), indexSelectedVoice, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();// dismiss the alertbox after chose option
                if (selectVoiceListener != null) {
                    selectVoiceListener.onSelectVoiceListener(voices.get(item));
                }
            }
        });
        alt_bld.setCancelable(true);
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public interface SelectVoiceListener {
        void onSelectVoiceListener(Voice voice);
    }
}
