package pk.com.Taj.app.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class UIHelper {

    public static Toast showLongToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static Toast showShortToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showLongToast(Context context, int messageId) {
        Toast toast = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static Toast showShortToast(Context context, int messageId) {
        Toast toast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static void showAlertDialog(Context context, String title, String message) {
        showAlertDialog(context,title,message,null);
    }


    public static void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener buttonClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", buttonClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, String title, int messageId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(messageId);
        alertDialogBuilder.setPositiveButton("OK", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showErrorDialog(Context context, String title, String message) {
        showErrorDialog(context,title,message,null);
    }

    public static void showErrorDialog(Context context, String title, String message, DialogInterface.OnClickListener buttonClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("OK", buttonClickListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showErrorDialog(Context context, String title, int messageId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(messageId);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("OK", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonClickListener) {
        showConfirmDialog(context, title, message, "YES", "NO", positiveButtonClickListener);
    }

    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        showConfirmDialog(context, title, message, "YES", "NO", positiveButtonClickListener, negativeButtonClickListener);
    }

    public static void showConfirmDialog(Context context, String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonClickListener) {
        showConfirmDialog(context, title, message, positiveButtonText, negativeButtonText, positiveButtonClickListener, null);
    }

    public static void showConfirmDialog(Context context, String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveButtonClickListener);
        builder.setNegativeButton(negativeButtonText, negativeButtonClickListener);
        builder.show();
    }

    public static void showConfirmDialog(Context context, String title, int messageId, DialogInterface.OnClickListener positiveButtonClickListener) {
        showConfirmDialog(context, title, messageId, "YES", "NO", positiveButtonClickListener);
    }

    public static void showConfirmDialog(Context context, String title, int messageId, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        showConfirmDialog(context, title, messageId, "YES", "NO", positiveButtonClickListener, negativeButtonClickListener);
    }

    public static void showConfirmDialog(Context context, String title, int messageId, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonClickListener) {
        showConfirmDialog(context, title, messageId, positiveButtonText, negativeButtonText, positiveButtonClickListener,null);
    }

    public static void showConfirmDialog(Context context, String title, int messageId, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(messageId);
        builder.setPositiveButton(positiveButtonText, positiveButtonClickListener);
        builder.setNegativeButton(negativeButtonText, negativeButtonClickListener);
        builder.show();
    }
}
