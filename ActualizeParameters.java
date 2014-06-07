/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smizsk;

import java.util.ArrayList;

/**
 *
 * @author madaooo
 */
public class ActualizeParameters {

    static int max = 0, min = 0;
    static ArrayList<Integer> averageBandwith = new ArrayList<>();
    int aver =0;
    /// dam tu tylko poszczegolne obliczenia

    public int calculateBandtwitch(String maxInterfece, String S1, String S2, int delayTime) {
        int sumSpeedInterface = 0;
        String[] intEl = maxInterfece.split(";");

        //petla foreach
        for (String intEl1 : intEl) {
            sumSpeedInterface += Integer.parseInt(intEl1);
        }

        String[] T1all = S1.split(";");
        String[] T2all = S2.split(";");

        // teraz t2 sumujemy niah niah niah
        int T1 = 0;

        for (String intT11 : T1all) {
            T1 += Integer.parseInt(intT11);
        }

        int T2 = 0;

        for (String intT21 : T2all) {
            T2 += Integer.parseInt(intT21);
        }

        int ans = (((T1 - T2) * 8) * 100);
        int ans2 = ((sumSpeedInterface / 100) * (delayTime / 1000));
        int ansAll = Math.abs(ans / ans2); // kb/s

        averageBandwith.add(ansAll);

        return ansAll;
    }

    public int setMax(int ansAll) {
        // maksymalna przepustowosc
        if (max < ansAll) {
            max = ansAll;
        }
        return max;
    }

    public int setMin(int ansAll) {
        // minimalna
        if (ansAll > 0 && ansAll < min) {
            min = ansAll;

        } else if (min == 0) {
            min = ansAll;

        }
        return min;
    }

    public int calculateAverage() {

        if (averageBandwith.isEmpty() && averageBandwith.size() < 2) {
            return aver;
        } else {
            for(int i =0; i < averageBandwith.size();i++) {
                aver += averageBandwith.get(i);
            }
            aver /= averageBandwith.size();
            
            if(averageBandwith.size() > 1000) {
                averageBandwith.clear();
            }
            
            return aver;
        }
    }
    
    public String getSystem(String getOID) {
        String Name = null;
        String[] names = getOID.split(" ");

        return Name = names[0];
    }

}
