package com.lgq.finglerdemo;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final int MSG_AUTH_SUCCESS = 0;
    public static final int MSG_AUTH_FAILED = 1;
    public static final int MSG_AUTH_ERROR = 2;
    public static final int MSG_AUTH_HELP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取对象
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this);
        //判断硬件支持
        if (!fingerprintManager.isHardwareDetected()) {
            // no fingerprint sensor is detected, show dialog to tell user.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("没有指纹识别硬件");
            builder.setMessage("没有指纹识别");
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            // show this dialog.
            builder.create().show();
        }

        //判断是否锁屏
        KeyguardManager keyguardManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {
            // this device is secure.
        }

        //判断是否注册了指纹
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            // no fingerprint image has been enrolled.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("没有注册指纹");
            builder.setMessage("没有找到指纹");
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            // show this dialog
            builder.create().show();
        }



         Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d("lgq", "msg: " + msg.what + " ,arg1: " + msg.arg1);
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        setResultInfo("R.string.fingerprint_success");
                     /*   mCancelBtn.setEnabled(false);
                        mStartBtn.setEnabled(true);
                        cancellationSignal = null;*/
                        break;
                    case MSG_AUTH_FAILED:
                        setResultInfo("R.string.fingerprint_not_recognized");
                      /*  mCancelBtn.setEnabled(false);
                        mStartBtn.setEnabled(true);
                        cancellationSignal = null;*/
                        break;
                    case MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };

        CryptoObjectHelper cryptoObjectHelper = null;
        try {
            cryptoObjectHelper = new CryptoObjectHelper();
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    new CancellationSignal(), new MyAuthCallback(handler), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                setResultInfo("R.string.ErrorCanceled_warning");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                setResultInfo("R.string.ErrorHwUnavailable_warning");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                setResultInfo("R.string.ErrorLockout_warning");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                setResultInfo("R.string.ErrorNoSpace_warning");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                setResultInfo("R.string.ErrorTimeout_warning");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                setResultInfo("R.string.ErrorUnableToProcess_warning");
                break;
        }
    }

    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                setResultInfo("R.string.AcquiredGood_warning");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                setResultInfo("R.string.AcquiredImageDirty_warning");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                setResultInfo("R.string.AcquiredInsufficient_warning");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                setResultInfo("R.string.AcquiredPartial_warning");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                setResultInfo("R.string.AcquiredTooFast_warning");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                setResultInfo("R.string.AcquiredToSlow_warning");
                break;
        }
    }

    private void setResultInfo(String str){
        System.out.println(str);
    }
}
