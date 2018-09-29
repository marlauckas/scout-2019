package citruscircuits.scout.Managers;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citruscircuits.scout._superDataClasses.AppCc;

//Written by the Daemon himself ~ Calvin
public class InputManager {
    //Tasks
    // (1) Store Permanent Data In SDCARD In MainActivity
    // (2) Store Temporary Data During Scouting Process

    //Match Data Holders
    public static JSONObject mRealTimeMatchData;
    public static JSONObject mRealTimeInputtedData;

    //Main Inputs
    public static String mAllianceColor = "";

    public static String mScoutName = "unselected";
    public static int mScoutId = 0;

    public static int mMatchNum = 0;
    public static int mTeamNum = 0;

    public static int mCycleNum = 0;

    //Map-Scouting Variables
    public static String mStartingPosition;

    public static JSONArray climbDataArray = new JSONArray();
    public static ArrayList<HashMap<String, Object>> climbDataList = new ArrayList<HashMap<String, Object>>();

    public static JSONArray autoAllianceSwitchDataArray = new JSONArray();
    public static JSONArray teleAllianceSwitchDataArray = new JSONArray();
    public static JSONArray teleOpponentSwitchDataArray = new JSONArray();

    public static ArrayList<HashMap<String, Object>> autoAllianceSwitchDataList = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, Object>> teleAllianceSwitchDataList = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, Object>> teleOpponentSwitchDataList = new ArrayList<HashMap<String, Object>>();

    public static JSONArray autoScaleDataArray = new JSONArray();
    public static JSONArray teleScaleDataArray = new JSONArray();

    public static ArrayList<HashMap<String, Object>> autoScaleDataList = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, Object>> teleScaleDataList = new ArrayList<HashMap<String, Object>>();

    public static JSONArray alliancePlatformTakenAuto = new JSONArray();
    public static JSONArray alliancePlatformTakenTele = new JSONArray();
    public static JSONArray opponentPlatformTakenTele = new JSONArray();

    public static JSONArray dropTimes = new JSONArray();
    public static JSONArray incapTimes = new JSONArray();
    public static JSONArray unincapTimes = new JSONArray();

    public static int numGroundPyramidIntakeAuto = 0;
    public static int numGroundPyramidIntakeTele = 0;
    public static int numElevatedPyramidIntakeAuto = 0;
    public static int numElevatedPyramidIntakeTele = 0;
    public static int numSpill = 0;
    public static int numFoul = 0;

    public static JSONArray vaultDataArray = new JSONArray();
    public static int totalCubesVaulted = 0;
    public static boolean vaultOpen = false;

    public static boolean autoRunMade = false;

    public static void storeUserData(){
        AppCc.setSp("allianceColor", mAllianceColor);
        AppCc.setSp("scoutName", mScoutName);
        AppCc.setSp("scoutId", mScoutId);
        AppCc.setSp("matchNum", mMatchNum);
        AppCc.setSp("teamNum", mTeamNum);
        AppCc.setSp("cycleNum", mCycleNum);
    }

    public static void recoverUserData(){
        mAllianceColor = AppCc.getSp("allianceColor", "");
        mScoutName = AppCc.getSp("scoutName", "unselected");
        mScoutId = AppCc.getSp("scoutId", 0);
        mMatchNum = AppCc.getSp("matchNum", 0);
        mTeamNum = AppCc.getSp("teamNum", 0);
        mCycleNum = AppCc.getSp("cycleNum", 0);
    }

    public static void prepareUserDataForNextMatch(){
        mAllianceColor = "";
        //Scout Name Doesn't Change
        //Scout Id Doesn't Change
        mMatchNum++;
        mTeamNum = 0;
        //Cycle Number Doesn't Change

        storeUserData();
    }

    //TODO update the correct key input format later
    public static void updateRealTimeMatchData() throws JSONException, NullPointerException {
        mRealTimeInputtedData.put("teamNumber", mTeamNum);
        mRealTimeInputtedData.put("matchNumber", mMatchNum);
        mRealTimeInputtedData.put("scoutName", mScoutName);

        mRealTimeInputtedData.put("startingPosition", mStartingPosition);

        mRealTimeInputtedData.put("climb", climbDataArray);

        mRealTimeInputtedData.put("allianceSwitchAttemptAuto", autoAllianceSwitchDataArray);
        mRealTimeInputtedData.put("allianceSwitchAttemptTele", teleAllianceSwitchDataArray);
        mRealTimeInputtedData.put("opponentSwitchAttemptTele", teleOpponentSwitchDataArray);

        mRealTimeInputtedData.put("scaleAttemptAuto", autoScaleDataArray);
        mRealTimeInputtedData.put("scaleAttemptTele", teleScaleDataArray);

        mRealTimeInputtedData.put("drop", dropTimes);
        mRealTimeInputtedData.put("incap", incapTimes);
        mRealTimeInputtedData.put("unincap", unincapTimes);

        mRealTimeInputtedData.put("alliancePlatformIntakeAuto", alliancePlatformTakenAuto);
        mRealTimeInputtedData.put("alliancePlatformIntakeTele", alliancePlatformTakenTele);
        mRealTimeInputtedData.put("opponentPlatformIntakeTele", opponentPlatformTakenTele);

        mRealTimeInputtedData.put("numGroundPyramidIntakeAuto", numGroundPyramidIntakeAuto);
        mRealTimeInputtedData.put("numGroundPyramidIntakeTele", numGroundPyramidIntakeTele);
        mRealTimeInputtedData.put("numElevatedPyramidIntakeAuto", numElevatedPyramidIntakeAuto);
        mRealTimeInputtedData.put("numElevatedPyramidIntakeTele", numElevatedPyramidIntakeTele);

        mRealTimeInputtedData.put("numSpill", numSpill);
        mRealTimeInputtedData.put("numFoul", numFoul);

        mRealTimeMatchData.put(mTeamNum + "Q" + mMatchNum, mRealTimeInputtedData);
        AppCc.setSp("currentMatchDataKey", mTeamNum + "Q" + mMatchNum);
        AppCc.setSp("mRealTimeMatchData", mRealTimeMatchData.toString());
    }

    public static void recoverRealTimeMatchData() throws JSONException, NullPointerException {
        mRealTimeMatchData = AppCc.getJSONSp("mRealTimeMatchData");
        mRealTimeInputtedData = mRealTimeMatchData.getJSONObject(AppCc.getSp("currentMatchDataKey", ""));

        mTeamNum = mRealTimeInputtedData.getInt("teamNumber");
        mMatchNum = mRealTimeInputtedData.getInt("matchNumber");
        mScoutName = mRealTimeInputtedData.getString("scoutName");

        mStartingPosition = mRealTimeInputtedData.getString("startingPosition");

        climbDataArray = mRealTimeInputtedData.getJSONArray("climb");

        autoAllianceSwitchDataArray = mRealTimeInputtedData.getJSONArray("allianceSwitchAttemptAuto");
        teleAllianceSwitchDataArray = mRealTimeInputtedData.getJSONArray("allianceSwitchAttemptTele");
        teleOpponentSwitchDataArray = mRealTimeInputtedData.getJSONArray("opponentSwitchAttemptTele");

        autoScaleDataArray = mRealTimeInputtedData.getJSONArray("scaleAttemptAuto");
        teleScaleDataArray = mRealTimeInputtedData.getJSONArray("scaleAttemptTele");

        alliancePlatformTakenAuto = mRealTimeInputtedData.getJSONArray("alliancePlatformIntakeAuto");
        alliancePlatformTakenTele = mRealTimeInputtedData.getJSONArray("alliancePlatformIntakeTele");
        opponentPlatformTakenTele = mRealTimeInputtedData.getJSONArray("opponentPlatformIntakeTele");

        numGroundPyramidIntakeAuto = mRealTimeInputtedData.getInt("numGroundPyramidIntakeAuto");
        numGroundPyramidIntakeTele = mRealTimeInputtedData.getInt("numGroundPyramidIntakeTele");
        numElevatedPyramidIntakeAuto = mRealTimeInputtedData.getInt("numElevatedPyramidIntakeAuto");
        numElevatedPyramidIntakeTele = mRealTimeInputtedData.getInt("numElevatedPyramidIntakeTele");
    }
}