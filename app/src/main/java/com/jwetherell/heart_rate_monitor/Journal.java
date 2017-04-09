package com.jwetherell.heart_rate_monitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Journal extends Activity {

    private static SentiWordNetDemoCode SA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);
        try {
            SA = new SentiWordNetDemoCode();
            //Log.i("Debug", ""+SA.extract("good", "a"));
        }catch(Exception e){
            Log.i("Debug", "SA Failure");};
    }

    public void analyze(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        String msg = editText.getText().toString();

        //normalize
        msg = msg.toLowerCase();
        msg = msg.trim();
        msg = msg.replaceAll("[^a-zA-Z0-9\\s]", "");
        double val = 0;
        double totCount = 0;
        String[] words = msg.split(" ");
        for (int i = 0; i < words.length; i++) {
            double noun = 0; double verb = 0; double adj = 0;
            try{
            noun = SA.extract(words[i], "n");}catch (Exception e){};
            try{
            adj = SA.extract(words[i], "a");}catch (Exception e){};
            try{
            verb = SA.extract(words[i], "v");}catch (Exception e){};

            double count = 0;
            double addVal = 0;
            double epsilon = 0.000001;
            if (noun <= 0-epsilon || noun >= 0+epsilon){
                addVal += noun;
                count +=1;
            }
            if (adj <= 0-epsilon || adj >= 0+epsilon){
                addVal += adj;
                count +=1;
            }
            if (verb <= 0-epsilon || verb >= 0+epsilon){
                addVal += verb;
                count +=1;
            }
            if (count >= 0 + epsilon){
            addVal/=count;
            val+=addVal;
            totCount+=1;}
            Log.i("debug", words[i] + " " + noun + " " + adj + " " + verb);
        }
        double epsilon = 0.000001;
        if (totCount >= 0+epsilon) val/=totCount;
        //val/=words.length;

        try {
            FileOutputStream fOut = MainActivity.appstate.openFileOutput("journal.txt", Context.MODE_APPEND);
            fOut.write(((new Date()).toString() + " " + val + "\n").getBytes());
            Log.i("debug", val + "");
            fOut.close();
        }catch (Exception e){Log.i("debug", "fail");};

        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/journal.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("debug", line);
            }
            fileReader.close();
        }catch (Exception e){Log.i("debug", "read fail");};
        Intent intent = new Intent(Journal.this, MainActivity.class);
        Journal.this.startActivity(intent);
    }

    public class SentiWordNetDemoCode {

        private Map<String, Double> dictionary;

        public SentiWordNetDemoCode() throws IOException {
            // This is our main dictionary representation
            dictionary = new HashMap<String, Double>();

            // From String to list of doubles.
            HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

            BufferedReader csv = null;
            try {
                csv = new BufferedReader(new InputStreamReader((MainActivity.appstate).getResources().openRawResource(R.raw.swn)));
                int lineNumber = 0;

                String line;
                while ((line = csv.readLine()) != null) {
                    lineNumber++;

                    // If it's a comment, skip this line.
                    if (!line.trim().startsWith("#")) {
                        // We use tab separation
                        String[] data = line.split("\t");
                        String wordTypeMarker = data[0];

                        // Example line:
                        // POS ID PosS NegS SynsetTerm#sensenumber Desc
                        // a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
                        // ascetic#2 practicing great self-denial;...etc

                        // Is it a valid line? Otherwise, through exception.
                        if (data.length != 6) {
                            throw new IllegalArgumentException(
                                    "Incorrect tabulation format in file, line: "
                                            + lineNumber);
                        }

                        // Calculate synset score as score = PosS - NegS
                        Double synsetScore = Double.parseDouble(data[2])
                                - Double.parseDouble(data[3]);

                        // Get all Synset terms
                        String[] synTermsSplit = data[4].split(" ");

                        // Go through all terms of current synset.
                        for (String synTermSplit : synTermsSplit) {
                            // Get synterm and synterm rank
                            String[] synTermAndRank = synTermSplit.split("#");
                            String synTerm = synTermAndRank[0] + "#"
                                    + wordTypeMarker;

                            int synTermRank = Integer.parseInt(synTermAndRank[1]);
                            // What we get here is a map of the type:
                            // term -> {score of synset#1, score of synset#2...}

                            // Add map to term if it doesn't have one
                            if (!tempDictionary.containsKey(synTerm)) {
                                tempDictionary.put(synTerm,
                                        new HashMap<Integer, Double>());
                            }

                            // Add synset link to synterm
                            tempDictionary.get(synTerm).put(synTermRank,
                                    synsetScore);
                        }
                    }
                }

                // Go through all the terms.
                for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
                        .entrySet()) {
                    String word = entry.getKey();
                    Map<Integer, Double> synSetScoreMap = entry.getValue();

                    // Calculate weighted average. Weigh the synsets according to
                    // their rank.
                    // Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
                    // Sum = 1/1 + 1/2 + 1/3 ...
                    double score = 0.0;
                    double sum = 0.0;
                    for (Map.Entry<Integer, Double> setScore : synSetScoreMap
                            .entrySet()) {
                        score += setScore.getValue() / (double) setScore.getKey();
                        sum += 1.0 / (double) setScore.getKey();
                    }
                    score /= sum;

                    dictionary.put(word, score);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (csv != null) {
                    csv.close();
                }
            }
        }

        public double extract(String word, String pos) {
            return dictionary.get(word + "#" + pos);
        }


    }
}
