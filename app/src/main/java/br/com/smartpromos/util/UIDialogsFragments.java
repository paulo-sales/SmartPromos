package br.com.smartpromos.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import br.com.smartpromos.ui.fragment.DialogHelpLogin;
import br.com.smartpromos.ui.fragment.DialogLoading;
import br.com.smartpromos.ui.fragment.DialogMarker;
import br.com.smartpromos.ui.fragment.DialogUI;

/**
 * Created by Paulo on 14/04/2016.
 */
public class UIDialogsFragments extends Fragment {

    FragmentActivity fragmentActivity;
    public DialogLoading loadingDialog;

    public void uiGetActivity( FragmentActivity fragmentActivity){

        this.fragmentActivity = fragmentActivity;
    }

    public void showDialogUseCupom(String title, String descDialog, String extra) {

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("description", descDialog);

        if(extra != null || !extra.equals("")){
            bundle.putString("extra", extra);
        }
        DialogUI newFragment = new DialogUI();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    public void showDialog(String title, String descDialog) {

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("description", descDialog);

        DialogUI newFragment = new DialogUI();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    public void showHelpDialog() {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        DialogHelpLogin newFragment = new DialogHelpLogin();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    public void showLoading(){

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        loadingDialog = new DialogLoading();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, loadingDialog)
                .addToBackStack(null).commit();
    }


    public void showLoadingLogin(String cliente){

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("cliente", cliente);

        loadingDialog = new DialogLoading();
        loadingDialog.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, loadingDialog)
                .addToBackStack(null).commit();
    }

    public void showDialogMarker(String place) {

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("place", place);

        DialogMarker newFragment = new DialogMarker();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

}
