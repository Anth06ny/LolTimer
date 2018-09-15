package timer.anthony.com.loltimer.model.beans.responseBean;

import com.google.gson.annotations.SerializedName;

import timer.anthony.com.loltimer.model.beans.dao.SummonerBean;

public class GetSummonersResponseBean {

    public Data data;

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("SummonerBarrier")
        private SummonerBean summonerBarrier;
        @SerializedName("SummonerBoost")
        private SummonerBean summonerBoost;
        @SerializedName("SummonerDot")
        private SummonerBean summonerDot; //Embrassement
        @SerializedName("SummonerExhaust")
        private SummonerBean summonerExhaust;   //Fatigue
        @SerializedName("SummonerFlash")
        private SummonerBean summonerFlash;
        @SerializedName("SummonerHaste")
        private SummonerBean summonerHaste; //Fantome
        @SerializedName("SummonerHeal")
        private SummonerBean summonerHeal;
        @SerializedName("SummonerMana")
        private SummonerBean summonerMana;
        @SerializedName("SummonerSmite")
        private SummonerBean summonerSmite;
        @SerializedName("SummonerTeleport")
        private SummonerBean summonerTeleport;

        public SummonerBean getSummonerBarrier() {
            return summonerBarrier;
        }

        public void setSummonerBarrier(SummonerBean summonerBarrier) {
            this.summonerBarrier = summonerBarrier;
        }

        public SummonerBean getSummonerBoost() {
            return summonerBoost;
        }

        public void setSummonerBoost(SummonerBean summonerBoost) {
            this.summonerBoost = summonerBoost;
        }

        public SummonerBean getSummonerDot() {
            return summonerDot;
        }

        public void setSummonerDot(SummonerBean summonerDot) {
            this.summonerDot = summonerDot;
        }

        public SummonerBean getSummonerExhaust() {
            return summonerExhaust;
        }

        public void setSummonerExhaust(SummonerBean summonerExhaust) {
            this.summonerExhaust = summonerExhaust;
        }

        public SummonerBean getSummonerFlash() {
            return summonerFlash;
        }

        public void setSummonerFlash(SummonerBean summonerFlash) {
            this.summonerFlash = summonerFlash;
        }

        public SummonerBean getSummonerHaste() {
            return summonerHaste;
        }

        public void setSummonerHaste(SummonerBean summonerHaste) {
            this.summonerHaste = summonerHaste;
        }

        public SummonerBean getSummonerHeal() {
            return summonerHeal;
        }

        public void setSummonerHeal(SummonerBean summonerHeal) {
            this.summonerHeal = summonerHeal;
        }

        public SummonerBean getSummonerMana() {
            return summonerMana;
        }

        public void setSummonerMana(SummonerBean summonerMana) {
            this.summonerMana = summonerMana;
        }

        public SummonerBean getSummonerSmite() {
            return summonerSmite;
        }

        public void setSummonerSmite(SummonerBean summonerSmite) {
            this.summonerSmite = summonerSmite;
        }

        public SummonerBean getSummonerTeleport() {
            return summonerTeleport;
        }

        public void setSummonerTeleport(SummonerBean summonerTeleport) {
            this.summonerTeleport = summonerTeleport;
        }
    }
}
