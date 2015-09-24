package com.ubicomp.flashcard;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class ScrollCardsActivity extends Activity {
    private static final String TAG = ScrollCardsActivity.class.getSimpleName();
    private boolean mVoiceMenuEnabled = true;
    private CardScrollView mCardScroller;
    private CardScrollAdapter mAdapter;
    private boolean mAutoDelete = true;//TODO this one need to save as sharedpreferences
    private List<CardBuilder> cards= new ArrayList<CardBuilder>();
    CreateCardsTask cardstask =new CreateCardsTask();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        // Requests a voice menu on this activity. As for any other window feature,
        // be sure to request this before setContentView() is called
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        //start asyntask
        cardstask.execute(getApplicationContext());

        //initialize UI
        mAdapter = new CardAdapter(createLoadCard(this));
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);
        setContentView(mCardScroller);
        setCardScrollerListener();
    }
    // make things
    private List<CardBuilder> createLoadCard(Context context) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        cards.add(new CardBuilder(context, CardBuilder.Layout.CAPTION)
                .addImage(R.drawable.stevejobs));
//                .setText("loading images ...")
//                .setFootnote("Available processors:" + Integer.toString(//determine available processors
//                        Runtime.getRuntime().availableProcessors())));


        return cards;
    }
    private void setCardScrollerListener() {
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;
                openOptionsMenu();
                // Toggles voice menu. Invalidates menu to flag change.
//                mVoiceMenuEnabled = !mVoiceMenuEnabled;
//                getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
        setContentView(mCardScroller);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }

    //tap interface

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.voice_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection. Menu items typically start another
        // activity, start a service, or broadcast another intent.
        switch (item.getItemId()) {
            case R.id.create_card:

                break;
            case R.id.remember_cards:

                break;
            case R.id.menu_autodelete_on:   mAutoDelete =true; break;
            case R.id.menu_autodelete_off:   mAutoDelete = false; break;


            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }
//To display the menu, call openOptionsMenu() when required, such as a tap on the touchpad.
//The following examples detects a tap gesture on an activity and then calls openOptionsMenu().

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//Android knows about several types of menus (e.g. Options Menu and Context Menu).
// onMenuItemSelected is the generic callback. You don't need to use this usually.
// onOptionsItemSelected is the callback of the options menu and onContextItemSelected is the
// callback of the context menu. Use these two specific ones instead.


    // voice interface
    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.voice_menu, menu);
            return true;
        }
        // Good practice to pass through, for options menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            // Dynamically decides between enabling/disabling voice menu.
            return mVoiceMenuEnabled;
        }
        // Good practice to pass through, for options menu.
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            switch (item.getItemId()) {
                case R.id.create_card:

                    break;
                case R.id.remember_cards:

                    break;
                case R.id.menu_autodelete_on:   mAutoDelete =true; break;
                case R.id.menu_autodelete_off:   mAutoDelete = false; break;


                default: return true;  // No change.
            }
//            mCardScroller.setAdapter(new CardAdapter(createCards(this)));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    private class CreateCardsTask extends AsyncTask<Context, Integer, List<CardBuilder>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected List<CardBuilder> doInBackground(Context... params) {
            Log.v(TAG+"ASYN","Strat dib");
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.v(TAG+"ASYN","end dib");
            return cards;
        }

        protected void onProgressUpdate(Integer... progress) {
//            CardBuilder cb =(CardBuilder)mAdapter.getItem(0);
//            cb.setText("loading images:"+Integer.toString(progress[0]));
            //.set("loading images"+Integer.toString(progress[0]));
        }

        protected void onPostExecute(List<CardBuilder> result) {
//            listener.processFinish(result);
            Log.v(TAG, "OnPostExecute");
            //temp stuff to show loading card


            mAdapter = new CardAdapter(cards);
            mCardScroller.setAdapter(mAdapter);
            // Tells the CardScrollView to activate and be ready for display.
            mCardScroller.activate();
            //redraw UI
            mCardScroller.invalidate();
        }
    }

}

