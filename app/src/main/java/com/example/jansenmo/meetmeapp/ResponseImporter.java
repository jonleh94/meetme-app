package com.example.jansenmo.meetmeapp;

import android.os.NetworkOnMainThreadException;
import android.util.JsonReader;

//import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * Created by lehj on 28.10.2015.
 */
public class ResponseImporter {
    public <F> ArrayList<F> readJsonStream(InputStream in) throws IOException, NetworkOnMainThreadException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return this.<F>readJsonObject(reader);
        } finally {
            reader.close();
        }
    }

    public <R> ArrayList<R> readJsonObject(JsonReader reader) throws
            IOException, NetworkOnMainThreadException {
        ArrayList<R> objectArray = new ArrayList<R>();
        reader.beginObject();
        while (reader.hasNext()) {
            String test = reader.nextName();
            if (test.equals("scoreBoard")) {
                objectArray = this.<R>readScoreArray(reader);
            }
        }
        return objectArray;
    }

    public static <R> ArrayList<R> readScoreArray(JsonReader reader) throws
            IOException, NetworkOnMainThreadException {
        ArrayList<R> scoreArray = new ArrayList();
        reader.beginArray();
        try {
            while (reader.hasNext()) {
                int score = 0;
                String username = null;

                reader.beginObject();
                while (reader.hasNext()) {
                    String test = reader.nextName();
                    if (test.equals("score")) {
                        score = reader.nextInt();
                    } else if (test.equals("username")) {
                        username = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                scoreArray.add((R) new Score(username, score));
            }
        }
        //    }}
        //catch (IOException e) {System.out.println(e);}
        catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } finally {
            reader.endArray();
            return scoreArray;
        }
    }

       /* public <R> ArrayList<R> readTeamRankings(JsonReader reader) throws IOException{
            ArrayList<R> teamArrayList = new ArrayList<R>();
            reader.beginArray();
            try {
                while (reader.hasNext()) {
                    String team = null;
                    String name = null;
                    Integer score = null;

                    reader.beginObject();
                    while (reader.hasNext()) {
                        String test = reader.nextName();
                        if (test.equals("nation")) {
                            team = reader.nextString();}
                        else if (test.equals("score")) {
                            score = reader.nextInt();}
                        else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    teamArrayList.add((R)(new String[]{team, score.toString()}));
                }
            }
            //    }}
            //catch (IOException e) {System.out.println(e);}
            catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } finally {
                reader.endArray();
                return teamArrayList;
            }
        }

        public <R> ArrayList<R> readTopUserRankings(JsonReader reader) throws IOException{
            ArrayList<R> topUserArrayList = new ArrayList<R>();
            reader.beginArray();
            try {
                while (reader.hasNext()) {
                    String team = null;
                    String name = null;
                    Integer score = null;

                    reader.beginObject();
                    while (reader.hasNext()) {
                        String test = reader.nextName();
                        if (test.equals("nation")) {
                            team = reader.nextString();}
                        else if (test.equals("name")) {
                            name = reader.nextString();}
                        else if (test.equals("score")) {
                            score = reader.nextInt();}
                        else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    topUserArrayList.add((R) (new String[]{name, team, score.toString()}));
                }
            }
            //    }}
            //catch (IOException e) {System.out.println(e);}
            catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } finally {
                reader.endArray();
                return topUserArrayList;
            }
        }

        public <R> ArrayList<R> readFriends(JsonReader reader) throws IOException{
            ArrayList<R> friendsArrayList = new ArrayList<R>();
            reader.beginArray();
            try {
                while (reader.hasNext()) {
                    String team = null;
                    String name = null;
                    Integer score = null;

                    reader.beginObject();
                    while (reader.hasNext()) {
                        String test = reader.nextName();
                        if (test.equals("nation")) {
                            team = reader.nextString();}
                        if (test.equals("username")) {
                            name = reader.nextString();}
                        else if (test.equals("score")) {
                            score = reader.nextInt();}
                        else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    if (team == null) {
                        friendsArrayList.add((R) (new String[]{name, score.toString()}));}
                    else {friendsArrayList.add((R) (new String[]{name, team}));}
                }
            }
            //    }}
            //catch (IOException e) {System.out.println(e);}
            catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } finally {
                reader.endArray();
                return friendsArrayList;
            }
        }
    }
*/
}