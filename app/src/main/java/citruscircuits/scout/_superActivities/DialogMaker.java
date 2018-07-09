package citruscircuits.scout._superActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.net.ParseException;

import org.json.JSONObject;

import citruscircuits.scout.A0A;
import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AppUtils;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

//Written by the Daemon himself ~ Calvin
public class DialogMaker extends AppTc{

    public void initSeesawDialog(Activity a, boolean isOpponentSeesaw, boolean isScale){
        SeesawDialog mSeesawDialog = new SeesawDialog(a, isOpponentSeesaw, isScale);
        mSeesawDialog.show();
    }

    public class SeesawDialog extends Dialog implements View.OnClickListener {

        public Activity context;

        public boolean isOpponentSeesaw, isScale;

        public TextView tv_DialogTitle;
        public ToggleButton tbtn_Outcome;
        public RadioButton rbtn_OpponentOwned, rbtn_Balanced,
                    rbtn_Layer1, rbtn_Layer2, rbtn_Layer3;

        public JSONObject mSeesawInfo;

        public SeesawDialog(Activity a, boolean isOpponentSeesaw, boolean isScale){
            super(a);
            SeesawDialog.this.context = a;

            SeesawDialog.this.isOpponentSeesaw = isOpponentSeesaw;
            SeesawDialog.this.isScale = isScale;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_seesaws);

            tv_DialogTitle = (TextView) findViewById(R.id.dialogTitle);
            if(isOpponentSeesaw && isScale){        tv_DialogTitle.setText(R.string.tv_titleIsOpponentScale);}
            else if(!isOpponentSeesaw && isScale){      tv_DialogTitle.setText(R.string.tv_titleIsAllianceScale);}
            else if(isOpponentSeesaw && !isScale){      tv_DialogTitle.setText(R.string.tv_titleIsOpponentSwitch);}
            else if(!isOpponentSeesaw && !isScale){      tv_DialogTitle.setText(R.string.tv_titleIsAllianceSwitch);}

            tbtn_Outcome = (ToggleButton) findViewById(R.id.tbtn_outcome);
            tbtn_Outcome.setOnClickListener(SeesawDialog.this);

            rbtn_OpponentOwned = (RadioButton) findViewById(R.id.rbtn_opponentOwned);
            rbtn_Balanced = (RadioButton) findViewById(R.id.rbtn_balanced);
            rbtn_OpponentOwned.setOnClickListener(SeesawDialog.this);
            rbtn_Balanced.setOnClickListener(SeesawDialog.this);

            rbtn_Layer1 = (RadioButton) findViewById(R.id.rbtn_layer1);
            rbtn_Layer2 = (RadioButton) findViewById(R.id.rbtn_layer2);
            rbtn_Layer3 = (RadioButton) findViewById(R.id.rbtn_layer3);
            rbtn_Layer1.setOnClickListener(SeesawDialog.this);
            rbtn_Layer2.setOnClickListener(SeesawDialog.this);
            rbtn_Layer3.setOnClickListener(SeesawDialog.this);
        }

        public void onClick(View v){
            switch (v.getId()) {
                case R.id.tbtn_outcome:
                    break;
                case R.id.rbtn_opponentOwned:
                    break;
                case R.id.rbtn_balanced:
                    break;
                case R.id.rbtn_layer1:
                    break;
                case R.id.rbtn_layer2:
                    break;
                case R.id.rbtn_layer3:
                    break;

            }
        }
    }


    public static class QRScan extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

        QRCodeReaderView qrCodeReader;
        public String qrString = "";
        Intent intent;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_scan);

            intent = getIntent();

            qrCodeReader = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
            qrCodeReader.setOnQRCodeReadListener(this);
            qrCodeReader.setQRDecodingEnabled(true);
            qrCodeReader.setTorchEnabled(true);
            qrCodeReader.setAutofocusInterval(2000L);

            qrCodeReader.setFrontCamera();

            if(InputManager.mScoutID >= 7){
                qrCodeReader.setBackCamera();
            }
        }

        @Override
        public void onQRCodeRead(String text, PointF[] points) {
            Intent intent = new Intent(this, A0A.class);
            qrString = text;
            String prevQrString = AppCc.getSp("qrString", "");

            try{
                if(Integer.parseInt(prevQrString.substring(0, prevQrString.indexOf("|"))) > Integer.parseInt(qrString.substring(0, qrString.indexOf("|")))){
                    AppUtils.makeToast(this, "WRONG CYCLE NUMBER!");
                    qrString = prevQrString;

                } else if((!qrString.contains("|")) || (Integer.parseInt(prevQrString.substring(0, prevQrString.indexOf("|"))) <= 0)) {
                    if(!qrString.contains("|")) { AppUtils.makeToast(this, "NO PIPE!"); }
                    if(Integer.parseInt(prevQrString.substring(0, prevQrString.indexOf("|"))) <= 0) { AppUtils.makeToast(this, "INVALID CYCLE NUM!"); }

                    intent.putExtra("qrObtained", false);
                    InputManager.cycleNum = Integer.parseInt(qrString.substring(0, qrString.indexOf("|")));
                    AppCc.setSp("cycle", InputManager.cycleNum);
                    AppUtils.makeToast(this, "SUCCESS SCAN!");
                    startActivity(intent);
                }

                Log.e("CYCLENUMBER!", Integer.parseInt(qrString.substring(0, qrString.indexOf("|"))) + "");

            }catch (ParseException | IndexOutOfBoundsException e){
                e.printStackTrace();
            }catch (NullPointerException ne){
                AppUtils.makeToast(this, "FAIL SCAN!");
            }

            Log.e("QRSTRING", qrString+"");
            AppCc.setSp("qrString", qrString);
            intent.putExtra("qrObtained", true);
            startActivity(intent);
        }

        @Override
        protected void onResume() {
            super.onResume();
            qrCodeReader.startCamera();
        }

        @Override
        protected void onPause() {
            super.onPause();
            qrCodeReader.stopCamera();
        }
    }

}
