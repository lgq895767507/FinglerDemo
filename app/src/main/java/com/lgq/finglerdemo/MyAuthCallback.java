package com.lgq.finglerdemo;

import android.os.Handler;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by vcc on 2017/5/15.
 */

public class MyAuthCallback extends FingerprintManagerCompat.AuthenticationCallback {

    private Handler handler = null;

    public MyAuthCallback(Handler handler) {
        super();

        this.handler = handler;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);

        if (handler != null) {
            handler.obtainMessage(MainActivity.MSG_AUTH_ERROR, errMsgId, 0).sendToTarget();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);

        if (handler != null) {
            handler.obtainMessage(MainActivity.MSG_AUTH_HELP, helpMsgId, 0).sendToTarget();
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        if (handler != null) {
            handler.obtainMessage(MainActivity.MSG_AUTH_SUCCESS).sendToTarget();
        }
        /*try {
            result.getCryptoObject().getCipher().doFinal();

            if (handler != null) {
                handler.obtainMessage(MainActivity.MSG_AUTH_SUCCESS).sendToTarget();
            }
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();

        if (handler != null) {
            handler.obtainMessage(MainActivity.MSG_AUTH_FAILED).sendToTarget();
        }
    }
}

