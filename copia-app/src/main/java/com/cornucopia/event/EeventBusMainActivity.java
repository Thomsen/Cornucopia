package com.cornucopia.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cornucopia.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class EeventBusMainActivity extends Activity implements OnClickListener {

    private Button mBtnEvent;

    private Button mBtnEventSub;

    private TextView mTvEventShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);

        mBtnEvent = (Button) findViewById(R.id.btn_eventbus);
        mBtnEventSub = (Button) findViewById(R.id.btn_eventbus_sub);
        mTvEventShow = (TextView) findViewById(R.id.tv_eventbus_show);

        mBtnEvent.setOnClickListener(this);
        mBtnEventSub.setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // only register once
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_eventbus) {
                EventBus.getDefault().post(new MessageEvent("event message click"));
            }
        if (v.getId() == R.id.btn_eventbus_sub)  {
                Intent intent = new Intent(this, EventBusSubActivity.class);
            startActivity(intent);
        }
    }

    public void onEvent(MessageEvent event) {
        // onEvent must public
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        mTvEventShow.setText(event.message);
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {

    }

    public void onEventBackgroundThread(MessageEvent event) {

    }

    public void onEventAsync(MessageEvent event) {

    }

}
